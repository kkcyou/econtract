package com.yaoan.module.econtract.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Comparator;
import java.util.LinkedList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class JsonSorter {



    static String appSecret = "at23pxnPBNQY3JiA8N5U1gabiQqxZwqH_Gihg7a_wrULmlOPVP-iiRjv9JWYPrDk";
    public static void main(String[] args) {
        // 你的复杂 JSON 字符串
        String jsonString = "{" +
                "\"orderid\": \"i3khJ4dMv3\"," +
                "\"order_type\": 1," +
                "\"contract\": {" +
                "\"contract_id\": \"xxxxxxxxx\"," +
                "\"contract_name\":\"采购合同\"," +
                "\"amount\": 2" +
                "}," +
                "\"credit_order_list\": [" +
                "{" +
                "\"credit_orderid\": \"CREDIT_ORDERID_1\"," +
                "\"unit_price\": 100000," +
                "\"num\": \"  \"" +
                "}," +
                "{" +
                "\"credit_orderid\": \"CREDIT_ORDERID_2\"," +
                "\"unit_price\": 90000," +
                "\"num\": 2" +
                "}" +
                "]," +
                "\"buyer_corpid\": \"wwfedd7e5292d63a35\"," +
                "\"buyer_userid\": \"zhangsan\"," +
                "\"product_id\": \"xxxxxxxxxxx\"," +
                "\"product_name\": \"xxxxxxxxxxxxx\"," +
                "\"product_detail\": \"xxxxxxxxxxxx\"," +
                "\"unit_name\": \"台\"," +
                "\"remark\":  \"备注\"," +
                "\"sign\":  \"wiHKgIQSoggv4Mf5e0jJJNOLWKmMBSi16AWYo6tTaJU=\"" +
                "}";

        encryptData(jsonString);
    }

    /**
     * 系统对接加密方法
     * @param jsonString
     */
    public static String encryptData(String jsonString) {
        linkedList.clear();
        // 解析 JSON 字符串为 JsonObject
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        transitionJsonObject(jsonObject);
        //header
//        Map<String, String> headerMap = new HashMap<>();
//        headerMap.put("appId","2088101568338364");
//        headerMap.put("timestamp",System.currentTimeMillis()+"");
//        headerMap.put("nonceStr", generateNonceStr());
//        headerMap.put("signType","SHA256");
        //添加header
//        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            linkedList.add(new KeyValuePair(key, value));
//        }

        //参数名ASCII码从小到大排序（字母升序排序），如果遇到相同字符则按照第二个字符的键值 ASCII 码递增排序
        String joinStr =sortAndJoin(linkedList,"&", new String[]{"sign","signType"});
        System.out.println("拼接后的参数:"+joinStr);
        //HMAC-SHA256 进行sign
        String signStr = signStr(joinStr);
        return signStr;
    }

    static LinkedList<KeyValuePair> linkedList = new LinkedList<>();
    private static void  transitionJsonObject(JsonObject jsonObject) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();
            if (value.isJsonObject()) {
                transitionJsonObject(value.getAsJsonObject());
            } else if (value.isJsonArray()) {
                sortJsonArray(value.getAsJsonArray());
            }else{
                System.out.println("parameter add  "+key + ": " + value.getAsString());
                linkedList.add(new KeyValuePair(key, value.getAsString()));
            }
        }
    }


    private static void sortJsonArray(JsonArray jsonArray) {
        for (JsonElement element : jsonArray) {
            if (element.isJsonObject()) {
                transitionJsonObject(element.getAsJsonObject());
            } else if (element.isJsonArray()) {
                sortJsonArray(element.getAsJsonArray());
            }
        }
    }

    /**
     * 拼接字符
     * @param list 参数列表
     * @param delimiter 拼接字符串
     * @param strings //需要排除的key
     * @return
     */
    private static String sortAndJoin(LinkedList<KeyValuePair> list, String delimiter,String[] strings) {
        list.sort(Comparator.comparing(kv -> kv.key));
        StringBuilder result = new StringBuilder();
        for (KeyValuePair valuePair : list) {
            if(Arrays.asList(strings).contains(valuePair.key) || valuePair.value.trim().isEmpty()){
                continue;
            }
            if (result.length() > 0) {
                result.append(delimiter);
            }
            result.append(valuePair.key).append("=").append(valuePair.value);
        }
        return result.toString();
    }

    /**
     * 生成随机的 nonce_str
     */
    private static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * HMAC-SHA256签名
     * @param sign
     * @return
     */
    private static String signStr(String sign){
        try {
            // 创建HMAC-SHA256的Mac对象
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            // 使用密钥初始化Mac对象
            hmacSha256.init(new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            // 计算HMAC-SHA256值
            byte[] hmacBytes = hmacSha256.doFinal(sign.getBytes());
            // 将结果转换为Base64编码
            String signature = Base64.getEncoder().encodeToString(hmacBytes);
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}


