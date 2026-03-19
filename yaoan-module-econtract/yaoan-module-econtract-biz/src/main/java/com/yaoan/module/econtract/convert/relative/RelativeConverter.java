package com.yaoan.module.econtract.convert.relative;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeCompanyDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.econtract.controller.admin.relative.vo.*;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @Author：doujl
 * @Description: 转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper(componentModel = "spring")
public interface RelativeConverter {

    RelativeConverter INSTANCE = Mappers.getMapper(RelativeConverter.class);

    Relative toEntity(RelativeCreateReqVO bean);

    Relative toEntity(RelativeUpdateReqVO bean);

    Relative toEntity(ContactReqVO bean);

    RelativeListRespVO toVO(Relative relative);

    RelativeRespVO toRespVO(Relative relative);

    PageResult<RelativeListRespVO> convertPage(PageResult<Relative> page);

    RelativeByUserRespVO toEntity(Relative relative);

    List<RelativeByUserRespVO> convert2List(List<Relative> bean);

    @Mappings({
            @Mapping(source = "supplyCn", target = "name"),
            @Mapping(source = "orgCode", target = "code"),
            @Mapping(ignore = true, target = "tenantType", defaultValue = "1"),
            @Mapping(source = "supplyCn", target = "contactName"),
            @Mapping(ignore = true, target = "relativeType", defaultValue = "1"),
            @Mapping(ignore = true, target = "entityType", defaultValue = "2"),
            @Mapping(ignore = true, target = "cardType", defaultValue = "1"),
            @Mapping(source = "orgCode", target = "cardNo"),
            @Mapping(source = "legalPerson", target = "legalName"),
            @Mapping(ignore = true, target = "legalCardType", defaultValue = "1"),
            @Mapping(source = "legalIdCard", target = "legalCardNo"),
            @Mapping(source = "bankAccount", target = "bankAccount"),
            @Mapping(source = "bankName", target = "bankName"),
            @Mapping(source = "addr", target = "address"),
            @Mapping(source = "tel", target = "contactTel")


    })
    Relative supplyToEntity(SupplyDO supplyDO);

    default LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    default Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    PageResult<RelativeBpmListBpmRespVO> convertBpmPage(PageResult<Relative> relativePageResult);

    List<RelativeBpmListBpmRespVO> cList(List<Relative> list);

    RelativeBpmListBpmRespVO c(Relative bean);

    @Mapping(target = "id", ignore = true)
    List<UserDTO> relative2CreateDTOs(List<RelativeContact> relativeContactList);


    RelativeContactDTO contactDO2DTO(RelativeContact bean);
    List<RelativeContactDTO> contactDO2DTOs(List<RelativeContact> contactList);

    List<RelativeContact> contactDTO2DOs(List<RelativeContactDTO> contactDTOList);
    RelativeContact contactDTO2DO(RelativeContactDTO bean);

    List<RelativeCompanyDTO> contactSimpleDo2Dtos(List<RelativeContact> relativeContactList);

    RelativeDTO do2Dto(Relative relative);

    List<RelativeDTO> listDo2Dto(List<Relative> relatives);
}
