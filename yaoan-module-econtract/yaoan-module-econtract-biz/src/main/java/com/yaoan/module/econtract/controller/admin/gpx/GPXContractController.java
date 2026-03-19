package com.yaoan.module.econtract.controller.admin.gpx;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.service.gcy.gpmall.GPMallProjectService;
import com.yaoan.module.econtract.service.gpx.contract.GPXContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 电子交易合同控制器
 */
@Slf4j
@RestController
@RequestMapping("trading/contract")
@Validated
@Tag(name = "合同管理", description = "合同模块操作接口")
public class GPXContractController {

    @Resource
    private GPXContractService gpxContractService;
    @Resource
    private GPMallProjectService gpMallProjectService;

    /**
     * 新增合同/编辑合同（HLJ_pro_gpx）(电子交易)
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/create")
    @Operation(summary = "新增合同/编辑合同（HLJ_pro_gpx）")
    public CommonResult<String> create(@Valid @RequestBody GPXContractCreateReqVO contractCreateReqVO) throws Exception {
        return CommonResult.success(gpxContractService.create(contractCreateReqVO));
    }

    /**
     * 删除合同(电子交易)
     */
    @GetMapping(value = "/delete/{id}")
    @Operation(summary = "删除合同")
    public CommonResult<Boolean> delete(@PathVariable String id) {
        gpxContractService.deleteById(id);
        return success(true);
    }

    /**
     * 合同详情(电子交易 兼容 非交易的签署查看)
     */
    @GetMapping(value = "/queryById/{id}")
    @Operation(summary = "合同详情(电子交易 兼容 非交易的签署查看)")
    public CommonResult<GPXContractRespVO> queryById(@PathVariable String id) throws Exception {
        GPXContractRespVO result = gpxContractService.queryById(id);
        return CommonResult.success(result);
    }

    /**
     * 电子交易作废接口
     *
     * @param id
     * @param file
     * @return {@link CommonResult }<{@link Boolean }>
     * @throws Exception
     */
    @PutMapping(value = "/invalidate/{id}")
    @Operation(summary = "合同作废")
    @OperateLog(enable = true)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> invalidateContractById(@PathVariable String id, @ModelAttribute MultipartFile file) throws Exception {
        //TODO 暂时忽略此逻辑，绕开黑龙江交互(亚冬会结束后就恢复)
        Boolean flag = gpxContractService.invalidateContractById(id, file);
        return CommonResult.success(flag);
    }

    /**
     * 打印合同
     */
    @GetMapping(value = "/printContract/{id}")
    @Operation(summary = "打印合同", description = "打印合同")
    @OperateLog(logArgs = false)
    public String printContract(@PathVariable String id) throws Exception {
        return gpxContractService.printContract(id);
    }

    /**
     * 下载合同
     */
    @GetMapping(value = "/downloadTContract/{id}")
    @Operation(summary = "下载合同", description = "下载合同")
    @OperateLog(logArgs = false)
    public void downloadTContract(HttpServletResponse response, @PathVariable String id) throws Exception {
        gpxContractService.downloadTContract(response, id);
    }


}
