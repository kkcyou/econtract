package com.yaoan.module.econtract.service.model;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.model.vo.client.ModelClientRespVO;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListReqVO;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.*;
import com.yaoan.module.econtract.dal.dataobject.model.Model;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:38
 */
public interface ModelService extends IService<Model> {

    /**
     * 展示当前用户创建的所有状态的模板(制作模板)
     */
    ModelBigPageRespVO getMyModelPage(ModelListReqVO vo) throws Exception;

    /**
     * 展示全部审批通过的列表()
     */
    PageResult<ModelStoreRespVO> getAllModelPage(ModelListReqVO vo) throws Exception;

    /**
     * 新增模板-上传文件-保存
     * 上传成功，返回url；上传失败，返回“fail”
     */
    ModelIdCodeRespVO insertModelByUpload(ModelCreateReqVO vo) throws Exception;

    /**
     * 编辑模板(激活状态不可编辑) doujiale
     */
    String updateModel(ModelUpdateVO vo) throws Exception;

    /**
     * 批量删除模板
     */
    Object deleteModels(List<String> ids) throws Exception;

    Object submitApproval(ModelSubmitReqVo vo);

    /**
     * 查看单个模板（jiale）
     */
    ModelSingleRespVo getModel(String id) throws Exception;

    /**
     * 新增模板-直接发送审批
     * 返回 model_bpm_id
     */
    String insertModelAndSubmitApprove(ModelCreateSubmitReqVO vo) throws Exception;

    /**
     * 编辑模板-直接发送审批
     * 返回 model_bpm_id
     */
    String updateModelAndSubmitApprove(ModelUpdateSubmitReqVO vo) throws Exception;

    /**
     * 根据合同类型，获得模板
     * 返回模板id和名称
     */
    ModelGetContractTypeRespVO getModelByContractType(IdReqVO vo);

    /**
     * 查看模板历史版本
     * @param code
     * @return
     */
    PageResult<ModelPageRespVO> getModelHistory(String code) throws Exception;

    /**
     * 查看模板历史版本
     * @param code
     * @return
     */
    List<ModelPageRespVO> getModelHistoryV1(String code) throws Exception;

    /**
     * 获得最新版本号
     */
    ModelVersionVO getModelVersion(String code,Integer i) throws Exception;

    /**
     * 修改启用状态 -生效时间 启用字段
     */
    Boolean updateModelEnable(String id);

    /**
     * 修改收藏状态
     */
    Boolean updateModelCollect(String id);

    /**
     * 定时任务修改有效期生效状态
     */
    void updateModelTimeEffect();

    /**
     * --------------------------------------------- 对外API ----------------------------------------
     * */
    PageResult<ModelApiListRespVO> list(ModelApiListReqVO vo);

    PageResult<ModelApiListRespVO> searchList(ModelApiListReqVO vo);

    String insertRtf4Model(ModelCreateReqVO reqVO);

    List<ModelIdAndNameVO> getMyModelList();


    void getModelByOrgId(String orgId);

    List<ModelClientRespVO> listModelClient();
}
