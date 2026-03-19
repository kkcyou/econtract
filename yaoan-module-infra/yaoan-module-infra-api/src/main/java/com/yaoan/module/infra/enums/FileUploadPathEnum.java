package com.yaoan.module.infra.enums;

import lombok.Getter;

import java.io.File;

/**
 * @author doujiale
 */

@Getter
public enum FileUploadPathEnum {
    /**
     * 文件上传路径枚举
     */
    CONTRACT_DRAFT("合同起草", 0, "contract/draft/"),
    CONTRACT_SIGNING("合同签署", 1, "contract/signing/"),
    CONTRACT_APPENDIX("合同附件", 2, "contract/appendix/"),
    CONTRACT_SUPPLEMENTAL("合同补充协议", 3, "contract/supplemental/"),
    CONTRACT_TERMINATED("合同中止", 4, "contract/terminated/"),
    CONTRACT_FILING("合同归档", 5, "contract/filing/"),
    TEMPLATE("范本文件", 6, "template/"),
    MODEL("模板文件", 7, "model/"),
    PERFORMANCE_CONFIRM("履约确认", 8, "performance/task/confirm/"),
    PARAM_ICON("参数图标", 9, "param/icon/"),
    CONTRACT_LOAN("合同借阅", 10, "contract/loan/"),
    FILE_VERSION_PATH("文件版本", 11, "file/version/"),
    CONTRACT_EXAMINATION("合同审查", 12, "contract/examination/"),
    TEMP_PATH("临时文件地址", 13, "/temp/save/"),
    CONTRACT_SIGNING_XML("带原数据的合同文件", 14, "contract/signing/xml"),
    CONTRACT_AI_DRAFT("合同ai智能起草", 15, "contract/ai/draft/"),

    CONTRACT_DRAFT_BEFORE("合同签署前", 101, "contract/beforeSign/"),


    TEMPLATE_THUMBNAIL("范本缩略图文件", 601, "template/thumbnail/"),
    MODEL_THUMBNAIL("模板缩略图文件", 701, "model/thumbnail/"),

    CX_SAVE_PATH("畅写文件", 8092, "changxie/save/"),
    CX_CLEAN_PATH("清稿文件", 123, "changxie/clean/"),

    FILE_COPY_PATH("拷贝文件", 404, "copy/save/"),
    CONTRACT_PDF_WATERMARK("水印", 404, "contract/watermark/"),

    FOXI_SAVE_PATH("福昕文件", 8080, "foxi/save/"),
    FOXI_PDF("鲲鹏pdf文件", 8081, "foxi/pdf/"),
    ;


    private final String name;
    private final int code;
    private final String path;

    FileUploadPathEnum(String name, int code, String path) {
        this.name = name;
        this.code = code;
        this.path = path;
    }

    public static FileUploadPathEnum getInstance(Integer code) {
        for (FileUploadPathEnum value : FileUploadPathEnum.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        return null;
    }

    public String getPath() {
        return path;
    }

    public String getPath(String identifier, String fileName) {
        return path + identifier + File.separator + fileName;
    }

    public String getPath(String identifier, String sourceType, String fileName) {
        return path + identifier + File.separator + sourceType + File.separator + fileName;
    }
}
