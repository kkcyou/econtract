package com.yaoan.module.econtract.service.foxi;

import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveRespDTO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareRespVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveRespVO;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-5 16:24
 */
public interface FoxiService {

    FoxiSaveRespVO saveBack(FoxiSaveReqVO reqVO);

    FoxiSaveRespVO saveBackTest(FoxiSaveReqVO reqVO);

    PdfCompareRespVO pdfCompare(PdfCompareReqVO reqVO);

    void initial();
}
