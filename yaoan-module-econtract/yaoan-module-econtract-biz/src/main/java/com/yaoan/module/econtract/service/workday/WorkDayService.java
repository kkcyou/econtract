package com.yaoan.module.econtract.service.workday;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.workday.vo.InitReqVO;
import com.yaoan.module.econtract.controller.admin.workday.vo.UpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.workday.WorkdayDO;

import java.time.LocalDate;
import java.util.Date;

public interface WorkDayService extends IService<WorkdayDO> {

    PageResult<WorkdayDO> getList(InitReqVO vo) throws Exception;

    String initByYear(InitReqVO vo) throws Exception;

    String updateDayType(UpdateReqVO vo) throws Exception;

    String getWarningDate(int warningIsWorkday, int beforeDay, int warningDay , LocalDate currentDate)  throws Exception;
    String getContractSignEndTime (int warningIsWorkday, int beforeDay, int warningDay , LocalDate currentDate)  throws Exception;

    /**
     * 更新节假日
     * 每个节假日之前进行更新
     */
    String refreshHoliday() throws Exception;

    /**
     * 计算n个工作日前/后的时间，跳过休息日和节假日
     * @param startDate
     * @param n
     * @param flag 0:向前算，1:向后算
     * @return
     */
    Date calculateJumpRestDays(Date startDate, int n, int flag);
}
