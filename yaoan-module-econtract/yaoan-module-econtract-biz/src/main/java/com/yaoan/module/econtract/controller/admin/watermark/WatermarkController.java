package com.yaoan.module.econtract.controller.admin.watermark;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkCreateReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkPageReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkRespVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.page.WatermarkPageRespVO;
import com.yaoan.module.econtract.convert.watermark.WatermarkConvert;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;
import com.yaoan.module.econtract.service.watermark.WatermarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;



@Tag(name = "管理后台 - 水印管理")
@RestController
@RequestMapping("/ecms/watermark")
@Validated
public class WatermarkController {

    @Resource
    private WatermarkService watermarkService;

    @PostMapping("/create")
    @Operation(summary = "创建水印管理")
    public CommonResult<String> createWatermark(@Valid @RequestBody WatermarkCreateReqVO createReqVO) {
        return success(watermarkService.createWatermark(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新水印管理")
    public CommonResult<Boolean> updateWatermark(@Valid @RequestBody WatermarkUpdateReqVO updateReqVO) {
        watermarkService.updateWatermark(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除水印管理")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteWatermark(@RequestParam("id") String id) {
        watermarkService.deleteWatermark(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得水印管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WatermarkRespVO> getWatermark(@RequestParam("id") String id) {
        return success( watermarkService.getWatermark(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获得水印管理列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<WatermarkRespVO>> getWatermarkList(@RequestParam("ids") Collection<String> ids) {
        List<WatermarkDO> list = watermarkService.getWatermarkList(ids);
        return success(WatermarkConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/page")
    @Operation(summary = "获得水印管理分页")
    public CommonResult<PageResult<WatermarkPageRespVO>> getWatermarkPage(@Valid @RequestBody WatermarkPageReqVO pageVO) {
        return success(watermarkService.getWatermarkPage(pageVO));
    }
    

}
