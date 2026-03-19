package com.yaoan.module.econtract.dal.dataobject.performance.perfTask;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * 履约任务用户中间表实体类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_perf_user")
public class PerfTaskUserDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 2875701587177216237L;
    /**
     * 履约任务负责人id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 履约任务id
     */
    private String perfTaskId;
    /**
     * 负责人id
     */
    private Long userId;

}
