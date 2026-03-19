package com.yaoan.module.econtract.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class AmountUtil {

    private static final String UNIT      = "万仟佰拾亿仟佰拾万仟佰拾元角分";
    private static final String DIGIT     = "零壹贰叁肆伍陆柒捌玖";
    private static final double MAX_VALUE = 9999999999999.99D;

    private static final Map<Character, String> DIGIT_MAP = Maps.newHashMap();

    private static final Map<Character, String> UNIT_MAP = Maps.newHashMap();

    private static final Map<Character, String> BIG_UNIT_MAP = Maps.newHashMap();

    /**
     * 缺省的取整模式
     */
    public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;


    static {
        DIGIT_MAP.put('壹', "1");
        DIGIT_MAP.put('贰', "2");
        DIGIT_MAP.put('叁', "3");
        DIGIT_MAP.put('肆', "4");
        DIGIT_MAP.put('伍', "5");
        DIGIT_MAP.put('陆', "6");
        DIGIT_MAP.put('柒', "7");
        DIGIT_MAP.put('捌', "8");
        DIGIT_MAP.put('玖', "9");

        UNIT_MAP.put('仟', "1000");
        UNIT_MAP.put('佰', "100");
        UNIT_MAP.put('拾', "10");
        UNIT_MAP.put('元', "1");
        UNIT_MAP.put('角', "0.1");
        UNIT_MAP.put('分', "0.01");

        BIG_UNIT_MAP.put('亿', "100000000");
        BIG_UNIT_MAP.put('万', "10000");
    }
    /**
     * 金额大小写转化服务
     * <li>trsferCapital(5412321154656.01) == 伍万肆千壹佰贰拾叁亿贰千壹佰壹拾伍万肆千陆佰伍拾陆元零壹分</li>
     * <li>trsferCapital(1000000000.01) == 壹拾亿元零壹分</li>
     * <li>trsferCapital(1000000001) == 壹拾亿零壹元整</li>
     * <li>trsferCapital(1000.99) == 壹千元玖角玖分</li>
     * <li>trsferCapital(100) == 壹佰元整</li>
     * <li>trsferCapital(0.01) == 壹分</li>
     * <li>trsferCapital(0) == 零元整</li>
     *
     * @param value
     * @return  转化大写
     */
    public static String trsferCapital(double value) {
        if ((value < 0) || (value > MAX_VALUE)) {
            return "参数非法!";
        }
        long l = Math.round(value * 100);
        if (l == 0) {
            return "零元整";
        }
        String strValue = l + "";
        // i用来控制数
        int i = 0;
        // j用来控制单位
        int j = UNIT.length() - strValue.length();
        String rs = "";
        boolean isZero = false;
        for (; i < strValue.length(); i++, j++) {
            char ch = strValue.charAt(i);
            if (ch == '0') {
                isZero = true;
                if ((UNIT.charAt(j) == '亿') || (UNIT.charAt(j) == '万') || (UNIT.charAt(j) == '元')) {
                    rs = rs + UNIT.charAt(j);
                    isZero = false;
                }
            } else {
                if (isZero) {
                    rs = rs + "零";
                    isZero = false;
                }
                rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
            }
        }
        if (!rs.endsWith("分")) {
            rs = rs + "整";
        }
        rs = rs.replaceAll("亿万", "亿");
        return rs;
    }

    /**
     * 金额大写转化为数字
     *
     * @param capital
     * @return
     */
    public static BigDecimal trsferDouble(String capital) {

        if (StringUtils.isBlank(capital)){
            return new BigDecimal(0);
        }
        BigDecimal retrunDouble = new BigDecimal(0);
        BigDecimal tmpInt = new BigDecimal(0);
        for (char c : capital.toCharArray()){
            if (DIGIT_MAP.containsKey(c)){
                tmpInt = new BigDecimal(DIGIT_MAP.get(c));
            }else if (UNIT_MAP.containsKey(c)){
                retrunDouble = retrunDouble.add(tmpInt.multiply(new BigDecimal(UNIT_MAP.get(c))));
                tmpInt = new BigDecimal(0);
            }else if (BIG_UNIT_MAP.containsKey(c)){
                retrunDouble = retrunDouble.add(tmpInt).multiply(new BigDecimal(BIG_UNIT_MAP.get(c)));
                tmpInt = new BigDecimal(0);
            }
        }
        return retrunDouble;
    }
    public static void main(String[] args) {
        double amount = 1111.33;
//        String chineseAmount = toChinese(amount);
        String chineseAmount = trsferCapital(amount);
        System.out.println(chineseAmount);  // 输出：壹拾壹元叁角叁分
    }
}
