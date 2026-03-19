package com.yaoan.module.econtract.service.relative;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.param.vo.*;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistApplyReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistHandleReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.*;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface RelativeService {

    String saveRelative(RelativeCreateReqVO relativeCreateReqVO);

    void saveContact(ContactReqVO contactReqVO);

    void updateById(RelativeUpdateReqVO relativeUpdateReqVO) throws Exception;

    void removeByIds(ReqIdsVO reqIdsVO);

    void deleteContact(String id);

    RelativeRespVO queryRelativeById(String id);

    PageResult<RelativeListRespVO> queryAllRelative(RelativePageReqVO relativePageReqVO);

    PageResult<RelativeListRespVO> queryAllByContact(RelativePageReqVO relativePageReqVO);

    RelativeByUserRespVO queryRelativeByLoginUser();

    
    String saveRelativeBlacklist(BlacklistApplyReqVO relativeCreateReqVO);
    PageResult<BlacklistRespVO> getRelativeBlacklist(BlacklistPageReqVO relativePageReqVO);

    String auditRelativeBlacklistApply(BlacklistHandleReqVO blacklistHandleReqVO);
    String rejectRelativeBlacklistApply(BlacklistHandleReqVO blacklistHandleReqVO);

    BlacklistRespVO queryRelativeBlacklistById(String id);

    Relative selectById(String id);

    String submit(RelativeSubReqVO vo);

    BigRelativeBpmRespVO getBpmAllTaskPage(Long loginUserId, @Valid RelativeBpmPageReqVO pageVO);

    BigRelativeBpmRespVO getBpmDoneTaskPage(Long loginUserId, @Valid RelativeBpmPageReqVO pageVO);

    BigRelativeBpmRespVO getBpmToDoTaskPage(Long loginUserId, @Valid RelativeBpmPageReqVO pageVO);

    PageResult<RelativeBpmListBpmRespVO> listApplication(RelativeBpmPageReqVO vo);

    /**
     *     获取相对方一个默认的联系人id
     */
    Long getOneDefaultContactId(String relativeId);

    /**
     *     获取相对方全部联系人id
      */
    List<Long> getAllContactId(String relativeId);

}
