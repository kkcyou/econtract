package com.yaoan.module.econtract.controller.admin.workbench.vo.task;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 17:35
 */
@Data
public class WorkBenchTaskReqVO extends PageParam {

    private String processDefKey;

    List<String> instanceIdList;


}
