package com.yaoan.module.econtract.framework.core.event.version;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 18:28
 */
@SuppressWarnings("ALL")
@Data
public class FileVersionEvent extends ApplicationEvent {
    /**
     * 业务id
     */
    @NotNull(message = "业务id不能为空")
    private String businessId;
    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 备注
     */
    private String remark;


    public FileVersionEvent(Object source) {
        super(source);
    }
}
