package com.yaoan.module.system.convert.dept;

import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyRespVO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyUpdateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.CompanySimpleSaveReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.SaasCompanyRespVO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserCreateReqVO;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.framework.core.event.CompanyInitializeEvent;
import com.yaoan.module.system.framework.core.event.SupplyCompanyInitializeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CompanyConvert {

    CompanyConvert INSTANCE = Mappers.getMapper(CompanyConvert.class);


    List<CompanyRespVO> convertList(List<CompanyDO> list);

    CompanyRespVO convert(CompanyDO bean);

    CompanyDO convert(CompanyCreateReqVO bean);

    CompanyDO convert(CompanyUpdateReqVO bean);

    DeptDO toDeptDO(CompanyDO bean);

    List<CompanyRespDTO> convertList03(List<CompanyDO> list);

    CompanyRespDTO convert03(CompanyDO bean);


    default CompanyInitializeEvent convert(Object source, CompanyDO companyDO,String username,String password,String idCard,String nickName,String email) {
        CompanyInitializeEvent event = new CompanyInitializeEvent(source);
        event.setId(companyDO.getId());
        event.setTenantId(companyDO.getTenantId());
        event.setCreditCode(companyDO.getCreditCode());
        event.setName(companyDO.getName());
        event.setPhone(companyDO.getPhone());
        event.setPassword(password);
        event.setUsername(username);
        event.setIdCard(idCard);
        event.setNickname(nickName);
        event.setEmail(email);
        return event;
    }

    default SupplyCompanyInitializeEvent convertSupply(Object source, CompanyDO companyDO, String username, String password, String idCard, String nickName, String email) {
        SupplyCompanyInitializeEvent event = new SupplyCompanyInitializeEvent(source);
        event.setId(companyDO.getId());
        event.setTenantId(companyDO.getTenantId());
        event.setCreditCode(companyDO.getCreditCode());
        event.setName(companyDO.getName());
        event.setPhone(companyDO.getPhone());
        event.setPassword(password);
        event.setUsername(username);
        event.setIdCard(idCard);
        event.setNickname(nickName);
        event.setEmail(email);
        return event;
    }

//    @Mapping(source = "phone", target = "username")
    @Mapping(source = "phone", target = "mobile")
//    @Mapping(source = "name", target = "nickname")
    @Mapping(source = "id", target = "companyId")
    UserCreateReqVO company2User(CompanyInitializeEvent event);

    @Mapping(source = "phone", target = "mobile")
//    @Mapping(source = "name", target = "nickname")
    @Mapping(source = "id", target = "companyId")
    UserCreateReqVO company2User(SupplyCompanyInitializeEvent event);

    CompanyDO simpleReq2Do(CompanySimpleSaveReqVO reqVO);


}
