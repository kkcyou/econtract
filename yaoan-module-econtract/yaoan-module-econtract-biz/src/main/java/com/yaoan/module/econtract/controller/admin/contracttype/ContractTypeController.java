package com.yaoan.module.econtract.controller.admin.contracttype;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleRespVO;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.DelReqVo;
import com.yaoan.module.econtract.convert.contracttype.ContractTypeConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.contracttype.ContractTypeService;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.system.controller.admin.user.vo.user.UserExcelVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/8 17:19
 */
@Slf4j
@RestController
@RequestMapping("econtract/contracttype")
@Validated
@Tag(name = "合同类型模块", description = "合同类型模块")
public class ContractTypeController {


    @Resource
    private ContractTypeService contractTypeService;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ModelService modelService;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;

    /**
     * 新增合同类型V2
     */
    @PostMapping(value = "/insert")
    @Operation(summary = "新增合同类型", description = "新增合同类型")
    public CommonResult<Object> insertV2(@RequestBody @Valid ContractTypeCreateV2Vo vo) {
        if (ObjectUtil.isEmpty(vo)) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
        }
        contractTypeService.insert(vo);
        return success(true);
    }

    /**
     * 合同类型列表展示（根据合同类型分类显示）
     */
    @PostMapping("/page")
    @Operation(summary = "获得合同类型分页列表")
    public CommonResult<PageResult<ContractTypePageV2RespVo>> getContractTypePageV2(@RequestBody ContractTypeListV2ReqVO vo) {
        return success(contractTypeService.getContractTypePage(vo));
    }

    /**
     * 删除合同类型
     */
    @PostMapping(value = "/delete")
    @Operation(summary = "删除合同类型", description = "删除合同类型")
    public CommonResult<Object> deleteV2(@RequestBody DelReqVo vo) {
        List<String> ids = vo.getIds();
        if (CollUtil.isEmpty(ids)) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
        }
        List<ContractType> contractTypes = contractTypeService.listByIds(ids);
        if (isContractTypeUsed(contractTypes)) {
            throw exception(ErrorCodeConstants.CONTRACT_TYPE_USED_ERROR);
        }
        contractTypeService.delete(ids);
        return success(true);
    }

    /**
     * 编辑合同类型
     */
    @PostMapping(value = "/updateContractType")
    @Operation(summary = "编辑合同类型", description = "编辑合同类型")
    public CommonResult<Object> updateV2(@RequestBody ContractTypeUpdateV2Vo vo) {
        contractTypeService.update(vo);
        return success(true);
    }

    /**
     * 查询合同类型详情
     */
    @GetMapping(value = "/getContractTypeDetail/{id}")
    @Operation(summary = "查询合同类型详情", description = "查询合同类型详情")
    public CommonResult<ContractTypeSelectRespVO> getContractTypeDetail( @PathVariable("id") String id) {
        ContractTypeSelectRespVO respVO = contractTypeService.getContractTypeDetail(id);
        return success(respVO);
    }

    /**
     * 展示合同类型下拉框
     * */
    @PostMapping(value = "/selectList")
    @Operation(summary = "展示合同类型列表", description = "展示合同类型列表")
    public CommonResult<Object> selectList(@RequestBody ContractTypeSelectReqVO vo) {

        return success(contractTypeService.selectList(vo));
    }


    /**
     * 修改合同类型状态
     * */
    @PostMapping(value = "/updateContractTypeStatus")
    @Operation(summary = "修改合同类型状态", description = "修改合同类型状态")
    public CommonResult<Boolean> updateContractTypeStatus(@Valid @RequestBody ContractTypeUpdateStatusReqVO vo) {
        Boolean result =  contractTypeService.updateContractTypeStatus(vo);
        return success(result);
    }

    @PostMapping("/export-excel")
    @Operation(summary = "导出合同类型 Excel")
    @OperateLog(type = EXPORT)
    public void exportTestDemoExcel(HttpServletResponse response, @RequestBody ContractTypeListV2ReqVO exportReqVO) throws IOException {
        PageResult<ContractTypePageV2RespVo> result = contractTypeService.getContractTypePage(exportReqVO);
        // 导出 Excel
        List<ContractTypePageV2RespVo> list = flattenData(result.getList());
        List<ContractTypeExcelVO> data = ContractTypeConverter.INSTANCE.convertList02(list);
        String fileName = "合同类型.xlsx";
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ExcelUtils.write(response, fileName, "合同类型", ContractTypeExcelVO.class, data);

    }
    @GetMapping(value = "/isContractActive/{id}")
    @Operation(summary = "判断合同类型是否使用", description = "判断合同类型是否使用")
    public CommonResult<Object> isContractActive(@PathVariable("id") String id) {
        return success(contractTypeService.isContractActive(id));
    }

    /**
     * 展示所有启用的一级合同类型
     * */
    @GetMapping(value = "/listActiveContractType")
    @Operation(summary = "展示所有启用的一级合同类型", description = "展示所有启用的一级合同类型")
    public CommonResult<List<ContractTypeRespVO>> listActiveContractType() {
        return success(contractTypeService.listActiveContractType());
    }

    /**
     * 判断是否需要用印
     * */
    @PostMapping(value = "/isNeedSignet")
    @Operation(summary = "判断合同类型是否需要用印", description = "判断合同类型是否需要用印")
    public CommonResult<Object> isNeedSignet(@RequestBody ContractTypeSignetReqVO signetReqVO) {
        return success(contractTypeService.isNeedSignet(signetReqVO));
    }



    /**
     * 合同类型是否含调用过
     */
    public boolean isContractTypeUsed(List<ContractType> types) {
        for (ContractType type : types) {
            if (CollUtil.isNotEmpty(contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractType, type.getId())))) {
                return true;
            }
            if (CollUtil.isNotEmpty(modelService.list(new LambdaQueryWrapperX<Model>().eq(Model::getContractType, type.getId())))) {
                return true;
            }
            if (CollUtil.isNotEmpty(paramMapper.selectList(new LambdaQueryWrapperX<Param>().eq(Param::getContractType, type.getId())))) {
                return true;
            }
            if (CollUtil.isNotEmpty(contractTemplateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getContractType, type.getId())))) {
                return true;
            }

        }
        return false;
    }
    // 将多级结构数据展开为平级列表
    private List<ContractTypePageV2RespVo> flattenData(List<ContractTypePageV2RespVo> data) {
        List<ContractTypePageV2RespVo> flatList = new ArrayList<>();
        for (ContractTypePageV2RespVo item : data) {
            flatList.add(item);
            if (ObjectUtil.isNotEmpty(item.getChildren())) {
                flatList.addAll(flattenData(item.getChildren()));
            }
        }
        return flatList;
    }

//
//    @Resource
//    private ContractTypeService contractTypeService;
//    @Resource
//    private ContractMapper contractMapper;
//    @Resource
//    private ModelService modelService;
//    @Resource
//    private ParamMapper paramMapper;
//
//    /**
//     * 合同类型列表展示（根据合同类型分类显示）
//     */
//    @PostMapping("/page")
//    @Operation(summary = "获得合同类型分页列表")
//    public CommonResult<PageResult<ContractTypePageRespVo>> getContractTypePage(@RequestBody ContractTypeListReqVo vo) {
//        return success(contractTypeService.getContractTypePage(vo));
//    }
//
//    /**
//     * 下拉列表显示合同类型
//     */
//    @PostMapping("/selectList")
//    @Operation(summary = "下拉列表显示合同类型")
//    public CommonResult<List<ContractTypeSimpleRespVo>> getContractTypeSimpleList( ) {
//        return success(contractTypeService.getContractTypeSimpleList( ));
//    }
//
//    /**
//     * 新增合同类型（根据分类ID筛选 abstract）
//     * 根据左侧合同类型分类的选择弹出列表展示
//     */
//    @PostMapping(value = "/insert")
//    @Operation(summary = "新增合同类型", description = "新增合同类型")
//    public CommonResult<Object> insert(@RequestBody @Valid ContractTypeCreateVo vo) {
//        if (ObjectUtil.isEmpty(vo)) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
//        }
//        contractTypeService.insert(vo);
//        return success(true);
//    }
//
//    /**
//     * 批量删除
//     */
//    @PostMapping(value = "/delete")
//    @Operation(summary = "批量删除合同类型", description = "批量删除合同类型")
//    public CommonResult<Object> delete(@RequestBody DelReqVo vo) {
//        List<String> ids = vo.getIds();
//        if (CollUtil.isEmpty(ids)) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_EMPTY_PARAM_ERROR);
//        }
//        List<ContractType> contractTypes = contractTypeService.listByIds(ids);
//        if (isContractTypeUsed(contractTypes)) {
//            throw exception(ErrorCodeConstants.CONTRACT_TYPE_USED_ERROR);
//        }
//        contractTypeService.removeBatchByIds(ids);
//        return success(contractTypeService.removeBatchByIds(ids));
//    }
//
//    /**
//     * 编辑合同类型
//     */
//    @PostMapping(value = "/updateContractType")
//    @Operation(summary = "编辑合同类型", description = "编辑合同类型")
//    public CommonResult<Object> update(@RequestBody ContractTypeUpdateVo vo) {
//        contractTypeService.updateType(vo);
//        return success(true);
//    }
//
//    /**
//     * 合同类型是否含调用过
//     */
//    public boolean isContractTypeUsed(List<ContractType> types) {
//        for (ContractType type : types) {
//            if (CollUtil.isNotEmpty(contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractType, type.getId())))) {
//                return true;
//            }
//            if (CollUtil.isNotEmpty(modelService.list(new LambdaQueryWrapperX<Model>().eq(Model::getContractType, type.getId())))) {
//                return true;
//            }
//            if (CollUtil.isNotEmpty(paramMapper.selectList(new LambdaQueryWrapperX<Param>().eq(Param::getContractType, type.getId())))) {
//                return true;
//            }
//            //还差一个条款没查
//
//        }
//        return false;
//    }
//
//    /**
//     * 展示下拉框合同类型
//     * */
//    @PostMapping(value = "/showSelectContractType")
//    @Operation(summary = "展示下拉框合同类型", description = "展示下拉框合同类型")
//    public CommonResult<Object> showSelectContractType(ContractTypeSelectReqVO reqVO) {
//
//        return success(contractTypeService.showSelectContractType( reqVO));
//    }


}
