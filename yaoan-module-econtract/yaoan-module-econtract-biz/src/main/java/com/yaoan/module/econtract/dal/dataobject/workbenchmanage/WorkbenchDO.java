package com.yaoan.module.econtract.dal.dataobject.workbenchmanage;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 工作台管理 DO
 *
 * @author lls
 */
@TableName("ecms_workbench")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkbenchDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 工作台编码
     */
    private String code;
    /**
     * 工作台名称
     */
    private String name;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 组件名称
     */
    private String componentName;

}
