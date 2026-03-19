package com.yaoan.module.econtract.controller.admin.codegeneration.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/9 10:11
 */
@Data
public class CodeGenDetailRespVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码类型/业务类型
     */
    private String businessType;
    /**
     * 业务名称
     */
    private String businessName;
    /**
     * 业务名称
     */
    private String childrenType;
    /**
     * 子类类型标签
     */
    private String childrenTypeTag;

    /**
     * 编码格式
     */
    private String generateRule;

    /**
     * 需要长度
     */
    private Integer length;

    /**
     * 业务前缀
     */
    private String prefix;

    /**
     * 备注
     */
    private String remark;

    private String creator;

    private String creatorName;
    private LocalDateTime createTime;
    /**
     * 状态
     */
    private Integer status;
}
