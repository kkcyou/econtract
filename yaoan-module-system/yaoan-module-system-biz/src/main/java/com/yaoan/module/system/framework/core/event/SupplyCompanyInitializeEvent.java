package com.yaoan.module.system.framework.core.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 供应商公司初始化 Event
 *
 * @author lls
 */
@SuppressWarnings("ALL")
@Data
public class SupplyCompanyInitializeEvent extends ApplicationEvent {

    @NotNull(message = "公司编号不能为空")
    private Long id;

    @NotNull(message = "单位名称不能为空")
    private String name;

//    @NotNull(message = "信用代码不能为空")
    private String creditCode;

    //    @NotNull(message = "用户名不能为空")
    private String username;

    //    @NotNull(message = "密码不能为空")
    private String password;

//    @NotNull(message = "部门 ID不能为空")
//    private Long deptId;

    @NotBlank(message = "手机号不能为空")
    @Size(max = 11, message = "联系电话长度不能超过11个字符")
    private String phone;
    private String email;
    private String idCard;
    private String nickname;
    /**
     * 相对方id
     */
    private String relativeId;
    private Long tenantId;

    /**
     * 公司id
     */
    private Long companyId;

    public SupplyCompanyInitializeEvent(Object source) {
        super(source);
    }

}
