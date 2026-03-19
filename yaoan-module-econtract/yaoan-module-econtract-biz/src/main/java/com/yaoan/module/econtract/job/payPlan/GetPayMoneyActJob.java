package com.yaoan.module.econtract.job.payPlan;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.quartz.core.handler.JobHandler;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PayMoneyActDO;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.payMoneyAct.PayMoneyActMapper;
import com.yaoan.module.econtract.enums.BusinessTokenConfigEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanServiceImpl;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.system.api.config.SystemConfigApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取监管的支付计划信息
 */
@Slf4j
@Component("GetPayMoneyActJob")
public class GetPayMoneyActJob implements JobHandler {
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;

    @Resource
    private SuperVisionApi superVisionApi;
    @Resource
    private EcmsGcyBuyPlanServiceImpl gcyBuyPlanService;

    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private PayMoneyActMapper payMoneyActMapper;


    @Override
    @TenantIgnore
    public String execute(String param) throws Exception {
        System.out.println("获取监管的支付计划信息定时任务开始");
        ArrayList<String> platformList = new ArrayList<>();
        platformList.add("ecms");
//        platformList.add("zhubajie");
//        platformList.add("JdMall");
//        platformList.add("gpms-gpx-5.3");
        //查询前一天数据
        String initToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
        //获取配置项
        String timeConfig = systemConfigApi.getConfigByKey("PayMoneyTime");
        LocalDate previousDay = LocalDate.now().minusDays(1);
        LocalDateTime startOfPreviousDay = previousDay.atStartOfDay();
        LocalDateTime endOfPreviousDay = previousDay.atTime(LocalTime.MAX);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (String platform : platformList) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("platform", platform);
            if (StringUtils.isNotEmpty(timeConfig)) {
                String[] timeArr = timeConfig.split("&");
                map.put("startTime", timeArr[0]);
                map.put("endTime", timeArr[1]);
            } else {
                map.put("startTime", startOfPreviousDay.format(formatter));
                map.put("endTime", endOfPreviousDay.format(formatter));
            }
            Object json = JSONObject.toJSON(map);
            EncryptDTO encryptDTO = Sm4Utils.convertParam(json);
            EncryptResponseDto payMoneyAct = superVisionApi.getPayMoneyAct(initToken, encryptDTO);
            String moneyResult = null;
            try {
                moneyResult = Sm4Utils.decryptEcb(payMoneyAct.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (StringUtils.isEmpty(moneyResult)) {
                System.out.println("获取监管的" + platform + "支付计划信息定时任务结束没有需要查询的数据时间:" + previousDay);
                continue;
            }
            List<PayMoneyActDO> payMoneyActDOList = JSONObject.parseArray(moneyResult, PayMoneyActDO.class);
            if (CollectionUtil.isEmpty(payMoneyActDOList)) {
                System.out.println("获取监管的" + platform + "支付计划信息定时任务结束没有需要查询的数据时间:" + previousDay);
                continue;
            }
            payMoneyActMapper.insertBatch(payMoneyActDOList);
            System.out.println("支付计划信息落库完成条数：" + payMoneyActDOList.size());
            //同步合同表支付信息   当数据量太大时分批执行
            int paySize = payMoneyActDOList.size();
            if (paySize > 1000) {
                int numSize = 0;
                int endSize = 1000;
                while (numSize < paySize) {
                    if (endSize > paySize) {
                        endSize = paySize;
                    }
                    List<PayMoneyActDO> payMoneyActSub = payMoneyActDOList.subList(numSize, endSize);
                    numSize += 1000;
                    endSize += 1000;
                    //查询合同
                    List<String> contractId = payMoneyActSub.stream().filter(l -> l.getSuccess() == 1 && l.getActMoney().compareTo(BigDecimal.ZERO) > 0).map(PayMoneyActDO::getContractGuid).collect(Collectors.toList());
                    selectAndUpdateContract(contractId, payMoneyActSub);
                }
            } else {
                selectAndUpdateContract(payMoneyActDOList.stream().filter(l -> l.getSuccess() == 1 && l.getActMoney().compareTo(BigDecimal.ZERO) > 0).map(PayMoneyActDO::getContractGuid).collect(Collectors.toList()), payMoneyActDOList);
            }
        }

        System.out.println("获取监管的支付计划信息定时任务结束");
        return "获取监管的支付计划信息定时任务完成";
    }

    //查询合同信息
    public void selectAndUpdateContract(List<String> contractId, List<PayMoneyActDO> payMoneyList) {
        List<ContractOrderExtDO> orderContractList = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getTotalMoney, ContractOrderExtDO::getStatus, ContractOrderExtDO::getPayedAmount)
                .eq(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())
                .in(ContractOrderExtDO::getId, contractId)
        );
        List<ContractOrderExtDO> updateContractList = new ArrayList<>();
        Map<String, PayMoneyActDO> payMoneyActMap = CollectionUtils.convertMap(payMoneyList, PayMoneyActDO::getContractGuid);
        orderContractList.forEach(contract -> {
            PayMoneyActDO payMoneyActDO = payMoneyActMap.get(contract.getId());
            if(ObjectUtil.isEmpty(contract.getPayedAmount())){
                contract.setPayedAmount(BigDecimal.ZERO);
            }
            if (contract.getPayedAmount().compareTo(contract.getTotalMoney()) < 0 && ObjectUtil.isNotEmpty(payMoneyActDO)
                    && payMoneyActDO.getActMoney().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal payMoney = contract.getPayedAmount().add(payMoneyActDO.getActMoney());
                if (payMoney.compareTo(contract.getTotalMoney()) > 0) {
                    payMoney = contract.getTotalMoney();
                }
                contract.setPayedAmount(payMoney);
                updateContractList.add(contract);
            }
        });
        if (CollectionUtil.isNotEmpty(updateContractList)) {
            contractOrderExtMapper.updateBatch(updateContractList);
        }
    }
}
