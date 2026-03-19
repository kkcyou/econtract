package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Schema(description = "合同草拟-搜索框字符串")
@Data
public class ReqVO {
    /**
     * 搜索框字符串
     */
    @Schema(description = "搜索框字符串")
    String searchText;
}
