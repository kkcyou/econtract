package com.yaoan.module.econtract.dal.mysql.relativeBlacklist;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.relativeBlacklist.RelativeBlacklist;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lls
 * @since  2024-08-28
 */

@Mapper
public interface RelativeBlacklistMapper extends BaseMapperX<RelativeBlacklist> {
    default PageResult<RelativeBlacklist> selectRegisterPage(BlacklistPageReqVO blacklistPageReqVO) {
        MPJQueryWrapper<RelativeBlacklist> mpjQueryWrapper = new MPJQueryWrapper<RelativeBlacklist>()
                .leftJoin("ecms_relative c on t.relative_id = c.id AND c.deleted = 0")
                .selectAll(RelativeBlacklist.class).orderByDesc("t.update_time");
        //mpjQueryWrapper.select("c.code","c.name","c.entity_type as entityType");
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getApplyStatus())){
            mpjQueryWrapper.eq("t.apply_status", blacklistPageReqVO.getApplyStatus());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getApplyMsg())){
            mpjQueryWrapper.like("t.apply_msg", blacklistPageReqVO.getApplyMsg());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getApplyType())){
            mpjQueryWrapper.eq("t.apply_type", blacklistPageReqVO.getApplyType());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getCode())){
            mpjQueryWrapper.like("c.code", blacklistPageReqVO.getCode());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getName())){
            mpjQueryWrapper.like("c.name", blacklistPageReqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getEntityType())){
            mpjQueryWrapper.eq("c.entity_type", blacklistPageReqVO.getEntityType());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getRelativeType())){
            mpjQueryWrapper.eq("c.relative_type", blacklistPageReqVO.getRelativeType());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getSourceType())) {
            mpjQueryWrapper.eq("c.source_type", blacklistPageReqVO.getSourceType());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getCreator())){
            mpjQueryWrapper.eq("t.creator", blacklistPageReqVO.getCreator());
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getStartDate()) && ObjectUtil.isNotEmpty(blacklistPageReqVO.getEndDate())){
            mpjQueryWrapper.and(relativeBlacklistMPJQueryWrapper -> relativeBlacklistMPJQueryWrapper.between("t.create_time", blacklistPageReqVO.getStartDate(), blacklistPageReqVO.getEndDate()));
        }
        if (ObjectUtil.isNotEmpty(blacklistPageReqVO.getAuditType())){
            mpjQueryWrapper.eq("t.audit_type", blacklistPageReqVO.getAuditType());
        }
        // 相对方的租户隔离逻辑
        Long tenantId = TenantContextHolder.getTenantId();
        mpjQueryWrapper.in("c.tenant_id",tenantId,0);
        return selectPage(blacklistPageReqVO, mpjQueryWrapper);
    }
}