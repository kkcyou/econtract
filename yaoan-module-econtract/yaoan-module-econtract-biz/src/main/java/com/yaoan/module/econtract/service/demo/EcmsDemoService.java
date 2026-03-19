package com.yaoan.module.econtract.service.demo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoPageReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;

/**
 * <p>
 * demo 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
public interface EcmsDemoService {

    PageResult<EcmsDemo> getDemoPage(DemoPageReqVO reqVO);

    String createDemo(DemoCreateReqVO reqVO);

    void updateDemo(DemoUpdateReqVO reqVO);

    void deleteDemo(String id);

    EcmsDemo getDemo(String id);
}
