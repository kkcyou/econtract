package com.yaoan.module.econtract.controller.admin.catalog;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogShowVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogVO;
import com.yaoan.module.econtract.service.catalog.PurCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/catalog")
@Tag(name = "采购目录", description = "采购目录")
public class PurCatalogController {
    @Resource
    private PurCatalogService purCatalogService;
    /**
     *根据code获取采购目录子列表
     */
    @PostMapping(value = "/getPurCatalogByCode")
    @Operation(summary = "根据code获取采购目录子列表", description = "根据code获取采购目录子列表")
    @OperateLog(enable = false, logArgs = false)
    @DataPermission(enable = false)
    public CommonResult<List<PurCatalogShowVO>> getPurCatalogByCode(@RequestBody PurCatalogVO purCatalogVO) {
        List<PurCatalogShowVO> list = purCatalogService.getPurCatalogByCode(purCatalogVO);
        return success(list);
    }
}
