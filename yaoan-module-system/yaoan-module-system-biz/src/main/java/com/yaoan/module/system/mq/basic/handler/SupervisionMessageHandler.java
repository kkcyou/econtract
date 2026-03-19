package com.yaoan.module.system.mq.basic.handler;

import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @author doujiale
 */
@Slf4j
@Service
public class SupervisionMessageHandler {

    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private UserRoleMapper userRoleMapper;


    public boolean supervisionUserNewConsumer(String message) {
        return supervisionUserSaveOrUpdateConsumer(message);
    }


    /**
     * 监管 人员 新增 或 修改
     *
     * @param message 队列中的消息内容
     * @return boolean
     */
    public boolean supervisionUserSaveOrUpdateConsumer(String message) {
        log.info("【RabbitMQ】【暂无逻辑执行】从队列中接收到的监管用户消息为:{}", message);
//        Gson gson = new Gson();
//        UserBasciBO userBasciBO = gson.fromJson(message, UserBasciBO.class);
//        AdminUserDO adminUserDO = adminUserMapper.selectOne(AdminUserDO::getPlatformUserId, userBasciBO.getUserGuid());
//        if (adminUserDO == null) {
//            AdminUserDO adminUser = new AdminUserDO().setPlatformUserId(userBasciBO.getUserGuid()).setUsername(userBasciBO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
//                    .setEmail(userBasciBO.getEmail()).setMobile(userBasciBO.getMobile()).setNickname(userBasciBO.getUserName()).setStatus(userBasciBO.getValid())
//                    .setSupplyId(userBasciBO.getSupplierGuid()).setRegionCode(userBasciBO.getRegionCode()).setOrgId(userBasciBO.getOrgGuid()).setType(4);
//            adminUserMapper.insert(adminUser);
//            userRoleMapper.insert(new UserRoleDO().setUserId(adminUser.getId()).setRoleId(178L));
//            log.info("【RabbitMQ】监管人员新增成功,用户名:{},用户id:{}", adminUser.getUsername(), adminUser.getId());
//        } else {
//            AdminUserDO adminUser = adminUserDO.setUsername(userBasciBO.getLoginName()).setPassword("$2a$04$iP/bOEspceye4h22lrWpr.gSURbu5D9X6Gb.kroqP3CuHI8bdtBt2")
//                    .setEmail(userBasciBO.getEmail()).setMobile(userBasciBO.getMobile()).setNickname(userBasciBO.getUserName()).setStatus(userBasciBO.getValid())
//                    .setSupplyId(userBasciBO.getSupplierGuid()).setRegionCode(userBasciBO.getRegionCode()).setOrgId(userBasciBO.getOrgGuid()).setType(4);
//            adminUserMapper.updateById(adminUser);
//            log.info("【RabbitMQ】监管人员修改成功,用户名:{},用户id:{}", adminUser.getUsername(), adminUser.getId());
//        }
        return true;
    }

    /**
     * 监管 人员信息变更
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    public boolean supervisionUserUpdateConsumer(String message) {
        return supervisionUserSaveOrUpdateConsumer(message);
    }


    /**
     * 监管 人员信息删除
     *
     * @param message 队列中的消息内容
     * @return boolean
     **/
    public boolean supervisionUserDeleteConsumer(String message) {
        log.info("【RabbitMQ】【暂无逻辑执行】从队列中接收到监管人员删除信息，变更消息为:{}", message);
        
//        Gson gson = new Gson();
//
//        UserBasciBO userVO = gson.fromJson(message, UserBasciBO.class);
//        if (StringUtils.isNotBlank(userVO.getUserGuid())) {
//            adminUserMapper.delete(new LambdaQueryWrapper<AdminUserDO>().eq(AdminUserDO::getPlatformUserId, userVO.getUserGuid()));
//            log.info("【RabbitMQ】监管人员删除成功,用户id:{}", userVO.getUserGuid());
//        }
        return true;
    }
}
