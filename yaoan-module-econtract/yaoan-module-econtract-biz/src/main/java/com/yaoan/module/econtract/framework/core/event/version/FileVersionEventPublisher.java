package com.yaoan.module.econtract.framework.core.event.version;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @description: {@link FileVersionEvent} 的生产者
 * @author: Pele
 * @date: 2024/8/29 18:33
 */
@AllArgsConstructor
@Validated
public class FileVersionEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void sendEvent(@Valid FileVersionEvent event) {
        publisher.publishEvent(event);
    }

}
