package com.yaoan.module.econtract.controller.admin.cx;

import com.alibaba.fastjson.JSON;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXAddRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.GetDocContentRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.InsertDocContentRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.watermark.CXWaterMarkRespDTO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXCallbackReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXCallbackRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.add.CXAddReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft.CXCleanDraftReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft.CXCleanDraftRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.converter.CXFileConverterReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.getcontent.GetDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.insertcontent.InsertDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.ordercontract.CXCreateContractByOrderReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.save.CXSaveReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.watermark.CXWaterMarkReqVO;
import com.yaoan.module.econtract.service.cx.ChangXieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.ServletException;
import java.io.IOException;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 畅写功能模块
 * @author: Pele
 * @date: 2024/4/25 10:15
 */
@Slf4j
@RestController
@RequestMapping("econtract/design")
@Validated
@Tag(name = "畅写功能模块", description = "畅写功能模块")
public class ChangXieController {

    @Resource
    private ChangXieService changXieService;


    /**
     * 畅写保存
     */
    @PostMapping(value = "/coauthoring/CommandService.ashx")
    public CommonResult<CXSaveRespDTO> cxSave(@RequestBody @Validated CXSaveReqVO reqVO) {
        return success(changXieService.cxSave(reqVO));
    }


    /**
     * 向畅写后台保存文档
     */
    @PostMapping("/saveFile")
    @Operation(summary = "后台保存")
    public CXRespVO saveFile(@RequestBody @Validated CXReqVO reqVO) {
        return changXieService.saveFile(reqVO);
    }

    /**
     * 畅写回调保存
     * status 1 接收到每个用户连接或从文档协同编辑断开连接。回调保存需返回{"error":0}
     * status 2 文档关闭收到，代表最关闭文档用户。回调保存需返回{"error":0}
     * status 3 文档关闭时并且畅写服务器保存文档处理出错。回调保存需返回{"error":-1}。 注：不返回{"error":-1}，编辑的文档内容会丢失
     * status 4 用户关闭文档,且未做任何更改。回调保存需返回{"error":0}
     * status 6 执行强制保存。回调保存需返回{"error":0}
     * status 7 执行强制保存并且畅写服务器保存文档处理出错。回调保存需返回{"error":-1}。注：不返回{"error":-1}，编辑的文档内容会丢失
     */
    @PostMapping("/callback")
    @PermitAll
    public CXCallbackRespVO singleFileUploads(@RequestBody @Validated CXCallbackReqVO reqVO) throws ServletException, IOException {
        log.info("【畅写回调】："+ JSON.toJSONString(reqVO));
        return changXieService.singleFileUploads(reqVO);
    }


    /**
     * 文件转换
     */
    @Operation(summary = "文件转换")
    @PostMapping(value = "/converter")
    public CommonResult<CXFileConverterRespDTO> converter(@RequestBody @Validated CXFileConverterReqVO reqVO) {
        return success(changXieService.converter(reqVO));
    }

    /**
     * 指定内容域插入数据
     */
    @PostMapping(value = "/addtocontentcontrol")
    public CommonResult<CXAddRespDTO> addtocontentcontrol(@RequestBody @Validated CXAddReqVO reqVO) {
        return success(changXieService.addtocontentcontrol(reqVO));
    }

    /**
     * 水印
     */
    @PostMapping(value = "/insertwatermark")
    public CommonResult<CXWaterMarkRespDTO> insertwatermark(@RequestBody @Validated CXWaterMarkReqVO reqVO) {
        return success(changXieService.insertwatermark(reqVO));
    }

    /**
     * 通过订单，生成合同
     */
    @PostMapping(value = "/createContractFileByOrder")
    public CommonResult<Long> createContractByOrder(@RequestBody @Validated CXCreateContractByOrderReqVO reqVO) throws Exception {
        return success(changXieService.createContractByOrder(reqVO));
    }

    /**
     * 提取word文档内容
     */
    @PostMapping(value = "/getDocContent")
    public CommonResult<GetDocContentRespDTO> getDocContent(@RequestBody @Validated GetDocContentReqVO reqVO) throws Exception {
        return success(changXieService.getDocContent(reqVO));
    }

    /**
     * 将提取的word文档内容插入指定文档
     */
    @PostMapping(value = "/insertContentToDoc")
    public CommonResult<InsertDocContentRespDTO> insertContentToDoc(@RequestBody @Validated InsertDocContentReqVO reqVO) throws Exception {
        return success(changXieService.insertContentToDoc(reqVO));
    }

    /**
     * 一键清稿（接受全部修订、删除全部批注）
     */
    @PostMapping(value = "/cleandraft")
    public CommonResult<CXCleanDraftRespVO> cleandraft(@RequestBody @Validated CXCleanDraftReqVO reqVO) throws Exception {
        return success(changXieService.cleandraft(reqVO));
    }
    /**
     * 一键清稿V2（接受全部修订、删除全部批注）
     */
    @GetMapping(value = "/cleandraftV2/{fileId}")
    public CommonResult<Long> cleandraftV2( @PathVariable Long fileId ) throws Exception {
        return success(changXieService.cleandraftV2(fileId));
    }

    /**
     * 畅写 流式文件转pdf文件
     */
    @PostMapping(value = "/converterDocx2Pdf/{fileId}")
    public CommonResult<Long> converterDocx2Pdf(@PathVariable Long fileId) throws Exception {
        return success(changXieService.converterDocx2Pdf(fileId));
    }
}


