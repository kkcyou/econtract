package com.yaoan.module.econtract.api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:01
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "模板DTO对象", description = "模板DTO对象")
public class ModelDTO extends PageParam implements Serializable {

    private static final long serialVersionUID = 7851420050910165585L;
    /**
     * 模板id
     */
    private String id;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板分类ID
     */
    private Integer categoryId;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 时效模式
     */
    private Integer timeEffectModel;

    /**
     * 模板生效时间
     */
    private LocalDateTime effectStartTime;

    /**
     * 模板失效时间
     */
    private Date effectEndTime;

    /**
     * 模板描述
     */
    private String content;


    /**
     * 远端文件对应ID（上传后的文件）
     */
    private Long remoteFileId;


    /**
     * 模板文件，文件base64数据
     */
    private String fileBase64;

    /**
     * 模板状态
     */
    private Integer status;

    /**
     * 审批状态（0=未审核，1=通过，2=不通过,，3=已审批）
     */
    private Integer approveStatus;
    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0=没删除，1=已删除
     */
    private Boolean deleted;

    /**
     * 查询创建时间范围的起始时间
     */
    private LocalDateTime startCreateTime;

    /**
     * 查询创建时间范围的结束时间
     */
    private LocalDateTime endCreateTime;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 查询审批时间范围的开始时间
     */
    private LocalDateTime startApproveTime;

    /**
     * 查询审批时间范围的结束时间
     */
    private LocalDateTime endApproveTime;

    /**
     * 需要上传的文件
     * */
    private MultipartFile file;



    /**
     * 参数接收器
     * */
    private String paramListReciever;

    /**
     * 业务平台
     * */
    private String platform;
}

