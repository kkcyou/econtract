package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @description: 商品采购目录实体类
 * @author: zhc
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_goods_pur_catalog")
public class GoodsPurCatalogDO extends BaseDO {
    private static final long serialVersionUID = 6684881310480119200L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 商品主键ID--曜安表中的主键ID
     */
    private String goodsId;
    /**
     * 商品Guid
     */
    private String goodsGuid;

    /**
     * 采购目录编码
     */
    private String purCatalogCode;
    /**
     * 采购目录编码名称
     */
    private String purCatalogName;


}
