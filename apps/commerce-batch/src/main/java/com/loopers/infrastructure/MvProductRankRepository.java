package com.loopers.infrastructure;

import com.loopers.domain.MvProductRankWeekly;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MvProductRankRepository extends JpaRepository<MvProductRankWeekly, Long> {
}
