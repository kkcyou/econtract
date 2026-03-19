package com.yaoan.module.econtract.dal.dataobject.term;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_term")
public class ContractTermDO extends BaseDO implements Serializable {

    /**
     * 合同条款id --- 主键
     */
    @TableId(value = "contract_term_id", type = IdType.ASSIGN_UUID)
    private String contractTermId;

    /**
     * id
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 条款编码
     */
    private String termCode;

    /**
     * 条款名称
     */
    private String termName;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    private String termType;
    /**
     * 条款内容
     */
    private String termContent;
    /**
     * 条款序号
     */
    private Integer termNum;

    /**
     * 是否展示序号
     */
    private Boolean showSort;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    private String title;

    private String termComment;


    /**
     * ---------------------------------------------------------------- 内蒙追加 ----------------------------------------------------------------
     * */

    /**
     * 是否展示名称
     */
    private Boolean showName;
    /**
     * 能否编辑 0能编辑  1不能编辑
     */
    private Integer editable;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;


    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;
}
