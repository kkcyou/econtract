package com.yaoan.module.system.mq.basic.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.dal.mysql.user.SupplyMapper;
import com.yaoan.module.system.mq.basic.domain.vo.MqSupplierUserVO;
import com.yaoan.module.system.mq.basic.domain.vo.MqSupplierVO;
import com.yaoan.module.system.mq.basic.domain.vo.SupplierInfoVO;
import com.yaoan.module.system.mq.basic.domain.vo.UserSynVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

import static com.yaoan.framework.common.enums.CommonStatusEnum.ENABLE;

/**
 * @author doujiale
 */
@Slf4j
@Service
public class SupplierMessageHandler {

    @Resource
    private SupplyMapper supplyMapper;
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RelativeApi relativeApi;
    
    /**
     * 供应商注册同步
     *
     * @param message 队列中的消息内容
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean supplierRegisterConsumer(String message) throws Exception {
        try {
            setDefaultTenantId();
            log.info("【RabbitMQ】从队列中接收到新增供应商消息为:{}", message);
            Gson gson = getGson();
            MqSupplierVO mqSupplierVO = gson.fromJson(message, MqSupplierVO.class);
            SupplyDO supplyDO = new SupplyDO();
            if (mqSupplierVO != null && mqSupplierVO.getSupplierInfo() != null) {
                SupplierInfoVO supplierInfo = mqSupplierVO.getSupplierInfo();
                supplyDO.setId(supplierInfo.getId())
                        .setSupplyCn(supplierInfo.getSupplyCn())
                        .setOrgCode(supplierInfo.getOrgCode())
                        .setBankAccount(supplierInfo.getBankAccount())
                        .setBankName(supplierInfo.getBankName())
                        .setBusinessType(supplierInfo.getSupplyType())
                        .setCompanyType(supplierInfo.getCompanyType())
                        .setEcokindcode(supplierInfo.getECOKINDCODE())
                        .setHyhf(supplierInfo.getHyhf())
                        .setAddr(supplierInfo.getSupplyAddr())
                        .setFax(supplierInfo.getSupplyFax())
                        .setTel(supplierInfo.getSupplyTel())
                        .setType(supplierInfo.getSupplyType())
                        .setLegalPerson(supplierInfo.getLegalPerson())
                        .setLegalTel(supplierInfo.getLegalTel())
                        .setLegalAddr(supplierInfo.getLegalAddr())
                        .setPersonAddr(supplierInfo.getPersonAddr())
                        .setPersonMobile(supplierInfo.getPersonMobile())
                        .setPersonTel(supplierInfo.getPersonTel())
                        .setPersonName(supplierInfo.getPersonName())
                        .setReginCode(supplierInfo.getZoneCode())
                        .setLegalMobile(supplierInfo.getLegalMobile())
                        .setLegalIdCard(supplierInfo.getLegalIdcardno())
                        .setPersonEmail(supplierInfo.getPersonEmail())
                        .setSupplierEmail(supplierInfo.getSupplyEmail())
                        .setLegalEmail(supplierInfo.getLegalEmail())
                        .setSupplierZip(supplierInfo.getSupplyZip())
                        .setRegAddr(supplierInfo.getRegAddr());
                supplyMapper.insert(supplyDO);
               
                log.info("【RabbitMQ】调整后的供应商信息内容：{}", JSON.toJSONString(supplyDO));
                log.info("【RabbitMQ】供应商新增同步信息成功！supplierId:{}", supplyDO.getId());
            }
            //取出信息中携带的主用户 的用户名和用户id
            log.info("开始新增供应商的主用户信息");
            String userId = mqSupplierVO.getUserId();
            String userName = mqSupplierVO.getUserName();
            log.info("====userId: {},==userName:{}", userId, userName);
            AdminUserDO oldUser = adminUserMapper.selectOne(AdminUserDO::getPlatformUserId, userId);
            if (ObjectUtil.isEmpty(oldUser)) {
                //设置user的默认值
                AdminUserDO adminUserDO = new AdminUserDO().setPlatformUserId(userId).setUsername(userName).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setNickname(supplyDO.getSupplyCn()).setStatus(ENABLE.getStatus())
                        .setSupplyId(supplyDO.getId()).setType(2);
                buildUserDeptAndCompany(adminUserDO);
                adminUserMapper.insert(adminUserDO);
                userRoleMapper.insert(new UserRoleDO().setUserId(adminUserDO.getId()).setRoleId(195L));
                RelativeDTO relativeDTO = buildRelativeDTO(supplyDO,adminUserDO);
                relativeApi.saveRelative(relativeDTO);
                log.info("【RabbitMQ】供应商新增主用户信息成功！userGuid:{}", adminUserDO.getPlatformUserId());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return true;
    }
    public void setDefaultTenantId(){
        TenantContextHolder.setTenantId(0L);
    }
    public void buildUserDeptAndCompany(AdminUserDO adminUserDO){
        adminUserDO.setDeptId(166L);
        adminUserDO.setCompanyId(24L);
    }
    public RelativeDTO buildRelativeDTO(SupplyDO supplyDO,AdminUserDO adminUserDO) {
        RelativeDTO relativeDTO = new RelativeDTO();
        relativeDTO.setId(supplyDO.getId());
        relativeDTO.setCode(supplyDO.getOrgCode());
        relativeDTO.setName(supplyDO.getSupplyCn());
//        relativeDTO.contactId();
        //相对方类型 供应商1 
        relativeDTO.setRelativeType(1);
//        supplyDO.getCompanyType()
        //相对方性质 企业2 
        relativeDTO.setEntityType("2");
        //来源 导入2
        relativeDTO.setSourceType(2);
        relativeDTO.setLevelNo("1");
        //黑名单状态
        relativeDTO.setStatus("0");
        //统一社会信用代码2  身份证1
        relativeDTO.setCardType(2);
        relativeDTO.setCardNo(supplyDO.getOrgCode());
        relativeDTO.setLegalCardNo(supplyDO.getLegalIdCard());
        relativeDTO.setBankAccountName("");
        relativeDTO.setBankAccount(supplyDO.getBankAccount());
        relativeDTO.setBankName(supplyDO.getBankName());
        relativeDTO.setArea(supplyDO.getReginCode());
        relativeDTO.setAddress(supplyDO.getAddr());
        relativeDTO.setContactTel(supplyDO.getTel());
        relativeDTO.setEmail(supplyDO.getSupplierEmail());
        relativeDTO.setFax(supplyDO.getFax());
        relativeDTO.setCreator("163");
        relativeDTO.setUpdater("163");
        if(adminUserDO.getId()!=null){
            relativeDTO.setContactId(adminUserDO.getId());
            relativeDTO.setContactAccount(adminUserDO.getUsername());
            relativeDTO.setContactName(adminUserDO.getNickname());
        }
        return relativeDTO;
    }
    /**
     * 供应商单位信息修改
     *
     * @param message 队列中的消息内容
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean supplierUpdateConsumer(String message) {
        log.info("【RabbitMQ】从队列中接收到修改供应商:{}", message);
        setDefaultTenantId();
        Gson gson = new Gson();
        SupplierInfoVO supplierInfo = gson.fromJson(message, SupplierInfoVO.class);
        SupplyDO supplier = supplyMapper.selectById(supplierInfo.getId());
        if (supplier != null) {
            supplier.setSupplyCn(supplierInfo.getSupplyCn())
                    .setOrgCode(supplierInfo.getOrgCode())
                    .setBankAccount(supplierInfo.getBankAccount())
                    .setBankName(supplierInfo.getBankName())
                    .setBusinessType(supplierInfo.getSupplyType())
                    .setCompanyType(supplierInfo.getCompanyType())
                    .setEcokindcode(supplierInfo.getECOKINDCODE())
                    .setHyhf(supplierInfo.getHyhf())
                    .setAddr(supplierInfo.getSupplyAddr())
                    .setFax(supplierInfo.getSupplyFax())
                    .setTel(supplierInfo.getSupplyTel())
                    .setType(supplierInfo.getSupplyType())
                    .setLegalPerson(supplierInfo.getLegalPerson())
                    .setLegalTel(supplierInfo.getLegalTel())
                    .setLegalAddr(supplierInfo.getLegalAddr())
                    .setPersonAddr(supplierInfo.getPersonAddr())
                    .setPersonMobile(supplierInfo.getPersonMobile())
                    .setPersonTel(supplierInfo.getPersonTel())
                    .setPersonName(supplierInfo.getPersonName())
                    .setLegalMobile(supplierInfo.getLegalMobile())
                    .setLegalIdCard(supplierInfo.getLegalIdcardno())
                    .setPersonEmail(supplierInfo.getPersonEmail())
                    .setSupplierEmail(supplierInfo.getSupplyEmail())
                    .setLegalEmail(supplierInfo.getLegalEmail())
                    .setSupplierZip(supplierInfo.getSupplyZip())
                    .setReginCode(supplierInfo.getZoneCode())
                    .setRegAddr(supplierInfo.getRegAddr());
            supplyMapper.updateById(supplier);
            RelativeDTO relativeDTO = buildRelativeDTO(supplier,new AdminUserDO());
            relativeApi.updateRelative(relativeDTO);
            
            log.info("【RabbitMQ】调整后的供应商信息内容：{}", JSON.toJSONString(supplier));
            log.info("【RabbitMQ】供应商更新同步信息成功！supplierId:{}", supplierInfo.getId());
        } else {
            SupplyDO supplyDO = new SupplyDO().setId(supplierInfo.getId())
                    .setSupplyCn(supplierInfo.getSupplyCn())
                    .setOrgCode(supplierInfo.getOrgCode())
                    .setBankAccount(supplierInfo.getBankAccount())
                    .setBankName(supplierInfo.getBankName())
                    .setBusinessType(supplierInfo.getSupplyType())
                    .setCompanyType(supplierInfo.getCompanyType())
                    .setEcokindcode(supplierInfo.getECOKINDCODE())
                    .setHyhf(supplierInfo.getHyhf())
                    .setAddr(supplierInfo.getSupplyAddr())
                    .setFax(supplierInfo.getSupplyFax())
                    .setTel(supplierInfo.getSupplyTel())
                    .setType(supplierInfo.getSupplyType())
                    .setLegalPerson(supplierInfo.getLegalPerson())
                    .setLegalTel(supplierInfo.getLegalTel())
                    .setLegalAddr(supplierInfo.getLegalAddr())
                    .setPersonAddr(supplierInfo.getPersonAddr())
                    .setPersonMobile(supplierInfo.getPersonMobile())
                    .setPersonTel(supplierInfo.getPersonTel())
                    .setPersonName(supplierInfo.getPersonName())
                    .setLegalMobile(supplierInfo.getLegalMobile())
                    .setLegalIdCard(supplierInfo.getLegalIdcardno())
                    .setPersonEmail(supplierInfo.getPersonEmail())
                    .setSupplierEmail(supplierInfo.getSupplyEmail())
                    .setLegalEmail(supplierInfo.getLegalEmail())
                    .setSupplierZip(supplierInfo.getSupplyZip())
                    .setReginCode(supplierInfo.getZoneCode())
                    .setRegAddr(supplierInfo.getRegAddr());
            supplyMapper.insert(supplyDO);

            RelativeDTO relativeDTO = buildRelativeDTO(supplyDO,new AdminUserDO());
            relativeApi.saveRelative(relativeDTO);
            log.info("【RabbitMQ】供应商更新>insert,同步信息成功！supplierId:{}", supplierInfo.getId());
        }
        return true;
    }

    /**
     * 供应商 人员注册同步
     *
     * @param message 队列中的消息内容
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean supplierUserConsumer(String message) {
        setDefaultTenantId();
        log.info("【RabbitMQ】从队列中接收到的新增供应商用户消息为:{}", message);
        Gson gson = new Gson();
        UserSynVO userVO = gson.fromJson(message, UserSynVO.class);
        AdminUserDO sqlUser = adminUserMapper.selectOne(new LambdaQueryWrapperX<AdminUserDO>()
                .eq(AdminUserDO::getPlatformUserId, userVO.getUserGuid())
                .orderByAsc(AdminUserDO::getCreateTime)
                .last(" limit 1"));
        if (ObjectUtil.isNotEmpty(sqlUser)) {
            sqlUser.setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                    .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUsername()).setStatus(0)
                    .setSupplyId(userVO.getSupplierGuid()).setType(2);
            buildUserDeptAndCompany(sqlUser);
            adminUserMapper.updateById(sqlUser);
            log.info("【RabbitMQ】供应商用户变更同步信息成功！userGuid:{}", userVO.getUserGuid());
        } else {
            AdminUserDO adminUserDO = new AdminUserDO().setPlatformUserId(userVO.getUserGuid()).setUsername(userVO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                    .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUsername()).setStatus(userVO.getValid())
                    .setSupplyId(userVO.getSupplierGuid()).setOrgId(userVO.getOrgGuid()).setType(2);
            buildUserDeptAndCompany(adminUserDO);
            adminUserMapper.insert(adminUserDO);
            userRoleMapper.insert(new UserRoleDO().setUserId(adminUserDO.getId()).setRoleId(195L));
        }
        log.info("【RabbitMQ】供应商新增用户同步信息成功！userGuid:{}", userVO.getUserGuid());
        return true;
    }

    /**
     * 供应商 人员信息变更
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    public Boolean supplierUserUpdateConsumer(String message) {
        setDefaultTenantId();
        log.info("【RabbitMQ】从队列中接收到供应商人员信息变更消息为:{}", message);
        Gson gson = new Gson();
        MqSupplierUserVO userVO = gson.fromJson(message, MqSupplierUserVO.class);
        if (userVO != null) {
            AdminUserDO adminUserDO = adminUserMapper.selectOne(AdminUserDO::getPlatformUserId, userVO.getId());
            if (adminUserDO != null) {
                adminUserDO.setUsername(userVO.getAccountName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(0)
                        .setSupplyId(userVO.getSupplierId()).setType(2);
                buildUserDeptAndCompany(adminUserDO);
                adminUserMapper.updateById(adminUserDO);
                log.info("【RabbitMQ】供应商用户变更同步信息成功！userGuid:{}", userVO.getId());
            }else {
                log.info("【RabbitMQ】供应商用户需要补全！userGuid:{}", userVO.getId());
                adminUserDO.setUsername(userVO.getAccountName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
                        .setEmail(userVO.getEmail()).setMobile(userVO.getMobile()).setNickname(userVO.getUserName()).setStatus(0)
                        .setSupplyId(userVO.getSupplierId()).setType(2);
                buildUserDeptAndCompany(adminUserDO);
                adminUserMapper.insert(adminUserDO);
                userRoleMapper.insert(new UserRoleDO().setUserId(adminUserDO.getId()).setRoleId(195L));
                log.info("【RabbitMQ】供应商用户变更同步信息，补全成功！userGuid:{}", userVO.getId());
            }
        }
        return true;
    }

    /**
     * 获取Gson对象
     *
     * @return Gson对象
     */
    public static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        return builder.create();
    }

}
