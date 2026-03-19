package com.yaoan.module.econtract.controller.admin.code.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "CodeRuleInfoReqVO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class CodeRuleInfoReqVO {
    /**
     * 编号前缀
     */
    @NotBlank(message = "编号前缀不能为空")
    private String prefix;

    /**
     * 连接符
     */
    private String connector;

    /**
     * 固定文本
     */
    private String fixedText;

    /**
     *  上次重置日期
     */
    private LocalDate lastResetDate;

    /**
     * 下一个序列号
     */
    private Long nextSerialNumber;

    public void incrementSerialNumber() {
        this.nextSerialNumber++;
    }

    public void resetSerialNumber() {
        this.nextSerialNumber = 1L;
        this.lastResetDate = LocalDate.now();
    }
}
