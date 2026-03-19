package com.yaoan.module.econtract.dal.dataobject.contract;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;

@Data
@TableName("ecms_contract_xml_config")
public class ContractXmlConfigDO extends BaseDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同配置名称
     */
    private String name;

    /**
     * 合同配置编码
     */
    private String code;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 状态(0:关闭,1:开启)
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;

    /**
     * XML配置内容
     */
    private String xmlContent;
    private String configJson;
}
