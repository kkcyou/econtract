package com.yaoan.module.econtract.dal.dataobject.gpx;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 15:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gpx_package_detail_param")
public class PackageDetailParamDO extends BaseDO {
    private static final long serialVersionUID = -6291409441620778617L;
    ;
    /**
     * 主键id
     */
    @TableId(value = "id")
    private String id;
    /**
     * 明细ID
     */
    private String detailId;
    /**
     * 包id
     */
    private String packageId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 序号
     */
    private Integer paramNum;
    /**
     * 自定义序号
     */
    private String paramNumStr;
    /**
     * 明细名称
     */
    private String deatilName;
    /**
     * 加星参数 0/1 否/是
     */
    private Integer parameterStar;
    /**
     * 技术参数
     */
    private String detailParameter;
}
