package com.yaoan.module.system.framework.core.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 公司初始化 Event
 *
 * @author doujl
 */
@SuppressWarnings("ALL")
@Data
public class CompanyInitializeEvent extends ApplicationEvent {

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

    private Long tenantId;

    public CompanyInitializeEvent(Object source) {
        super(source);
    }

}
