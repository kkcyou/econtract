package com.yaoan.module.econtract.util.gcy;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSON;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.pgx.dto.WarnPlatformRequestDTO;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.KeyGenerator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * 国密SM4加密解密工具
 */
public class Sm4Utils {
    /**
     * 默认key
     */
    public static final String DEFAULT_KEY = "4f4bb890621e5b97215de5af6c273b89";
    /**
     * 预警专用key
     */
    public static final String INSPECTION_KEY = "b044b6ef939f733895b2fa8e4ee68888";
    private static final String ALGORITHM_NAME = "SM4";

    /**
     * @param keySize
     * @return
     * @throws Exception
     * @explain
     */
    public static byte[] generateKey(int keySize) throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * sm4加密
     *
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     * @explain 加密模式：ECB
     * 密文长度不固定，会随着被加密字符串长度的变化而变化
     */
    public static String encryptEcb(String paramStr) throws Exception {
        return encryptEcb(DEFAULT_KEY, paramStr);
    }

    /**
     * sm4加密
     * 预警专用
     *
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     * @explain 加密模式：ECB
     * 密文长度不固定，会随着被加密字符串长度的变化而变化
     */
    public static String encryptEcb4Inspection(String paramStr) throws Exception {
        return encryptEcb(INSPECTION_KEY, paramStr);
    }

    /**
     * sm4解密
     *
     * @param paramStr 待解密字符串
     */
    public static String decryptEcb4Inspection(String paramStr) throws Exception {
        return decryptEcb(INSPECTION_KEY, paramStr);
    }

    /**
     * sm4解密
     *
     * @param paramStr 待解密字符串
     */
    public static byte[] decryptEcb4InspectionV2(byte[] paramStr) throws Exception {
        return decrypt(DEFAULT_KEY, paramStr);
    }

    /**
     * sm4加密
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待加密字符串
     * @return 返回16进制的加密字符串
     * @throws Exception
     * @explain 加密模式：ECB
     * 密文长度不固定，会随着被加密字符串长度的变化而变化
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        SymmetricCrypto sm4 = SmUtil.sm4(ByteUtils.fromHexString(hexKey));
        return sm4.encryptHex(paramStr);
    }

    public static byte[] encrypt(String hexKey, byte[] paramStr) throws Exception {
        SymmetricCrypto sm4 = SmUtil.sm4(ByteUtils.fromHexString(hexKey));
        return sm4.encrypt(paramStr);
    }

    /**
     * sm4解密
     *
     * @param paramStr 待解密字符串
     */
    public static String decryptEcb(String paramStr) throws Exception {
        return decryptEcb(DEFAULT_KEY, paramStr);
    }


    public static EncryptDTO convertParam(Object paramStr) throws Exception {
        String requestParamStr = JsonUtils.toJsonString(paramStr);
        return EncryptDTO.builder().mac(org.apache.commons.codec.digest.DigestUtils.sha256Hex(requestParamStr)).requestParam(Sm4Utils.encryptEcb(requestParamStr)).build();
    }

    /**
     * 预警专用
     */
//    public static WarnPlatformRequestDTO convertParam4Inspection(Object paramStr) throws Exception {
//        String requestParamStr = JsonUtils.toJsonString(paramStr);
//        return WarnPlatformRequestDTO.builder().mac(org.apache.commons.codec.digest.DigestUtils.sha256Hex(requestParamStr)).message(Sm4Utils.encryptEcb4Inspection(requestParamStr)).build();
//    }

    public static EncryptDTO convertParam(String requestParamStr) throws Exception {
        return EncryptDTO.builder().mac(org.apache.commons.codec.digest.DigestUtils.sha256Hex(requestParamStr)).requestParam(Sm4Utils.encryptEcb(requestParamStr)).build();
    }

//    public static void main(String[] args) throws Exception {
//
//        EncryptDTO encryptDTO1 = convertParam(11);
//    }

    public static EncryptDTO convertFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();

        // 将文件内容转换为String
        String requestParamStr = new String(file.getBytes(), StandardCharsets.UTF_8);
//        String requestParamStr = JSONObject.toJSONString(paramStr);
        return EncryptDTO.builder().mac(org.apache.commons.codec.digest.DigestUtils.sha256Hex(requestParamStr)).requestParam(Sm4Utils.encryptEcb(requestParamStr)).build();
    }

    public static MultipartFile convertFile2(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        byte[] inputBytes = outputStream.toByteArray();
        byte[] encrypt = Sm4Utils.encrypt(DEFAULT_KEY, inputBytes);
        return new MockMultipartFile(
                file.getName(),
                file.getOriginalFilename(),
                file.getContentType(),
                new ByteArrayInputStream(encrypt));

    }

    /**
     * sm4解密
     *
     * @param hexKey   16进制密钥（忽略大小写）
     * @param paramStr 待解密字符串
     */
    public static String decryptEcb(String hexKey, String paramStr) throws Exception {
        SymmetricCrypto sm4 = SmUtil.sm4(ByteUtils.fromHexString(hexKey));
        return sm4.decryptStr(paramStr);
    }

    public static byte[] decrypt(String hexKey, byte[] paramStr) throws Exception {
        SymmetricCrypto sm4 = SmUtil.sm4(ByteUtils.fromHexString(hexKey));
        return sm4.decrypt(paramStr);
    }

    public static void main(String[] argv) throws Exception {
        String budgetStr = "[{\"bgtId\":\"CDC56E0B5CD840F4978642A289AFC96D\",\"mofDivCode\":630000000,\"mofDivName\":\"青海省本级\",\"agencyCode\":\"001001\",\"agencyName\":\"中共青海省委办公厅（本级）\",\"fiscalYear\":2023,\"corBgtDocNo\":\"青财预【2023】100号\",\"bgtDocTitel\":\"采购测试\",\"docDate\":\"2023-09-12 08:00:00.000000\",\"bgtDec\":\"工程建设\",\"budgetLevelCode\":2,\"budgetLevelName\":\"省级\",\"supBgtDocNo\":1,\"proCode\":630000242000000000000,\"proName\":\"测试项目\",\"fundTypeCode\":1001,\"fundTypeName\":\"当年预算\",\"manageMofDepCode\":\"08\",\"manageMofDepName\":\"行政处\",\"foundTypeCode\":1,\"foundTypeName\":\"年初预算\",\"expFuncCode\":141001,\"expFuncName\":\"可执行指标单-00022545\",\"govBgtEcoCode\":50301,\"govBgtEcoName\":\"房屋建筑物购建\",\"depBgtEcoCode\":31001,\"depBgtEcoName\":31001,\"bgtTypeCode\":1001,\"bgtTypeName\":\"当年预算\",\"sourceTypeCode\":1001,\"sourceTypeName\":\"一般公共预算资金\",\"amount\":60000000,\"bgtMofDepCode\":\"08\",\"bgtMofDepName\":\"行政处\",\"adjBatchNo\":10448,\"isAdjust\":1,\"isGovPur\":1,\"foundSourceCode\":\"-\",\"isDeleted\":2,\"createTime\":\"2023-09-13 09:41:19.000000\",\"updateTime\":\"2023-09-19 11:43:00.151000\",\"exchangeState\":-1,\"exchangeStateName\":\"待交换\",\"exchangeTime\":\"2023-09-19 11:43:00.147000\"}]";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyU3RhdHVzIjoiMiIsImxvZ2luVHlwZSI6MSwidXNlcl9uYW1lIjoic3lzdGVtIiwidXNlclR5cGVJbmZvcyI6W3sidXNlclR5cGVJZCI6IjAiLCJjb21tb25UeXBlIjoiMCIsInN5c3RlbVR5cGUiOiIxIiwic3RhdHVzIjoiMSJ9XSwidXNlck5hbWUiOiJzeXN0ZW0iLCJ1c2VySWQiOiIwMDE0ZjY2NmFjNTIxMWU5YThhNWZhMTYzZTViOGM5NSIsImNsaWVudF9pZCI6ImdwbS1ncG1zIiwiYXVkIjpbIkFMTCJdLCJpZGVudGl0eVR5cGUiOjEsInVzZXJBY2NvdW50Ijoic3lzdGVtIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sInN5c3RlbVR5cGUiOiIxIiwidXNlclR5cGVOb3ciOiIwIiwiZXhwIjoxNjk5MzAzMjQ5LCJqdGkiOiJmNzliMzRjYy1hMmNkLTQ3ZDctOTBmNS1jMWVmMmUxMTI5YzUifQ.JhcZykR_OJ5ZbmQfxjV_pvuibYy9XuCHhnTBzPdmynUhbGnmUF4wvQOZ3GBUqUgYX4emtLpQ8Lydbx0qdNU8_KfeiKNw5iqwKJrNF8fYyUUWz3gGEwRxbQidHB-nWzOsuVgR8cPIOsO9SEUUgH4yTshx3XqYGzGEu603A24OnkNsFx8iujYxKknrhUSTdVkVf1OajHR3fhgoahfejRmGCwc-JL5lWIUPaQpXdBBY9utH7d0RINTUOMVwzYYFUIOgNu4VZcXnlLMhtQ3eWHMS-m8TNbay5nxgtRkk9fCvMNb8TnJcAqK8N4Qrqt1DaDvBqAQTzLPBucJ-fQV3ZCmmkg";
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("message", encryptEcb(budgetStr));
        param.put("mac", org.apache.commons.codec.digest.DigestUtils.sha256Hex(budgetStr));
        System.out.println("请求参数为：" + JSON.toJSONString(param));
        System.out.println(Sm4Utils.decryptEcb("f7427d1a5f104da9f4898afd03eb20cf31d2378fff451cc21dc79ef556780e68"));
    }

}
