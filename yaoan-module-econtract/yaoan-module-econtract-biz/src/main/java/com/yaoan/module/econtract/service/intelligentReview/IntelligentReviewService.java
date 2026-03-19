package com.yaoan.module.econtract.service.intelligentReview;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.RecentUseModelVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.*;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.sign.vo.VerificationRespVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IntelligentReviewService {

    /**
     * 智能填写映射code
     * @param toJsonReqVO
     * @return
     */
    List<JsonRespVO> toJson(ToJsonReqVO toJsonReqVO) throws Exception;

    /**
     * 智能填写映射code 表单
     * @param formToJsonReqVO
     * @return
     */
    List<JsonFormRespVO> toJsonForm(FormToJsonReqVO formToJsonReqVO) throws Exception;


    GetTokenRespVO  getToken(String appId, String tokenId) throws UnsupportedEncodingException;

    ErrorInfosRespVO getErrorInfos(ErrorInfosReqVO vo) throws JsonProcessingException;

    CorrectionRespVO correction(CorrectionReqVO reqVO) throws Exception;

    TokenRespVO generateToken();

    TaskIdRespVO upload(MultipartFile file) throws Exception;

    ContractDetailRespVO detail(String taskIds);

    ContractDetailRespVO getDetail(String taskId);

    Map<String, String> jsonV2(ToJsonReqVO toJsonReqVO) throws IOException;

    Map<String, Object> saveFile(String caseID, byte[] documentData, String sign) throws IOException;

    CorrectionRespVO intelligentReview(CorrectionReqVO reqVO) throws Exception;
}
