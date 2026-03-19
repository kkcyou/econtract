package com.yaoan.module.econtract.controller.admin.copyrecipient;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangePageReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangePageRespVO;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageReqVO;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageRespVO;
import com.yaoan.module.econtract.service.contracttype.ContractTypeService;
import com.yaoan.module.econtract.service.copyrecipient.CopyRecipientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 抄送人模块
 * @author: Pele
 * @date: 2024/4/1 22:35
 */
@Slf4j
@RestController
@RequestMapping("econtract/copyrecipient")
@Validated
@Tag(name = "抄送人模块", description = "抄送人模块")
public class CopyRecipientController {
    @Resource
    private CopyRecipientService copyRecipientService;

    /**
     * 抄送人查看列表
     */
    @PostMapping("/list")
    @Operation(summary = "抄送人查看列表")
    public CommonResult<PageResult<CopyRecipientPageRespVO>> list(@RequestBody @Validated CopyRecipientPageReqVO reqVO) {
        PageResult<CopyRecipientPageRespVO> result = copyRecipientService.list(reqVO);
        return success(result);
    }

}
