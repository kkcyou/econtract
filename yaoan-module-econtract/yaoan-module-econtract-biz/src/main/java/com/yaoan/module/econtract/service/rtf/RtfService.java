package com.yaoan.module.econtract.service.rtf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.module.econtract.controller.admin.rtf.vo.ModelRespVO;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
public interface RtfService {

    String getRtfByFileId(Long id) throws Exception;

    String getRtfByModelId(String id) throws Exception;

    String getRtfByTemplateId(String id) throws Exception;
    ModelRespVO getRtfByModelId1(String id) throws Exception;
}
