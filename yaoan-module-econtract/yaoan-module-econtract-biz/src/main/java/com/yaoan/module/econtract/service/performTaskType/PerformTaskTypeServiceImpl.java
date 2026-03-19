package com.yaoan.module.econtract.service.performTaskType;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.*;
import com.yaoan.module.econtract.convert.performTaskType.PerformTaskTypeConverter;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import com.yaoan.module.econtract.dal.mysql.performTaskType.PerformTaskTypeMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/3 15:23
 */
@Service
public class PerformTaskTypeServiceImpl implements PerformTaskTypeService {
    final static Boolean NOT_MODIFIED = false;
    @Resource
    private PerformTaskTypeMapper performTaskTypeMapper;
    @Resource
    private AdminUserApi userApi;

    @Override
    public PageResult<PerformTaskTypeRespVO> getPerformTaskTypePage(PerformTaskTypeListReqVo vo) {
        //1，找出初始类型的creator
        List<PerformTaskTypeDO> initDOs = performTaskTypeMapper.selectList(new LambdaQueryWrapperX<PerformTaskTypeDO>().eq(PerformTaskTypeDO::getModifiable, false));
        Long initCreatorId;
        AdminUserRespDTO initUser = new AdminUserRespDTO();
        if (CollUtil.isNotEmpty(initDOs)) {
            initCreatorId = Long.valueOf(initDOs.get(0).getCreator());
            initUser = userApi.getUser(initCreatorId);
        }
        //2，找出当前用户的id
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();

        //条件查询
        LambdaQueryWrapperX<PerformTaskTypeDO> wrapperX = new LambdaQueryWrapperX<PerformTaskTypeDO>();

        wrapperX.and((queryWrapper) -> {

            //创建时间
            if (ObjectUtil.isNotNull(vo.getCreateTimeStart())) {
                queryWrapper.between(PerformTaskTypeDO::getCreateTime, vo.getCreateTimeStart(), vo.getCreateTimeEnd());
            }
            if (StringUtils.isNotBlank(vo.getSearchText())) {
                queryWrapper.like(PerformTaskTypeDO::getName, vo.getSearchText());
            }
            queryWrapper.eq(PerformTaskTypeDO::getModifiable, true).or().eq(PerformTaskTypeDO::getModifiable, NOT_MODIFIED);
        });

        PageResult<PerformTaskTypeDO> doPageResult = performTaskTypeMapper.selectPage(vo, wrapperX);
        PageResult<PerformTaskTypeRespVO> voPageResult = PerformTaskTypeConverter.INSTANCE.convert2R(doPageResult);
        AdminUserRespDTO loginUser = userApi.getUser(loginUserId);

        String loginUserName = ObjectUtil.isNull(loginUser) ? "" : loginUser.getNickname();
        String initCreatorName = ObjectUtil.isNull(initUser) ? "" : initUser.getNickname();
        voPageResult.getList().forEach(v -> {
            if (v.getModifiable().equals(NOT_MODIFIED)) {
                v.setCreatorName(initCreatorName);
            } else {
                v.setCreatorName(loginUserName);
            }
        });

        return voPageResult;
    }

    @Override
    public String create(PerformTaskTypeCreateReqVO reqVO) {
        PerformTaskTypeDO taskTypeDO = PerformTaskTypeConverter.INSTANCE.convert2DO(reqVO);

        //校验名称和编码唯一性
        isCodeExist(taskTypeDO.getCode());
        isNameExist(taskTypeDO.getName());

        performTaskTypeMapper.insert(taskTypeDO);

        return taskTypeDO.getId();
    }

    @Override
    public void updatePerformTaskType(PerformTaskTypeUpdateReqVO reqVO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();

        //校验是否有权限编辑
        if (!isPermission(reqVO, loginUserId)) {
            throw exception(PERFORM_TASK_TYPE_NO_PERMISSION_UPDATE_ERROR);
        }
        //校验名称是否唯一
        isNameExist(reqVO.getName());
        PerformTaskTypeDO taskTypeDO = PerformTaskTypeConverter.INSTANCE.convert2DO(reqVO);
        performTaskTypeMapper.updateById(taskTypeDO);
    }

    @Override
    public void deletePerformTaskType(DeleteVO reqVO) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();

        List<String> ids = reqVO.getIds();
        List<PerformTaskTypeDO> performTaskTypes = performTaskTypeMapper.selectList(new LambdaQueryWrapperX<PerformTaskTypeDO>().in(PerformTaskTypeDO::getId, ids));


        for (PerformTaskTypeDO performTaskType : performTaskTypes) {
            if (!loginUserId.equals(Long.valueOf(performTaskType.getCreator()))) {
                throw exception(PERFORM_TASK_TYPE_NO_PERMISSION_DELETE_ERROR);
            }
        }
        performTaskTypeMapper.deleteBatchIds(ids);
    }

    /**
     * 名称是否已经存在
     */
    public void isNameExist(String name) {
        List<PerformTaskTypeDO> dos = performTaskTypeMapper.selectList(new LambdaQueryWrapperX<PerformTaskTypeDO>().eq(PerformTaskTypeDO::getName, name));
        if (CollUtil.isNotEmpty(dos)) {
            throw exception(ErrorCodeConstants.PERFORM_TASK_TYPE_NAME_EXIST_ERROR);
        }
    }

    /**
     * 编码是否已经存在
     */
    public void isCodeExist(String code) {
        List<PerformTaskTypeDO> dos = performTaskTypeMapper.selectList(new LambdaQueryWrapperX<PerformTaskTypeDO>().eq(PerformTaskTypeDO::getCode, code));
        if (CollUtil.isNotEmpty(dos)) {
            throw exception(ErrorCodeConstants.PERFORM_TASK_TYPE_CODE_EXIST_ERROR);
        }
    }

    /**
     * 校验是否有权限编辑
     */
    private boolean isPermission(PerformTaskTypeUpdateReqVO reqVO, Long loginUserId) {
        PerformTaskTypeDO taskTypeDO = performTaskTypeMapper.selectById(reqVO.getId());
        if (ObjectUtil.isNull(taskTypeDO)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        return StringUtils.equals(taskTypeDO.getCreator(), String.valueOf(loginUserId));
    }


    /**
     * 转换
     */
    public List<PerformTaskTypeAllVO> convertToAllVO(List<PerformTaskTypeDO> vos) {
        if (vos == null) {
            return null;
        }

        List<PerformTaskTypeAllVO> list = new ArrayList<PerformTaskTypeAllVO>(vos.size());
        for (PerformTaskTypeDO performTaskTypeDO : vos) {
            list.add(performTaskTypeDOToPerformTaskTypeAllVO(performTaskTypeDO));
        }

        return list;
    }

    public PerformTaskTypeAllVO performTaskTypeDOToPerformTaskTypeAllVO(PerformTaskTypeDO performTaskTypeDO) {
        if (performTaskTypeDO == null) {
            return null;
        }

        PerformTaskTypeAllVO performTaskTypeAllVO = new PerformTaskTypeAllVO();
        performTaskTypeAllVO.setId(performTaskTypeDO.getId());
        performTaskTypeAllVO.setCreateTime(performTaskTypeDO.getCreateTime());
        performTaskTypeAllVO.setUpdateTime(performTaskTypeDO.getUpdateTime());
        performTaskTypeAllVO.setCreator(performTaskTypeDO.getCreator());
        performTaskTypeAllVO.setUpdater(performTaskTypeDO.getUpdater());
        performTaskTypeAllVO.setDeleted(performTaskTypeDO.getDeleted());
        performTaskTypeAllVO.setCode(performTaskTypeDO.getCode());
        performTaskTypeAllVO.setName(performTaskTypeDO.getName());
        performTaskTypeAllVO.setModifiable(performTaskTypeDO.getModifiable());
        return performTaskTypeAllVO;
    }

    /**
     * 转换
     */
    public List<PerformTaskTypeRespVO> convertAll2Resp(List<PerformTaskTypeAllVO> allVOs) {
        if (allVOs == null) {
            return null;
        }

        List<PerformTaskTypeRespVO> list = new ArrayList<PerformTaskTypeRespVO>(allVOs.size());
        for (PerformTaskTypeAllVO performTaskTypeAllVO : allVOs) {
            list.add(performTaskTypeAllVOToPerformTaskTypeRespVO(performTaskTypeAllVO));
        }

        return list;
    }

    public PerformTaskTypeRespVO performTaskTypeAllVOToPerformTaskTypeRespVO(PerformTaskTypeAllVO performTaskTypeAllVO) {
        if (performTaskTypeAllVO == null) {
            return null;
        }

        PerformTaskTypeRespVO performTaskTypeRespVO = new PerformTaskTypeRespVO();
        performTaskTypeRespVO.setId(performTaskTypeAllVO.getId());
        performTaskTypeRespVO.setCode(performTaskTypeAllVO.getCode());
        performTaskTypeRespVO.setName(performTaskTypeAllVO.getName());
        performTaskTypeRespVO.setCreatorName(performTaskTypeAllVO.getCreatorNikeName());
        performTaskTypeRespVO.setModifiable(performTaskTypeAllVO.getModifiable());
        if (performTaskTypeAllVO.getCreateTime() != null) {
            performTaskTypeRespVO.setCreateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(performTaskTypeAllVO.getCreateTime()));
        }

        return performTaskTypeRespVO;
    }
}
