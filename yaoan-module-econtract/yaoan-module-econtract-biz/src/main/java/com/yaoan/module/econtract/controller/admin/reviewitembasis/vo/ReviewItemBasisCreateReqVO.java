package com.yaoan.module.econtract.controller.admin.reviewitembasis.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 合同审查规则依据创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewItemBasisCreateReqVO extends ReviewItemBasisBaseVO {

}
