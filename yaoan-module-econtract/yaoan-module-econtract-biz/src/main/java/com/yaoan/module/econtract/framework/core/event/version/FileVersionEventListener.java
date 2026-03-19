package com.yaoan.module.econtract.framework.core.event.version;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.version.vo.FileVersionSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.enums.FileVersionEnums;
import com.yaoan.module.econtract.service.version.FileVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yaoan.module.econtract.enums.ContractStatusEnums.*;

/**
 * @description: {@link FileVersionEvent} 的监听器
 * @author: Pele
 * @date: 2024/8/29 18:31
 */
@Slf4j
@Component
public class FileVersionEventListener implements ApplicationListener<FileVersionEvent> {

    @Override
    public final void onApplicationEvent(FileVersionEvent event) {

        FileVersionService fileVersionService = SpringUtil.getBean(FileVersionService.class);
        ContractMapper contractMapper = SpringUtil.getBean(ContractMapper.class);

//        if (Objects.equals(FileVersionEnums.CONTRACT.getCode(), event.getBusinessType())) {
            ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                    .eq(ContractDO::getId, event.getBusinessId()).select(ContractDO::getId, ContractDO::getStatus));
            if (ObjectUtil.isNotNull(contractDO)) {
                List<Integer> statusList = new ArrayList<>();
                statusList.add(TO_BE_SENT.getCode());
                statusList.add(BE_SENT_BACK.getCode());
                statusList.add(SENT.getCode());
                statusList.add(TO_BE_CONFIRMED.getCode());
                statusList.add(TO_BE_CHECK.getCode());
                statusList.add(CHECKING.getCode());
                statusList.add(CHECK_REJECTED.getCode());
                statusList.add(APPROVE_BACK.getCode());
                //只有这些状态的合同才需要留痕
                if (statusList.contains(contractDO.getStatus())) {
                    try {
                        fileVersionService.save(new FileVersionSaveReqVO(event.getBusinessType(), event.getBusinessId(), event.getRemark()));
                    } catch (Exception e) {
                        log.error("【文件留痕异常】" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
//        }


    }


}
