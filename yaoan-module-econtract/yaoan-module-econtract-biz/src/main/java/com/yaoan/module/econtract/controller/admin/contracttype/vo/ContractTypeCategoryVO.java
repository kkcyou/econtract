package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 14:37
 */
@Data
public class ContractTypeCategoryVO {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 分类编码
     */
    private String code;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 子级list
     */
    private List<ContractTypeCategoryVO> childrenList;

    /**
     * 叶子节点合同类型数据list
     */
    private List<ContractType> dataList;
}
