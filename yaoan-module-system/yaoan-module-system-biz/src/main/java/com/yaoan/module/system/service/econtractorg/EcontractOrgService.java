package com.yaoan.module.system.service.econtractorg;

import java.util.*;
import javax.validation.*;

import com.yaoan.module.system.controller.admin.econtractorg.vo.*;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 电子合同单位信息 Service 接口
 *
 * @author admin
 */
public interface EcontractOrgService {

    /**
     * 创建电子合同单位信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createEcontractOrg(@Valid EcontractOrgSaveReqVO createReqVO);
    void saveEcontractOrg(@Valid EcontractOrgSaveReqVO createReqVO);

    /**
     * 更新电子合同单位信息
     *
     * @param updateReqVO 更新信息
     */
    void updateEcontractOrg(@Valid EcontractOrgSaveReqVO updateReqVO);

    /**
     * 删除电子合同单位信息
     *
     * @param id 编号
     */
    void deleteEcontractOrg(String id);

    /**
     * 获得电子合同单位信息
     *
     * @param id 编号
     * @return 电子合同单位信息
     */
    EcontractOrgDO getEcontractOrg(String id);

    /**
     * 获得电子合同单位信息列表
     *
     * @param ids 编号
     * @return 电子合同单位信息列表
     */
    List<EcontractOrgDO> getEcontractOrgList(Collection<String> ids);

    /**
     * 获得电子合同单位信息分页
     *
     * @param pageReqVO 分页查询
     * @return 电子合同单位信息分页
     */
    PageResult<EcontractOrgDO> getEcontractOrgPage(EcontractOrgPageReqVO pageReqVO);

    /**
     * 获得电子合同单位信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 电子合同单位信息列表
     */
    List<EcontractOrgDO> getEcontractOrgList(EcontractOrgExportReqVO exportReqVO);

}
