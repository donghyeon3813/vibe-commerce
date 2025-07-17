package com.loopers.domain.point;

import com.loopers.infrastructure.point.PointJpaRepostiroy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class PointServiceIntegrationTest {

    @Autowired
    PointService pointService;

    @Autowired
    PointJpaRepostiroy pointJpaRepostiroy;

    @DisplayName("포인트 정보를 조회할 때")
    @Nested
    class Info {

        /*
         * Facade 에서 유저 서비스를 mock 으로 가져와서 데이터가 있다고 가정하고 테스트를 고려하였으나
         * PointService 만 테스트를 하는 것과 같은 효과를 불러온다 생각하여 현재 클래스에서 테스트를 구현
         * */
        @BeforeEach
        void init() {
            PointModel pointModel = new PointModel(9999L, 100);
            pointJpaRepostiroy.save(pointModel);
        }

        @DisplayName("해당 아이디의 회원이 존재할 경우 포인트가 반환된다.")
        @Test
        void returnsPointInfo_whenUserExists() {
            Long userUid = 9999L;

            PointModel pointModel = pointService.getPointInfo(userUid);

            assertThat(pointModel).isNotNull();
            assertThat(pointModel.getPoint()).isEqualTo(100);

        }

        @DisplayName("해당 아이디의 회원이 존재하지 않을 경우 Null 이 반환된다.")
        @Test
        void returnsNull_whenUserDoesNotExist() {
            Long userUid = 99999L;
            PointModel pointModel = pointService.getPointInfo(userUid);

            assertThat(pointModel).isNull();
        }

    }

}
