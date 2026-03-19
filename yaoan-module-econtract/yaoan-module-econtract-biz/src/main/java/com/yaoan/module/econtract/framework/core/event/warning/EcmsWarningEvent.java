package com.yaoan.module.econtract.framework.core.event.warning;

import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

/**
 */
@SuppressWarnings("ALL")
@Data
public class EcmsWarningEvent extends ApplicationEvent {

    /**
     * 1手动， 2自动
     */
    private String flag;
    /**
     * 模块来源id
     */
    private String modelCode;
    /**
     * 业务id
     */
    private String bussinessId;

    /**
     * 模块来源id
     */
    private TaskForWarningReqDTO taskParams;

    public EcmsWarningEvent(Object source) {
        super(source);
    }

}
