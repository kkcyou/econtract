package com.yaoan.module.econtract.service.modelcategory;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.convert.modelcategory.ModelCategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:40
 */
@Service
public class ModelCategoryServiceImpl extends ServiceImpl<ModelCategoryMapper, ModelCategory> implements ModelCategoryService {
    @Resource
    private ModelCategoryMapper modelCategoryMapper;

    @Override
    public PageResult<ModelCategory> getModelCategoryPage(ModelCategoryDTO modelCategoryDTO) {
        return modelCategoryMapper.selectPage(modelCategoryDTO);
    }

    @Override
    public int insertmodelCategory(ModelCategoryDTO dto) {
        ModelCategory ecmsDemo = ModelCategoryConverter.INSTANCE.toEntity(dto);
        modelCategoryMapper.insert(ecmsDemo);
        return ecmsDemo.getId();
    }

    @Override
    public int updateModelCategory(ModelCategoryDTO modelCategoryDTO) {
        ModelCategory ecmsDemo = ModelCategoryConverter.INSTANCE.toEntity(modelCategoryDTO);
        modelCategoryMapper.updateById(ecmsDemo);
        return ecmsDemo.getId();
    }

    @Override
    public ModelCategory getModelCategoey(Integer id) {
        ModelCategory modelCategory = modelCategoryMapper.selectById(id);
        return modelCategory;
    }
    @Override
    public List<ModelCategory> getModelCategorys(List<Integer> ids) {
        if (CollectionUtil.isEmpty(ids)){
            return new ArrayList<>();
        }
        List<ModelCategory> modelCategorys = modelCategoryMapper.selectBatchIds(ids);
        return modelCategorys;
    }
}
