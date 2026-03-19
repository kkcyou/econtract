package com.yaoan.module.econtract.controller.admin.workbench.vo.task;

import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/4 17:36
 */
@Data
public class BigWorkBenchTaskRespVO {
    private WorkBenchTaskCountRespVO countRespVO;
    private PageResult<WorkBenchTaskListRespVO> pageResult;
}
