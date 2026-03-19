package com.yaoan.module.econtract.controller.admin.sign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author doujiale
 */
@Data
@Builder
@Schema(description = "合同文件 Request VO")
public class SignInfoVO {


    private String signName;
    private Integer index;
    private String appName;
    private String compName;
    private String userName;
    private String keySn;
    private String signSn;
    private String signTime;
    private String hash;
    private byte[] cert;
    private byte[] signData;
    private String newHash;
    private String hashType;
    private String digitalSigVerify;
    private String signId;
    private String tsTime;
    private String extParam;
    private byte[] sealImgData;
    private Boolean tamper;
}
