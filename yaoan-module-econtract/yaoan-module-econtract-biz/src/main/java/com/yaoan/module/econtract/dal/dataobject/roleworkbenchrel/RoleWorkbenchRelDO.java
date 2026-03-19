package com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 角色工作台关联 DO
 *
 * @author admin
 */
@TableName("ecms_role_workbench_rel")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleWorkbenchRelDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 工作台id
     */
    private String workbenchId;
    /**
     * 工作台名称
     */
    private String workbenchName;

}
