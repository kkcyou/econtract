package com.yaoan.module.econtract.api.demo;

import com.yaoan.module.econtract.api.demo.dto.DemoRespDTO;
import com.yaoan.module.econtract.convert.demo.DemoConverter;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import com.yaoan.module.econtract.service.demo.EcmsDemoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 部门 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class DemoApiImpl implements DemoApi {

    @Resource
    private EcmsDemoService demoService;

    @Override
    public DemoRespDTO getDept(String id) {
        EcmsDemo dept = demoService.getDemo(id);
        return DemoConverter.INSTANCE.convert03(dept);
    }

}
