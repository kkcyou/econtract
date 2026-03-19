package com.yaoan.module.econtract.controller.admin.sign;


import com.reach.pdf.sign.PdfSignatureContext;
import com.reach.pdf.sign.base.client.ReachSignPdfClient;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.sign.vo.VerificationReqVO;
import com.yaoan.module.econtract.controller.admin.sign.vo.VerificationRespVO;
import com.yaoan.module.econtract.service.contract.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@Tag(name = "签章", description = "签章")
@RequestMapping("econtract/sign")
public class PdfController {
    @Resource
    private ContractService contractService;

    @RequestMapping("/pdf")
//    @Operation(summary = "pdf文件操作", description = "pdf文件操作")
    public void pdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PdfSignatureContext.execute(request,response);
    }

    @RequestMapping("/getKeySn")
    public void getKeySn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String mobilePhone = request.getParameter("mobilePhone");
        String data = ReachSignPdfClient.getInstance().verifyAccount(account, password, mobilePhone);
        response.getWriter().write(data);
    }

    @RequestMapping("/getKeySnByToken")
    public void getKeySnByToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String token = request.getParameter("token");
        String data = ReachSignPdfClient.getInstance().verifyToken(token);
        response.getWriter().write(data);
    }

    /**
     * 平台-验章信息
     * */
    @PostMapping("/verification")
    @Operation(summary = "验章信息")
    @OperateLog(logArgs = false)
    public CommonResult<VerificationRespVO> verification(VerificationReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        return success(contractService.verification(file.getInputStream()));
    }
}
