package com.loopers.domain.point;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public PointModel getPointInfo(Long userUid) {

        return pointRepository.findByUserUid(userUid);
    }
}
