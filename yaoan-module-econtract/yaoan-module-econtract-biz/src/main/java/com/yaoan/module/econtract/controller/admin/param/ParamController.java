package com.yaoan.module.econtract.controller.admin.param;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.param.vo.*;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.param.ParamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/param")
@Tag(name = "参数", description = "参数")
public class ParamController {
    @Resource
    private ParamService paramService;
    /**
     * 新增/修改参数V2
     * @param params
     * @return
     */
    @PostMapping(value = "/createV2")
    @Operation(summary = "新增/修改参数V2", description = "新增/修改参数V2")
    @OperateLog(logArgs = false)
    public CommonResult<String> createParamV2(@Valid @RequestBody ParamReqVO params) throws JsonProcessingException {
        String id = paramService.saveParamV2(params);
        return success(id);
    }

    @DeleteMapping(value = "/delete/ids")
    @Operation(summary = "删除参数", description = "删除参数")
    @OperateLog(logArgs = false)
    public CommonResult deleteParam(@RequestBody ReqIdsVO params) throws Exception {
        List<String> ids = paramService.removeByIds(params);
        CommonResult<Object> objectCommonResult = new CommonResult<>();
        if(ObjectUtil.isEmpty(ids)){
            objectCommonResult.setCode(GlobalErrorCodeConstants.SUCCESS.getCode());
            objectCommonResult.setMsg("删除成功");
        }else {
            objectCommonResult.setCode(ErrorCodeConstants.DATA_IS_CALL.getCode());
            objectCommonResult.setMsg("参数已被调用");
            objectCommonResult.setData(ids);
        }
        return objectCommonResult;

    }


    @PostMapping(value = "/page/list")
    @Operation(summary = "查询所有参数", description = "查询所有参数")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ParamRespVO>> queryAllParam(@RequestBody ParamPageReqVO param) {
        PageResult<ParamRespVO> paramPage = paramService.queryAllParam(param);
        return success(paramPage);
    }

    @GetMapping(value = "/query/{id}")
    @Operation(summary = "查看参数详细信息", description = "查看参数详细信息")
    @OperateLog(logArgs = false)
    public CommonResult<ParamRespVO> queryParamById(@PathVariable String id) throws Exception {
        ParamRespVO paramRespVO = paramService.queryParamById(id);
        return success(paramRespVO);
    }


    @PostMapping(value = "/queryParamAndCategory")
    @Operation(summary = "查询分类和对应的参数", description = "查询分类和对应的参数")
    @OperateLog(logArgs = false)
    public CommonResult<List<ParamListRespVO>> queryParamAndCategory(@RequestBody BaseParamVO param) {
        List<ParamListRespVO> trees = paramService.queryParamAndCategory(param);
        return success(trees);
    }


    /**
     * 根据模板展示参数（根据条款分组）
     */
    @GetMapping(value = "/queryParamByModelId/{id}")
    @Operation(summary = "根据模板展示参数（根据条款分组）", description = "根据模板展示参数（根据条款分组）")
    @OperateLog(logArgs = false)
    public CommonResult<List<TermV2RespVO>> queryParamByModelId(@PathVariable String id) throws Exception {
        return success(paramService.queryParamByModelId(id));
    }



}
