package com.yaoan.module.econtract.dal.dataobject.watermark;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.watermark.WatermarkTypeEnums;
import lombok.*;

/**
 * 水印管理 DO
 *
 * @author lls
 */
@TableName("ecms_watermark")
@KeySequence("ecms_watermark_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WatermarkDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 水印名称
     */
    private String name;

    private String content;
    /**
     * 水印类型
     * {@link WatermarkTypeEnums}
     * 自定义文字水印 = 0 业务字段 = 1
     */
    private Integer type;
    /**
     * 字号
     */
    private Integer watermarkSize;
    /**
     * 倾斜角度
     */
    private Integer watermarkAngle;
    /**
     * 水印透明度
     */
    private Integer watermarkAlpha;
    /**
     * 水印位置 充满full 
     */
    private String position;
    /**
     * 图片id 
     */
    private Integer fileId;
    /**
     * 图片url
     */
    private String fileUrl;
    /**
     * 图片宽度
     */
    private String picWidth;
    /**
     * 图片高度
     */
    private String picHeight;
    /**
     * 部门标识
     */
    private Long deptId;
 

}
