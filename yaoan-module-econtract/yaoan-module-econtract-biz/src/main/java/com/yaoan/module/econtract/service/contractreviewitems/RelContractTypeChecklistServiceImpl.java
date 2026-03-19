package com.yaoan.module.econtract.service.contractreviewitems;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelContractTypeChecklistDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.RelContractTypeChecklistMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewChecklistMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import dm.jdbc.util.StringUtil;
import org.dom4j.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 合同类型-审查清单关联 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RelContractTypeChecklistServiceImpl implements RelContractTypeChecklistService {

    @Resource
    private RelContractTypeChecklistMapper relContractTypeChecklistMapper;

    @Resource
    private ReviewChecklistMapper reviewChecklistMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;


    @Override
    public String createRelContractTypeChecklist(RelContractTypeChecklistSaveReqVO createReqVO) {

        //删除该合同类型下的关联
        relContractTypeChecklistMapper.delete(RelContractTypeChecklistDO::getContractType, createReqVO.getContractType());

        // 插入
        RelContractTypeChecklistDO relContractTypeChecklist = BeanUtils.toBean(createReqVO, RelContractTypeChecklistDO.class);
        relContractTypeChecklistMapper.insert(relContractTypeChecklist);

        // 返回
        return relContractTypeChecklist.getId();
    }

    @Override
    public void updateRelContractTypeChecklist(RelContractTypeChecklistSaveReqVO updateReqVO) {
        // 校验存在
        validateRelContractTypeChecklistExists(updateReqVO.getId());
        // 更新
        RelContractTypeChecklistDO updateObj = BeanUtils.toBean(updateReqVO, RelContractTypeChecklistDO.class);
        relContractTypeChecklistMapper.updateById(updateObj);
    }

    @Override
    public void deleteRelContractTypeChecklist(String id) {
        // 校验存在
        validateRelContractTypeChecklistExists(id);
        // 删除
        relContractTypeChecklistMapper.deleteById(id);
    }

    private void validateRelContractTypeChecklistExists(String id) {
        if (relContractTypeChecklistMapper.selectById(id) == null) {
//            throw exception(REL_CONTRACT_TYPE_CHECKLIST_NOT_EXISTS);
        }
    }

    @Override
    public List<ReviewChecklistRespVO> getRelContractTypeChecklist(String contractType, String projectCategoryCode) {
        if (StringUtil.isNotEmpty(projectCategoryCode)) {
            ContractType contractType1 = contractTypeMapper.selectOne(ContractType::getPlatId, ProjectCategoryEnums.getInstance(projectCategoryCode).getValue());
            contractType = contractType1.getId();
        }
        //查询所有关联
        List<RelContractTypeChecklistDO> relContractTypeChecklistDOS =
                relContractTypeChecklistMapper.selectList(RelContractTypeChecklistDO::getContractType, contractType);

        //取出所有的清单id
        List<String> reviewListIds = relContractTypeChecklistDOS.stream().map(RelContractTypeChecklistDO::getReviewListId).distinct().collect(Collectors.toList());
        if (reviewListIds.isEmpty()) {
//            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "该合同类型没有设置审查清单，请先设置审查清单");
            return null;
        }
        List<ReviewChecklistDO> reviewChecklistDOS =
                reviewChecklistMapper.selectList(
                        //ReviewChecklistDO::getId, reviewListIds, ReviewChecklistDO::getStatus, Boolean.TRUE
                        new QueryWrapper<ReviewChecklistDO>().in("id", reviewListIds).eq("status", Boolean.TRUE)
                );

        return BeanUtils.toBean(reviewChecklistDOS, ReviewChecklistRespVO.class);
    }

    @Override
    public PageResult<RelContractTypeChecklistDO> getRelContractTypeChecklistPage(RelContractTypeChecklistPageReqVO pageReqVO) {
        return relContractTypeChecklistMapper.selectPage(pageReqVO);
    }

}