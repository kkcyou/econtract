package com.yaoan.module.econtract.controller.admin.performtasktype.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:44
 */
@Data
public class DeleteVO {
    /**
     * 要删除的主键
     */
    @NotNull(message = "删除参数不能为空")
    private List<String> ids;
}
