package com.yaoan.module.system.framework.core.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * {@link CompanyInitializeEvent} 的生产者
 *
 * @author doujl
 */
@AllArgsConstructor
@Validated
public class CompanyInitializeEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void sendCompanyInitializeEvent(@Valid CompanyInitializeEvent event) {
        publisher.publishEvent(event);
    }

}
