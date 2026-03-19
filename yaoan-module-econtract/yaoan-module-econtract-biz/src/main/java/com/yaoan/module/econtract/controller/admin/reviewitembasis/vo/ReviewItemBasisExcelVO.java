package com.yaoan.module.econtract.controller.admin.reviewitembasis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 合同审查规则依据 Excel VO
 *
 * @author admin
 */
@Data
public class ReviewItemBasisExcelVO {

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
