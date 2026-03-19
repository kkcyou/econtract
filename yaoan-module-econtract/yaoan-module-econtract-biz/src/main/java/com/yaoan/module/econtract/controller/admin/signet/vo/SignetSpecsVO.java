package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 印章规格表
 * @Author  WSH
 * @Date: 2024-10-08 11:24:38
 */

@Data
public class SignetSpecsVO{
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 编码
     */
    private String code;

    /**
     * 印章形状
     */
    private String shape;


    /**
     * 圆的半径/长方形/椭圆/正方形的长度（mm）
     */
    private Double high;

    /**
     * 长方形/椭圆/正方形的宽度（mm）
     */
    private Double width;

}
