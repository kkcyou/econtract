package com.yaoan.module.econtract.controller.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 参数绑定模板Vo对象
 * @author: Pele
 * @date: 2023/8/17 14:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "参数绑定模板Vo对象", description = "参数绑定模板Vo对象")
public class ParamModelVo implements Serializable {

    private static final long serialVersionUID = -5896301084064358293L;
    /**
     * 模板id
     */
    private Integer id;

    /**
     * 参数id
     */
    private String paramId;

    /**
     * 参数序号
     */
    private Integer paramNum;

    /**
     * 参数在模板上的位置（坐标）
     */
    private String location;

    /**
     * 标识id（文件上传和范本生成 模板的时候前端会传）
     */
    private String tagId;
}
