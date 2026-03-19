package com.yaoan.module.system.service.oauth2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.string.StrUtils;
import com.yaoan.module.econtract.api.modelcategory.ClientModelCategoryApi;
import com.yaoan.module.econtract.api.modelcategory.ModelCategoryApi;
import com.yaoan.module.econtract.api.modelcategory.dto.ClientModelCategoryDTO;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.controller.admin.oauth2.vo.client.*;
import com.yaoan.module.system.convert.auth.OAuth2ClientConvert;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import com.yaoan.module.system.dal.mysql.oauth2.OAuth2ClientMapper;
import com.yaoan.module.system.dal.redis.RedisKeyConstants;
import com.google.common.annotations.VisibleForTesting;
import com.yaoan.module.system.util.oauth2.AppIdAndSecretUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * OAuth2.0 Client Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class OAuth2ClientServiceImpl implements OAuth2ClientService {

    @Resource
    private OAuth2ClientMapper oauth2ClientMapper;
    @Resource
    private ModelCategoryApi modelCategoryApi;
    @Resource
    private ClientModelCategoryApi clientModelCategoryApi;
    @Resource
    private CompanyApi companyApi;

    @Transactional
    @Override
    public Long createOAuth2Client(OAuth2ClientCreateReqVO createReqVO) {
        validateClientIdExists(null, createReqVO.getClientId());
        // 插入客户端数据
        OAuth2ClientDO oauth2Client = OAuth2ClientConvert.INSTANCE.convert(createReqVO);
        oauth2ClientMapper.insert(oauth2Client);
        //同步客户端信息到模板分类表  父id为0
        int modelCategoryId = modelCategoryApi.insertModelCategory(new ModelCategoryDTO().setName(oauth2Client.getName()).setCode(oauth2Client.getClientId()).setParentId(0));
        //同步数据到客户端，模板分类关联表
        clientModelCategoryApi.insertClientModelCategory(new ClientModelCategoryDTO().setCategoryId(modelCategoryId).setClientId(oauth2Client.getClientId()));
        return oauth2Client.getId();
    }


    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.OAUTH_CLIENT,
            allEntries = true) // allEntries 清空所有缓存，因为可能修改到 clientId 字段，不好清理
    public void updateOAuth2Client(OAuth2ClientUpdateReqVO updateReqVO) {
        // 校验存在
        validateOAuth2ClientExists(updateReqVO.getId());
        // 校验 Client 未被占用
        validateClientIdExists(updateReqVO.getId(), updateReqVO.getClientId());

        // 更新
        OAuth2ClientDO updateObj = OAuth2ClientConvert.INSTANCE.convert(updateReqVO);
        oauth2ClientMapper.updateById(updateObj);
    }

    @Override
    @CacheEvict(cacheNames = RedisKeyConstants.OAUTH_CLIENT,
            allEntries = true) // allEntries 清空所有缓存，因为 id 不是直接的缓存 key，不好清理
    public void deleteOAuth2Client(Long id) {
        // 校验存在
        validateOAuth2ClientExists(id);
        // 删除
        oauth2ClientMapper.deleteById(id);
    }

    private void validateOAuth2ClientExists(Long id) {
        if (oauth2ClientMapper.selectById(id) == null) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
    }

    @VisibleForTesting
    void validateClientIdExists(Long id, String clientId) {
        OAuth2ClientDO client = oauth2ClientMapper.selectByClientId(clientId);
        if (client == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的客户端
        if (id == null) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
        if (!client.getId().equals(id)) {
            throw exception(OAUTH2_CLIENT_EXISTS);
        }
    }

    @Override
    public OAuth2ClientDO getOAuth2Client(Long id) {
        return oauth2ClientMapper.selectById(id);
    }

    @Override
    public OAuth2ClientDO getOAuth2Client(String clientId, String clientSecret) {
        return oauth2ClientMapper.selectOne(OAuth2ClientDO::getClientId, clientId, OAuth2ClientDO::getSecret, clientSecret);
    }

    @Override
    @Cacheable(cacheNames = RedisKeyConstants.OAUTH_CLIENT, key = "#clientId",
            unless = "#result == null")
    public OAuth2ClientDO getOAuth2ClientFromCache(String clientId) {
        return oauth2ClientMapper.selectByClientId(clientId);
    }

    @Override
    public PageResult<OAuth2ClientDO> getOAuth2ClientPage(OAuth2ClientPageReqVO pageReqVO) {
        return oauth2ClientMapper.selectPage(pageReqVO);
    }

    @Override
    public OAuth2ClientDO validOAuthClientFromCache(String clientId, String clientSecret, String authorizedGrantType,
                                                    Collection<String> scopes, String redirectUri) {
        // 校验客户端存在、且开启
        OAuth2ClientDO client = getSelf().getOAuth2ClientFromCache(clientId);
        if (client == null) {
            throw exception(OAUTH2_CLIENT_NOT_EXISTS);
        }
        if (ObjectUtil.notEqual(client.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            throw exception(OAUTH2_CLIENT_DISABLE);
        }

        // 校验客户端密钥
        if (StrUtil.isNotEmpty(clientSecret) && ObjectUtil.notEqual(client.getSecret(), clientSecret)) {
            throw exception(OAUTH2_CLIENT_CLIENT_SECRET_ERROR);
        }
        // 校验授权方式
        if (StrUtil.isNotEmpty(authorizedGrantType) && !CollUtil.contains(client.getAuthorizedGrantTypes(), authorizedGrantType)) {
            throw exception(OAUTH2_CLIENT_AUTHORIZED_GRANT_TYPE_NOT_EXISTS);
        }
        // 校验授权范围
        if (CollUtil.isNotEmpty(scopes) && !CollUtil.containsAll(client.getScopes(), scopes)) {
            throw exception(OAUTH2_CLIENT_SCOPE_OVER);
        }
        // 校验回调地址
        if (StrUtil.isNotEmpty(redirectUri) && !StrUtils.startWithAny(redirectUri, client.getRedirectUris())) {
            throw exception(OAUTH2_CLIENT_REDIRECT_URI_NOT_MATCH, redirectUri);
        }
        return client;
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private OAuth2ClientServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }


    //==========2024-03-05  新增====
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createOAuth2ClientV2(OAuth2ClientCreateReqVO createReqVO) {
        //生成APPID
        String appId = AppIdAndSecretUtil.generateAppId();
        //生成secret
        String secret = AppIdAndSecretUtil.generateAppSecret();
        // 插入客户端数据
        OAuth2ClientDO oauth2Client = OAuth2ClientConvert.INSTANCE.convert(createReqVO);
        oauth2Client.setClientId(appId);
        oauth2Client.setSecret(secret);
        oauth2ClientMapper.insert(oauth2Client);
        //同步客户端信息到模板分类表  父id为0
        int modelCategoryId = modelCategoryApi.insertModelCategory(new ModelCategoryDTO().setName(oauth2Client.getName()).setCode(oauth2Client.getClientId()).setParentId(0).setCompanyId(createReqVO.getCompanyId()));
        //同步数据到客户端，模板分类关联表
        clientModelCategoryApi.insertClientModelCategory(new ClientModelCategoryDTO().setCategoryId(modelCategoryId).setClientId(oauth2Client.getClientId()).setOauth2ClientId(oauth2Client.getId()));
        return oauth2Client.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOAuth2ClientV2(OAuth2ClientUpdateReqVO updateReqVO) {
        // 校验存在
        validateOAuth2ClientExists(updateReqVO.getId());
        // 校验 Client 未被占用
        OAuth2ClientDO client = oauth2ClientMapper.selectById(updateReqVO.getId());
        validateClientIdExists(updateReqVO.getId(), client.getClientId());
        // 更新
        OAuth2ClientDO updateObj = OAuth2ClientConvert.INSTANCE.convert(updateReqVO);
        oauth2ClientMapper.updateById(updateObj);
        //获取关联的模板分类id
        Integer modelCategoryId = clientModelCategoryApi.getModelCategoryId(client.getClientId());
        //修改模板分类名称
        if (ObjectUtil.isNotEmpty(modelCategoryId)) {
            ModelCategoryDTO modelCategoey = modelCategoryApi.getModelCategoey(modelCategoryId);
            if (ObjectUtil.isNotEmpty(modelCategoey)) {
                if ((!modelCategoey.getCompanyId().equals(updateReqVO.getCompanyId()) && ObjectUtil.isNotEmpty(updateReqVO.getCompanyId()))) {
                    modelCategoryApi.updateModelCategory(new ModelCategoryDTO().setId(modelCategoryId).setCompanyId(updateReqVO.getCompanyId()));
                }
            }
        }

    }

    @Override
    public PageResult<OAuth2ClientV2RespVO> getOAuth2ClientPageV2(OAuth2ClientPageReqVO pageVO) {
        PageResult<OAuth2ClientDO> pageResult = oauth2ClientMapper.selectPage(pageVO);
        PageResult<OAuth2ClientV2RespVO> result = OAuth2ClientConvert.INSTANCE.convertPageV2(pageResult);
        return result;
    }

    @Override
    public void refreshSecret(Long id) {
        //生成secret
        String secret = AppIdAndSecretUtil.generateAppSecret();
        oauth2ClientMapper.updateById(new OAuth2ClientDO().setId(id).setSecret(secret));
    }

    @Override
    public OAuth2ClientRespVO getOAuth2ClientV2(Long id) {
        OAuth2ClientDO oAuth2ClientDO = oauth2ClientMapper.selectById(id);
        OAuth2ClientRespVO convert = OAuth2ClientConvert.INSTANCE.convert(oAuth2ClientDO);
        if(ObjectUtil.isNotEmpty(oAuth2ClientDO)){
        //获取模板分类id
        Integer modelCategoryId = clientModelCategoryApi.getModelCategoryId(oAuth2ClientDO.getClientId());
        //获取公司id
        Long companyId = modelCategoryId==null?null:modelCategoryApi.getModelCategoey(modelCategoryId).getCompanyId();
        convert.setCompanyId(companyId);
        }
        return convert;
    }


}
