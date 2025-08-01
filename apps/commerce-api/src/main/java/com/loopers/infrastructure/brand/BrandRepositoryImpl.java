package com.loopers.infrastructure.brand;

import com.loopers.domain.brand.Brand;
import com.loopers.domain.brand.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BrandRepositoryImpl implements BrandRepository {

    private final BrandJpaRepository brandJpaRepository;

    @Override
    public Optional<Brand> getBrand(Long brandUid) {
        return brandJpaRepository.findById(brandUid);
    }

    @Override
    public List<Brand> getBrandsByBrandUids(List<Long> brandUids) {
        return brandJpaRepository.findByIdIn(brandUids);
    }
}
