package com.yaoan.module.econtract.controller.admin.agreement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Schema(description = "Pref Agreement Rel Create ReqVO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PrefAgreementRelCreateReqVO extends PrefAgreementRelBase{
    private static final long serialVersionUID = -5456024383445418553L;

    /**
     * 履约协议id-主键
     */
    @Schema(description = "履约协议id-主键", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String id;

}
