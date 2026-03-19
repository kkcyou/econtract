package com.yaoan.module.econtract.service.perfRemind;


import com.yaoan.module.econtract.controller.admin.perfRemind.vo.PerfRemindVO;
/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface PerfRemindService {
String createPerfRemind(PerfRemindVO perfRemindVO);
    PerfRemindVO queryPerfRemind();

}
