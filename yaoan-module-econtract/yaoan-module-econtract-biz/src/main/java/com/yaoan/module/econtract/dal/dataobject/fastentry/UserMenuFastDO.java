package com.yaoan.module.econtract.dal.dataobject.fastentry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联
 *
 * @author ruoyi
 */
@TableName("ecms_user_menu_fast")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserMenuFastDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户 ID
     */
    private Long userId;
    /**
     * 角色 ID
     */
    private Long menuId;

    /**
     * 排序
     */
    private Long sort;


    private Long tenantId;

}
