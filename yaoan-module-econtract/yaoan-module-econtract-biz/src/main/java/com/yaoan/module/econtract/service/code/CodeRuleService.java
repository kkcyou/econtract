package com.yaoan.module.econtract.service.code;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleCreateReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;

import javax.annotation.Resource;

public interface CodeRuleService {
    /**
     * 新增(修改)编号规则
     */
    String createOrUpdate(CodeRuleCreateReqVO codeRuleCreateReqVO);

    /**
     * 获取编号规则
     */
    ListCodeRuleRespVO getById(String id);

    /**
     * 根据编号规则生成编号
     */
    String generate( CodeQueryReqVO vo);

    /**
     * 根据合同类型生成编号
     */
    String generate4ContractType( CodeQueryReqVO vo);

    /**
     * 获取编号规则列表
     */
    PageResult<ListCodeRuleRespVO> list(ListCodeRuleReqVO reqVO);

    /**
     * 删除编号规则
     */
    void delete(String id);

    /**
     * 修改编号规则状态
     */
    void updateStatus(String id);

    /**
     * 导出编号规则
     */
    PageResult<ListCodeRuleRespVO> export(ListCodeRuleReqVO reqVO);
}
