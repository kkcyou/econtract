package com.yaoan.module.econtract.service.cx;

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
import com.yaoan.module.infra.enums.FileUploadPathEnum;

import java.io.IOException;
import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 10:19
 */
public interface ChangXieService {

    CXRespVO saveFile(CXReqVO reqVO);

    CXCallbackRespVO singleFileUploads(CXCallbackReqVO reqVO) throws IOException;

    CXFileConverterRespDTO converter(CXFileConverterReqVO reqVO);

    CXAddRespDTO addtocontentcontrol(CXAddReqVO reqVO);

    CXWaterMarkRespDTO insertwatermark(CXWaterMarkReqVO reqVO);

    CXSaveRespDTO cxSave(CXSaveReqVO reqVO);

    Long createContractByOrder(CXCreateContractByOrderReqVO reqVO) throws Exception;

    GetDocContentRespDTO getDocContent(GetDocContentReqVO reqVO);

    InsertDocContentRespDTO insertContentToDoc(InsertDocContentReqVO reqVO);

    CXCleanDraftRespVO cleandraft(CXCleanDraftReqVO reqVO);

    Long cleandraftV2(Long fileId);

    Map<String,String> cleandraftV3(Long fileId);

    Long converterDocx2Pdf(Long fileId);

    Long converterDocx2PdfV2(Long fileId, FileUploadPathEnum pathEnum);

    Long converterDocx2PdfV3(Map<String,String> map, FileUploadPathEnum pathEnum, Long pdfFileId);

}
