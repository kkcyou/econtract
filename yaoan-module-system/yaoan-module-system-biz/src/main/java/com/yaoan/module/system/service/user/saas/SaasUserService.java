package com.yaoan.module.system.service.user.saas;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserPageRespVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserSaveReqVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserTransferAdminUserReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.UserPageReqVO;

import javax.validation.Valid;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 20:08
 */
public interface SaasUserService {

    Long save(@Valid SaasUserSaveReqVO reqVO);

    PageResult<SaasUserPageRespVO> querySaasUserList(UserPageReqVO reqVO);

    /**
     * 删除用户
     *
     * @param id 用户编号
     */
    void deleteUser(Long id);

    /**
     * 修改状态
     *
     * @param id     用户编号
     * @param status 状态
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * 转让管理员
     */
    void transferAdminUser(Long id);
    /**
     * 转交合同
     */
    void transferContract(SaasUserTransferAdminUserReqVO reqVO);
    void validateUserExistsContract(Long id,String  type);
    void validateUserExists(Long id);

}
