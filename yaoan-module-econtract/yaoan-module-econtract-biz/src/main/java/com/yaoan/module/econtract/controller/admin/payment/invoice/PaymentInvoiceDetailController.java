package com.yaoan.module.econtract.controller.admin.payment.invoice;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailPageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailRespVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceDetailSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDetailDO;
import com.yaoan.module.econtract.service.payment.invoice.PaymentInvoiceDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发票信息明细")
@RestController
@RequestMapping("/econtract/invoice-detail")
@Validated
public class PaymentInvoiceDetailController {

    @Resource
    private PaymentInvoiceDetailService invoiceDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建发票信息明细")
    public CommonResult<String> createInvoiceDetail(@Valid @RequestBody PaymentInvoiceDetailSaveReqVO createReqVO) {
        return success(invoiceDetailService.createInvoiceDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发票信息明细")
    public CommonResult<Boolean> updateInvoiceDetail(@Valid @RequestBody PaymentInvoiceDetailSaveReqVO updateReqVO) {
        invoiceDetailService.updateInvoiceDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发票信息明细")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteInvoiceDetail(@RequestParam("id") String id) {
        invoiceDetailService.deleteInvoiceDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发票信息明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<PaymentInvoiceDetailRespVO> getInvoiceDetail(@RequestParam("id") String id) {
        PaymentInvoiceDetailDO invoiceDetail = invoiceDetailService.getInvoiceDetail(id);
        return success(BeanUtils.toBean(invoiceDetail, PaymentInvoiceDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发票信息明细分页")
    public CommonResult<PageResult<PaymentInvoiceDetailRespVO>> getInvoiceDetailPage(@Valid PaymentInvoiceDetailPageReqVO pageReqVO) {
        PageResult<PaymentInvoiceDetailDO> pageResult = invoiceDetailService.getInvoiceDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PaymentInvoiceDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出发票信息明细 Excel")
    public void exportInvoiceDetailExcel(@Valid PaymentInvoiceDetailPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PaymentInvoiceDetailDO> list = invoiceDetailService.getInvoiceDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "发票信息明细.xls", "数据", PaymentInvoiceDetailRespVO.class,
                BeanUtils.toBean(list, PaymentInvoiceDetailRespVO.class));
    }

}