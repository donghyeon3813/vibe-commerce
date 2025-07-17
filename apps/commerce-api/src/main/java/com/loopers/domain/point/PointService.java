package com.loopers.domain.point;

import com.loopers.application.point.PointCreateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public PointModel getPointInfo(Long userUid) {

        return pointRepository.findByUserUid(userUid);
    }

    public PointModel createPoint(PointCreateInfo info) {
        PointModel pointModel = PointModel.createPointModel(info.getUserId(), info.getPoint());
        return pointRepository.save(pointModel);
    }
}
