package com.yaoan.module.econtract.controller.admin.foxi.vo.save;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-5 16:32
 */
@Data
public class FoxiSaveDataReqVO {

    private String docId;
    /**
     * 是修改完最新的文件的下载地址
     */
    private String docURL;
    /**
     * docURL的base64编码，用于用户对docURL进行校验
     */
    private String docUrlEncode;
    /**
     * 这个文件在这次编辑过程中被哪些人修改过。: docURL的base64编码，用于用户对docURL
     */
    private List<ModifyByReqVO> modifyBy;
    /**
     * 是标示这个文件有没有被修改
     */
    private boolean unchanged;
    /**
     * 这个文件最新的缩略图
     */
    private String pngUrl;
    /**
     * 这个文件最新提取的文本
     */
    private String txtUrl;
    /**
     * txUrl的base64编码，用于用户对txUrl进行校验
     */
    private String txtUrlEncode;
    private String pngUrlEncode;

}

