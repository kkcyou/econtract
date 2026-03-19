package com.yaoan.module.infra.controller.admin.file.vo.file;

import com.yaoan.module.infra.enums.FileUploadPathEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author doujiale
 */
@Schema(description = "管理后台 - 上传文件 Request VO")
@Data
public class FileUploadExtReqVO {

    @Schema(description = "文件附件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件附件不能为空")
    private MultipartFile file;

    @Schema(description = "文件附件", example = "yudaoyuanma.png")
    private String path;

    /**
     * {@link FileUploadPathEnum}
     * */
    @Schema(description = "业务类型，参考 FileUploadPathEnum", example = "1")
    private Integer code;

}
