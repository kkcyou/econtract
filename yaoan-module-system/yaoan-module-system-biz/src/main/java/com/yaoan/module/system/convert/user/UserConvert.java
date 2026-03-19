package com.yaoan.module.system.convert.user;

import com.yaoan.module.econtract.api.contract.dto.UserDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyAllInfoRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthLoginReqVO;
import com.yaoan.module.system.controller.admin.user.vo.profile.UserProfileRespVO;
import com.yaoan.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.yaoan.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.*;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.social.SocialUserDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserPageItemRespVO convert(AdminUserDO bean);

    UserPageItemRespVO.Dept convert(DeptDO bean);

    AdminUserDO convert(UserCreateReqVO bean);

    AdminUserDO convert(UserUpdateReqVO bean);

    UserExcelVO convert02(AdminUserDO bean);

    AdminUserDO convert(UserImportExcelVO bean);

    UserProfileRespVO convert03(AdminUserDO bean);

    List<UserProfileRespVO.Role> convertList(List<RoleDO> list);

    UserProfileRespVO.Dept convert02(DeptDO bean);

    AdminUserDO convert(UserProfileUpdateReqVO bean);

    AdminUserDO convert(UserProfileUpdatePasswordReqVO bean);

    List<UserProfileRespVO.Post> convertList02(List<PostDO> list);

    List<UserProfileRespVO.SocialUser> convertList03(List<SocialUserDO> list);

    List<UserSimpleRespVO> convertList04(List<AdminUserDO> list);

    AdminUserRespDTO convert4(AdminUserDO bean);

    List<AdminUserRespDTO> convertList4(List<AdminUserDO> users);

    UserUpdateReqVO convert(AuthLoginReqVO authVo);

    AdminUserDO convert2UserDo(AuthLoginReqVO authVo);
    UserCompanyDeptRespDTO toUserCompanyDeptRespDTO(AdminUserDO userDO);


    List<AdminUserRespDTO> convert2DO(List<AdminUserDO> dos);

    UserCompanyAllInfoRespDTO convert2CompanyDTO(CompanyDO companyDO);

    List<AdminUserRespDTO> selectListByDeptIdsAndUserIds(List<AdminUserDO> users);

    List<AdminUserDO> dto2DOList(List<UserDTO> userDTOList);

    AdminUserDO dto2DO(UserDTO dto);
}
