package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 智能审查结果创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewResultCreateReqVO extends ReviewResultBaseVO {

}
