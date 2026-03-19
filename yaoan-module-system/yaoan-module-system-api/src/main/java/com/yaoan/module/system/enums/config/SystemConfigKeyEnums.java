package com.yaoan.module.system.enums.config;

import com.yaoan.module.econtract.enums.breakpointtype.BreakpointTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 18:37
 */
@Getter
@AllArgsConstructor
public enum SystemConfigKeyEnums {

    /**
     * 系统动态配置 枚举类
     */
    IF_FLOWABLE("if_flowable", "是否走审批流"),
    IF_BATCH_SUBMIT("if_batch_submit", "是否批量提交"),
    IF_BATCH_APPROVE("if_batch_approve", "是否批量审批"),
    REGION_DATA_VERSION("region_data_version","区划数据版本"),
    APPROVER_PERMISSION_SCAN_ANNOTATION("approver_permission_scan_annotation","审批人的浏览批注的权限"),
    CARBON_COPY_PERMISSION_SCAN_ANNOTATION("carbon_copy_permission_scan_annotation","抄送人的浏览批注的权限"),
    DEPOSIT_MONEY_PAY_MAX_RATIO("deposit_money_pay_max_ratio","履约保证金及质量保证金的最高可支付比例"),

    REQUIRED_MODEL_CATEGORY("required_model_category", "上线的模板分类"),
    IS_BACK("is_back", "是否托底"),
    IS_CHECK_EVALUATE("is_check_evaluate", "是否检查信用评价"),
    AGENCY_REGION("agency_region", "需要代理机构审核的区划"),
    AGENCY_ID("agency_id", "指定代理机构的用户id"),
    IF_PASS_COUNT("if_pass_count", "是否通过的指定的供应商或者采购单位的总数量"),

    /**
     * 文件服务域名
     */
    FILE_SERVER_DOMAIN("file_server_domain", "文件服务域名"),

    /**
     * 文件转换格式是否用自己的方法
     */
    IF_FILE_CONVERT_SELF("if_file_convert_self", "文件转换格式是否用自己的方法"),
    /**
     * 是否需要走监管新版本的接口  n：否  y：是    2024-07-15 新增
     */
    IF_NEW_JIANGUAN("if_new_jianguan", "是否需要走监管新版本的接口"),
    /**
     * 版本号 V1.0--》走1.0 V2.0 走2.0
     */
    VERSION_NUM("version_num", "版本号配置"),
    /**
     * 协议定点设置是否开启采购人确认合同后允许退回合同至草稿开关is_return_draft_by_org:y:是  n：否
     */
    IS_RETURN_DRAFT_BY_ORG("is_return_draft_by_org", "协议定点设置是否开启采购人确认合同后允许退回合同至草稿开关"),


    /**
     * 合同借阅pdf加水印枚举类
     */
    BORROW_PDF_WATERMARK("borrow_pdf_watermark","合同借阅pdf加水印"),
    /**
     * 合同签署pdf加水印枚举类
     */
    SIGN_PDF_WATERMARK("sign_pdf_watermark","合同签署pdf加水印"),


    MODEL_MAX_COUNT_PER_CATEGORY("model_max_count_per_category","每个模板分类下可存在的模板的最大数量"),
    REGION_ROOT("region_root","区划根节点id"),

    CODE_RULE_MODEL("code_rule_model","模板通用编号生成规则"),
    CODE_RULE_TERM("code_rule_term","条款通用编号生成规则"),
    CODE_RULE_TEMPLATE("code_rule_template","范本通用编号生成规则"),
    CODE_RULE_INVOICE("code_rule_invoice","收款通用编号生成规则"),
    IF_NEED_SIGNET("if_need_signet","是否走用印审批"),

    IF_ORDER_MERGE("if_order_merge","是否订单合并"),

    MODEL_COUNT_CONTROL("model_count_control","单位端政采和非政采模板数量控制：货物，服务，工程，非政采"),

    IF_CONVERT_DOC("if_convert_doc", "是否需要转成doc文件"),

    IF_NEED_MODEL_CATEGORY("if_need_model_category", "是否需要模板分类"),
    /**
     * PDF是否添加XML
     */
    IF_ADD_XML("if_add_xml", "PDF是否添加XML"),

    /**
     * {@link BreakpointTypeEnums}
     * */
    BREAKPOINT_TYPE("breakpoint_type", "断点续传类型"),

    ;

    private final String key;
    private final String info;


    public static SystemConfigKeyEnums getInstance(String key) {
        for (SystemConfigKeyEnums value : SystemConfigKeyEnums.values()) {
            if (value.getKey().equals(key)) {
                return value;
            }
        }
        return null;
    }
}
