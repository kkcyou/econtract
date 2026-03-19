package com.yaoan.module.econtract.controller.admin.contracttemplate.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.CommonOfModelAndTemplateBpmProcessRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/23 11:12
 */
@Schema(description = "查看单个范本 Response VO")
@Data
@ToString(callSuper = true)
public class TemplateSingleVo {

    @Schema(description = "范本id", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String id;

    @Schema(description = "范本编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @Size(max = 30, message = "范本编码长度不能超过100个字符")
    private String code;

    @Schema(description = "范本名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @Size(max = 30, message = "范本名称长度不能超过100个字符")
    private String name;

    /**
     * 范本类型
     */
    @Schema(description = "范本类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateCategoryId;

    /**
     * 范本类型Str
     */
    @Schema(description = "范本类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateCategoryStr;

    /**
     * 范本简介（最多500个字）
     */
    @Schema(description = "范本简介", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    @Size(max = 500, message = "范本简介长度不能超过500个字符")
    private String templateIntro;


    /**
     * 范本来源(官方范本 或 标准范本)
     */
    @Schema(description = "范本来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String templateSource;

    /**
     * 发布机构
     */
    @Schema(description = "发布机构", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private String publishOrgan = "";

    /**
     * 发布时间
     */
    @Schema(description = "发布时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-07-21 16:02:57")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    /**
     * 范本字数
     */
    @Schema(description = "范本字数", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Integer wordCount;

    /**
     * 远端文件ID
     */
    @Schema(description = "远端文件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "demo")
    private Long remoteFileId;

    @Schema(description = "范本相关图片的地址集合", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    private List<String> picList;

    /**
     * 流程信息响应VO集合
     */
    private List<CommonOfModelAndTemplateBpmProcessRespVO> bpmProcessRespVOList;


    /**
     * 版本
     */
    private Double version;

    /**
     * 区划编码
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 个人发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTimeReciever;

    /**
     * 范本上传方式 0.wps 1.富文本
     */
    private Integer uploadType;

    /**
     * 范本内容
     */
    private String content;

    /**
     * 条款说明list
     */
    private List<TermReqVO> termList;

    /**
     * 上传源文件ID
     */
    private Long sourceFileId;

    /**
     * ofd文件对应ID（审批后的文件）
     */
    private Long ofdFileId;

    /**
     * 模板时效 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "模板时效标识")
    private Integer timeEffectModel;

    /**
     * 短期时效-开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime effectStartTime;

    /**
     * 短期时效-结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime effectEndTime;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 文件名称
     */
    private String fileName;
}
