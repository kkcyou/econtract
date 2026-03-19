package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/11 19:49
 */
@Data
public class RelativeBpmPageReqVO extends CommonBpmAutoPageReqVO {

    private List<String> statusList;

}
