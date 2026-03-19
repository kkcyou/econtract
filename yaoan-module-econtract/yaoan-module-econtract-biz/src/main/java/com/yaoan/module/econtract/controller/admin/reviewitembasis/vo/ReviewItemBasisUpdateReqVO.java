package com.yaoan.module.econtract.controller.admin.reviewitembasis.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则依据更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewItemBasisUpdateReqVO extends ReviewItemBasisBaseVO {

}
