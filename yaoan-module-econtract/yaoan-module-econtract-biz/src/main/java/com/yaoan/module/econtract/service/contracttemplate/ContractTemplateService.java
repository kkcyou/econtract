package com.yaoan.module.econtract.service.contracttemplate;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelPageRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelVersionVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.DeleteVO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Pele
 * @description 针对表【ecms_contract_template】的数据库操作Service
 * @createDate 2023-08-09 16:13:20
 */
public interface ContractTemplateService extends IService<ContractTemplate> {


    /**
     * 获得单个范本信息
     */
    TemplateSingleVo getTemplateById(String id) throws Exception;

    /**
     * 保存范本（有上传）
     */
    String saveTemplateByUpload(TemplateCreateVo vo) throws Exception;

    /**
     * 范本制作的列表展示（自己创建的，全状态范本）
     */
    PageResult<TemplateSimpleVo> getMyAllTemplatePage(TemplateListReqVo vo) throws Exception;

    /**
     * 更新范本
     */
    String updateTemplate(TemplateUpdateReqVO reqVO) throws Exception;

    /**
     * 删除范本
     */
    void deleteTemplate(DeleteVO reqVO);

    /**
     * 范本库的范本列表展示（全表，审批通过状态范本）
     */
    PageResult<TemplateAllPermissionReqVo> getAllTemplates(TemplateListReqVo vo) throws Exception;

    /**
     * 范本列表
     * @param vo
     * @return
     */
    PageResult<TemplateAllPermissionReqVo> getAllPage(TemplateListReqVo vo) ;

    /**
     * 新增范本-直接发送审批
     * 返回 model_bpm_id
     */
    String insertTemplateAndSubmitApprove(TemplateCreateSubmitReqVO vo) throws Exception;

    /**
     * 编辑范本_直接发送审批
     * 返回 model_bpm_id
     */
    String updateTemplateAndSubmitApprove(TemplateUpdateSubmitReqVO vo) throws Exception;

    /**
     * 推荐五个范本
     */
    public List<TemplateSimpleVo> getFiveTemplates(int id) throws Exception;

    /**
     * 获取所有范本列表
     */
    PageResult<ContractTemplate> getPage(TemplateListReqVo dto);

    /**
     * 获得最新版本号
     */
    TemplateVersionVO getTemplateVersion(String code, Integer i);

    /**
     * 查看模板历史版本
     * @param code
     * @return
     */
    List<TemplateHistoryRespVO> getModelHistory(String code);
    /**
     * 获取范本引用情况列表
     */
    PageResult<TemplateQuotePageRespVO> getTemplateQuotePage(TemplateQuotePageReqVO vo);

    /**
     * 批量发布/批量取消
     */
    void batchPublish(BatchPublishReqVO reqVO);

    List<TermListRespVO> selectTermsByTemplateId(IdReqVO reqVO);

    TemplateOneRespVo getTemplate(String id);

    String insertRtf4Template(TemplateCreateVo reqVO);

    void downloadTemplate(HttpServletResponse response, String templateId) throws Exception;
}
