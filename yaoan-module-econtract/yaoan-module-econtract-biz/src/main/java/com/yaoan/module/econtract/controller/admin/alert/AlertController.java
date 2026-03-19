package com.yaoan.module.econtract.controller.admin.alert;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertReqVO;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertRespVO;
import com.yaoan.module.econtract.service.alert.AlertService;
import com.yaoan.module.econtract.util.PdfConvertHtmlUtil;
import com.yaoan.module.econtract.util.WkHtmlToPdfManager;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.module.econtract.enums.StatusConstants.MODEL_RTF_PDF_TEMP_PATH;

/**
 * @description: 合同提醒接口
 * @author: Pele
 * @date: 2023/11/8 15:08
 */
@Slf4j
@RestController
@RequestMapping("econtract/alert")
@Validated
@Tag(name = "合同提醒", description = "合同提醒操作接口")
public class AlertController {

    @Resource
    private AlertService alertService;
    @Resource
    private FileApi fileApi;
    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;
    /**
     * 合同提醒列表
     */
    @PostMapping("/getContractAlertPage")
    @OperateLog(logArgs = false)
    @Operation(summary = "合同提醒列表 ")
    public CommonResult<PageResult<AlertRespVO>> getContractAlertPage(@RequestBody AlertReqVO vo) {
        return success(alertService.getContractAlertPage(vo));
    }
   
    /**
     * 测试
     */
    @PostMapping("/test")
    @OperateLog(logArgs = false)
    @Operation(summary = "test ")
    public CommonResult<Long> test(@ModelAttribute AlertReqVO vo) throws Exception {
        String uuid = String.valueOf(UUID.randomUUID());
        String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
        wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(vo.getModelContent(), path);
        Long rtf_fileId = fileApi.uploadFile("ss" + ".pdf", FileUploadPathEnum.MODEL.getPath("sssss", "name"), IoUtil.readBytes(FileUtil.getInputStream(path)));

        return success(rtf_fileId);
    }

}
