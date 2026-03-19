package com.yaoan.module.econtract.framework.core.event;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import com.yaoan.module.econtract.dal.mysql.ledger.LedgerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * {@link EcmsContractResultEvent} 的监听器
 *
 * @author doujl
 */
@Slf4j
@Component
public class EcmsContractResultEventListener
        implements ApplicationListener<EcmsContractResultEvent> {

    @Override
    public final void onApplicationEvent(EcmsContractResultEvent event) {

        LedgerMapper ledgerMapper = SpringUtil.getBean(LedgerMapper.class);
        Ledger ledger = ledgerMapper.selectOne(Ledger::getContractId, event.getId());
        if (ledger != null) {
            ledger.setContractStatus(event.getResult());
            ledgerMapper.updateById(ledger);
        }
    }

}
