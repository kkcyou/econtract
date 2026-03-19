package com.yaoan.module.econtract.service.modelcategory;

import com.yaoan.module.econtract.api.modelcategory.ClientModelCategoryApi;
import com.yaoan.module.econtract.api.modelcategory.dto.ClientModelCategoryDTO;
import com.yaoan.module.econtract.convert.modelcategory.ClientModelCategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ClientModelCategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zhc
 * @date: 2024-03-05 15:40
 */
@Service
public class ClientModelCategoryApiServiceImpl implements ClientModelCategoryApi {
    @Resource
    private ClientModelCategoryMapper clientModelCategoryMapper;

    @Override
    public int insertClientModelCategory(ClientModelCategoryDTO clientModelCategoryDTO) {
        int id = clientModelCategoryMapper.insert(ClientModelCategoryConverter.INSTANCE.toEntity(clientModelCategoryDTO));
        return id;
    }

    @Override
    public Integer getModelCategoryId(String clientId) {
        ClientModelCategory clientModelCategory = clientModelCategoryMapper.selectOne(ClientModelCategory::getClientId, clientId);
        return clientModelCategory==null?null:clientModelCategory.getCategoryId();
    }
}
