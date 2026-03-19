package com.yaoan.module.econtract.controller.admin.rtf;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.econtract.controller.admin.rtf.vo.ModelRespVO;
import com.yaoan.module.econtract.service.rtf.RtfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 富文本
 *
 * @author: doujl
 * @date: 2023/8/3 15:56
 */
@Slf4j
@RestController
@RequestMapping("econtract/rtf")
@Validated
@Tag(name = "富文本控制器", description = "富文本控制器")
public class RtfController {

    @Resource
    private RtfService rtfService;


    @GetMapping("/file/{id}")
    @Operation(summary = "根据文件id获取富文本内容")
    public CommonResult<String> getRtfByFileId(@PathVariable("id") Long id) throws Exception {
        return success(rtfService.getRtfByFileId(id));
    }

    @GetMapping("/model/{id}")
    @Operation(summary = "根据模板id获取富文本内容")
    public CommonResult<String> getRtfByModelId(@PathVariable("id") String id) throws Exception {
        return success(rtfService.getRtfByModelId(id));
    }

    @GetMapping("/template/{id}")
    @Operation(summary = "根据范本id获取富文本内容")
    public CommonResult<String> getRtfByTemplateId(@PathVariable("id") String id) throws Exception {
        return success(rtfService.getRtfByTemplateId(id));
    }
    /**
     * 根据模板id获取富文本内容、以及条款
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/model/v1/{id}")
    @DataPermission(enable = false)
    @Operation(summary = "根据模板id获取富文本内容、以及条款")
    public CommonResult<ModelRespVO> getRtfByModelId1(@PathVariable("id") String id) throws Exception {
        return success(rtfService.getRtfByModelId1(id));
    }
}
