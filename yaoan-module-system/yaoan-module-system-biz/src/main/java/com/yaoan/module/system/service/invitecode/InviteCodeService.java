package com.yaoan.module.system.service.invitecode;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.system.controller.admin.invitecode.vo.*;
import com.yaoan.module.system.dal.dataobject.invitecode.InviteCodeDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 邀请码管理 Service 接口
 *
 * @author admin
 */
public interface InviteCodeService {

    /**
     * 创建邀请码管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Integer createInviteCode(@Valid InviteCodeSaveReqVO createReqVO);

    /**
     * 更新邀请码管理
     *
     * @param updateReqVO 更新信息
     */
    void updateInviteCode(@Valid InviteCodeSaveReqVO updateReqVO);

    /**
     * 删除邀请码管理
     *
     * @param id 编号
     */
    void deleteInviteCode(Integer id);

    /**
     * 获得邀请码管理
     *
     * @param id 编号
     * @return 邀请码管理
     */
    InviteCodeDO getInviteCodeInfo(Integer id);

    /**
     * 获得邀请码管理列表
     *
     * @param ids 编号
     * @return 邀请码管理列表
     */
    List<InviteCodeDO> getInviteCodeList(Collection<Integer> ids);

    /**
     * 获得邀请码管理分页
     *
     * @param pageReqVO 分页查询
     * @return 邀请码管理分页
     */
    PageResult<InviteCodeDO> getInviteCodePage(InviteCodePageReqVO pageReqVO);

    /**
     * 获得邀请码管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 邀请码管理列表
     */
    List<InviteCodeDO> getInviteCodeList(InviteCodeExportReqVO exportReqVO);

    /**
     * 校验邀请码
     * @param code
     */
    void validInviteCode(String code, Long userId);

    /**
     * 获取一个邀请码
     */
    String getInviteCode();

}
