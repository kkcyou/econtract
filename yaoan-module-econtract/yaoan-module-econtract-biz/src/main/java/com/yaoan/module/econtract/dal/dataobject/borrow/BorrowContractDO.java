package com.yaoan.module.econtract.dal.dataobject.borrow;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_borrow_contract_rel")
public class BorrowContractDO extends DeptBaseDO implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 借阅id
     */
    private String borrowId;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 审批结果
     */
    private Integer result;

    /**
     * 借阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;

    /**
     * 借阅类型 1=纸质文件 0=电子文件
     */
    private Integer borrowType;

    /**
     * 电子文件权限 : 1=带水印查看 2=无水印查看 3=带水印下载 4=无水印下载
     */
    private Integer borrowPermission;
}
