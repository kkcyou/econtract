package com.yaoan.module.system.api.dict;

import com.yaoan.module.system.api.dict.dto.DictDataRespDTO;
import com.yaoan.module.system.controller.admin.dict.vo.data.DictDataExportReqVO;
import com.yaoan.module.system.convert.dict.DictDataConvert;
import com.yaoan.module.system.dal.dataobject.dict.DictDataDO;
import com.yaoan.module.system.service.dict.DictDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 字典数据 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class DictDataApiImpl implements DictDataApi {

    @Resource
    private DictDataService dictDataService;

    @Override
    public void validateDictDataList(String dictType, Collection<String> values) {
        dictDataService.validateDictDataList(dictType, values);
    }

    @Override
    public DictDataRespDTO getDictData(String dictType, String value) {
        DictDataDO dictData = dictDataService.getDictData(dictType, value);
        return DictDataConvert.INSTANCE.convert02(dictData);
    }

    @Override
    public DictDataRespDTO parseDictData(String dictType, String label) {
        DictDataDO dictData = dictDataService.parseDictData(dictType, label);
        return DictDataConvert.INSTANCE.convert02(dictData);
    }

    @Override
    public List<DictDataRespDTO> getDictDataList(String dictType) {
        DictDataExportReqVO reqVO = new DictDataExportReqVO().setDictType(dictType);
        List<DictDataDO> dictDataList = dictDataService.getDictDataList(reqVO);
        return DictDataConvert.INSTANCE.convertList03(dictDataList);
    }


}
