package com.yaoan.module.econtract.enums;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/16 18:11
 */
public class StatusConstants {

    public static final String ADMIN_ID = "1";


    public static final int INSERT_SUCCESS = 1;
    public static final Boolean BOOLEAN_SUCCESS = true;

    public static final int TEMP_INTEGER = 999;
    public static final int STATUS_IS_ACTIVE = 1;
    public static final int STATUS_IS_INACTIVE = 0;

    /**
     * wps的服务地址
     */
    public static final String WPS_DOWNLOAD_URL = "http://120.53.0.50:48080/admin-api/econtract/wps" + "/weboffice/getFile?_w_fileid=";
//    private static final String WPS_DOWNLOAD_URL = "http://81.70.160.180:48080/admin-api/econtract/wps" + "/weboffice/getFile?_w_fileid=";

    /**
     * 每个图片保持在 6-7kb（要求：低于10kb）
     */
    public static final int PDF_PICS_DPI = 10;


    /**
     * 准备上传的目录的路径
     */
//    public static final String READY_TO_UPLOAD_PATH = "C:/Users/Pele/Desktop/text/ready_to_up";
    public static final String READY_TO_UPLOAD_PATH = "/data/file/ready_to_up";
//    public static final String READY_TO_UPLOAD_PATH = "C:/Users/lml13/Desktop/";
    public static final String MODEL_RTF_PDF_TEMP_PATH = "/Users/doujiale/Desktop/";
    //        public static final String MODEL_RTF_PDF_TEMP_PATH = "C:/Users/Pele/Desktop/text/ready_to_up";
    public static final String JPG = ".jpg";

    public static final String SUFFIX_DOC = "doc";
    public static final String SUFFIX_DOCX = "docx";
    public static final String SUFFIX_PDF = "pdf";
    public static final String SUFFIX_OFD = "ofd";
    public static final String CONTAIN_SUFFIX_TYPE = "类型";

    /**
     * 标识
     */
    public static final Integer AMOUNT_FLAG_MONEY = 8;
    public static final Integer AMOUNT_FLAG_NUMBER = 1;

    /**
     * 拼接内容
     */
    public static final String SUFFIX_PENDING = " 待办";
    public static final String PLEASE_PAY_ATTENTION = "  ，请关注。";
    public static final String WAITE_TO_BE_APPROVED = "待审批";
    public static final String WAITE_TO_BE_HANDLE = "待处理";
    public static final String WAITE_TO_BE_CREATE_CONTRACT = "待起草合同";
    public static final String FILE_TYPE_DOC = ".doc";
    public static final String FILE_TYPE_PDF = ".pdf";
    public static final String REDIS_CONTRACT_ID = "c_id_";
    public static final String REDIS_QUOTATION_ID = "qua_id_";

    /**
     * 付款管理常量
     */
    public static final String TO_APPROVE = " 待审批";
    public static final String REJECTED = " 被退回";
    public static final Integer TO_APPROVE_INDEX = 11;
    public static final Integer REJECTED_INDEX = 22;
}
