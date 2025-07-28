package com.loopers.domain.brand;

import com.loopers.infrastructure.brand.BrandJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BrandServiceIntegrationTest {
    @Autowired
    private BrandService brandService;
    @Autowired
    private BrandJpaRepository brandJpaRepository;

    @DisplayName("브랜드 정보를 조회할 때")
    @Nested
    class Info {

        @DisplayName("브랜드 정보가 없으면 Optional.empty 를 반환한다.")
        @Test
        void returnNull_whenBrandNotFound() {
            Optional<Brand> brand = brandJpaRepository.findById(1L);
            assertThat(brand).isEmpty();

            Optional<Brand> brandInfo = brandService.getBrandInfo(1L);
            assertThat(brandInfo).isEmpty();

        }

        @DisplayName("브랜드 정보가 있으면 정상적으로 객체를 반환한다.")
        @Test
        void returnBrand_whenBrandFound() {

            Brand brand = Brand.of("무신사");
            Brand savedBrand = brandJpaRepository.save(brand);

            Optional<Brand> brandInfo = brandService.getBrandInfo(savedBrand.getId());

            assertThat(brandInfo).isPresent();
            assertThat(brandInfo.get().getName()).isEqualTo("무신사");
        }
    }
}
