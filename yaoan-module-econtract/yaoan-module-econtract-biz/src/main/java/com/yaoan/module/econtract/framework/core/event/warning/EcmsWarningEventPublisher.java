package com.yaoan.module.econtract.framework.core.event.warning;

import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEvent;
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
public class EcmsWarningEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void setEvent(@Valid EcmsWarningEvent event) {
        publisher.publishEvent(event);
    }

}
