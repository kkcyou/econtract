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
 * 履约任务实体类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_perf_task")
public class PerfTaskDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 932107491323877591L;
    /**
     * 履约任务id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同履约id
     */
    private String contractPerfId;
    /**
     * 履约任务类型id
     */
    private String perfTaskTypeId;


    /**
     * 履约任务名称
     */
    private String name;

    /**
     * 前置履约任务id
     */
    private String beforTaskId;
    /**
     * 履约时间
     */
    private Date perfTime;
    /**
     * 单位
     */
    private String unit;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 履约内容
     */
    private String content;
    /**
     * 履约提醒时间
     */
    private Date remindTime;
    /**
     * 履约任务状态编码
     */
    private Integer taskStatus;
    /**
     * 确认人
     */
    private Long confirmer;
    /**
     * 确认时间
     */
    private Date confirmTime;
    /**
     * 履约文件
     */
    private Long fileId;
    /**
     * 履约说明
     */
    private String remark;
}
