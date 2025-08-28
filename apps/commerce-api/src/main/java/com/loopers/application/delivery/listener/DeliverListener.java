package com.loopers.application.delivery.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeliverListener {

    @EventListener
    public void orderDelivery(DeliveryEvent.OrderResultEvent event) {
        log.info("order delivery event: {}", event);
        //주문 정보를 받아서 외부 데이터 플랫폼에 보낸다.
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @EventListener
    public void paymentDelivery(DeliveryEvent.PaymentResultEvent event) {
        log.info("payment delivery event: {}", event);
        //결제 정보를 받아서 외부 데이터 플랫폼에 보낸다.
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
