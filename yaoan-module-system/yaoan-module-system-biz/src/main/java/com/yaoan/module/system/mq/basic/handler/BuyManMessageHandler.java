package com.yaoan.module.system.mq.basic.handler;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import com.yaoan.module.system.dal.mysql.dept.CompanyMapper;
import com.yaoan.module.system.dal.mysql.dept.DeptMapper;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.dal.mysql.user.OrganizationMapper;
import com.yaoan.module.system.mq.basic.domain.vo.MqOrganizationVO;
import com.yaoan.module.system.mq.basic.domain.vo.MqUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author doujiale
 */
@Slf4j
@Service
public class BuyManMessageHandler {

    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private CompanyMapper companyMapper;
    @Resource
    private SystemuserRelMapper systemuserRelMapper;
    @Resource
    private DeptMapper deptMapper;
    

    /**
     * 采购人单位信息变更
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean buyManUpdateConsumer(String message) throws Exception {
        try{
            log.info("【RabbitMQ】由队列取得修改采购人单位变更信息：{}", message);
            MqOrganizationVO organizationVO = SupplierMessageHandler.getGson().fromJson(message, MqOrganizationVO.class);
            OrganizationDO organizationDO = organizationMapper.selectById(organizationVO.getOrgGuid());
            if (organizationDO == null) {
                OrganizationDO organization = new OrganizationDO();
                organization.setId(organizationVO.getOrgGuid()).setName(organizationVO.getOrgName()).setCode(organizationVO.getOrgCode())
                        .setAddress(organizationVO.getAddress()).setLinkFax(organizationVO.getLinkFax()).setLinkMan(organizationVO.getLinkMan())
                        .setLinkPhone(organizationVO.getLinkPhone()).setType(organizationVO.getOrgType() != null ? organizationVO.getOrgType().toString() : null)
                        .setFixedPhone(organizationVO.getLinkMobile()).setPostCode(organizationVO.getPostCode())
                        .setRegionGuid(organizationVO.getRegionGuid()).setRegionCode(organizationVO.getRegionCode()).setLegal(organizationVO.getLegal()).setLegalPhone(organizationVO.getLegalPhone());
                organizationMapper.insert(organization);
                
               // insertCompanyByORG(organization);
                
                log.info("【RabbitMQ】采购人单位变更信息>insert,同步信息成功！orgGuid:{}", organizationVO.getOrgGuid());
            } else {
                organizationDO.setName(organizationVO.getOrgName()).setCode(organizationVO.getOrgCode())
                        .setAddress(organizationVO.getAddress()).setLinkFax(organizationVO.getLinkFax()).setLinkMan(organizationVO.getLinkMan())
                        .setLinkPhone(organizationVO.getLinkPhone()).setType(organizationVO.getOrgType() != null ? organizationVO.getOrgType().toString() : null)
                        .setFixedPhone(organizationVO.getLinkMobile()).setPostCode(organizationVO.getPostCode())
                        .setRegionGuid(organizationVO.getRegionGuid()).setRegionCode(organizationVO.getRegionCode()).setLegal(organizationVO.getLegal()).setLegalPhone(organizationVO.getLegalPhone());
                organizationMapper.updateById(organizationDO);
//                LambdaQueryWrapperX<SystemuserRelDO> lambdaQueryWrapperX =  new LambdaQueryWrapperX<>();
//                lambdaQueryWrapperX.eq(SystemuserRelDO::getBuyerOrgId,organizationDO.getId());
//                
//                SystemuserRelDO systemuserRelDO = systemuserRelMapper.selectOne(lambdaQueryWrapperX);
//                //设置租户信息，避免下方查询报错
//                TenantContextHolder.setTenantId(systemuserRelDO.getCurrentTenantId());
//                //CompanyDO companyDO = new CompanyDO();//companyMapper.selectById(systemuserRelDO.getCompanyId());
//                CompanyDO companyDO = companyMapper.selectById(systemuserRelDO.getCompanyId());
//                companyDO.setId(systemuserRelDO.getCompanyId());
//                companyDO.setName(organizationDO.getName());
//                companyDO.setCreditCode(organizationDO.getCode());
//                companyMapper.updateById(companyDO);
                log.info("【RabbitMQ】采购人单位变更信息同步信息成功！orgGuid:{}", organizationVO.getOrgGuid());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return true;
    }


    /**
     * 采购人人员注册同步
     *
     * @param message 队列中的消息内容
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean buyManUserRegisterConsumer(String message) throws Exception {
        try{
            log.info("【RabbitMQ】从队列中接收到的采购人注册信息内容为：{}", message);
            MqUserVO userVO = SupplierMessageHandler.getGson().fromJson(message, MqUserVO.class);
            List<SystemuserRelDO> systemuserRelDOList= systemuserRelMapper.selectList(new LambdaQueryWrapperX<SystemuserRelDO>().eq(SystemuserRelDO::getBuyerOrgId, userVO.getOrgGuid()));
            SystemuserRelDO systemuserRelDO = null;
            if(systemuserRelDOList.size()>0){
                systemuserRelDO = systemuserRelDOList.get(0);
                TenantContextHolder.setTenantId(systemuserRelDO.getCurrentTenantId());
            }
            AdminUserDO sqlUser = adminUserMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>()
                    .eq(AdminUserDO::getPlatformUserId, userVO.getUserGuid())
                    .orderByAsc(AdminUserDO::getCreateTime)
                    .last(" limit 1"));
            if (ObjectUtil.isNotEmpty(sqlUser)) {
                sqlUser.setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(userVO.getValid())
                        .setSupplyId(userVO.getSupplierId()).setRegionCode(userVO.getRegionCode()).setOrgId(userVO.getOrgGuid()).setType(1);
                adminUserMapper.updateById(sqlUser);
            } else {
                AdminUserDO adminUserDO = new AdminUserDO().setPlatformUserId(userVO.getUserGuid()).setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(userVO.getValid())
                        .setSupplyId(userVO.getSupplierId()).setRegionCode(userVO.getRegionCode()).setOrgId(userVO.getOrgGuid()).setType(1);
                if(systemuserRelDO != null){
                    adminUserDO.setDeptId(systemuserRelDO.getDeptId());
                }
                adminUserMapper.insert(adminUserDO);
                userRoleMapper.insert(new UserRoleDO().setUserId(adminUserDO.getId()).setRoleId(189L));
            }
            log.info("【RabbitMQ】采购人注册信息同步成功！userGuid:{}", userVO.getUserGuid());
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        
    }

    /**
     * 采购人 人员信息变更
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean buyManUserUpdateConsumer(String message) throws Exception {
        try{
            log.info("【RabbitMQ】从队列中接收到采购人人员信息变更消息为:{}", message);
            MqUserVO userVO = SupplierMessageHandler.getGson().fromJson(message, MqUserVO.class);
            AdminUserDO adminUserDO = adminUserMapper.selectOne(AdminUserDO::getPlatformUserId, userVO.getUserGuid());
            if (adminUserDO != null) {
                adminUserDO.setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(userVO.getValid())
                        .setSupplyId(userVO.getSupplierId()).setRegionCode(userVO.getRegionCode()).setOrgId(userVO.getOrgGuid()).setType(1);
                adminUserMapper.updateById(adminUserDO);


                log.info("【RabbitMQ】采购人信息变更信息同步成功！userGuid:{}", userVO.getUserGuid());
            } else {
                log.info("【RabbitMQ】从队列中接收到采购人人员信息,需要补全变更消息为:{}", message);
                adminUserDO.setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(userVO.getValid())
                        .setSupplyId(userVO.getSupplierId()).setRegionCode(userVO.getRegionCode()).setOrgId(userVO.getOrgGuid()).setType(1);
                adminUserMapper.insert(adminUserDO);
                userRoleMapper.insert(new UserRoleDO().setUserId(adminUserDO.getId()).setRoleId(189L));
                log.info("【RabbitMQ】采购人信息变更信息同步补全成功！userGuid:{}", userVO.getUserGuid());
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        
        return true;
    }

    /**
     * 采购人单位注册同步
     *
     * @param message 队列中的消息内容
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean buyManRegisterConsumer(String message) throws Exception {
        try {
            log.info("【RabbitMQ】从队列中接收到的新增采购单位消息内容为：{}", message);
            MqOrganizationVO organizationVO = SupplierMessageHandler.getGson().fromJson(message, MqOrganizationVO.class);
            OrganizationDO organization = new OrganizationDO().setId(organizationVO.getOrgGuid()).setName(organizationVO.getOrgName()).setCode(organizationVO.getOrgCode())
                    .setAddress(organizationVO.getAddress()).setLinkFax(organizationVO.getLinkFax()).setLinkMan(organizationVO.getLinkMan())
                    .setLinkPhone(organizationVO.getLinkPhone()).setType(organizationVO.getOrgType() != null ? organizationVO.getOrgType().toString() : null)
                    .setFixedPhone(organizationVO.getLinkMobile()).setPostCode(organizationVO.getPostCode())
                    .setRegionGuid(organizationVO.getRegionGuid()).setRegionCode(organizationVO.getRegionCode()).setLegal(organizationVO.getLegal()).setLegalPhone(organizationVO.getLegalPhone());
            organizationMapper.insert(organization);
            //根据机构信息新增系统公司等信息
           // insertCompanyByORG(organization);

            log.info("【RabbitMQ】采购人单位注册信息同步成功！orgGuid:{}", organizationVO.getOrgGuid());
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        
        return true;
    }
    public void insertCompanyByORG(OrganizationDO organizationDO){
        /**
         * 同时新增公司
         */
        CompanyDO companyDO = new CompanyDO();
        companyDO.setName(organizationDO.getName());
        companyDO.setCreditCode(organizationDO.getCode());
        companyDO.setSort(1);
        //是否供应商
        companyDO.setSupplier(0);
        //状态
        companyDO.setStatus(0);
        companyDO.setMajor(3);
        companyDO.setTenantId(Long.valueOf(0));
        companyMapper.insert(companyDO);
        DeptDO deptDO = new DeptDO();
        deptDO.setName(companyDO.getName());
        deptDO.setStatus(0);
        deptDO.setSort(1);
        deptDO.setParentId(Long.valueOf(0));
        
        deptDO.setCompanyId(companyDO.getId());
        deptDO.setTenantId(Long.valueOf(0));
        deptMapper.insert(deptDO);
        
        System.out.println(companyDO.getId());
        SystemuserRelDO systemuserRelDO = new SystemuserRelDO();
        systemuserRelDO.setCompanyId(companyDO.getId());
        systemuserRelDO.setBuyerOrgId(organizationDO.getId());
        systemuserRelDO.setCurrentTenantId(Long.valueOf(0));
        systemuserRelDO.setDeptId(deptDO.getId());
        systemuserRelMapper.insert(systemuserRelDO);
    }
    /**
     * 采购人 人员信息删除
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    public boolean buyManUserRemoveConsumer(String message) {
        log.info("【RabbitMQ】由队列取得采购人 人员信息删除信息：{}", message);
        MqUserVO userVO = SupplierMessageHandler.getGson().fromJson(message, MqUserVO.class);
        if (StringUtils.isNotBlank(userVO.getUserGuid())) {
            AdminUserDO adminUserDO = adminUserMapper.selectOne(AdminUserDO::getPlatformUserId, userVO.getUserGuid());
            if (adminUserDO != null) {
                adminUserMapper.deleteById(adminUserDO);
                log.info("【RabbitMQ】采购人人员信息删除信息同步成功！userGuid:{}", userVO.getUserGuid());
            }
        }
        return true;
    }

}
