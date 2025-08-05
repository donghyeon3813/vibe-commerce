package com.loopers.domain.point;

import com.loopers.application.point.PointCreateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Optional<PointModel> getPointInfo(Long userUid) {

        return pointRepository.findByUserUid(userUid);
    }

    @Transactional
    public PointModel createPoint(PointCreateInfo info) {
        PointModel pointModel = PointModel.create(info.getUserId(), info.getPoint());
        return pointRepository.save(pointModel);
    }
    public void deduct(PointModel pointModel, int totalAmount) {
        pointModel.deduct(totalAmount);
        pointRepository.saveAndFlush(pointModel);
    }
}
