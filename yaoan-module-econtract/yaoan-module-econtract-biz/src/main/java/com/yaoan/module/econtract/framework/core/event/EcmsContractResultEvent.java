package com.yaoan.module.econtract.framework.core.event;

import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotNull;

/**
 * 合同实例的结果发生变化的 Event
 * {@link ContractStatusEnums#getResult()} 结果，所以增加该事件
 * @author doujl
 */
@SuppressWarnings("ALL")
@Data
public class EcmsContractResultEvent extends ApplicationEvent {

    /**
     * 合同编号
     */
    @NotNull(message = "合同的编号不能为空")
    private String id;
    /**
     * 合同实例的结果
     */
    @NotNull(message = "流程实例的结果不能为空")
    private Integer result;

    public EcmsContractResultEvent(Object source) {
        super(source);
    }

}
