package com.yaoan.module.econtract.service.outwardcontract;

import java.util.*;
import javax.validation.*;

import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractExportReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.outwardcontract.OutwardContractDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 对外合同 Service 接口
 *
 * @author Pele
 */
public interface OutwardContractService {

    /**
     * 创建对外合同
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOutwardContract(@Valid OutwardContractCreateReqVO createReqVO);

    /**
     * 更新对外合同
     *
     * @param updateReqVO 更新信息
     */
    void updateOutwardContract(@Valid OutwardContractUpdateReqVO updateReqVO);

    /**
     * 删除对外合同
     *
     * @param id 编号
     */
    void deleteOutwardContract(String id);

    /**
     * 获得对外合同
     *
     * @param id 编号
     * @return 对外合同
     */
    OutwardContractDO getOutwardContract(String id);

    /**
     * 获得对外合同列表
     *
     * @param ids 编号
     * @return 对外合同列表
     */
    List<OutwardContractDO> getOutwardContractList(Collection<String> ids);

    /**
     * 获得对外合同分页
     *
     * @param pageReqVO 分页查询
     * @return 对外合同分页
     */
    PageResult<OutwardContractDO> getOutwardContractPage(OutwardContractPageReqVO pageReqVO);

    /**
     * 获得对外合同列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 对外合同列表
     */
    List<OutwardContractDO> getOutwardContractList(OutwardContractExportReqVO exportReqVO);

}
