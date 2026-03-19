package com.yaoan.module.econtract.controller.admin.agreement;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelCreateReqVO;
import com.yaoan.module.econtract.service.agreement.PrefAgreementRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.io.IOException;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 履约管理-添加补充协议
 */
@Slf4j
@RestController
@RequestMapping("econtract/agreement")
@Validated
@Tag(name = "履约管理补充协议", description = "履约管理补充协议")
public class AgreementController {

    @Resource
    private PrefAgreementRelService prefAgreementRelService;
    /**
     * 添加履约协议（修改）
     * @param
     * @return
     */
    @PostMapping(value = "/create")
    @Operation(summary = "添加补充协议")
    public CommonResult<String> create(@RequestBody PrefAgreementRelCreateReqVO prefAgreementRelCreateReqVO) throws Exception {
        String id = prefAgreementRelService.createAgreement(prefAgreementRelCreateReqVO);
        return success(id);
    }
}
