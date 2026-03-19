package com.yaoan.module.econtract.dal.dataobject.annotation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/26 15:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_annotation_user_rel")
public class AnnotationUserRelDO  extends DeptBaseDO {
    private static final long serialVersionUID = -1952172048254669204L;
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    private String annotationId;
    private String userId;
    private String taskId;
}
