package com.yaoan.module.econtract.convert.contracttype;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contracttype.dto.ContractTypeDTO;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.*;
import com.yaoan.module.econtract.dal.dataobject.category.ContractTypeCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/8 17:00
 */
@Mapper
public interface ContractTypeConverter {
    ContractTypeConverter INSTANCE = Mappers.getMapper(ContractTypeConverter.class);

    ContractType toEntity(ContractTypeDTO ContractTypeDTO);
    ContractType createVoToEntity(ContractTypeCreateVo bean);
    ContractType createVoToEntityV2(ContractTypeCreateV2Vo bean);
    @Mapping(target = "needSignet", expression = "java(map(bean.getNeedSignet()))")
    ContractTypeSelectRespVO toSelectRespVO(ContractType bean);
    @Mapping(target = "needSignet", expression = "java(map(bean.getNeedSignet()))")
    ContractTypePageV2RespVo toSelectResp(ContractType bean);
    List<ContractTypePageV2RespVo> toSelectRespList(List<ContractType> bean);
    ContractType updateStatusReqVotoEntityV2(ContractTypeUpdateStatusReqVO bean);
    PageResult<ContractTypePageV2RespVo> convertPageV2( PageResult<ContractType> page);

    ContractTypeDTO toDTO(ContractType ContractType);

    List<ContractTypeDTO> toDTOs(List<ContractType> demos);

    List<ContractType> toEntities(List<ContractTypeDTO> demos);

    List<ContractTypeExcelVO> convertList02(List<ContractTypePageV2RespVo> list);
    @Mapping(target = "createTime", expression = "java(vo.getCreateTime()==null?null:localDateTimeToString(vo.getCreateTime()))")
    ContractTypeExcelVO convert02(ContractType vo);

    PageResult<ContractTypePageRespVo> convertPage(PageResult<ContractType> page);

    ContractType updateVotoEntity(ContractTypeUpdateVo vo);
    ContractType updateVotoEntityNew(ContractTypeUpdateV2Vo vo);

    List<ContractTypePageRespVo> convertPage(List<ContractType> contractTypes);

    List<ContractTypeSimpleRespVo> convert2SimpleVO(List<ContractType> contractTypes);

    List<ContractTypeCategoryVO> convert2VO(List<ContractTypeCategory> rootList);

    default String localDateTimeToString(LocalDateTime source) {
        if (source == null) {
            return null; // 处理 null 值
        }
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return source.format(formatter); // 转换为字符串
    }

    default Integer map(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }

    List<ContractTypeRespVO> listDO2Resp(List<ContractType> contractTypeList);
}
