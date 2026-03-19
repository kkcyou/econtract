   package com.yaoan.module.econtract.util;

   import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;

   import java.time.LocalDateTime;
   import java.time.format.DateTimeFormatter;
   import java.util.concurrent.atomic.AtomicInteger;

   /**
    * @date
    * @description 合同编码生成工具类
    */
   public class ContractCodeUtil {
       public static final String CONTRACT_CODE_PREFIX_KC = "KC-";
       public static final String CONTRACT_CODE_PREFIX_JD = "JD-";
       public static final String CONTRACT_CODE_PREFIX_ZBJ = "ZBJ-";
       public static final String CONTRACT_CODE_PREFIX_MC = "MC-";
       public static final String CONTRACT_CODE_PREFIX_XC = "XC-";
       public static final String CONTRACT_CODE_PREFIX_ECMS = "ECMS-";
       // 使用AtomicInteger确保线程安全的自增
       private static final AtomicInteger SEQUENCE_COUNTER = new AtomicInteger(0);

       // 日期格式
       private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

       public static String getCodeByPrefix(String prefix) {
           LocalDateTime date = LocalDateTime.now();
           String formattedDate = DATE_FORMATTER.format(date);
           // 使用AtomicInteger生成递增序列号
           // 最多生成1千万个唯一序列号
           int sequenceNumber = SEQUENCE_COUNTER.incrementAndGet();
            // 序列号左填充0至7位
           String formattedSequence = String.format("%04d", sequenceNumber);
           return prefix + formattedDate +  formattedSequence;
       }
       /**
        * 平台标识 京东：JD  猪八戒：ZBJ  框彩：KC 数采卖场：MC  电子交易：XC
        * 平台标识+"-"+8位日期位数+"-"+4位自增序列->例如：KC-20210801-0001
        */
       public static String getContractCode(String contractForm) {
           LocalDateTime date = LocalDateTime.now();
           String formattedDate = DATE_FORMATTER.format(date);
           // 使用AtomicInteger生成递增序列号
           int sequenceNumber = SEQUENCE_COUNTER.incrementAndGet(); // 最多生成1千万个唯一序列号
           String formattedSequence = String.format("%04d", sequenceNumber); // 序列号左填充0至7位
           String contractCode = "";
           switch (PlatformEnums.getInstance(contractForm)) {
               case GPMS_GPX:
                   //电子交易-项目采购
                   contractCode = CONTRACT_CODE_PREFIX_XC + formattedDate + "-" + formattedSequence;
                   break;
               case GPMALL:
                   //数采-电子卖场（协议定点）
                   contractCode = CONTRACT_CODE_PREFIX_MC + formattedDate + "-" + formattedSequence;
                   break;
               case GP_GPFA:
                   //数采-框采平台
                   contractCode = CONTRACT_CODE_PREFIX_KC + formattedDate + "-" + formattedSequence;
                   break;
               case PROCESSLESS:
                   //无过程采购
                   contractCode = CONTRACT_CODE_PREFIX_ECMS + formattedDate + "-" + formattedSequence;
                   break;
               case JDMALL:
                   //京东商城
                   contractCode = CONTRACT_CODE_PREFIX_JD + formattedDate + "-" + formattedSequence;
                   break;
               case ZHUBAJIE:
                   //猪八戒卖场
                   contractCode = CONTRACT_CODE_PREFIX_ZBJ + formattedDate + "-" + formattedSequence;
                   break;
           }
           return contractCode;
       }

       public static void main(String[] args) {
           for (int i = 0; i < 100; i++) {
               String contractCode = getContractCode("zhubajie");
               System.out.println(contractCode);

           }

       }
   }
