package com.yaoan.framework.security.core.filter;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


// 标记为最高优先级，确保第一个执行
@Order(Ordered.HIGHEST_PRECEDENCE)
// 注册为Spring Bean（若使用Spring Boot，@Component会自动扫描）
@Component
@Log4j2
public class LicenseCheckFilter extends OncePerRequestFilter {


    @Value("${validate.license}")
    private String validate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 执行License校验（复用之前的checkLicense方法）
        boolean isLicenseValid = checkLicense(validate);

        // 2. 校验失败：直接返回403，中断请求链
        if (!isLicenseValid) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"code\":403,\"message\":\"License校验失败，拒绝访问\"}");
            }
            return; // 不再执行后续过滤器
        }

        // 3. 校验通过：继续执行后续过滤器和控制器
        filterChain.doFilter(request, response);

    }


    public static boolean checkLicense(String str) {
        if (str != null && !str.trim().isEmpty()) {
            String dataAndMac;
            try {
                dataAndMac = decryptPassword(str);
            } catch (Exception var7) {
                log.info("validate为{}", new Object[]{str});
                log.info("--->validate解析错误");
                return false;
            }
            String[] split = dataAndMac.split(",");
            if (split.length != 2) {
                return false;
            } else {
                List<String> macList = getMacList();
                LocalDate validateDate = LocalDate.parse(split[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                boolean macCheck = macList.contains(split[1]);
                if (!macCheck) {
                    log.info("MAC地址验证结果为{}", new Object[]{macCheck});
                }

                boolean timeCheck = LocalDate.now().isBefore(validateDate);
                if (!timeCheck) {
                    log.info("有效期验证结果为{}", new Object[]{timeCheck});
                }

                return macCheck && timeCheck;
            }
        } else {
            if (ObjectUtil.isEmpty(str)) {
                log.info("----------------------------------------no validate date------------------------------------");
            }

            return false;
        }
    }


    public static final String decryptPassword(String encrypted) {
        if (encrypted == null) {
            return "";
        } else if (encrypted.isEmpty()) {
            return "";
        } else {
            BigInteger bi_confuse = new BigInteger("0933910847463829827159347601486730416058");
            BigInteger bi_r1 = new BigInteger(encrypted, 16);
            BigInteger bi_r0 = bi_r1.xor(bi_confuse);
            return new String(bi_r0.toByteArray());
        }
    }


    public static List<String> getMacList() {
        ArrayList<String> list = new ArrayList<>();

        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface anInterface = (NetworkInterface) networkInterfaces.nextElement();
                byte[] hardwareAddress = anInterface.getHardwareAddress();
                if (hardwareAddress != null) {
                    list.add(getMac(hardwareAddress));
                }
            }

            return list;
        } catch (SocketException var4) {
            return list;
        }
    }

    private static String getMac(byte[] hardwareAddress) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < hardwareAddress.length; ++i) {
            if (i != 0) {
                sb.append("-");
            }

            String s = Integer.toHexString(hardwareAddress[i] & 255);
            sb.append(s.length() == 1 ? 0 + s : s);
        }

        return sb.toString().toUpperCase();
    }
}

