package com.loopers.domain.issue;

import java.util.List;

public interface CouponIssueRepository {

    List<CouponIssue> findByUserUidAndUseFlag(int UserFlag, Long userUid);
}
