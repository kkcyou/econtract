package com.yaoan.module.econtract.enums;

import com.yaoan.framework.common.exception.ErrorCode;

/**
 * econtract 错误码枚举类
 * <p>
 * 使用 2开头
 *
 * @author Pele
 */
public interface ErrorCodeConstants {
    /**
     * 通用异常 2090000~2090099
     */
    ErrorCode CODE_EXIST_ERROR = new ErrorCode(2090001, "编码已存在");
    ErrorCode EMPTY_DATA_ERROR = new ErrorCode(2090002, "检索数据为空");
    ErrorCode EMPTY_PIC_DATA_ERROR = new ErrorCode(2090003, "图片数据为空");
    ErrorCode EMPTY_FIELD_ERROR = new ErrorCode(2090004, "检索数据字段属性为空");
    ErrorCode EMPTY_REQUIRE_PARAM_ERROR = new ErrorCode(2090005, "请求参数为空");
    ErrorCode FLOWABLE_SUBMIT_REQUEST_ERROR = new ErrorCode(2090006, "工作流提交请求参数异常");
    ErrorCode SYSTEM_ERROR = new ErrorCode(2090007, "{}");
    ErrorCode EMPTY_DATA_ERROR_V2 = new ErrorCode(2002708, "{}检索数据为空");
    ErrorCode UPLOAD_ERROR = new ErrorCode(2090009, "上传文件异常");

    /**
     * 模板相关 2001000 ~ 2001099
     */
    ErrorCode MODEL_ERROR = new ErrorCode(2001000, "模板操作异常");
    ErrorCode MODEL_EMPTY_PARAM_ERROR = new ErrorCode(2001001, "模板参数为空异常");
    ErrorCode MODEL_INSERT_UPLOAD_ERROR = new ErrorCode(2001002, "模板新增上传异常");
    ErrorCode MODEL_INSERT_UPDATE_TYPE_ERROR = new ErrorCode(2001002, "模板新增上传时，更新类型状态异常");
    ErrorCode MODEL_DOWNLOAD_ERROR = new ErrorCode(2001003, "模板下载异常");
    ErrorCode SUBMIT_ERROR__EMPTY_PARAM = new ErrorCode(2001004, "参数为空，提交审批失败");
    ErrorCode EDITE_ERROR_STATUS_ACTIVE = new ErrorCode(2001005, "存在启动状态模板，编辑失败");
    ErrorCode DELETE_ERROR_EMPTY_PARAM = new ErrorCode(2001006, "参数为空，删除失败");
    ErrorCode DELETE_ERROR_STATUS_ACTIVE = new ErrorCode(2001007, "存在启动状态模板，删除失败");
    ErrorCode MODEL_UPDATE_ERROR = new ErrorCode(2001008, "模板更新失败");
    ErrorCode DELETE_ERROR = new ErrorCode(2001009, "模板删除失败");
    ErrorCode MODEL_STATUS_NOT_MODIFIABLE = new ErrorCode(2001010, "只有模板状态为待送审或者审批未通过，才可以编辑或删除。");
    ErrorCode MODEL_CHECK_ID = new ErrorCode(2001011, "模板id不可为空");
    ErrorCode MODEL_CHECK_NAME = new ErrorCode(2001012, "模板id不可为空");
    ErrorCode MODEL_CHECK_CATEGORY_ID = new ErrorCode(2001013, "模板分类id不可为空");
    ErrorCode MODEL_CHECK_CONTRACT_TYPE = new ErrorCode(2001014, "模板的合同类型不可为空");
    ErrorCode MODEL_CHECK_TIME_EFFECT_MODEL = new ErrorCode(2001015, "模板的时效模式不可为空");
    ErrorCode MAXLENTH_IS_NULL = new ErrorCode(20010156, "字数长度要求不能为空");
    ErrorCode MODEL_CHECK_CONTRACT_TYPE_EMPTY = new ErrorCode(2001016, "{}");
    ErrorCode MODEL_CATEGORY_CHECK_EMPTY = new ErrorCode(2001017, "{}");
    ErrorCode MONGOLIA_TEMPLATE_EXIST = new ErrorCode(20010157, "全部或部分区划已有相同模板类型、模板形式或品目的合同模板");
    ErrorCode MODEL_NOT_EXISTS = new ErrorCode(20010001, "模板文件不存在");
    ErrorCode FILE_UPLOAD_ERROR = new ErrorCode(200300, "文件上传失败");

    ErrorCode MODEL_CATEGORY_EXISTS_MODELS_ERROR = new ErrorCode(2001105, "模板分类下已存在{}个模板，请联系管理人新增模板");
    ErrorCode MODEL_CATEGORY_NOT_MODELS_ERROR = new ErrorCode(2001106, "通用类模板分类下均不存在模板，请新增模板");
    ErrorCode MODEL_NOT_FIND_EMPTY = new ErrorCode(200101, "该合同甲方：{}尚未确认合同模版，无法起草合同，请先联系采购人确认模版！");



    /**
     * 模板分类相关 2001100 ~ 2001199
     */
    ErrorCode MODEL_CATEGORY_ERROR = new ErrorCode(2001100, "模板分类操作异常");
    ErrorCode MODEL_CATEGORY_EMPTY_PARAM_ERROR = new ErrorCode(2001101, "模板分类参数为空异常");
    ErrorCode MODEL_CATEGORY_SUBMODEL_ACTIVE_ERROR = new ErrorCode(2001102, "模板分类所属的模板处于启用状态，导致模板分类操作异常");
    ErrorCode MODEL_CATEGORY_NAME_EXISTS_ERROR = new ErrorCode(2001102, "模板分类名称已存在，导致模板分类操作异常");
    ErrorCode MODEL_CATEGORY_CODE_EXISTS_ERROR = new ErrorCode(2001102, "模板分类编号已存在，导致模板分类操作异常");


    /**
     * 台账相关 2001200 ~ 2001299
     */
    ErrorCode LEDGER_ERROR = new ErrorCode(2001200, "台账操作异常");

    /**
     * 合同类型 2001300 ~ 2001399
     */
    ErrorCode CONTRACT_TYPE_EMPTY_PARAM_ERROR = new ErrorCode(2001301, "合同类型参数为空，导致异常");
    ErrorCode CONTRACT_TYPE_USED_ERROR = new ErrorCode(2001302, "合同类型已被调用，不可删除");
    ErrorCode CONTRACT_TYPE_NAME_EXISTS_ERROR = new ErrorCode(2001303, "合同类型名称已存在，导致异常");
    ErrorCode CONTRACT_TYPE_CODE_EXISTS_ERROR = new ErrorCode(2001304, "合同类型编号已存在，导致异常");
    ErrorCode CONTRACT_PAGE_PARAM_ERROR = new ErrorCode(2001305, "合同请求status错误");
    ErrorCode CONTRACT_CONFIGURE_ERROR = new ErrorCode(2001306, "请先配置好【编号规则】和【审批流程】");
    ErrorCode CONTRACT_STOP_ERROR = new ErrorCode(2001307, "合同类型正在调用中，不可停用");
    ErrorCode CONTRACT_FIVE_ERROR = new ErrorCode(2001307, "当前层级已经达到5级，不允许继续添加");

    ErrorCode EXIST_DRAFT = new ErrorCode(20024006, "该计划已发起合同");

    /**
     * 范本相关 2001400 ~ 2001499
     */
    ErrorCode TEMPLATE_ERROR = new ErrorCode(2001400, "合同范本操作异常");
    ErrorCode TEMPLATE_SAVE_UPLOAD_ERROR = new ErrorCode(2001401, "合同范本上传保存异常");
    ErrorCode TEMPLATE_SAVE_UPLOAD_UNKNOWN_FILE_ERROR = new ErrorCode(2001402, "合同范本上传保存异常,未知文件格式");
    ErrorCode TEMPLATE_SAVE_UPLOAD_EMPTY_PUBLISH_TIME_ERROR = new ErrorCode(2001403, "官方范本,发布时间不可为空");
    ErrorCode TEMPLATE_SAVE_UPLOAD_EMPTY_PUBLISH_ORGANIZATION_ERROR = new ErrorCode(2001404, "官方范本,发布机构不可为空");
    ErrorCode TEMPLATE_SAVE_UPLOAD_UNKNOWN_TEMPLATE_SOURCE_ERROR = new ErrorCode(2001405, "未知范本来源");
    ErrorCode TEMPLATE_UPLOAD_APPROVED_ERROR = new ErrorCode(2001406, "无法操作开启审批流的范本");
    ErrorCode TEMPLATE_DELETE_APPROVED_ERROR = new ErrorCode(2001407, "无法删除开启审批流的范本");
    ErrorCode TEMPLATE_FILE_DELETE_ERROR = new ErrorCode(2001408, "无法删除异常");
    ErrorCode TEMPLATE_FILE_UPLOAD_ERROR = new ErrorCode(2001409, "范本文件不能为空");
    ErrorCode TEMPLATE_FILE_NOT_FOUND_ERROR = new ErrorCode(2001410, "范本源文件未找到");
    ErrorCode TEMPLATE_FILE_CONTENT_ERROR = new ErrorCode(2001411, "范本内容不能为空");
    ErrorCode TEMPLATE_EMPTY_ERROR = new ErrorCode(2001412, "参考文本数据为空");
    ErrorCode TEMPLATE_CATEGORY_EMPTY_ERROR = new ErrorCode(2001413, "参考文本分类数据为空");
    ErrorCode FILE_NAME_NOT_RIGHT = new ErrorCode(2003002, "文件类型错误,只支持PDF格式");

    /**
     * 履约任务类型相关 2001500 ~ 2001599
     */
    ErrorCode PERFORM_TASK_TYPE_NAME_EXIST_ERROR = new ErrorCode(2001501, "履约任务类型名称已存在，请重新输入");
    ErrorCode PERFORM_TASK_TYPE_CODE_EXIST_ERROR = new ErrorCode(2001502, "履约任务类型编码已存在，请重新输入");
    ErrorCode PERFORM_TASK_TYPE_NO_PERMISSION_UPDATE_ERROR = new ErrorCode(2001503, "无权限更改当前履约任务类型");
    ErrorCode PERFORM_TASK_TYPE_NO_PERMISSION_DELETE_ERROR = new ErrorCode(2001504, "无权限删除当前履约任务类型");
    ErrorCode PERFORM_TASK_EMPTY_ERROR = new ErrorCode(2001505, "检索到相应的履约任务异常");
    ErrorCode PERFORM_TASK_TYPE_IS_NULL = new ErrorCode(2001506, "履约任务类型不能为空");
    ErrorCode PERFORM_TASK_NAME_IS_NULL = new ErrorCode(2001507, "履约任务名称不能为空");
    ErrorCode PERFORM_TASK_NOT_ACCEPTANCE = new ErrorCode(2001508, "请先进行验收");
    ErrorCode PERFORM_TASK_NOT_APPROVAL = new ErrorCode(2001509, "请先处理验收申请");
    ErrorCode PERFORM_TASK_NOT_ACCEPTANCE_AGAIN = new ErrorCode(2001510, "验收不通过请重新发起验收");
    ErrorCode PERFORM_TASK_NOT_PAYMENT_DELAY = new ErrorCode(2001511, "请先处理付款延期申请");
    /**
     * 参数相关 2002000 ~ 2002099
     */
    ErrorCode IMG_NOT_NULL = new ErrorCode(2002000, "图片不可为空");

    /**
     * 参数分类相关 2002100 ~ 2002199
     */
    ErrorCode DATA_IS_CALL = new ErrorCode(2002001, "此数据已被调用，不可删除");
    ErrorCode NAME_EXISTS = new ErrorCode(2002003, "名称已存在");
    ErrorCode CODE_EXISTS = new ErrorCode(2002004, "编码已存在");

    /**
     * 相对方和联系人相关 2002200 ~ 2002299
     */
    ErrorCode USER_ERROR = new ErrorCode(2002204, "用户不存在");
    ErrorCode COMPNAY_ERROR = new ErrorCode(2002206, "联系人所属公司与此相对方公司不匹配");
    ErrorCode COMPNAY_EXISTS = new ErrorCode(2002207, "单位名称或信用代码已存在");
    ErrorCode IDCARD_EXISTS = new ErrorCode(2002208, "身份证号已存在");
    ErrorCode DEPT_NOT_EXISTS = new ErrorCode(2002209, "部门不存在");
    /**
     * 履约管理 2002300 ~ 2002399
     */
    ErrorCode POSTTASK_TO_early = new ErrorCode(2002300, "前置任务履约时间必须早于后置任务的履约时间");
    ErrorCode NOT_DEL_ADN_UPDATE = new ErrorCode(2002301, "仅履约中、待履约、履约任务未开始的履约任务可编辑、删除");
    ErrorCode CREATE_REMIND_ERROR = new ErrorCode(2002301, "提醒设置已存在不可新增");
    ErrorCode IN_PERFORMANCE_ERROR = new ErrorCode(2002306, "履约中，只可以改为履约暂停，履约结束，履约完成");
    ErrorCode PERFORMANCE_PAUSE_ERROR = new ErrorCode(2002307, "履约暂停，只可以改为履约中，履约结束，履约完成");
    ErrorCode PERFORMANCE_OVER_TIME_ERROR = new ErrorCode(2002308, "履约超期，只可以改为超期暂停，超期结束，超期完成");
    ErrorCode OVER_TIME_PAUSE_ERROR = new ErrorCode(2002309, "超期暂停，只可以改为履约超期，超期结束，超期完成");
    ErrorCode SURE_PERF_ERROR = new ErrorCode(2002310, "超期完成，超期结束，履约完成，履约结束，待履约不可确认履约");
    ErrorCode PERFORMANCE_TIME_ERROR = new ErrorCode(2002311, "履约时间仅可在合同开始日期之后");
    ErrorCode PERFORMANCE_TIME_ERROR2 = new ErrorCode(2002312, "履约时间仅可在合同开始日期及结束日期之间");
    ErrorCode PERFORMANCE_EXIST = new ErrorCode(2002313, "此合同已存在，不能重复建立履约");
    ErrorCode PAYMENT_EXCEEDS_CONTRACT = new ErrorCode(2002313, "款项金额大于合同总金额,请重新添加履约计划");
    ErrorCode PAYMENT_PAYMENTTIME_EARLY = new ErrorCode(2002314, "本期计划支付日期不能小于上期支付计划日期或大于下期支付计划日期");


    /**
     * 签章相关 2003000 ~ 2003099
     */
    ErrorCode SIGN_ERROR = new ErrorCode(2003000, "签章操作异常");

    /**
     * 合同管理 2002400 ~ 2002499
     */
    ErrorCode CONTRACT_NAME_NOT_EXISTS = new ErrorCode(2002400, "合同不存在");
    ErrorCode TERMINATE_CONTRACT_NOT_EXISTS = new ErrorCode(2002401, "要终止的合同不存在");
    ErrorCode TERMINATE_CONTRACT_EXISTS = new ErrorCode(2002402, "终止合同已存在");
    ErrorCode CONTRACT_COMPNAY_ERROR = new ErrorCode(2002403, "合同履约不匹配");
    ErrorCode CONTRACT_DOCUMENT_EXISTS = new ErrorCode(2002404, "合同已归档");
    ErrorCode CONTRACT_BPM_EXISTS = new ErrorCode(20024005, "该合同已提交申请，不可重复提交！");
    ErrorCode NO_DATA_FIND_ERROR = new ErrorCode(2003000, "标识未检测到可用数据错误");
    ErrorCode FILE_NAME_NOT_NULL = new ErrorCode(2003001, "文件名不能为空");
    ErrorCode CONTRACT_EXPIRE = new ErrorCode(20024006, "合同已过期，请编辑后重新提交审批！");
    ErrorCode CONTRACT_IS_LOCKED = new ErrorCode(20024007, "合同{}已锁定，不允许操作！");
    ErrorCode CONTRACT_IS_CHECKING = new ErrorCode(20024008, "合同审批中，不允许撤销！");

    /**
     * 条款管理 2002800 ~ 2002899
     */
    ErrorCode UNCHANGEABLE_ERROR = new ErrorCode(2002800, "该条款已发布，不可修改！");
    ErrorCode TERM_APPLIED_ERROR = new ErrorCode(2002801, "该条款不在待申请状态，不可重复提交！");

    /**
     * 关联关系 2002500 ~ 2002599
     */
    ErrorCode CANCEL_TYPE_ERROR = new ErrorCode(2002500, "合同变更协议不可取消");
    ErrorCode OUTLINE_AGREEMENT_REPETITION = new ErrorCode(2002501, "已有框架协议，不可重复关联");
    ErrorCode CONTRACT_CHANGE_AGREEMENT_REPETITION = new ErrorCode(2002502, "已有变更协议，不可重复关联");
    ErrorCode CONTRACT_RENEWAL_AGREEMENT_REPETITION = new ErrorCode(2002503, "已有合同续签协议，不可重复关联");
    ErrorCode OTHER_AGREEMENT_REPETITION = new ErrorCode(2002504, "已有其它协议，不可重复关联");
    ErrorCode CONTRACT_CHANGE_ERROR = new ErrorCode(2002505, "已终止的合同不可变更");
    ErrorCode OUTLINE_AGREEMENT_ERROR = new ErrorCode(2002506, "该合同已被关联为框架协议，暂不支持框架协议变为子合同");
    ErrorCode CONTRACT_RENEWAL_CHANGE_ERROR = new ErrorCode(2002507, "变更合同、续签合同不可为其他合同的框架协议");
    ErrorCode MASTER_CONTRACT_EXIST_OUTLINE_AGREEMENT = new ErrorCode(2002508, "主合同框架协议已关联框架协议，则不可重复关联");
    /**
     * 系统对接  2002600 ~ 2002699
     */
    ErrorCode SEND_EXIST_ERROR = new ErrorCode(2002600, "此数据已推送，请勿重复推送");

    ErrorCode REASON_NOT_NULL = new ErrorCode(2002601, "退回原因不可为空");

    /**
     * 付款管理 2002700 ~ 2002799
     */
    ErrorCode PAYMENT_APPLICATION_SEND_EXIST_ERROR = new ErrorCode(2002700, "此数据已提交申请，请勿重复提交");
    ErrorCode PAYMENT_SCHEDULE_SAVE_EXIST_ERROR = new ErrorCode(2002701, "支付计划已申请，请勿重复申请");
    ErrorCode PAYMENT_SCHEDULE_NOT_PAYED_ERROR = new ErrorCode(2002702, "需要等之前的支付计划,先申请通过");
    ErrorCode PAYMENT_SCHEDULE_NOT_PAYED_FOR_SINGLE_ERROR = new ErrorCode(2002703, "{}期支付计划已申请，请勿重复提交");
    ErrorCode PAYMENT_SCHEDULE_NOT_PAID_FOR_SINGLE_ERROR = new ErrorCode(2002707, "该支付计划已发起付款不可再发起变动");

    /**
     * 订单相关 2002900 ~ 2002999
     */
    ErrorCode EXIST_ORDER_GUID = new ErrorCode(2002900, "存在订单重复推送");

    /**
     * 合同变更 2003000 ~ 2003099
     */
    ErrorCode CONTRACT_CHANGE_APPROVING = new ErrorCode(2003000, "该合同有变动协议未完成审批。请等待变更协议完成审批");
    ErrorCode CONTRACT_CHANGE_UPDATE_ERROR = new ErrorCode(2003001, "该变动协议处于审批中不可编辑");
    ErrorCode CONTRACT_CHANGE_SAVE_ERROR = new ErrorCode(2003002, "该变动协议保存失败");
    ErrorCode CONTRACT_CHANGE_UPDATE_SUBMIT_ERROR = new ErrorCode(2003003, "该变动协议编辑提交失败");
    ErrorCode CONTRACT_CHANGE_SAVE_SUBMIT_ERROR = new ErrorCode(2003004, "该变动协议保存提交失败");

    /**
     * 合同借阅申请 2003100 ~ 2003199
     */
    //申请编辑失败
    ErrorCode CONTRACT_BORROW_EDIT_ERROR = new ErrorCode(2003100, "未找到业务数据");

    /**
     * 系统对接错误 2003200 ~ 2003299
     */
    /**
     * 验签失败
     */
    ErrorCode SIGN_VERIFY_ERROR = new ErrorCode(2003200, "验签失败");
    /**
     * 无加密信息
     */
    ErrorCode NO_ENCRYPT_INFO = new ErrorCode(2003201, "无加密信息");

    /**
     * 表单配置 2003300 ~ 2003399
     */
    ErrorCode EXIST_DBNAME = new ErrorCode(2003301, "数据字段已存在");
    /**
     * 内蒙电子卖场订单相关 2002700 ~ 2002799
     */
    ErrorCode REQUEST_OUT_OF_BOUNDS = new ErrorCode(2002700, "请求查询数量过大（查询不可超过100条）");
    ErrorCode UNKNOWN_CONTRACT_ID = new ErrorCode(2002702, "存在未知合同id");
    ErrorCode PUSH_ERROR = new ErrorCode(2002703, "订单id不存在，请检查该id是否正确");
    ErrorCode GOMall_Query_Error = new ErrorCode(2002704, "{}");
    ErrorCode ORDER_ID_NULL = new ErrorCode(2002705, "订单id不能为空");
    ErrorCode ORDER_DATA_NULL = new ErrorCode(2002706, "订单数据为空");


    ErrorCode PLAN_IN_USED_ERROR = new ErrorCode(2002800, "计划有关联合同数据，不可删除");

    /**
     * 黑龙江参考文本相关 2002800 ~ 2002899
     */
    ErrorCode EMPTY_TERM_ERROR = new ErrorCode(2002801, "条款不可为空");

    /**
     * 印章相关 2002900 ~ 2002999
     */
    ErrorCode EMPTY_NO_LONG_TERM_ERROR = new ErrorCode(2002901, "印章非长期有效，无法授权");
    ErrorCode AUTHORIZE_DATE_OUT_OF_SIGNET_VALIDITY_ERROR = new ErrorCode(2002902, "授权日期不在印章有效期内");
    ErrorCode AUTHORIZE_REPEAT_ERROR = new ErrorCode(2002902, "重复授权");
    ErrorCode SEAL_RANGE_ERROR = new ErrorCode(2002903, "印章使用范围错误");
    ErrorCode SEAL_EXPIRE_ERROR = new ErrorCode(2002903, "印章已过期，无法修改状态");
    ErrorCode SEAL_UPDATE_STATUS_ERROR = new ErrorCode(2002903, "印章修改状态错误");
    ErrorCode SEAL_ENABLED_ERROR = new ErrorCode(2002903, "印章已启用");
    ErrorCode SEAL_DISABLED_ERROR = new ErrorCode(2002903, "印章已停用");


    /**
     * 归档管理 2003000 ~ 2003999
     */
    ErrorCode CONTRACT_ARCHIVES_NOT_EXISTS = new ErrorCode(2003000, "合同档案不存在");
    ErrorCode CONTRACT_ARCHIVES_EXISTS = new ErrorCode(2003001, "合同档案已存在");
    ErrorCode CONTRACT_ARCHIVES_ID_NOT_EXISTS = new ErrorCode(2003002, "id不存在");
    ErrorCode CONTRACT_ARCHIVES_PAPER_NO_RETURN = new ErrorCode(2003002, "纸质合同未归还，不可借阅");
    ErrorCode CONTRACT_ARCHIVES__NO_PAPER = new ErrorCode(2003002, "无纸质合同，不可借阅");
    /**
     * 编号管理 2004000 ~ 2004999
     */
    ErrorCode CODE_NAME_EXISTS_ERROR = new ErrorCode(2004000, "名字已存在，请重新输入");

    /**
     * 风险预警管理 2005000 ~ 2005999
     */
    ErrorCode COUNTERPART_CREDIT_ERROR = new ErrorCode(2005000, "【{}】相对方信用评分低于阈值，为{}分，可能导致合同履行风险，建议对供应商进行资信调查。");
    ErrorCode COUNTERPART_OPERATE_ERROR = new ErrorCode(2005001, "相对方经营情况存在异常，可能导致合同履行风险，建议对供应商进行资信调查。");
    /**
     * 数据缺失
     */
    ErrorCode SERVER_ERROR = new ErrorCode(500, "系统异常");
    ErrorCode DATA_MISSING = new ErrorCode(-1, "{}");
    ErrorCode SIGN_ERROR2 = new ErrorCode(-2, "签名错误，数据可能被篡改！");
    ErrorCode DATA_EXIST = new ErrorCode(-3, "{}");
    ErrorCode DATA_DEAL_ERROR = new ErrorCode(-4, "{}");

    ErrorCode CONTRACT_BACK_ERROR = new ErrorCode(2002901, "{}");
    ErrorCode DIY_ERROR = new ErrorCode(2002902, "{}");
    ErrorCode DATA_ERROR = new ErrorCode(2002903, "数据异常，请联系管理员~");

}