package com.yaoan.module.econtract.service.amount;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountYearRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractTypeSignedStatisticRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.SearchYearReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallAlertListReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallAlertListRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallBigAlertListRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.small.SmallMoneySmallRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.small.SmallNumberSmallRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.GPMallAlertEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;
import static com.yaoan.module.econtract.enums.StatusConstants.CONTAIN_SUFFIX_TYPE;
import static java.math.RoundingMode.DOWN;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 21:37
 */
@Service
@Slf4j
public class ContractAmountServiceImpl implements ContractAmountService {
    static final String FIRST_CHAR = "0";
    static final Integer TWELVE_MONTHS = 12;
    static final String STAGE_DRAFTED_CODE = "0";
    static final String STAGE_DRAFTED_INFO = "待起草";
    static final String STAGE_DRAFTED_STAGE_NAME = "起草阶段";
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Override
    public GPMallBigAlertListRespVO listAlert(GPMallAlertListReqVO vo) {
        GPMallBigAlertListRespVO result = new GPMallBigAlertListRespVO();

        List<Integer> statusList = new ArrayList<Integer>();
        //确认阶段
        //被退回（确认阶段）=1
        statusList.add(ContractStatusEnums.BE_SENT_BACK.getCode());
        //待确认（确认阶段）=3
        statusList.add(ContractStatusEnums.TO_BE_CONFIRMED.getCode());
        //签署阶段
        //已确认（签署阶段）=4
        statusList.add(ContractStatusEnums.TO_BE_SIGNED.getCode());
        //签署完成(签署阶段）=6
        statusList.add(ContractStatusEnums.SIGN_COMPLETED.getCode());
        //内部审批阶段
        //待送审(内部审批阶段）=11
        statusList.add(ContractStatusEnums.TO_BE_CHECK.getCode());
        //审核中(内部审批阶段）=12
        statusList.add(ContractStatusEnums.CHECKING.getCode());
        //审核未通过(内部审批阶段）=13
        statusList.add(ContractStatusEnums.CHECK_REJECTED.getCode());
        //审批被退回(内部审批阶段）=14
        statusList.add(ContractStatusEnums.APPROVE_BACK.getCode());

        LambdaQueryWrapperX<SimpleContractDO> wrapperX = new LambdaQueryWrapperX<SimpleContractDO>()

                .inIfPresent(SimpleContractDO::getStatus, statusList);
        if (ObjectUtil.isNotNull(vo.getTime0())) {
            if (vo.getTime0().equals(vo.getTime1())) {
                // 得到 time0 的后一天
                LocalDateTime nextDay = vo.getTime0().toLocalDateTime().plusDays(1);
                wrapperX.betweenIfPresent(SimpleContractDO::getUpdateTime, vo.getTime0(), nextDay);
            } else {
                wrapperX.betweenIfPresent(SimpleContractDO::getUpdateTime, vo.getTime0(), vo.getTime1());
            }
        }

//        wrapperX.orderByDesc(SimpleContractDO::getUpdateTime).last("limit 10");
        //先找出所有符合状态的合同
        List<SimpleContractDO> contractDOList = simpleContractMapper.selectList(wrapperX);

        //内部审批阶段
        List<SimpleContractDO> innerContractList = contractDOList.stream()
                .filter(contract -> contract.getStatus().equals(ContractStatusEnums.TO_BE_CHECK.getCode())
                        || contract.getStatus().equals(ContractStatusEnums.CHECKING.getCode())
                        || contract.getStatus().equals(ContractStatusEnums.CHECK_REJECTED.getCode())
                        || contract.getStatus().equals(ContractStatusEnums.APPROVE_BACK.getCode())
                )
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(innerContractList)) {
            result.setInnerApprove(enhanceContract(innerContractList));
        }

        //签署阶段合同
        List<SimpleContractDO> toBeSignedContractList = contractDOList.stream()
                .filter(contract -> contract.getStatus().equals(ContractStatusEnums.TO_BE_SIGNED.getCode())
                        || contract.getStatus().equals(ContractStatusEnums.SIGN_COMPLETED.getCode()))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(toBeSignedContractList)) {
            result.setToBeSigned(enhanceContract(toBeSignedContractList));
        }
        //确认阶段合同
        List<SimpleContractDO> toBeSentContractList = contractDOList.stream()
                .filter(contract -> contract.getStatus().equals(ContractStatusEnums.BE_SENT_BACK.getCode())
                        || contract.getStatus().equals(ContractStatusEnums.TO_BE_CONFIRMED.getCode()))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(toBeSentContractList)) {
            result.setToBeSent(enhanceContract(toBeSentContractList));
        }


        //控制总体数量在10条
        limitTen(result);

        return result;
    }

    /**
     * 控制总体数量在10条
     */
    private void limitTen(GPMallBigAlertListRespVO result) {
        List<GPMallAlertListRespVO> tempList = new ArrayList<GPMallAlertListRespVO>();
        List<GPMallAlertListRespVO> tempToSendList = new ArrayList<GPMallAlertListRespVO>();
        List<GPMallAlertListRespVO> tempToSignList = new ArrayList<GPMallAlertListRespVO>();
        List<GPMallAlertListRespVO> tempInnerList = new ArrayList<GPMallAlertListRespVO>();
        Integer toBeSentCount = 0;
        Integer toBeSigned = 0;
        Integer inner = 0;
        if (CollectionUtil.isNotEmpty(result.getInnerApprove())) {
            inner = result.getInnerApprove().size();
        }
        if (CollectionUtil.isNotEmpty(result.getToBeSent())) {
            toBeSentCount = result.getToBeSent().size();
        }
        if (CollectionUtil.isNotEmpty(result.getToBeSigned())) {
            toBeSigned = result.getToBeSigned().size();
        }


        Integer sumCount = toBeSentCount + toBeSigned + inner;


        //如果总数超过10，则需要简化
        if (10 < sumCount) {
            if (CollectionUtil.isNotEmpty(result.getInnerApprove())) {
                tempList.addAll(result.getInnerApprove());
            }
            if (CollectionUtil.isNotEmpty(result.getToBeSent())) {
                tempList.addAll(result.getToBeSent());
            }
            if (CollectionUtil.isNotEmpty(result.getToBeSigned())) {
                tempList.addAll(result.getToBeSigned());
            }

            tempList = tempList.stream()
                    .sorted(Comparator.comparing(GPMallAlertListRespVO::getUpdateTime).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
            tempInnerList = tempList.stream()
                    .filter(vo -> GPMallAlertEnums.ALERT_STAGE_TO_BE_CHECK.getStageName().equals(vo.getStageName()))
                    .collect(Collectors.toList());
            tempToSendList = tempList.stream()
                    .filter(vo -> GPMallAlertEnums.ALERT_STAGE_TO_BE_CONFIRM.getStageName().equals(vo.getStageName()))
                    .collect(Collectors.toList());
            tempToSignList = tempList.stream()
                    .filter(vo -> GPMallAlertEnums.ALERT_STAGE_SIGN_APPROVE.getStageName().equals(vo.getStageName()))
                    .collect(Collectors.toList());

            result.setToBeSigned(tempToSignList);
            result.setToBeSent(tempToSendList);
            result.setInnerApprove(tempInnerList);
        }
    }

    private List<GPMallAlertListRespVO> enhanceContract(List<SimpleContractDO> toBeSignedContractList) {
        List<GPMallAlertListRespVO> result = new ArrayList<GPMallAlertListRespVO>();
        for (SimpleContractDO contractDO : toBeSignedContractList) {
            GPMallAlertListRespVO vo = new GPMallAlertListRespVO();
            vo.setName(contractDO.getName());
            vo.setUpdateTime(contractDO.getUpdateTime());
            vo.setId(contractDO.getId());
            GPMallAlertEnums alertEnums = GPMallAlertEnums.getInstance(contractDO.getStatus());
            if (ObjectUtil.isNotNull(alertEnums)) {
                vo.setStageName(alertEnums.getStageName());
                vo.setName(contractDO.getName());
                vo.setStatus(contractDO.getStatus());
            }

            result.add(vo);
        }
        return result;
    }

    /**
     * 合同签约金额情况（折线图）
     */
    @Override
    public List<ContractAmountRespVO> getContractAmount(SearchYearReqVO vo) {
        //得到开始时间点
        if (ObjectUtil.isNull(vo.getSearchYear())) {
            //默认显示当年数据
            vo.setSearchYear(DateUtil.thisYear());
        }
        int year = vo.getSearchYear();
//        LocalDate startDate = Year.of(year).atDay(1);
        LocalDate startDate = getStartDate();
//        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        //0，找出最近十二个月合同
        LambdaQueryWrapperX<SimpleContractDO> wrapperX = new LambdaQueryWrapperX<SimpleContractDO>()
                // 签署完成
                .eq(SimpleContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
                //按最近十二个月的区间
                .betweenIfPresent(SimpleContractDO::getSignDate, startDate, LocalDate.now());

        List<SimpleContractDO> contractDOList = simpleContractMapper.selectList(wrapperX);

        //1,按年份分组
        DateTimeFormatter formatterYear = DateTimeFormatter.ofPattern("yyyy");
        Map<String, List<SimpleContractDO>> groupedByYearMap = contractDOList.stream()
                .collect(Collectors.groupingBy(task -> LocalDateTime.ofInstant(task.getSignDate().toInstant(), ZoneId.systemDefault()).format(formatterYear)));
        Integer biggerYear = 0;
        //如果跨年
        if (1 < groupedByYearMap.size()) {
            biggerYear = LocalDate.now().getYear();
        }
        List<ContractAmountYearRespVO> bigYearRespVOList = new ArrayList<ContractAmountYearRespVO>();
        for (Map.Entry<String, List<SimpleContractDO>> entryYear : groupedByYearMap.entrySet()) {
            ContractAmountYearRespVO yearRespVO = new ContractAmountYearRespVO();
            List<SimpleContractDO> monthContractList = entryYear.getValue();

            //2,按月份分组
            DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM");
            Map<String, List<SimpleContractDO>> groupedByMonthList = monthContractList.stream()
                    .collect(Collectors.groupingBy(task -> LocalDateTime.ofInstant(task.getSignDate().toInstant(), ZoneId.systemDefault()).format(formatterMonth)));

            //3,算出每个月的总金额
            List<ContractAmountRespVO> bigRespVOS = new ArrayList<ContractAmountRespVO>();
            for (Map.Entry<String, List<SimpleContractDO>> entry : groupedByMonthList.entrySet()) {

                ContractAmountRespVO bigRespVO = new ContractAmountRespVO();

                //计算金额
                List<SimpleContractDO> contractList = entry.getValue();
                if (CollectionUtil.isEmpty(contractList)) {
                    throw exception(EMPTY_DATA_ERROR);
                }
                BigDecimal sumMoney = new BigDecimal(0);
                for (SimpleContractDO simpleContractDO : contractList) {
                    if (ObjectUtil.isNotNull(simpleContractDO.getAmount())) {
                        sumMoney = sumMoney.add(new BigDecimal(simpleContractDO.getAmount()).setScale(2, DOWN), MathContext.DECIMAL32);
                    }
                }
                //赋值
                String monthIndex = entry.getKey();
                bigRespVO.setYearIndex(Integer.valueOf(entryYear.getKey()));
                bigRespVO.setMonthIndex(monthIndex);
                bigRespVO.setMonthName(getMonthName(monthIndex));
                bigRespVO.setMoney(sumMoney);
                bigRespVO.setOrderIndex(Integer.valueOf(monthIndex));
                bigRespVOS.add(bigRespVO);
            }
            //给月份排序
            Collections.sort(bigRespVOS, Comparator.comparing(ContractAmountRespVO::getOrderIndex));
            //填补空白月份和空白金额
//            fullMonths(bigRespVOS);
            bigRespVOS = fullMonthsNew(bigRespVOS, biggerYear);
            Collections.sort(bigRespVOS, Comparator.comparing(ContractAmountRespVO::getMonthIndex));
            yearRespVO.setMonthList(bigRespVOS);
            for (ContractAmountRespVO respVO : bigRespVOS) {
                if (respVO.getYearIndex() != null) {
                    yearRespVO.setYearIndex(respVO.getYearIndex());
                    break;
                }
            }
            bigYearRespVOList.add(yearRespVO);
        }

        return enhanceYearRespVOList(bigYearRespVOList);
    }

    /**
     * 给签约合同排序
     */
    private List<ContractAmountRespVO> enhanceYearRespVOList(List<ContractAmountYearRespVO> bigYearRespVOList) {
        List<ContractAmountRespVO> finalRespVoList = new ArrayList<ContractAmountRespVO>();
        List<ContractAmountYearRespVO> finalBigRespVoList = new ArrayList<ContractAmountYearRespVO>();
        Collections.sort(bigYearRespVOList, Comparator.comparing(ContractAmountYearRespVO::getYearIndex));
        for (ContractAmountYearRespVO yearRespVO : bigYearRespVOList) {
            for (ContractAmountRespVO yearCon : yearRespVO.getMonthList()) {
                ContractAmountRespVO respVO = new ContractAmountRespVO();
                if (yearCon.getYearIndex() != null) {
                    respVO.setYearIndex(yearCon.getYearIndex());
                    respVO.setMonthIndex(yearCon.getMonthIndex());
                    respVO.setMonthName(yearCon.getMonthName());
                    respVO.setMoney(yearCon.getMoney());
                    respVO.setOrderIndex(Integer.valueOf(yearCon.getMonthIndex()));
                    finalRespVoList.add(respVO);
                }
            }
        }
        //最终重组
        List<ContractAmountRespVO> rsList = new ArrayList<ContractAmountRespVO>();
        for (ContractAmountYearRespVO yearRespVO : finalBigRespVoList) {
            for (ContractAmountRespVO finalResultMonth : yearRespVO.getMonthList()) {
                ContractAmountRespVO rs = new ContractAmountRespVO();
                rs.setYearIndex(finalResultMonth.getYearIndex());
                rs.setMonthName(finalResultMonth.getMonthName());
                rs.setMoney(finalResultMonth.getMoney());
                rs.setOrderIndex(finalResultMonth.getOrderIndex());
                rsList.add(rs);
            }
        }
        // 按照 yearIndex 和 orderIndex 进行排序
        List<ContractAmountRespVO> sortedList = finalRespVoList.stream()
                .sorted(Comparator.comparing(ContractAmountRespVO::getYearIndex)
                        .thenComparing(ContractAmountRespVO::getOrderIndex))
                .collect(Collectors.toList());
        return sortedList;
    }

    /**
     * 填补空白月份和空白金额（最近十二月的新逻辑所用）
     *
     * @return
     */
    private List<ContractAmountRespVO> fullMonthsNew(List<ContractAmountRespVO> bigRespVOS, int biggerYear) {


        Integer year = bigRespVOS.get(0).getYearIndex();
        Map<String, ContractAmountRespVO> voMap = CollectionUtils.convertMap(bigRespVOS, ContractAmountRespVO::getMonthIndex);
        List<String> monthIndexList = bigRespVOS.stream().map(ContractAmountRespVO::getMonthIndex).collect(Collectors.toList());
        List<Integer> intList = monthIndexList.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        Collections.sort(intList);
        List<ContractAmountRespVO> resultList = new ArrayList<ContractAmountRespVO>();
        int start = intList.get(0);

        //是否跨年
        if (0 == biggerYear) {
            //非跨年
            fullMonths(bigRespVOS);
            resultList = bigRespVOS;
        } else {
            //跨年
            //去年
            if (biggerYear != bigRespVOS.get(0).getYearIndex()) {
                for (int i = start; i <= TWELVE_MONTHS; i++) {
                    ContractAmountRespVO result = new ContractAmountRespVO();
                    String indexStr = i < 10 ? "0" + 1 : String.valueOf(i);
                    ContractAmountRespVO sql = voMap.get(indexStr);
                    if (ObjectUtil.isNotNull(sql)) {
                        result.setYearIndex(sql.getYearIndex());
                        result.setMonthIndex(sql.getMonthIndex());
                        result.setMonthName(getMonthName(sql.getMonthIndex()));
                        result.setMoney(sql.getMoney());
                        resultList.add(result);
                    } else {
                        //空白月份
                        ContractAmountRespVO emptyMonthVO = new ContractAmountRespVO();
                        emptyMonthVO.setYearIndex(year);
                        emptyMonthVO.setMonthIndex(String.valueOf(i));
                        result.setMonthName(getMonthName(String.valueOf(i)));
                        emptyMonthVO.setMoney(BigDecimal.ZERO);
                        resultList.add(emptyMonthVO);
                    }
                }
            } else {
                //今年
                fullThisYearMonths(bigRespVOS);
                resultList = bigRespVOS;
            }
        }

        return resultList;
    }

    /**
     * 填补空白月份和空白金额
     */
    private void fullMonths(List<ContractAmountRespVO> bigRespVOS) {
        Map<String, ContractAmountRespVO> voMap = CollectionUtils.convertMap(bigRespVOS, ContractAmountRespVO::getMonthIndex);
        List<String> indexs = bigRespVOS.stream().map(ContractAmountRespVO::getMonthIndex).collect(Collectors.toList());
        List<String> cleanedIndexes = indexs.stream()
                .map(this::cleanZero)
                .collect(Collectors.toList());
        for (int i = 1; i <= TWELVE_MONTHS; i++) {

            //当月无合同，就设为空值
            if (!cleanedIndexes.contains(String.valueOf(i))) {
                String index = addZero(i);
                String monthIndex = String.valueOf(i);
                ContractAmountRespVO bigRespVO = new ContractAmountRespVO();
                bigRespVO.setMonthIndex(index);
                bigRespVO.setMonthName(getMonthName(monthIndex));
                bigRespVOS.add(bigRespVO);
            } else {
                String index = addZero(i);
                ContractAmountRespVO bigRespVO = voMap.get(index);
                //若金额为空
                if (ObjectUtil.isNull(bigRespVO.getMoney())) {
                    bigRespVO.setMoney(BigDecimal.valueOf(0));
                }
            }
        }
    }

    /**
     * 今年补充月份
     */
    private void fullThisYearMonths(List<ContractAmountRespVO> bigRespVOS) {
        Map<String, ContractAmountRespVO> voMap = CollectionUtils.convertMap(bigRespVOS, ContractAmountRespVO::getMonthIndex);

        List<String> indexs = bigRespVOS.stream().map(ContractAmountRespVO::getMonthIndex).collect(Collectors.toList());
        // 转换为List<Integer>
        List<Integer> intList = indexs.stream().map(Integer::valueOf).collect(Collectors.toList());
        // 找到最大值
        Integer maxMonth = intList.stream().max(Integer::compareTo).orElse(null);
        List<String> cleanedIndexes = indexs.stream()
                .map(this::cleanZero)
                .collect(Collectors.toList());
        List<ContractAmountRespVO> respVOList = new ArrayList<ContractAmountRespVO>();
        for (int i = 1; i <= maxMonth; i++) {
            ContractAmountRespVO respVO = new ContractAmountRespVO();
            //当月无合同，就设为空值
            if (!cleanedIndexes.contains(String.valueOf(i))) {
                String index = addZero(i);
                String monthIndex = String.valueOf(i);
                ContractAmountRespVO bigRespVO = new ContractAmountRespVO();
                bigRespVO.setMonthIndex(index);
                bigRespVO.setMonthName(getMonthName(monthIndex));
                bigRespVOS.add(bigRespVO);
            }
//            else {
//                String index = addZero(i);
//                ContractAmountRespVO bigRespVO = voMap.get(index);
//                //若金额为空
//                if (ObjectUtil.isNull(bigRespVO.getMoney())) {
//                    bigRespVO.setMoney(BigDecimal.valueOf(0));
//                }else {
//                    bigRespVO.setMoney(bigRespVO.getMoney());
//                }
//            }
        }
    }

    /**
     * 将单位数前面拼 “0”
     */
    public String addZero(int index) {
        String iAsString;
        if (index < 10) {
            iAsString = "0" + index;
        } else {
            iAsString = String.valueOf(index);
        }
        return iAsString;
    }

    /**
     * 自动转成月份名称
     */
    public String getMonthName(String m) {
        if (m.startsWith(FIRST_CHAR)) {
            m = m.substring(1);
        }
        return m + "月";

    }

    /**
     * monthIndex去零
     */
    public String cleanZero(String m) {
        if (m.startsWith(FIRST_CHAR)) {
            m = m.substring(1);
        }
        return m;
    }

    /**
     * 合同签约类型 统计 展示(饼状图)
     * 占比是不用返回，前端有组件自动计算
     */
    @Override
    public ContractTypeSignedStatisticRespVO contractTypeSignedStatistic(SearchYearReqVO vo) {
        List<SmallMoneySmallRespVO> smallMoneySmallRespVOList = new ArrayList<SmallMoneySmallRespVO>();
        List<SmallNumberSmallRespVO> smallNumberSmallRespVOList = new ArrayList<SmallNumberSmallRespVO>();

        //1,找出当年所有合同
        if (ObjectUtil.isNull(vo.getSearchYear())) {
            //默认显示当年数据
            vo.setSearchYear(DateUtil.thisYear());
        }
        int year = vo.getSearchYear();
        LocalDate startDate = Year.of(year).atDay(1);
        LocalDate endDate = Year.of(year).atMonth(12).atEndOfMonth();

        //1，找出当年合同
        LambdaQueryWrapperX<SimpleContractDO> wrapperX = new LambdaQueryWrapperX<SimpleContractDO>()
                // 签署完成
                .eq(SimpleContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
                //按年份
                .betweenIfPresent(SimpleContractDO::getCreateTime, startDate, endDate);
        List<SimpleContractDO> contractDOList = simpleContractMapper.selectList(wrapperX);
        if (CollectionUtil.isEmpty(contractDOList)) {
            return new ContractTypeSignedStatisticRespVO();
        }
        //统计所有存在的类型ID
        List<ContractType> types = contractTypeMapper.selectList();
        if (CollectionUtil.isEmpty(types)) {
            return new ContractTypeSignedStatisticRespVO();
        }
        Map<String, ContractType> typeMap = CollectionUtils.convertMap(types, ContractType::getId);

        //分组
        Map<String, List<SimpleContractDO>> groupedByType = contractDOList.stream()
                .filter(contract -> contract.getContractType() != null)
                .collect(Collectors.groupingBy(SimpleContractDO::getContractType));

        for (Map.Entry<String, List<SimpleContractDO>> entry : groupedByType.entrySet()) {
            ContractTypeSignedStatisticRespVO respVO = new ContractTypeSignedStatisticRespVO();
            String type = "";
            if (typeMap.get(entry.getKey()) != null) {
                type = typeMap.get(entry.getKey()).getName();
                if (!type.contains(CONTAIN_SUFFIX_TYPE)) {
                    type = type + CONTAIN_SUFFIX_TYPE;
                }
            }
            int count = entry.getValue().size();
            SmallMoneySmallRespVO smallMoneySmallRespVO = new SmallMoneySmallRespVO();
            SmallNumberSmallRespVO smallNumberSmallRespVO = new SmallNumberSmallRespVO();
            //类型名称
            smallMoneySmallRespVO.setName(type);
            //类型金额
            Double totalAmount = getSumMoney(entry.getValue());
            smallMoneySmallRespVO.setMoney(BigDecimal.valueOf(totalAmount).setScale(2, RoundingMode.HALF_UP));

            //类型份数
            smallNumberSmallRespVO.setName(type).setNumber(count);

            smallMoneySmallRespVOList.add(smallMoneySmallRespVO);
            smallNumberSmallRespVOList.add(smallNumberSmallRespVO);
        }

        return new ContractTypeSignedStatisticRespVO().setSmallMoneySmallRespVOList(smallMoneySmallRespVOList).setSmallNumberSmallRespVOList(smallNumberSmallRespVOList);
    }

    /**
     * 得到当月金额
     */
    private Double getSumMoney(List<SimpleContractDO> value) {
        Double result = 0.0;
        if (CollectionUtil.isEmpty(value)) {
            return result;
        }
        for (SimpleContractDO simpleContractDO : value) {
            if (ObjectUtil.isNull(simpleContractDO.getAmount())) {
                continue;
            }
            result = result + simpleContractDO.getAmount();
        }
        return result;
    }


    public LocalDate getStartDate() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int startMonth = currentMonth == 12 ? 1 : currentMonth;
        int startYear = currentMonth == 12 ? currentDate.getYear() : currentDate.getYear() - 1;
        return LocalDate.of(startYear, startMonth, 1);
    }


}
