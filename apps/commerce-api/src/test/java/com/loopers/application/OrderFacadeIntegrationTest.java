package com.loopers.application;

import com.loopers.application.order.OrderCommand;
import com.loopers.application.order.OrderFacade;
import com.loopers.application.order.OrderInfo;
import com.loopers.domain.like.Like;
import com.loopers.domain.order.OrderModel;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.order.OrderStatus;
import com.loopers.domain.payment.PaymentRepository;
import com.loopers.domain.point.PointModel;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.UserModel;
import com.loopers.infrastructure.order.OrderJpaRepository;
import com.loopers.infrastructure.payment.PaymentJpaRepository;
import com.loopers.infrastructure.point.PointJpaRepository;
import com.loopers.infrastructure.product.ProductJpaRepository;
import com.loopers.infrastructure.user.UserJpaRepository;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository pointRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

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
            userJpaRepository.save(userModel);

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
}
