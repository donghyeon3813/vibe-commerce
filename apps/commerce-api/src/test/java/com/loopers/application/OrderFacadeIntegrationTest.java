package com.loopers.application;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.domain.order.*;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.product.Product;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PointJpaRepository pointJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("주문 요청을 할 때")
    @Nested
    class Order {
        @DisplayName("등록되지 않은 유저면 NotFound를 반환한다.")
        @Test
        void throwsNotFound_whenUserNotFound() {
            String userId = "notFound";
            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(1L, 1));
            orderItemList.add(OrderCommand.Order.OrderItem.of(2L, 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(3L, 2));
            OrderCommand.Order order = OrderCommand.Order.of(orderItemList, userId, "주소", "01000000000", "홍길동");

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.order(order));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("상품을 찾을 수 없는 항목이 존재하면 NotFound를 반환한다.")
        @Test
        void throwsNotFound_whenProductNotFound() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel save = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(save.getId(), 1000);
            pointJpaRepository.save(pointModel);

            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(1L, 1));
            orderItemList.add(OrderCommand.Order.OrderItem.of(2L, 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(3L, 2));
            OrderCommand.Order order = OrderCommand.Order.of(orderItemList, userModel.getUserId(), "주소", "01000000000", "홍길동");

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.order(order));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }

        @DisplayName("포인트가 부족하면 BadRequest를 반환한다.")
        @Test
        void throwsBadRequest_whenInsufficientPoint() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), 1000);
            pointJpaRepository.save(pointModel);

            List<Product> productList = new ArrayList<>();
            productList.add(Product.create(9999L, "상의", 1000, 5));
            productList.add(Product.create(9999L, "하의", 1000, 5));
            productList.add(Product.create(9999L, "신발", 1000, 5));
            productJpaRepository.saveAll(productList);

            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(1L, 1));
            orderItemList.add(OrderCommand.Order.OrderItem.of(2L, 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(3L, 2));
            OrderCommand.Order order = OrderCommand.Order.of(orderItemList, userModel.getUserId(), "주소", "01000000000", "홍길동");

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.order(order));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("재고 부족하면 BadRequest를 반환한다.")
        @Test
        void throwsBadRequest_whenInsufficientStock() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), 1000);
            pointJpaRepository.save(pointModel);

            List<Product> productList = new ArrayList<>();
            productList.add(Product.create(9999L, "상의", 5, 5));
            productList.add(Product.create(9999L, "하의", 5, 5));
            productList.add(Product.create(9999L, "신발", 5, 5));
            productJpaRepository.saveAll(productList);

            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(1L, 10));
            orderItemList.add(OrderCommand.Order.OrderItem.of(2L, 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(3L, 2));
            OrderCommand.Order order = OrderCommand.Order.of(orderItemList, userModel.getUserId(), "주소", "01000000000", "홍길동");

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.order(order));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }
        @DisplayName("맞는 데이터를 요청시 주문 정보를 저장한다.")
        @Test
        void savesOrderAndPayment_whenValidRequest() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel saveUser = userJpaRepository.save(userModel);
            PointModel pointModel = PointModel.create(saveUser.getId(), 10000);
            pointJpaRepository.save(pointModel);

            Product product1 = productJpaRepository.save(Product.create(9999L, "상의", 1000, 5));  // name: 상의
            Product product2 = productJpaRepository.save(Product.create(9999L, "하의", 500, 5));  // name: 하의
            Product product3 = productJpaRepository.save(Product.create(9999L, "신발", 100, 5));   // name: 신발

            List<OrderCommand.Order.OrderItem> orderItemList = new ArrayList<>();
            orderItemList.add(OrderCommand.Order.OrderItem.of(product1.getId(), 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(product2.getId(), 2));
            orderItemList.add(OrderCommand.Order.OrderItem.of(product3.getId(), 2));
            OrderCommand.Order order = OrderCommand.Order.of(orderItemList, userModel.getUserId(), "주소", "01000000000", "홍길동");

            OrderInfo.OrderResponse orderResponse = orderFacade.order(order);

            Optional<OrderModel> orderModel = orderJpaRepository.findById(orderResponse.orderId());

            assertThat(orderModel).isPresent();
            assertThat(orderModel.get().getAmount()).isEqualTo(3200);
            assertThat(orderModel.get().getOrderStatus()).isEqualTo(OrderStatus.PAID);

        }

    }
    @DisplayName("동시에 주문 요청이 올 때")
    @Nested
    class ConcurrencyOrders {
        @DisplayName("다른 유저로 들어왔을때 재고와 포인트가 정확히 차감된다.")
        @Test
        void shouldDeductStockAndPointCorrectlyForDifferentUser() throws InterruptedException {

            UserModel user1 = userJpaRepository.save(UserModel.CreateUser("testUser1", "user1@test.com", Gender.MALE.name(), "2025-07-13"));
            pointJpaRepository.save(PointModel.create(user1.getId(), 10000));

            UserModel user2 = userJpaRepository.save(UserModel.CreateUser("testUser2", "user2@test.com", Gender.FEMALE.name(), "2025-07-13"));
            pointJpaRepository.save(PointModel.create(user2.getId(), 5000));

            Product product1 = productJpaRepository.save(Product.create(9999L, "상의", 100, 30));
            Product product2 = productJpaRepository.save(Product.create(9999L, "하의", 100, 30));

            List<OrderCommand.Order.OrderItem> items = List.of(
                    OrderCommand.Order.OrderItem.of(product1.getId(), 2),
                    OrderCommand.Order.OrderItem.of(product2.getId(), 3)
            );

            // 주문 객체 2개 (유저별)
            OrderCommand.Order order1 = OrderCommand.Order.of(items, user1.getUserId(), "서울시", "01012345678", "홍길동");
            OrderCommand.Order order2 = OrderCommand.Order.of(items, user2.getUserId(), "부산시", "01098765432", "임꺽정");

            int threadCount = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                int finalI = i;
                executorService.submit(() -> {
                    try {
                        if (finalI % 2 == 0) {
                            orderFacade.order(order1); // 유저1
                        } else {
                            orderFacade.order(order2); // 유저2
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);

            PointModel pointModel1 = pointJpaRepository.findByUserUid(user1.getId()).get();
            PointModel pointModel2 = pointJpaRepository.findByUserUid(user2.getId()).get();
            Product testProduct1 = productJpaRepository.findById(product1.getId()).get();
            Product testProduct2 = productJpaRepository.findById(product2.getId()).get();


            assertAll(
                    () -> assertThat(pointModel1.getPoint()).isEqualTo(7500), // 10번 성공 2500차감
                    () -> assertThat(pointModel2.getPoint()).isEqualTo(2500), // 10번 성공 2500차감
                    () -> assertThat(testProduct1.getQuantity()).isEqualTo(10), // 10번 성공 20 차감
                    () -> assertThat(testProduct2.getQuantity()).isEqualTo(0)); // 10번 성공 30 차감

        }
        @DisplayName("같은 유저로 주문 요청시 정확히 차감된다.")
        @Test
        void shouldDeductStockAndPointCorrectlyForSameUser() throws InterruptedException {
            // given
            UserModel userModel = UserModel.CreateUser("testUser1", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel savedUser = userJpaRepository.save(userModel);
            pointJpaRepository.save(PointModel.create(savedUser.getId(), 10000));// 총 1만원


            Product product1 = productJpaRepository.save(Product.create(9999L, "상의", 100, 25));
            Product product2 = productJpaRepository.save(Product.create(9999L, "하의", 100, 30));

            List<OrderCommand.Order.OrderItem> items = List.of(
                    OrderCommand.Order.OrderItem.of(product1.getId(), 2),
                    OrderCommand.Order.OrderItem.of(product2.getId(), 3)
            );

            OrderCommand.Order order = OrderCommand.Order.of(
                    items,
                    savedUser.getUserId(),
                    "서울시",
                    "01012345678",
                    "홍길동"
            );

            int threadCount = 5;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        orderFacade.order(order); // 주문 시도
                    }finally {
                        latch.countDown();
                    }

                });
            }

            latch.await();

            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);

            PointModel pointModel1 = pointJpaRepository.findByUserUid(savedUser.getId()).get();
            Product testProduct1 = productJpaRepository.findById(product1.getId()).get();
            Product testProduct2 = productJpaRepository.findById(product2.getId()).get();

            assertAll(
                    () -> assertThat(pointModel1.getPoint()).isEqualTo(7500),
                    () -> assertThat(testProduct1.getQuantity()).isEqualTo(15),
                    () -> assertThat(testProduct2.getQuantity()).isEqualTo(15));

        }

    }
    @DisplayName("주문 목록 조회를 할 때")
    @Nested
    class Infos {
        @DisplayName("등록되지 않은 유저면 NotFound를 반환한다.")
        @Test
        void throwsNotFound_whenUserNotFound() {
            String userId = "notFound";
            OrderCommand.GetOrders getOrders = OrderCommand.GetOrders.of(userId);

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.getOrders(getOrders));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
        @DisplayName("주문 요청 내역이 없으면 빈항목을 반환한다.")
        @Test
        void returnsEmpty_whenOrderNotFound() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            userJpaRepository.save(userModel);
            OrderCommand.GetOrders getOrders = OrderCommand.GetOrders.of(userModel.getUserId());

            OrderInfo.OrderDataList orderDataList = orderFacade.getOrders(getOrders);

            assertThat(orderDataList).isNotNull();
            assertThat(orderDataList.orders()).isEmpty();
        }

        @DisplayName("주문 요청 내역이 있으면 값을 반환한다.")
        @Test
        void returnsOrderDataList_whenOrderFound() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel savedUser = userJpaRepository.save(userModel);
            OrderModel orderModel = OrderModel.create(savedUser.getId(), 1000, Address.of("주소", "01000000000", "받는사람"));
            orderModel.addOrderItem(OrderItem.create(9999L, 5));
            orderModel.addOrderItem(OrderItem.create(9998L, 4));
            orderJpaRepository.save(orderModel);
            OrderModel orderModel2 = OrderModel.create(savedUser.getId(), 1000, Address.of("주소", "01000000000", "받는사람"));
            orderModel2.addOrderItem(OrderItem.create(9997L, 2));
            orderJpaRepository.save(orderModel2);

            OrderCommand.GetOrders getOrders = OrderCommand.GetOrders.of(userModel.getUserId());

            OrderInfo.OrderDataList orderDataList = orderFacade.getOrders(getOrders);

            assertThat(orderDataList).isNotNull();
            assertThat(orderDataList.orders()).hasSize(2);
        }

    }
    @DisplayName("주문 목록 조회를 할 때")
    @Nested
    class Info {
        @DisplayName("등록되지 않은 유저면 NotFound를 반환한다.")
        @Test
        void throwsNotFound_whenUserNotFound() {
            String userId = "notFound";
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            UserModel sevedUser = userJpaRepository.save(userModel);
            OrderModel orderModel2 = OrderModel.create(sevedUser.getId(), 1000, Address.of("주소", "01000000000", "받는사람"));
            orderModel2.addOrderItem(OrderItem.create(9997L, 2));
            OrderModel order = orderJpaRepository.save(orderModel2);
            OrderCommand.GetOrder getOrder = OrderCommand.GetOrder.of(userId, order.getId());

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.getOrder(getOrder));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND);
        }
        @DisplayName("orderId를 찾을 수 없으면 BadRequest를 반환한다.")
        @Test
        void throwsNotFound_whenOrderIdNotFound() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");
            userJpaRepository.save(userModel);

            OrderCommand.GetOrder getOrder = OrderCommand.GetOrder.of(userModel.getUserId(), 99999L);

            CoreException exception = assertThrows(CoreException.class, () -> orderFacade.getOrder(getOrder));

            assertThat(exception.getErrorType()).isEqualTo(ErrorType.BAD_REQUEST);
        }

        @DisplayName("orderId를 찾을 수 있으면 데이터를 반환한다.")
        @Test
        void returnOrderData_whenOrderIdFound() {
            UserModel userModel = UserModel.CreateUser("testId314", "test@test.com", Gender.MALE.name(), "2025-07-13");

            UserModel sevedUser = userJpaRepository.save(userModel);

            OrderModel orderModel2 = OrderModel.create(sevedUser.getId(), 1000, Address.of("주소", "01000000000", "받는사람"));
            orderModel2.addOrderItem(OrderItem.create(9997L, 2));
            OrderModel order = orderJpaRepository.save(orderModel2);
            OrderCommand.GetOrder getOrder = OrderCommand.GetOrder.of(userModel.getUserId(), order.getId());

            OrderInfo.OrderData orderData = orderFacade.getOrder(getOrder);

            assertThat(orderData).isNotNull();
            assertThat(orderData.order().getAmount()).isEqualTo(order.getAmount());
            assertThat(orderData.order().getUserUid()).isEqualTo(order.getUserUid());
        }
    }


}
