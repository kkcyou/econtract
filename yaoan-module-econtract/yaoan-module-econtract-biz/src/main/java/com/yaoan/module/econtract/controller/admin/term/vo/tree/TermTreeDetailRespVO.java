package com.yaoan.module.econtract.controller.admin.term.vo.tree;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/9 17:11
 */
@Data
public class TermTreeDetailRespVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 条款编码
     */
    private String code;

    /**
     * 条款名称
     */
    private String name;

    /**
     * 条款分类id
     */
    private String categoryId;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     */
    private String termType;


    /**
     * 条款内容
     */
    private String termContent;




}
