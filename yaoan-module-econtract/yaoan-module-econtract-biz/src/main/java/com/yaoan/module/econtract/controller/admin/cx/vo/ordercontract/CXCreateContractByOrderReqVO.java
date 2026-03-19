package com.yaoan.module.econtract.controller.admin.cx.vo.ordercontract;

import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/26 11:56
 */
@Data
public class CXCreateContractByOrderReqVO extends IdReqVO {

    /**
     * 内容域的标签name集合
     */
    private List<String> zoneNames;


}
