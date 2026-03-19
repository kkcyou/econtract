package com.yaoan.module.econtract.convert.changxie;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXAddReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.cleandraft.CXCleanDraftReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.GetDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.InsertDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.watermark.CXWaterMarkReqDTO;
import com.yaoan.module.econtract.controller.admin.cx.vo.add.CXAddJsonDTO;
import com.yaoan.module.econtract.controller.admin.cx.vo.add.CXAddReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft.CXCleanDraftReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.converter.CXFileConverterReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.getcontent.GetDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.insertcontent.InsertDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.save.CXSaveReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.watermark.CXWaterMarkReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 17:00
 */
@Mapper
public interface ChangXieConverter {
    ChangXieConverter INSTANCE = Mappers.getMapper(ChangXieConverter.class);

    CXFileConverterDTO req2DTO(CXFileConverterReqVO reqVO);

    @Mapping(target = "jsonArr", expression = "java(list2String(reqVO.getJsonArr()))")
    CXAddReqDTO addReq2DTO(CXAddReqVO reqVO);

    default String list2String(List<CXAddJsonDTO> jsonArr) {
        if (CollectionUtil.isEmpty(jsonArr)) {
            return null;
        }
        String result = JSONUtil.toJsonStr(jsonArr);

        return result;
    }

    CXWaterMarkReqDTO waterReq2DTO(CXWaterMarkReqVO reqVO);

    CXSaveReqDTO cxSaveReq2DTO(CXSaveReqVO reqVO);

    GetDocContentReqDTO getContentReq2DTO(GetDocContentReqVO reqVO);

    InsertDocContentReqDTO insertContentToDoc(InsertDocContentReqVO reqVO);

    CXCleanDraftReqDTO cleandraft2DTO(CXCleanDraftReqVO reqVO);
}
