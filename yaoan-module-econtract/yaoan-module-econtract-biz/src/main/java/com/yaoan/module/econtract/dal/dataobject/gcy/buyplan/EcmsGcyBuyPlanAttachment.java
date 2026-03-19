package com.yaoan.module.econtract.dal.dataobject.gcy.buyplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 采购附件表
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_buy_plan_attachment")
public class EcmsGcyBuyPlanAttachment extends BaseDO {


    private static final long serialVersionUID = -5073411199519556784L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 采购计划唯一识别码
     */
    private String buyPlanGuid;

    /**
     * 附件条目唯一识别码
     */
    private String attachmentGuid;

    /**
     * 附件类型(参见采购附件类型定义)。此处该参数为：采购计划备案/核准书(BuyPlanAtt-0010) (必备附件)。
     */
    private String attachmentType;

    /**
     * 文档名称
     */
    private String fileName;

    /**
     * 附件的全路径URL
     */
    private String filePath;

}
