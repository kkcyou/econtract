package com.yaoan.module.econtract.service.annotation;

import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateBatchReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.BigAnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 11:54
 */
public interface AnnotationService {

    String save(AnnotationSaveUpdateReqVO vo);

    String update(AnnotationSaveUpdateReqVO vo);

    List<BigAnnotationListRespVO> getAnnotationByFileId(IdReqVO vo);

    String delete(IdReqVO vo);

    String checkAnnotationsByFileId(IdReqVO vo);

    String saveBatch(AnnotationSaveUpdateBatchReqVO vo);
}
