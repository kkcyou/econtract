import java.io.*;
import java.util.Base64;
/**
 * 工作流流程变量的十六进制转换工具
 * SELECT ID_, NAME_, HEX(BYTES_) AS BYTES_HEX FROM act_ge_bytearray WHERE ID_ = 'b7becd78-5d35-11f0-aa0b-c2bfbee2b632';
 * */
public class JavaDeserializer {

    public static void main(String[] args) {
        String hexData = "ACED0005737200176A6176612E7574696C2E4C696E6B656448617368536574D86CD75A95DD2A1E020000787200116A6176612E7574696C2E48617368536574BA44859596B8B7340300007870770C000000103F400000000000047372000E6A6176612E6C616E672E4C6F6E673B8BE490CC8F23DF0200014A000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870000000000005A3197371007E0003000000000005A3047371007E0003000000000005A2FD7371007E0003000000000005A30F78";
        
        try {
            // 将十六进制字符串转换为字节数组
            byte[] bytes = hexStringToByteArray(hexData);
            
            // 反序列化对象
            Object obj = deserialize(bytes);
            
            // 打印对象信息
            System.out.println("反序列化结果: " + obj);
            System.out.println("对象类型: " + obj.getClass().getName());
            
            // 如果是集合类型，可以进一步处理
            if (obj instanceof java.util.Set) {
                java.util.Set<?> set = (java.util.Set<?>) obj;
                System.out.println("集合大小: " + set.size());
                System.out.println("集合内容: " + set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return ois.readObject();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}