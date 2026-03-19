package com.yaoan.module.econtract.controller.admin.annotation;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateBatchReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.BigAnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.annotation.AnnotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 审批批注
 * @author: Pele
 * @date: 2024/3/25 11:38
 */
@Slf4j
@RestController
@RequestMapping("econtract/annotation")
@Tag(name = "审批批注", description = "审批批注")
public class AnnotationController {
    @Resource
    private AnnotationService annotationService;

    /**
     * 新增批注(停用)
     */
    @PostMapping("/save")
    @Operation(summary = "新增批注")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@RequestBody @Validated AnnotationSaveUpdateReqVO vo) throws Exception {
        return success(annotationService.save(vo));
    }

    /**
     * 批量新增批注
     */
    @PostMapping("/saveBatch")
    @Operation(summary = "新增批注")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveBatch(@RequestBody @Validated AnnotationSaveUpdateBatchReqVO vo) throws Exception {
        return success(annotationService.saveBatch(vo));
    }

    /**
     * 修改批注
     */
    @PostMapping("/update")
    @Operation(summary = "修改批注")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@RequestBody AnnotationSaveUpdateReqVO vo) throws Exception {
        return success(annotationService.update(vo));
    }

    /**
     * 根据文件id显示批注
     */
    @PostMapping("/getAnnotationByFileId")
    @Operation(summary = "根据文件id显示批注")
    @OperateLog(logArgs = false)
    public CommonResult<List<BigAnnotationListRespVO>> getAnnotationByFileId(@RequestBody IdReqVO vo) throws Exception {
        return success(annotationService.getAnnotationByFileId(vo));
    }

    /**
     * 删除批注
     */
    @PostMapping("/delete")
    @Operation(summary = "保存补充协议")
    @OperateLog(logArgs = false)
    public CommonResult<String> delete(@RequestBody IdReqVO vo) throws Exception {
        return success(annotationService.delete(vo));
    }

    /**
     * 校验改文件的所有批注是否都解决了
     */
    @PostMapping("/checkAnnotationsByFileId")
    @Operation(summary = "校验改文件的所有批注是否都解决了")
    @OperateLog(logArgs = false)
    public CommonResult<String> checkAnnotationsByFileId(@RequestBody IdReqVO vo) throws Exception {
        return success(annotationService.checkAnnotationsByFileId(vo));
    }

}
