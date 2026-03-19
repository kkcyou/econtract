package com.yaoan.module.system.service.invitecode;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.service.permission.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.yaoan.module.system.controller.admin.invitecode.vo.*;
import com.yaoan.module.system.dal.dataobject.invitecode.InviteCodeDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.system.convert.invitecode.InviteCodeConvert;
import com.yaoan.module.system.dal.mysql.invitecode.InviteCodeMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * 邀请码管理 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class InviteCodeServiceImpl implements InviteCodeService {

    @Resource
    private InviteCodeMapper inviteCodeMapper;
    @Resource
    private RoleService roleService;

    @Override
    public Integer createInviteCode(InviteCodeSaveReqVO createReqVO) {
        // 校验验证码
        validateInviteCode(createReqVO);

        setDefaultValue(createReqVO);
        // 插入
        InviteCodeDO inviteCode = InviteCodeConvert.INSTANCE.convert(createReqVO);
        // 设置租户id
        inviteCode.setTenantId(ObjectUtil.isNull(createReqVO.getTenantId())?TenantContextHolder.getTenantId(): createReqVO.getTenantId());
        inviteCodeMapper.insert(inviteCode);
        // 返回
        return inviteCode.getId();
    }

    @Override
    public void updateInviteCode(InviteCodeSaveReqVO updateReqVO) {
        // 校验验证码
        validateInviteCode(updateReqVO);

        setDefaultValue(updateReqVO);
        // 更新
        InviteCodeDO updateObj = InviteCodeConvert.INSTANCE.convert(updateReqVO);
        // 设置租户id
        updateObj.setTenantId(ObjectUtil.isNull(updateReqVO.getTenantId())?TenantContextHolder.getTenantId(): updateReqVO.getTenantId());
        inviteCodeMapper.updateById(updateObj);
    }

    @Override
    public void deleteInviteCode(Integer id) {
        // 校验存在
        if (inviteCodeMapper.selectById(id) == null) {
            throw exception(DIY_ERROR, "邀请码不存在");
        }
        // 删除
        inviteCodeMapper.deleteById(id);
    }

    private void validateInviteCode(InviteCodeSaveReqVO saveReqVO) {
        // 判断是不是超管，只有超管才能保存验证码
        List<RoleRespDTO> roles = roleService.getRoleRespDTOSByUserId(SecurityFrameworkUtils.getLoginUserId());
        if (CollectionUtil.isNotEmpty(roles)) {
            List<String> roleCodes = roles.stream().map(RoleRespDTO::getCode).collect(Collectors.toList());
            // 判断是否是超级管理员， 不是则需要校验邀请码
            if (!roleCodes.contains(RoleCodeEnum.SUPER_ADMIN.getCode())) {
                throw exception(DIY_ERROR, "您无权操作邀请码，请联系超级管理员处理");
            }
        }

        // 如果是更新。校验存在
        if (ObjectUtil.isNotEmpty(saveReqVO.getId())) {
            if (inviteCodeMapper.selectById(saveReqVO.getId()) == null) {
                throw exception(DIY_ERROR, "邀请码不存在");
            }
        }
        // 校验邀请码
        String pattern = "^[A-Z0-9]{6}$";
        if (!saveReqVO.getCode().matches(pattern)) {
            throw exception(DIY_ERROR, "邀请码格式错误");
        }

        List<InviteCodeDO> inviteCodeDOS = inviteCodeMapper.selectList(InviteCodeDO::getCode, saveReqVO.getCode());
        List<InviteCodeDO> codeDOS = inviteCodeDOS.stream().filter(item -> !item.getId().equals(saveReqVO.getId())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(codeDOS)) {
            throw exception(DIY_ERROR, "邀请码已存在");
        }
    }

    @Override
    public InviteCodeDO getInviteCodeInfo(Integer id) {
        return inviteCodeMapper.selectById(id);
    }

    @Override
    public List<InviteCodeDO> getInviteCodeList(Collection<Integer> ids) {
        return inviteCodeMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<InviteCodeDO> getInviteCodePage(InviteCodePageReqVO pageReqVO) {
        return inviteCodeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<InviteCodeDO> getInviteCodeList(InviteCodeExportReqVO exportReqVO) {
        return inviteCodeMapper.selectList(exportReqVO);
    }

    @Override
    public void validInviteCode(String code, Long userId) {


        // 判断是不是超管，超管无需校验邀请码
        List<RoleRespDTO> roles = roleService.getRoleRespDTOSByUserId(userId);
        if (CollectionUtil.isNotEmpty(roles)) {
            List<String> roleCodes = roles.stream().map(RoleRespDTO::getCode).collect(Collectors.toList());
            // 判断是否是超级管理员， 不是则需要校验邀请码
            if (roleCodes.contains(RoleCodeEnum.SUPER_ADMIN.getCode())) {
                return;
            }
        }

        // region [校验邀请码]
        InviteCodeDO inviteCode = inviteCodeMapper.selectOne(InviteCodeDO::getCode, code);

        // 校验存在
        if (inviteCode == null) {
            throw exception(DIY_ERROR, "邀请码不存在");
        }
        // 校验状态
        if (!Integer.valueOf(1).equals(inviteCode.getStatus())) {
            throw exception(DIY_ERROR, "邀请码已禁用");
        }

        // 校验有效期
        if (!Integer.valueOf(-1).equals(inviteCode.getValidDays())) {
            // 如果日期不是不限日期，则判断是否过期
            if (inviteCode.getValidEndDate().isBefore(LocalDateTime.now())) {
                throw exception(DIY_ERROR, "邀请码已过期");
            }
        }

        // 校验使用次数
        if (!Integer.valueOf(-1).equals(inviteCode.getValidTimes())) {
            if (inviteCode.getValidTimes() <= 0) {
                throw exception(DIY_ERROR, "邀请码已用完");
            } else {
                inviteCode.setValidTimes(inviteCode.getValidTimes() - 1);
            }
        }
        //暂时不做与用户或者租户的关联
        /*
        // 校验关联用户
       if (!Long.valueOf(-1).equals(inviteCode.getUserId())){
            // 如果还未分配用户则分配
            if (ObjectUtil.isNull(inviteCode.getUserId()) || Long.valueOf(0).equals(inviteCode.getUserId())){
                inviteCode.setUserId(userId);
            } else {
                // 如果已分配用户则校验
                if (!inviteCode.getUserId().equals(userId)){
                    throw exception(DIY_ERROR, "邀请码已用完");
                }
            }
        }
        // 校验关联租户
        if (!Long.valueOf(-1).equals(inviteCode.getTenantId())){
            Long tenantId = TenantContextHolder.getTenantId();
            if (!tenantId.equals(inviteCode.getTenantId())){
                throw exception(DIY_ERROR, "邀请码不存在");
            }
        }
        */
        // endregion
        inviteCodeMapper.updateById(inviteCode);
    }

    @Override
    public String getInviteCode() {
        return generateInvitationCode();
    }

    private void setDefaultValue(InviteCodeSaveReqVO createReqVO) {
        // 如果没有设置有效期，则有效天数为空设置为默认7天
        if (ObjectUtil.isNull(createReqVO.getValidDays()) && ObjectUtil.isNull(createReqVO.getValidEndDate())) {
            createReqVO.setValidDays(7);
        }
        // 如果有效期为空，但截至日期不为空，则计算有效期
        if (ObjectUtil.isNull(createReqVO.getValidDays()) && ObjectUtil.isNotNull(createReqVO.getValidEndDate())) {
            createReqVO.setValidDays(Integer.valueOf(String.valueOf(DateUtil.betweenDay(DateUtils.of(LocalDateTime.now()), createReqVO.getValidEndDate(), false))));
        }

        // 如果有效天数不是永久，且有效截至日期未设置值，则计算有效期至
        if (createReqVO.getValidDays() != -1 && ObjectUtil.isNull(createReqVO.getValidEndDate())) {
            createReqVO.setValidEndDate(DateUtils.of(LocalDateTime.now().plusDays(createReqVO.getValidDays())));
        }
        // 如果有效次数为空，默认设置为不限次数
        if (ObjectUtil.isNull(createReqVO.getValidTimes())) {
            createReqVO.setValidTimes(IfNumEnums.RJ.getCode());
        }

        // 默认启用
        if (ObjectUtil.isNull(createReqVO.getStatus())) {
            createReqVO.setStatus(IfNumEnums.YES.getCode());
        }
    }

    private String generateInvitationCode() {
        long timestamp = System.currentTimeMillis();
        String timestampStr = String.valueOf(timestamp);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(timestampStr.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b).toUpperCase();
                ;
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 截取哈希值的十六位
            String subHash = hexString.substring(4, 10);

            // 将用“-”分隔的四组字符按组倒序处理
            String[] parts = subHash.split("");
            List<String> partList = Arrays.asList(parts);
            Collections.reverse(partList);
            StringBuilder reversedCode = new StringBuilder();
            for (int i = 0; i < partList.size(); i++) {
                reversedCode.append(partList.get(i));
            }

            // 判断邀请码是否存在
            List<InviteCodeDO> inviteCodeDOS = inviteCodeMapper.selectList(InviteCodeDO::getCode, reversedCode.toString());
            if (inviteCodeDOS.size() > 0) {
                // 如果邀请码已存在，则重新生成一个
                return generateInvitationCode();
            }
            return reversedCode.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
