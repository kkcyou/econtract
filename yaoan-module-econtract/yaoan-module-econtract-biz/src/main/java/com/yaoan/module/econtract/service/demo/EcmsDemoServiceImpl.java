package com.yaoan.module.econtract.service.demo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoPageReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.convert.demo.DemoConverter;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import com.yaoan.module.econtract.dal.mysql.demo.EcmsDemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * demo 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Service
public class EcmsDemoServiceImpl implements EcmsDemoService {

    @Resource
    private EcmsDemoMapper ecmsDemoMapper;

    @Override
    public PageResult<EcmsDemo> getDemoPage(DemoPageReqVO reqVO) {

        return ecmsDemoMapper.selectPage(reqVO);
    }

    @Override
    public String createDemo(DemoCreateReqVO reqVO) {
        EcmsDemo ecmsDemo = DemoConverter.INSTANCE.convert(reqVO);
        ecmsDemoMapper.insert(ecmsDemo);
        return ecmsDemo.getId();
    }

    @Override
    public void updateDemo(DemoUpdateReqVO reqVO) {
        EcmsDemo ecmsDemo = DemoConverter.INSTANCE.convert(reqVO);
        ecmsDemoMapper.updateById(ecmsDemo);
    }

    @Override
    public void deleteDemo(String id) {
        ecmsDemoMapper.deleteById(id);
    }

    @Override
    public EcmsDemo getDemo(String id) {
        return ecmsDemoMapper.selectById(id);
    }
}