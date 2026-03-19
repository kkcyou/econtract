package com.yaoan.module.econtract.framework.core.event.warning;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import com.yaoan.module.econtract.dal.mysql.ledger.LedgerMapper;
import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEvent;
import com.yaoan.module.econtract.service.warningcfg.WarningCfgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
public class EcmsWarningEventListener
        implements ApplicationListener<EcmsWarningEvent> {

    @Override
    public final void onApplicationEvent(EcmsWarningEvent event) {
        WarningCfgService warningCfgService = SpringUtil.getBean(WarningCfgService.class);
        warningCfgService.warningCheck(event.getFlag(), event.getTaskParams(), event.getBussinessId(), event.getModelCode());
    }

}


