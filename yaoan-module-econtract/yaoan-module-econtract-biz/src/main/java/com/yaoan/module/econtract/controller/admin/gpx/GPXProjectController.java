package com.yaoan.module.econtract.controller.admin.gpx;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.ExternallnterfaceResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.econtract.api.gcy.buyplan.MedicalApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractPlaybackRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractPlaybackV3RespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.SupplierCombinationInfoVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.*;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.AutoFillReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PlaybackReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.PackageInfo;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.ProjectInfo;
import com.yaoan.module.econtract.controller.admin.gpx.vo.supplier.SupplierListRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.supplier.SupplierReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.external.ExternalInterfaceService;
import com.yaoan.module.econtract.service.gpx.GPXService;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 交易执行
 */
@Slf4j
@RestController
@Validated
@Tag(name = "交易执行", description = "交易执行")
public class GPXProjectController {

    @Resource
    private GPXService gpxService;

    @Resource
    private ExternalInterfaceService externalInterfaceService;

    @Resource
    private RedisUtils redisUtils;


    /**
     * 交易执行-接收项目包信息
     */
    @PermitAll
    @Operation(summary = "交易执行-接收项目包信息")
    @PostMapping(value = "econtract/draft/v2/project")
    public CommonResult<String> draft(@RequestBody List<ProjectInfo> list) {
        return CommonResult.success(gpxService.draft(list));
    }

    @PermitAll
    @Operation(summary = "交易执行-接收项目包信息 加密")
    @PostMapping(value = "econtract/draft/project")
    @OperateLog(enable = true)
    ExternallnterfaceResult<String> draftProject(@RequestBody EncryptDTO encryptDTO) throws Exception {
        log.info("【交易执行接收数据】，接收时间{}", new Date());
        externalInterfaceService.checkSignGetClass(encryptDTO);
        List<ProjectInfo> projectInfos = JSONObject.parseArray(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), ProjectInfo.class);
        //日志打印包id
        if (CollUtil.isNotEmpty(projectInfos)) {
            String packageIds = "";
            for (ProjectInfo projectInfo : projectInfos) {
                if (CollUtil.isNotEmpty(projectInfo.getPackageInfoList())) {
                    for (PackageInfo packageInfo : projectInfo.getPackageInfoList()) {
                        packageIds = packageInfo.getId() + ",";
                    }
                }
            }
            log.info("【交易执行接收数据】，采购包ids:{}", packageIds);
        }
        //接收
        gpxService.draft(projectInfos);
        log.info("【交易执行接收数据】，接收结束时间{}", System.currentTimeMillis());
        EncryptDTO result = Sm4Utils.convertParam("success");
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }


    /**
     * 交易执行-起草列表
     */
    @Operation(summary = "交易执行-起草列表")
    @PostMapping(value = "econtract/gpx/list")
    public CommonResult<PageResult<GPXListRespVO>> listProject(@RequestBody GPXListReqVO vo) {
        return CommonResult.success(gpxService.list(vo));
    }


    @PermitAll
    @Operation(summary = "交易执行-接收项目包信息 查看加密的推送数据")
    @PostMapping(value = "econtract/draft/EncryptDTO")
    List<ProjectInfo> showEncryptDTO(@RequestBody EncryptDTO encryptDTO) throws Exception {

        externalInterfaceService.checkSignGetClass(encryptDTO);
        List<ProjectInfo> projectInfos = JSONObject.parseArray(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), ProjectInfo.class);
        return projectInfos;
    }

    /**
     * 交易执行-加密
     */
    @PermitAll
    @Operation(summary = "交易执行-加密")
    @PostMapping(value = "econtract/draft/encode")
    EncryptDTO encode(@RequestBody String str) throws Exception {
        EncryptDTO resultDTO = Sm4Utils.convertParam(str);
        return resultDTO;
    }

    /**
     * 交易执行-解密
     */
    @PermitAll
    @Operation(summary = "交易执行-解密")
    @PostMapping(value = "econtract/draft/show")
    String show(@RequestBody EncryptDTO encryptDTO) throws Exception {
        String rs = JSONObject.parseObject(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), String.class);
        return rs;
    }


    /**
     * 获取起草回显内容V3
     */
    @Operation(summary = "获取起草回显内容V3")
    @PostMapping(value = "econtract/gpx/playbackInfoV3")
    public CommonResult<GPXContractPlaybackV3RespVO> playbackInfoV3(@RequestBody PlaybackReqVO reqVO) throws Exception {
        return CommonResult.success(gpxService.playbackInfoV3(reqVO));
    }


    /**
     * 获取起草回显内容
     */
    @Operation(summary = "获取联合体信息")
    @PostMapping(value = "econtract/gpx/playSupplier/info")
    public CommonResult<List<SupplierCombinationInfoVO>> getSupplierInfoList(@RequestBody PlaybackReqVO reqVO) throws Exception {
        return CommonResult.success(gpxService.getSupplierInfoList(reqVO));
    }



}


