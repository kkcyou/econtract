package com.yaoan.module.econtract.dal.dataobject.version;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 10:36
 */
@TableName(value = "ecms_file_version", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVersionDO extends BaseDO {

    private static final long serialVersionUID = -5865394389971950151L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 拷贝文件id
     */
    private Long copyFileId;

    /**
     * 拷贝文件名称
     */
    private String copyFileName;

    /**
     * 拷贝文件URL
     */
    private String copyFileUrl;

    /**
     * 来源的拷贝文件id
     */
    private Long sourceCopyFileId;

    /**
     * 来源的拷贝文件名称
     */
    private String sourceCopyFileName;

    /**
     * 备注
     */
    private String remark;

}
