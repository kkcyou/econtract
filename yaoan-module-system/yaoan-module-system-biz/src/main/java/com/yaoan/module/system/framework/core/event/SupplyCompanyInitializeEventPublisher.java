package com.yaoan.module.system.framework.core.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * {@link SupplyCompanyInitializeEvent} 的生产者
 *
 * @author lls
 */
@AllArgsConstructor
@Validated
public class SupplyCompanyInitializeEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void sendCompanyInitializeEvent(@Valid SupplyCompanyInitializeEvent event) {
        publisher.publishEvent(event);
    }

}
