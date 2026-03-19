package com.yaoan.module.econtract.api.listen;

import com.yaoan.module.econtract.api.listen.dto.FileVersionEventDTO;

import javax.validation.Valid;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/19 17:45
 */
public interface FileVersionEventPublisherApi {
    public void sendEvent(@Valid FileVersionEventDTO event);
}
