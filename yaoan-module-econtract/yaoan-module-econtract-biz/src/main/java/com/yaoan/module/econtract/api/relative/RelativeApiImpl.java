package com.yaoan.module.econtract.api.relative;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.api.relative.dto.RelativeCompanyDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.econtract.convert.relative.RelativeConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.system.api.dept.CompanyApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserKey4Space;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

@Service
@Slf4j
public class RelativeApiImpl implements RelativeApi {
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Autowired
    private CompanyApi companyApi;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<RelativeDTO> getRelative(String id) {
        return Collections.emptyList();
    }

    @Override
    public List<RelativeContactDTO> getRelativeUserId(String id, Long deptId) {
        List<RelativeContact> relativeContacts = relativeContactMapper.selectList(new LambdaQueryWrapperX<RelativeContact>()
                .eq(RelativeContact::getRelativeId, id)
                .eqIfPresent(RelativeContact::getDeptId, deptId)
                .select(RelativeContact::getUserId, RelativeContact::getDeptId, RelativeContact::getCompanyId));
        return RelativeConverter.INSTANCE.contactDO2DTOs(relativeContacts);
    }

    @Override
    public List<RelativeContactDTO> getRelativeContactByUserIds(List<Long> ids) {
        List<RelativeContact> relativeContacts = relativeContactMapper.selectList(new LambdaQueryWrapperX<RelativeContact>().in(RelativeContact::getUserId, ids).select(RelativeContact::getUserId, RelativeContact::getCompanyId));
        return RelativeConverter.INSTANCE.contactDO2DTOs(relativeContacts);
    }


    @Override
    public String saveRelative(RelativeDTO relativeDTO) {
        Relative relative = BeanUtil.toBean(relativeDTO, Relative.class);
        relativeMapper.insert(relative);
        return relative.getId();
    }

    @Override
    public String updateRelative(RelativeDTO relativeDTO) {
        Relative relative = BeanUtil.toBean(relativeDTO, Relative.class);
        relativeMapper.updateById(relative);
        return relative.getId();
    }

    @Override
    public void saveRelativeContact(String mobile, String relativeId) {
        relativeContactMapper.insert(new RelativeContact().setContactTel(mobile).setRelativeId(relativeId));
    }

    @Override
    public List<RelativeContactDTO> getContacts4RelativeId(String relativeId) {

        List<RelativeContact> contactList = relativeContactMapper.selectList(RelativeContact::getRelativeId, relativeId);
        if (CollectionUtil.isEmpty(contactList)) {
            return Collections.emptyList();
        }
        return RelativeConverter.INSTANCE.contactDO2DTOs(contactList);
    }

    @Override
    public void saveRelativeContacts(List<RelativeContactDTO> contactDTOList) {
        if (CollectionUtil.isEmpty(contactDTOList)) {
            return;
        }
        List<RelativeContact> contactList = RelativeConverter.INSTANCE.contactDTO2DOs(contactDTOList);
        relativeContactMapper.saveOrUpdateBatch(contactList);
    }

    @Override
    public List<Long> calculateUsers4RelativeExpression(String contractId, Integer signatureType) {
        AtomicReference<List<Long>> result = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                //找出合同
                ContractDO contractDO = contractMapper.selectById(contractId);
                if (ObjectUtil.isNull(contractDO)) {
                    log.error(contractId + "的合同不存在,请确认合同编号是否正确。");
                    throw exception(DIY_ERROR, "请确认合同编号是否正确。");
                }
                // 找出相对方
                SignatoryRelDO signatoryRelDO = signatoryRelMapper.selectOne(new LambdaQueryWrapperX<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractId).eq(SignatoryRelDO::getType, signatureType));
                if (ObjectUtil.isNull(signatoryRelDO)) {
                    //有可能是旧数据，没相对方类型，就默认乙方，且是唯一相对方
                    signatoryRelDO = signatoryRelMapper.selectOne(SignatoryRelDO::getContractId, contractDO.getId());
                    if (ObjectUtil.isNull(signatoryRelDO)) {
                        log.error("相对方不存在，请确认是否已创建相对方信息。");
                        throw exception(DIY_ERROR, "请确认是否已创建相对方信息。");
                    }
                }
                List<RelativeContact> relativeContactList = relativeContactMapper.selectList(new LambdaQueryWrapperX<RelativeContact>()
                        .eq(RelativeContact::getRelativeId, signatoryRelDO.getSignatoryId())

                );
                if (CollectionUtil.isEmpty(relativeContactList)) {
                    Relative relative = relativeMapper.selectById(signatoryRelDO.getSignatoryId());
                    if (ObjectUtil.isNull(relative)) {
                        log.error("相对方联系人和负责人都不存在，请维护联系人或负责人信息后再操作。");
                        throw exception(DIY_ERROR, "请维护联系人或负责人信息后再操作");
                    } else {
                        List<Long> relativeIds = new ArrayList<>();
                        relativeIds.add(relative.getContactId());
                        result.set(relativeIds);
                    }
                } else {
                    List<Long> userIds = relativeContactList.stream().map(RelativeContact::getUserId).collect(Collectors.toList());
                    result.set(userIds);
                }
            });
        });

        return new ArrayList<>(result.get());
    }

    @Override
    public List<Long> calculateRelativesExpression(String contractId, Integer containCreatorFlag) {
        AtomicReference<List<Long>> result = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                //找出合同
                ContractDO contractDO = contractMapper.selectById(contractId);
                if (ObjectUtil.isNull(contractDO)) {
                    log.error(contractId + "的合同不存在,请确认合同编号是否正确。");
                    throw exception(DIY_ERROR, "请确认合同编号是否正确。");
                }
                List userIds = new ArrayList();
                // 找出相对方
                String signOrder = contractDO.getSignOrder();
                if (StringUtils.isNotEmpty(signOrder)) {
                    String[] s = signOrder.split("_");
                    userIds = Arrays.stream(s).map(Long::parseLong).collect(Collectors.toList());
                } else {
                    List<SignatoryRelDO> signatoryRelDO = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractId).orderByDesc(SignatoryRelDO::getSort));
                    for (SignatoryRelDO relDO : signatoryRelDO) {
                        String signatoryId = relDO.getSignatoryId();
                        Relative relative = relativeMapper.selectById(signatoryId);
                        Long userId = relative.getContactId();
                        // 如果相对方联系人字段为空，就去关系表查一条联系人数据
                        if (ObjectUtil.isNotEmpty(userId)) {
                            userIds.add(userId);
                        } else {
//                            userIds.add(relativeService.getOneDefaultContactId(relative.getId()));
                            userIds.add(relative.getVirtualId());
                        }
                    }
                }
                if (IfNumEnums.NO.getCode().equals(containCreatorFlag)) {
                    userIds.remove(Long.valueOf(contractDO.getCreator()));
                }
                result.set(userIds);
            });
        });
        return new ArrayList<Long>(result.get());
    }

    @Override
    public Long getRelativeContactId() {
        // 查询用户所属相对方
        // 把当前人所在相对方的联系人的签署任务也查出来
        String relativeId =redisUtils.get(getLoginUserKey4Space());
        Relative relative = relativeMapper.selectById(relativeId);
        if (ObjectUtil.isEmpty(relative)) {
            relative = relativeMapper.get4AffirmPage(getLoginUserId());

        }
        return relative.getVirtualId();
    }

    @Override
    public List<Long> calculateUsers4SortedRelativeExpression(String contractId, Integer containCreatorFlag) {
        AtomicReference<List<Long>> result = new AtomicReference<>(new ArrayList<>());
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                //找出合同
                ContractDO contractDO = contractMapper.selectById(contractId);
                if (ObjectUtil.isNull(contractDO)) {
                    log.error(contractId + "的合同不存在,请确认合同编号是否正确。");
                    throw exception(DIY_ERROR, "请确认合同编号是否正确。");
                }
                // 找出相对方
                List userIds = new ArrayList();

                String signOrder = contractDO.getSignOrder();
                if (StringUtils.isNotEmpty(signOrder)) {
                    String[] s = signOrder.split("_");
                    userIds = Arrays.stream(s).map(Long::parseLong).collect(Collectors.toList());
                } else {

                    List<SignatoryRelDO> signatoryRelDO = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractId).orderByAsc(SignatoryRelDO::getSort));
                    List<String> relIds = signatoryRelDO.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
                    if (CollectionUtil.isEmpty(relIds)) {
                        throw exception(DATA_ERROR);
                    }
                    Map<String, Relative> relMap = new HashMap<>();
                    List<Relative> relativeList = relativeMapper.selectList(Relative::getId, relIds);
                    if (CollUtil.isNotEmpty(relativeList)) {
                        relMap = CollectionUtils.convertMap(relativeList, Relative::getId);
                    }

                    for (SignatoryRelDO relDO : signatoryRelDO) {
                        String signatoryId = relDO.getSignatoryId();
                        Relative relative = relMap.get(signatoryId);
                        if (ObjectUtil.isNotNull(relative)) {
                            Long userId = relative.getContactId();
                            // 如果相对方联系人字段为空，就去关系表查一条联系人数据
                            if (ObjectUtil.isNotEmpty(userId)) {
                                userIds.add(userId);
                            } else {
//                                userIds.add(relativeService.getOneDefaultContactId(relative.getId()));
                                userIds.add(relative.getVirtualId());
                            }
                        }
                    }
                }
                if (IfNumEnums.NO.getCode().equals(containCreatorFlag)) {
                    userIds.remove(Long.valueOf(contractDO.getCreator()));
                }
                result.set(userIds);
            });
        });

        return new ArrayList<Long>(result.get());
    }

    @Override
    public List<Long> getExistRelativeVirtualIds(Set<Long> virtualIds) {
        List<Relative> relatives = relativeMapper.selectList(Relative::getVirtualId, virtualIds);
        return relatives.stream().map(Relative::getVirtualId).collect(Collectors.toList());
    }

    @Override
    public Map<Long, String> getRelativeNameVirtualIds(Set<Long> virtualIds) {
        List<Relative> relatives = relativeMapper.selectList(Relative::getVirtualId, virtualIds);
        return relatives.stream().collect(Collectors.toMap(Relative::getVirtualId, Relative::getName));
    }

    @Override
    public void saveRelativeContacts4Saas(List<RelativeContactDTO> contactDTOList) {
        if (CollectionUtil.isEmpty(contactDTOList)) {
            return;
        }
        List<RelativeContact> contactList = RelativeConverter.INSTANCE.contactDTO2DOs(contactDTOList);
        relativeContactMapper.insertBatch(contactList);
    }

    @Override
    public List<RelativeCompanyDTO> getCompanyIds4login() {
        List<RelativeContact> relativeContactList = relativeContactMapper.selectList(new LambdaQueryWrapperX<RelativeContact>().eq(RelativeContact::getUserId, SecurityFrameworkUtils.getLoginUserId()));
        if (CollectionUtil.isEmpty(relativeContactList)) {
            return Collections.emptyList();
        }
        List<RelativeCompanyDTO> relativeCompanyDTOS = RelativeConverter.INSTANCE.contactSimpleDo2Dtos(relativeContactList);
        return relativeCompanyDTOS;
    }

    @Override
    public Long getVirtualId4User(Long userId) {
        String relativeId = redisUtils.get(getLoginUserKey4Space());
        if(StringUtils.isBlank(relativeId)){
            throw exception(DATA_ERROR);
        }
        RelativeContact relativeContact = relativeContactMapper.selectOne(new LambdaQueryWrapperX<RelativeContact>().eq(RelativeContact::getUserId, userId).eq(RelativeContact::getRelativeId, relativeId));
        if (ObjectUtil.isNull(relativeContact)) {
            throw exception(DATA_ERROR);
        }
        Relative relative = relativeMapper.selectById(relativeId);
        if (ObjectUtil.isNull(relative)) {
            throw exception(DATA_ERROR);
        }

        return relative.getVirtualId();
    }

    @Override
    public RelativeDTO getOneByCreditCode(String creditCode) {
        List<Relative> relatives = relativeMapper.selectList(Relative::getCardNo, creditCode);
        if (CollectionUtil.isEmpty(relatives)) {
            return null;
        }
        RelativeDTO result = RelativeConverter.INSTANCE.do2Dto(relatives.get(0));
        return null;
    }

    @Override
    public RelativeDTO getOneByMoble4Individual(String mobile) {
        List<Relative> relatives = relativeMapper.selectList(new LambdaQueryWrapperX<Relative>()
                .eq(Relative::getContactTel, mobile)
                .eq(Relative::getActive, IfNumEnums.NO.getCode())
                .eq(Relative::getEntityType, EntityTypeEnums.INDIVIDUAL.getCode()));
        if (CollectionUtil.isEmpty(relatives)) {
            return null;
        }

        return RelativeConverter.INSTANCE.do2Dto(relatives.get(0));
    }

    @Override
    public RelativeDTO getIndividualRelativeUserId(Long loginUserId) {
        List<Relative> relatives = relativeMapper.selectList(new LambdaQueryWrapperX<Relative>().eq(Relative::getContactId, loginUserId).eq(Relative::getEntityType, EntityTypeEnums.INDIVIDUAL.getCode()));
        if (CollectionUtil.isEmpty(relatives)) {
            return null;
        }
        return RelativeConverter.INSTANCE.do2Dto(relatives.get(0));
    }

    @Override
    public List<RelativeContactDTO> getRelativeContactByCompanyIds(List<Long> companyIds) {
        if (CollectionUtil.isEmpty(companyIds)) {
            return Collections.emptyList();
        }
        List<RelativeContact> relativeContactList = relativeContactMapper.selectList(RelativeContact::getCompanyId, companyIds);

        return RelativeConverter.INSTANCE.contactDO2DTOs(relativeContactList);
    }
}
