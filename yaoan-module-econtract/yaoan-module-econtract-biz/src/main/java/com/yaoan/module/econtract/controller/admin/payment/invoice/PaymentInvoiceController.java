package com.yaoan.module.econtract.controller.admin.payment.invoice;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoicePageReqVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceRespVO;
import com.yaoan.module.econtract.controller.admin.payment.invoice.vo.PaymentInvoiceSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentInvoiceDO;
import com.yaoan.module.econtract.service.payment.invoice.PaymentInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 发票信息")
@RestController
@RequestMapping("/econtract/invoice")
@Validated
public class PaymentInvoiceController {

    @Resource
    private PaymentInvoiceService invoiceService;

    @PostMapping("/create")
    @Operation(summary = "创建发票信息")
    public CommonResult<String> createInvoice(@Valid @RequestBody PaymentInvoiceSaveReqVO createReqVO) {
        return success(invoiceService.createInvoice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发票信息")
    public CommonResult<Boolean> updateInvoice(@Valid @RequestBody PaymentInvoiceSaveReqVO updateReqVO) {
        invoiceService.updateInvoice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发票信息")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteInvoice(@RequestParam("id") String id) {
        invoiceService.deleteInvoice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发票信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<PaymentInvoiceRespVO> getInvoice(@RequestParam("id") String id) {
        PaymentInvoiceDO invoice = invoiceService.getInvoice(id);
        return success(BeanUtils.toBean(invoice, PaymentInvoiceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发票信息分页")
    public CommonResult<PageResult<PaymentInvoiceRespVO>> getInvoicePage(@Valid PaymentInvoicePageReqVO pageReqVO) {
        PageResult<PaymentInvoiceDO> pageResult = invoiceService.getInvoicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PaymentInvoiceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出发票信息 Excel")
    public void exportInvoiceExcel(@Valid PaymentInvoicePageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PaymentInvoiceDO> list = invoiceService.getInvoicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "发票信息.xls", "数据", PaymentInvoiceRespVO.class,
                BeanUtils.toBean(list, PaymentInvoiceRespVO.class));
    }
}
