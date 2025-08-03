package com.loopers.domain.brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Optional<Brand> getBrand(Long brandUid);

    List<Brand> getBrandsByBrandUids(List<Long> brandUids);
}
