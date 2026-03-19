package com.yaoan.module.econtract.api.demo;

import com.yaoan.module.econtract.api.demo.dto.DemoRespDTO;

/**
 * 部门 API 接口
 *
 * @author 芋道源码
 */
public interface DemoApi {

    /**
     * 获得Demo信息
     *
     * @param id id
     * @return Demo信息
     */
    DemoRespDTO getDept(String id);

}
