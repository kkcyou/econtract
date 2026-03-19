package com.yaoan.module.econtract.framework.core.event;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * {@link EcmsContractResultEvent} 的生产者
 *
 * @author doujl
 */
@AllArgsConstructor
@Validated
public class EcmsContractResultEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void sendContractResultEvent(@Valid EcmsContractResultEvent event) {
        publisher.publishEvent(event);
    }

}
