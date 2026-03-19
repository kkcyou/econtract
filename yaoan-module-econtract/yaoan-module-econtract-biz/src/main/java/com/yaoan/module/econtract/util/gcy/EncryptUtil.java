package com.yaoan.module.econtract.util.gcy;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
@Slf4j
public class EncryptUtil {
    //private static final String desKey = "expertdataextract743984";

    private static final Charset charset = Charset.forName("UTF8");

     public static void main(String args[]) {
         String desKey = "expertdataextract743984";
        String content = "卜明辉iuA23lNzcWDI+ioHGRGBzUraBP87RozXyJtzcj0cVEfCaWqiZPhqTemGQGUtyQnI4JK+AZ2a+qBKVcQyVVjqBH+yT6djB3vArMxDT0XQVjwdvERgt/Z3J+6+Vud4A89dl7QgmBfCusbwsVocvUMT3PCRkZh0k+FIB5FQtsnXKT6pi9Ev0yvaJ9Jpckyg+4hjPoiuIIjF5KCtNzY4hjCN10hBqfSEIN5VfYBRY6OsQPc7GlskvYY5IJxemv3/DL4yYmOoZ37YYaDOLyFf1fPEV0tJkdzVv5Ja/uNgo1NYLdOf5fjt//WxIqCN+TLDzPpWx3ZevhHGeT87t+2zv5OJ3LuvQ2ixP8Ps1lnbZd4loX1ndKRvCCAXajRHFaFnXfLT34msJKfE/6wsGVMgfs4GaJ/l+O3/9bEiMhklpS8Yl8JFNzOuHkL2EZ/l+O3/9bEilNtuSOy0KhX5NgP/11/sDpxg4wyrM6S731pBRAUbaWAY9PPE7h3H0M+tbCszB8xwsUvtJfEV6R/kkWgQMesfoAocBsfK95tZCUVWodKcvRg69WTO10/0xoWxeSuu3pHGHyp4bFZi2Fl0A6wS91jYSq/MTo+OGN8d8v71HsSi+w/27fiIWURV07gUJUEtMkePO7fts7+TidyJ5ZNGO8M93AcKoYls8LQZjV1xTYtEitOu9sBZhaHgm7pKeyXnjls/5RBSMShyfrJBt26XF8cKB1Yvbej3Fo6xPQp80AL0ynLPrWwrMwfMcLFL7SXxFekf5JFoEDHrH6DlKPqyqehQYwlFVqHSnL0YOvVkztdP9Mb2GvJfXa0zL/kMaSGadSvhdAOsEvdY2Ep8jaVVIzJUXPL+9R7EovsP9u34iFlEVdO4FCVBLTJHjzu37bO/k4ncieWTRjvDPdwHCqGJbPC0GY1dcU2LRIrTrvbAWYWh4Ju6Snsl545bP+UQUjEocn6yQbdulxfHCgdWL23o9xaOsZ3idjFe5FfLHbxEYLf2dydJSG38TgABE3X2gma9Zdn3tNj479pOpuPrb6Ufczchrx63Xvz1cziwYDxJhCLPl81yFIgxDm1Idm+kBRJ11qAbbav/bdBeas0iKyM4XTeNXwGJtm8YyOhYRpeHmh5FBqK1HRtoAuRHEZjHLCJLKABQJhMO8HW5SRP2EoTQopYtVLBR3kxeQAgSRAfzjmTFTPNRqGb5mq567WyFoivIHqS2vf2epKqzkOtV/Sb47+3s0xto8UjRA2QDTz81sHU+AyTJUgajeeWjoILIXDkpPjQn+9xXaRtb2/qjsmUjfciyHNKPT0MjGSk71TmVI3j3oq50mOOymYjqmNLeQRoiyEcpKDjOLaUbKQ7nD/dDX5cuEypCfqacw5rL0Cr2PnUyMRuClqiSu4NGuTu0JCiR1W495YdOFFxyyw9tQhTTFdfn9moJKDj6OGvyzt26ygRmvkFPjXtfdhqxFeFIpW0eZreUS/2YpiQsn8ThJONpx3EINgNI3ukARKLnCW9UlW7pI+uQcY3rl7wfKi7DfiUOvIkdsRNOUdrYaL3vKfPVnGii/onlk0Y7wz3cIQQqHDw08ym7OwkF5NTWI0pVxDJVWOoE8DRLfBlxDEfEU4p2x6nbUDDJ4N5MDIJX5EDhMgNHuRJ+f7RfIhvHWHo4Hs3ewadvvqITIdiYUY1HlfwnLBqHTAVuOkCj+XRIBCDXGjP86DDLjB+XxlNEsFOgEE9arHqQ59WLfeA37X/zREe67txB8mrfGFBqgX0TTd/8YjrObMCHARDYOlTOrkV0RSS2RGKduBQlQS0yR480KO5R/Qk3um1AUIp5RXDmj00Ny/0m+8humFRAR7dU0ouroDCnMPKxCUVWodKcvRjCdaCrl+aozrCr8JaIcbdJA0je6QBEoufRdQnhuWMt2glFVqHSnL0Y8yW9a8jw9KikkvmUl9IIz9sD0iYvyf33jXAaWCOXPhqq2zZ5Rta5jlpsHM5XqlQwqWgo5gQlxUg=";
        String test = "iuA23lNzcWDI+ioHGRGBzUraBP87RozXyJtzcj0cVEfCaWqiZPhqTXGHp3lhRWgi4JK+AZ2a+qBKVcQyVVjqBH+yT6djB3vAaDhag0uWogsdvERgt%2FZ3J+6+Vud4A89dl7QgmBfCusbwsVocvUMT3PCRkZh0k+FIB5FQtsnXKT6pi9Ev0yvaJ9Jpckyg+4hjPoiuIIjF5KCtNzY4hjCN10hBqfSEIN5VfYBRY6OsQPc7GlskvYY5IJxemv3%2FDL4yYmOoZ37YYaDOLyFf1fPEV0tJkdzVv5JavjxMTIYG10af5fjt%2F%2FWxIqCN+TLDzPpWx3ZevhHGeT87t+2zv5OJ3LuvQ2ixP8Ps1lnbZd4loX2AtOMir1yxDVUcUiXF8CbFn+X47f%2F1sSLW164lQsKCUt1%2FMbPH6ZA91tWUZ0K%2FmUOBsjywbLxDyQ8TqJkMs%2FbIdZuEMam8bizeRZlVA25oVGajloajPdSrr3nYw0GUTrGUOT+DbhpDmWJK0rnPiJ%2FIMqjTRyxoBYEG1XM4XYUPdJ55JzKdmWCt9N2xuEKfvlqXHIyIu8mU3eNrQS3ltIVk%2FQPB7PQ+FP8+0I%2FT7AOtUdoj7%2FN5ag2am65ZNw2wWB%2FkmkGIlbmsoRokVrZXmGtGGWfCjfUvnh806b1P+%2FRdmJiFmbWOVLx2MkZzExpqWOS4FCVBLTJHjzu37bO%2Fk4ncdP37siHzHIIlZh2hhAKte0IYLf969SB66WA0LgMghN+LLcwLARCDNGDlp+ZtccQWb3RtwgWhfM9vpAUSddagG6PUecOrNraUUDMe1eROyHYdvERgt%2FZ3J6%2FRSu4ERdMFQNIqAvMzzJIqu9DwZVpoMTkRMbKYOaLMfr8eYtysLpb3v7b+Yst5z64%2F34j8+IGAPmiS9MRoEmS2it50dEl8C64VKr9mkWvvVDYGedxjf6uUvQZW5F1+Ih07rcPoLFujW2vJqbkI6pMG1XM4XYUPdGxNX63o4uTpBwqhiWzwtBl2cbatl7g1b6LWi2mrCC8Nn+X47f%2F1sSINddiLnxbKAQfNd4dd7aMNRXGYHxriyOonDZdT0Yt5eQwltdtRG7+O9TwxpSb7u3KH%2FmNy+pJUN3fuVfbNhjjMSbG5RdLoJPkqgyIHM2at9hCnpBXAPPAgclxPMGZipwqf5fjt%2F%2FWxItbXriVCwoJS3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJDxOomQyz9sh1m4QxqbxuLN5FmVUDbmhUZqOWhqM91KuvedjDQZROsZQ5P4NuGkOZYkrSuc+In8gyqNNHLGgFgQbVczhdhQ90UWPJ9aNOynX03bG4Qp++WpccjIi7yZTd42tBLeW0hWQ8Jti4CfkYCO8jXZ++EmcqjvA2Q3oaAkaCJR9uLGYLe1PFrqlpcFg4hoJmbLMucslFsfcv7rLWXlQvqOh+UVPrsM2ZH5131Pif5fjt%2F%2FWxIsWOpx2UeLMmZ4PuBHn1h+zxnq7oRogaRsLrYMa%2F8cTbivJAc41Ivqq084IRxxWEe3MYK7seq34cf%2FK3ua7jCYiUuQeltS9hHQGJtm8YyOhY876HVbgAHfqAwosezF12c2GysoF6QUnrb6QFEnXWoBvvntnS5r5VhJho22g%2FNZ9A5JpBiJW5rKEaJFa2V5hrRhlnwo31L54fNOm9T%2Fv0XZiYhZm1jlS8djJGcxMaaljkuBQlQS0yR487t+2zv5OJ3HT9+7Ih8xyCJWYdoYQCrXuJJTogZVxIDxCXrq0i0g7bsUvtJfEV6R+2xZKIh3NGhvVE%2FP9YLwvuKLlieXC4ajohULeUiee5GjRHFaFnXfLT8Z6u6EaIGkZlcUZPEnUSaoqgzvB9XTcDjVZkgMbfYGMkQNEP3cZNwIT72DSGB7uiB0pLfZvoRCsEAruoaaC+Zo+EQQTT7BaBdFmhhzcBH3x7yQp%2FTBCJ7i9OMafug80ErHrlfKXl+82f5fjt%2F%2FWxItbXriVCwoJS3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJDxOomQyz9sh1m4QxqbxuLN5FmVUDbmhUZqOWhqM91KuvedjDQZROsZQ5P4NuGkOZYkrSuc+In8gyqNNHLGgFgQbVczhdhQ90zgfuoCV2E2j03bG4Qp++WpccjIi7yZTd42tBLeW0hWQ8Jti4CfkYCO8jXZ++EmcqjvA2Q3oaAkaCJR9uLGYLe1PFrqlpcFg4hoJmbLMucslFsfcv7rLWXlQvqOh+UVPrsM2ZH5131Pif5fjt%2F%2FWxIsWOpx2UeLMmZ4PuBHn1h+zxnq7oRogaRsLrYMa%2F8cTbivJAc41Ivqq084IRxxWEe3MYK7seq34cRtM%2FI%2Fs4zuCUuQeltS9hHQGJtm8YyOhY876HVbgAHfqAwosezF12c2GysoF6QUnrb6QFEnXWoBvvntnS5r5VhJho22g%2FNZ9A5JpBiJW5rKEaJFa2V5hrRhlnwo31L54fNOm9T%2Fv0XZiYhZm1jlS8djJGcxMaaljkuBQlQS0yR487t+2zv5OJ3HT9+7Ih8xyCJWYdoYQCrXuJJTogZVxIDxCXrq0i0g7bsUvtJfEV6R+2xZKIh3NGhkbt3+xY8NsKKLlieXC4ajrVpAW+nQZVTzRHFaFnXfLT8Z6u6EaIGkZlcUZPEnUSanG0uGY3VQ1bFUA2cyrSs2fJF84ZHAbeGKrgRk%2FhyjvZs5cMKc87xhM+MYsVNjOVggc+o3S9ebBBzkm4ZKQ8fSkHCqGJbPC0GY1dcU2LRIrTrTc2OIYwjdcHCqGJbPC0GUi4sGQoDn0kh4sg6XO6qF%2FiPPfmwrdc4nNUjETPnvqZNA%2FdXqyeVmLhhdVol3pHWcibc3I9HFRHOremrgT1X+OfcYjtuI7YDUpVxDJVWOoECdbZuKQaX+ueBqCRDt11OPjF9oUJTGQNHbxEYLf2dyev0UruBEXTBWwpJmTvOc1G5S1NwLJdNnf9F9GapowNlyi1moQjsMpdVi9t6PcWjrHUsCICbqdbCBCXrq0i0g7b3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJ77vdNS%2FxhJineWk+i5hqBUhCs9gylbJQcw3Ocdc7bASUYekRrQSkDnMZaJmSxEfMHbxEYLf2dyfEiANXoLBTrpr+FsuB33WxoqP6hr4KbNiO8DZDehoCRgE7Yep8H9ISU8WuqWlwWDiGgmZssy5yyUWx9y%2FustZeVC+o6H5RU+uwzZkfnXfU+J%2Fl+O3%2F9bEixY6nHZR4syZng+4EefWH7PGeruhGiBpGwutgxr%2FxxNuK8kBzjUi+qrTzghHHFYR7cxgrux6rfhznnH2p%2FaAFmZS5B6W1L2EdAYm2bxjI6FjzvodVuAAd+oDCix7MXXZz8sK1DA2lrqBsBSIieK2C6XQDrBL3WNhKr8xOj44Y3x3y%2FvUexKL7D%2Fbt+IhZRFXTuBQlQS0yR4%2Fm23qfoSXZJFzjCUjACUITO7fts7+TidyJ5ZNGO8M93AcKoYls8LQZjV1xTYtEitOu9sBZhaHgm0v%2FelONoBFOpf1gLlAawwXBgondGinm22IawfEbn+LEdUy6%2FtzE2POuFSq%2FZpFr7x9UEEEX0CqOiT90JNg51dE0HmOFbHzwvZfRQTGF53A4u4E3JHsh+lRGF2uw7aHuOIfMnVtQEWD2ZyAXlMePzRyVvjJySl94IcCGOeCAX3lbkAdtjuZ+JXyfLRG%2FCnFP%2F4778j0xFMRIKLlieXC4ajruvlbneAPPXZuTRJtepOtDwyTdU58SEUZurCFDYnWRsqNIcqBJuHpjnaaDPtilzgBOgFH815CMQqd5aT6LmGoFSEKz2DKVslBzDc5x1ztsBD%2Fok9zLRqj0cxlomZLER8wdvERgt%2FZ3J8SIA1egsFOu37HLccTde18gCcfEJKOgH3Mt0+yv1xnI3eXwbR4NXKtDV5kG9UBSM1T3pmMloJlYukp7JeeOWz%2FlEFIxKHJ+soXwp2WnR4+bzM1NKywf%2FdREClljGGvIMDQeY4VsfPC9TeztZxh0y%2FFspFq26LxZc+GF1WiXekdZyJtzcj0cVEc6t6auBPVf48eBlRKiM6DmSlXEMlVY6gQJ1tm4pBpf654GoJEO3XU4i1plXUs3lFGO8DZDehoCRoIlH24sZgt7U8WuqWlwWDiGgmZssy5yyUWx9y%2FustZeVC+o6H5RU+uwzZkfnXfU+J%2Fl+O3%2F9bEixY6nHZR4syZng+4EefWH7PGeruhGiBpGwutgxr%2FxxNuiIh5uruKX8iAJx8Qko6AfsQUkkuPJEKLIZvvmwUPYzCfydr1pV6XWieKWeTXTGGlotBiwwVGIp9DAV7n0ToKqvqITIdiYUY0dvERgt%2FZ3J61S8dl9p4eKjZmH2Nx8K5KXHIyIu8mU3XPHkbPWiBZAmuS32IlSihqMMIvnAUEM3S25g4win9390SH2A1GwWKiGVKBXiRPeZCMhnME6JFpqxp105RWPBO+Q4wgh2iq2jFtEkYhDgFul0S41K64MxuprcIEAgscPo0PVMiA4TM7wpC758KbZ5kr247FFNFzV40U%2FOUMhER5nOLd07KIM7f+3itm4GmMbscELHv+QOaNIidIaDXSwF1iyPv+kUDL8DVarOqbY8ZXPs59Un0H1FIG+xV3hTKBOkcRPnrg%2FXTkH5iPdfjlryneN3d84cc9m0aXLCjzugNdLn7JDjRB1SRvbpv3WS8P2kvQDjytKSHeiLChokE7y2JTEDlW98+iWLAwtpzsde0fG9Q5f2ddNHKUaATETFdU1BfuFkHhePJeWvhESmGK48pIPumsCLoP9yd1PEhd+5JjHhiX9j%2Favx7TeZnXqctTzzz+eIOcwesYjAmIn3pB%2FcS2YpWoqzJCTRAkB0ObP1S0zl+7ND5dCW7vDLe1+Lp%2FYMPz0tHsUfqmPQXR4GjUiWi4akdNDGJTBIPNER7ru3EHyAYm2bxjI6Fi79KrCW7zdDrgUJUEtMkeP59WLfeA37X+M2h2jO1MX0Ldkg7Iq7svIpQoFi7tHCeG+ohMh2JhRjYpixDO2fIVf98q0+LdryscuB6AorEaq9E%2F7WyrAodpI7fV+OSa417MOeQqEcX4kdBDP+feMsrG8AYm2bxjI6Fiv5KsNWiUdH6SS+ZSX0gjPA0je6QBEoudOC6Fqsr0EbwlFVqHSnL0Yfle35t5%2FePl+nD4V%2F%2FJO9P7QBUVJ7a5Ki6ugMKcw8rFkvuVsqioOYZfKo5DHcMP+e7YlT54X8pDd4HWFemfR1xLSDniqrzYG=";
        try {
            /*
            String encrypt = encryptStringToBase64(content,desKey);
            log.info("加密结果：" + encrypt);
            log.info("解密结果：" + decryptStringToBase64(encrypt,desKey));
            */
           // String encrypt2 = encryptString(content,"12345678");

            //log.info("加密结果：" + encrypt2);

            //String encrypt = encryptStringToBase64(content,desKey);
            //log.info("加密结果：" + encrypt);

            //log.info("解密结果：" + decryptString(test, "12345678"));
            String ss="0ij3i5oHsWsohq1dl1/d/UNpY4gINSAjCL4+YcYdAmH+s9wRdV8ADsxcnBBbqNBoDwl2ztYC8DVZPVNve8EFLw3EjX3tot2qVabaTsxmVbGVxTG4SVGN3UhB1m+KOaEwBSntbuEplZv8y1x2iRRj3nhxA/4aNAMH";
            String s = decryptString(ss, "ejujJrHj");
            log.info(s);
            //log.info("test----{}" + de);
        } catch (Exception e) {
            log.error("当前方法名:{},异常信息:", Thread.currentThread().getStackTrace()[1].getMethodName(), e);;
            e.printStackTrace();
        }
    }
/*    public static void main(String args[]) {
        String content = "iuA23lNzcWDI+ioHGRGBzUraBP87RozXyJtzcj0cVEfCaWqiZPhqTemGQGUtyQnI4JK+AZ2a+qBKVcQyVVjqBH+yT6djB3vArMxDT0XQVjwdvERgt/Z3J+6+Vud4A89dl7QgmBfCusbwsVocvUMT3PCRkZh0k+FIB5FQtsnXKT6pi9Ev0yvaJ9Jpckyg+4hjPoiuIIjF5KCtNzY4hjCN10hBqfSEIN5VfYBRY6OsQPc7GlskvYY5IJxemv3/DL4yYmOoZ37YYaDOLyFf1fPEV0tJkdzVv5Ja/uNgo1NYLdOf5fjt//WxIqCN+TLDzPpWx3ZevhHGeT87t+2zv5OJ3LuvQ2ixP8Ps1lnbZd4loX1ndKRvCCAXajRHFaFnXfLT34msJKfE/6wsGVMgfs4GaJ/l+O3/9bEiMhklpS8Yl8JFNzOuHkL2EZ/l+O3/9bEilNtuSOy0KhX5NgP/11/sDpxg4wyrM6S731pBRAUbaWAY9PPE7h3H0M+tbCszB8xwsUvtJfEV6R/kkWgQMesfoAocBsfK95tZCUVWodKcvRg69WTO10/0xoWxeSuu3pHGHyp4bFZi2Fl0A6wS91jYSq/MTo+OGN8d8v71HsSi+w/27fiIWURV07gUJUEtMkePO7fts7+TidyJ5ZNGO8M93AcKoYls8LQZjV1xTYtEitOu9sBZhaHgm7pKeyXnjls/5RBSMShyfrJBt26XF8cKB1Yvbej3Fo6xPQp80AL0ynLPrWwrMwfMcLFL7SXxFekf5JFoEDHrH6DlKPqyqehQYwlFVqHSnL0YOvVkztdP9Mb2GvJfXa0zL/kMaSGadSvhdAOsEvdY2Ep8jaVVIzJUXPL+9R7EovsP9u34iFlEVdO4FCVBLTJHjzu37bO/k4ncieWTRjvDPdwHCqGJbPC0GY1dcU2LRIrTrvbAWYWh4Ju6Snsl545bP+UQUjEocn6yQbdulxfHCgdWL23o9xaOsZ3idjFe5FfLHbxEYLf2dydJSG38TgABE3X2gma9Zdn3tNj479pOpuPrb6Ufczchrx63Xvz1cziwYDxJhCLPl81yFIgxDm1Idm+kBRJ11qAbbav/bdBeas0iKyM4XTeNXwGJtm8YyOhYRpeHmh5FBqK1HRtoAuRHEZjHLCJLKABQJhMO8HW5SRP2EoTQopYtVLBR3kxeQAgSRAfzjmTFTPNRqGb5mq567WyFoivIHqS2vf2epKqzkOtV/Sb47+3s0xto8UjRA2QDTz81sHU+AyTJUgajeeWjoILIXDkpPjQn+9xXaRtb2/qjsmUjfciyHNKPT0MjGSk71TmVI3j3oq50mOOymYjqmNLeQRoiyEcpKDjOLaUbKQ7nD/dDX5cuEypCfqacw5rL0Cr2PnUyMRuClqiSu4NGuTu0JCiR1W495YdOFFxyyw9tQhTTFdfn9moJKDj6OGvyzt26ygRmvkFPjXtfdhqxFeFIpW0eZreUS/2YpiQsn8ThJONpx3EINgNI3ukARKLnCW9UlW7pI+uQcY3rl7wfKi7DfiUOvIkdsRNOUdrYaL3vKfPVnGii/onlk0Y7wz3cIQQqHDw08ym7OwkF5NTWI0pVxDJVWOoE8DRLfBlxDEfEU4p2x6nbUDDJ4N5MDIJX5EDhMgNHuRJ+f7RfIhvHWHo4Hs3ewadvvqITIdiYUY1HlfwnLBqHTAVuOkCj+XRIBCDXGjP86DDLjB+XxlNEsFOgEE9arHqQ59WLfeA37X/zREe67txB8mrfGFBqgX0TTd/8YjrObMCHARDYOlTOrkV0RSS2RGKduBQlQS0yR480KO5R/Qk3um1AUIp5RXDmj00Ny/0m+8humFRAR7dU0ouroDCnMPKxCUVWodKcvRjCdaCrl+aozrCr8JaIcbdJA0je6QBEoufRdQnhuWMt2glFVqHSnL0Y8yW9a8jw9KikkvmUl9IIz9sD0iYvyf33jXAaWCOXPhqq2zZ5Rta5jlpsHM5XqlQwqWgo5gQlxUg=";
        String test = "iuA23lNzcWDI+ioHGRGBzUraBP87RozXyJtzcj0cVEfCaWqiZPhqTXGHp3lhRWgi4JK+AZ2a+qBKVcQyVVjqBH+yT6djB3vAaDhag0uWogsdvERgt%2FZ3J+6+Vud4A89dl7QgmBfCusbwsVocvUMT3PCRkZh0k+FIB5FQtsnXKT6pi9Ev0yvaJ9Jpckyg+4hjPoiuIIjF5KCtNzY4hjCN10hBqfSEIN5VfYBRY6OsQPc7GlskvYY5IJxemv3%2FDL4yYmOoZ37YYaDOLyFf1fPEV0tJkdzVv5JavjxMTIYG10af5fjt%2F%2FWxIqCN+TLDzPpWx3ZevhHGeT87t+2zv5OJ3LuvQ2ixP8Ps1lnbZd4loX2AtOMir1yxDVUcUiXF8CbFn+X47f%2F1sSLW164lQsKCUt1%2FMbPH6ZA91tWUZ0K%2FmUOBsjywbLxDyQ8TqJkMs%2FbIdZuEMam8bizeRZlVA25oVGajloajPdSrr3nYw0GUTrGUOT+DbhpDmWJK0rnPiJ%2FIMqjTRyxoBYEG1XM4XYUPdJ55JzKdmWCt9N2xuEKfvlqXHIyIu8mU3eNrQS3ltIVk%2FQPB7PQ+FP8+0I%2FT7AOtUdoj7%2FN5ag2am65ZNw2wWB%2FkmkGIlbmsoRokVrZXmGtGGWfCjfUvnh806b1P+%2FRdmJiFmbWOVLx2MkZzExpqWOS4FCVBLTJHjzu37bO%2Fk4ncdP37siHzHIIlZh2hhAKte0IYLf969SB66WA0LgMghN+LLcwLARCDNGDlp+ZtccQWb3RtwgWhfM9vpAUSddagG6PUecOrNraUUDMe1eROyHYdvERgt%2FZ3J6%2FRSu4ERdMFQNIqAvMzzJIqu9DwZVpoMTkRMbKYOaLMfr8eYtysLpb3v7b+Yst5z64%2F34j8+IGAPmiS9MRoEmS2it50dEl8C64VKr9mkWvvVDYGedxjf6uUvQZW5F1+Ih07rcPoLFujW2vJqbkI6pMG1XM4XYUPdGxNX63o4uTpBwqhiWzwtBl2cbatl7g1b6LWi2mrCC8Nn+X47f%2F1sSINddiLnxbKAQfNd4dd7aMNRXGYHxriyOonDZdT0Yt5eQwltdtRG7+O9TwxpSb7u3KH%2FmNy+pJUN3fuVfbNhjjMSbG5RdLoJPkqgyIHM2at9hCnpBXAPPAgclxPMGZipwqf5fjt%2F%2FWxItbXriVCwoJS3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJDxOomQyz9sh1m4QxqbxuLN5FmVUDbmhUZqOWhqM91KuvedjDQZROsZQ5P4NuGkOZYkrSuc+In8gyqNNHLGgFgQbVczhdhQ90UWPJ9aNOynX03bG4Qp++WpccjIi7yZTd42tBLeW0hWQ8Jti4CfkYCO8jXZ++EmcqjvA2Q3oaAkaCJR9uLGYLe1PFrqlpcFg4hoJmbLMucslFsfcv7rLWXlQvqOh+UVPrsM2ZH5131Pif5fjt%2F%2FWxIsWOpx2UeLMmZ4PuBHn1h+zxnq7oRogaRsLrYMa%2F8cTbivJAc41Ivqq084IRxxWEe3MYK7seq34cf%2FK3ua7jCYiUuQeltS9hHQGJtm8YyOhY876HVbgAHfqAwosezF12c2GysoF6QUnrb6QFEnXWoBvvntnS5r5VhJho22g%2FNZ9A5JpBiJW5rKEaJFa2V5hrRhlnwo31L54fNOm9T%2Fv0XZiYhZm1jlS8djJGcxMaaljkuBQlQS0yR487t+2zv5OJ3HT9+7Ih8xyCJWYdoYQCrXuJJTogZVxIDxCXrq0i0g7bsUvtJfEV6R+2xZKIh3NGhvVE%2FP9YLwvuKLlieXC4ajohULeUiee5GjRHFaFnXfLT8Z6u6EaIGkZlcUZPEnUSaoqgzvB9XTcDjVZkgMbfYGMkQNEP3cZNwIT72DSGB7uiB0pLfZvoRCsEAruoaaC+Zo+EQQTT7BaBdFmhhzcBH3x7yQp%2FTBCJ7i9OMafug80ErHrlfKXl+82f5fjt%2F%2FWxItbXriVCwoJS3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJDxOomQyz9sh1m4QxqbxuLN5FmVUDbmhUZqOWhqM91KuvedjDQZROsZQ5P4NuGkOZYkrSuc+In8gyqNNHLGgFgQbVczhdhQ90zgfuoCV2E2j03bG4Qp++WpccjIi7yZTd42tBLeW0hWQ8Jti4CfkYCO8jXZ++EmcqjvA2Q3oaAkaCJR9uLGYLe1PFrqlpcFg4hoJmbLMucslFsfcv7rLWXlQvqOh+UVPrsM2ZH5131Pif5fjt%2F%2FWxIsWOpx2UeLMmZ4PuBHn1h+zxnq7oRogaRsLrYMa%2F8cTbivJAc41Ivqq084IRxxWEe3MYK7seq34cRtM%2FI%2Fs4zuCUuQeltS9hHQGJtm8YyOhY876HVbgAHfqAwosezF12c2GysoF6QUnrb6QFEnXWoBvvntnS5r5VhJho22g%2FNZ9A5JpBiJW5rKEaJFa2V5hrRhlnwo31L54fNOm9T%2Fv0XZiYhZm1jlS8djJGcxMaaljkuBQlQS0yR487t+2zv5OJ3HT9+7Ih8xyCJWYdoYQCrXuJJTogZVxIDxCXrq0i0g7bsUvtJfEV6R+2xZKIh3NGhkbt3+xY8NsKKLlieXC4ajrVpAW+nQZVTzRHFaFnXfLT8Z6u6EaIGkZlcUZPEnUSanG0uGY3VQ1bFUA2cyrSs2fJF84ZHAbeGKrgRk%2FhyjvZs5cMKc87xhM+MYsVNjOVggc+o3S9ebBBzkm4ZKQ8fSkHCqGJbPC0GY1dcU2LRIrTrTc2OIYwjdcHCqGJbPC0GUi4sGQoDn0kh4sg6XO6qF%2FiPPfmwrdc4nNUjETPnvqZNA%2FdXqyeVmLhhdVol3pHWcibc3I9HFRHOremrgT1X+OfcYjtuI7YDUpVxDJVWOoECdbZuKQaX+ueBqCRDt11OPjF9oUJTGQNHbxEYLf2dyev0UruBEXTBWwpJmTvOc1G5S1NwLJdNnf9F9GapowNlyi1moQjsMpdVi9t6PcWjrHUsCICbqdbCBCXrq0i0g7b3X8xs8fpkD3W1ZRnQr+ZQ4GyPLBsvEPJ77vdNS%2FxhJineWk+i5hqBUhCs9gylbJQcw3Ocdc7bASUYekRrQSkDnMZaJmSxEfMHbxEYLf2dyfEiANXoLBTrpr+FsuB33WxoqP6hr4KbNiO8DZDehoCRgE7Yep8H9ISU8WuqWlwWDiGgmZssy5yyUWx9y%2FustZeVC+o6H5RU+uwzZkfnXfU+J%2Fl+O3%2F9bEixY6nHZR4syZng+4EefWH7PGeruhGiBpGwutgxr%2FxxNuK8kBzjUi+qrTzghHHFYR7cxgrux6rfhznnH2p%2FaAFmZS5B6W1L2EdAYm2bxjI6FjzvodVuAAd+oDCix7MXXZz8sK1DA2lrqBsBSIieK2C6XQDrBL3WNhKr8xOj44Y3x3y%2FvUexKL7D%2Fbt+IhZRFXTuBQlQS0yR4%2Fm23qfoSXZJFzjCUjACUITO7fts7+TidyJ5ZNGO8M93AcKoYls8LQZjV1xTYtEitOu9sBZhaHgm0v%2FelONoBFOpf1gLlAawwXBgondGinm22IawfEbn+LEdUy6%2FtzE2POuFSq%2FZpFr7x9UEEEX0CqOiT90JNg51dE0HmOFbHzwvZfRQTGF53A4u4E3JHsh+lRGF2uw7aHuOIfMnVtQEWD2ZyAXlMePzRyVvjJySl94IcCGOeCAX3lbkAdtjuZ+JXyfLRG%2FCnFP%2F4778j0xFMRIKLlieXC4ajruvlbneAPPXZuTRJtepOtDwyTdU58SEUZurCFDYnWRsqNIcqBJuHpjnaaDPtilzgBOgFH815CMQqd5aT6LmGoFSEKz2DKVslBzDc5x1ztsBD%2Fok9zLRqj0cxlomZLER8wdvERgt%2FZ3J8SIA1egsFOu37HLccTde18gCcfEJKOgH3Mt0+yv1xnI3eXwbR4NXKtDV5kG9UBSM1T3pmMloJlYukp7JeeOWz%2FlEFIxKHJ+soXwp2WnR4+bzM1NKywf%2FdREClljGGvIMDQeY4VsfPC9TeztZxh0y%2FFspFq26LxZc+GF1WiXekdZyJtzcj0cVEc6t6auBPVf48eBlRKiM6DmSlXEMlVY6gQJ1tm4pBpf654GoJEO3XU4i1plXUs3lFGO8DZDehoCRoIlH24sZgt7U8WuqWlwWDiGgmZssy5yyUWx9y%2FustZeVC+o6H5RU+uwzZkfnXfU+J%2Fl+O3%2F9bEixY6nHZR4syZng+4EefWH7PGeruhGiBpGwutgxr%2FxxNuiIh5uruKX8iAJx8Qko6AfsQUkkuPJEKLIZvvmwUPYzCfydr1pV6XWieKWeTXTGGlotBiwwVGIp9DAV7n0ToKqvqITIdiYUY0dvERgt%2FZ3J61S8dl9p4eKjZmH2Nx8K5KXHIyIu8mU3XPHkbPWiBZAmuS32IlSihqMMIvnAUEM3S25g4win9390SH2A1GwWKiGVKBXiRPeZCMhnME6JFpqxp105RWPBO+Q4wgh2iq2jFtEkYhDgFul0S41K64MxuprcIEAgscPo0PVMiA4TM7wpC758KbZ5kr247FFNFzV40U%2FOUMhER5nOLd07KIM7f+3itm4GmMbscELHv+QOaNIidIaDXSwF1iyPv+kUDL8DVarOqbY8ZXPs59Un0H1FIG+xV3hTKBOkcRPnrg%2FXTkH5iPdfjlryneN3d84cc9m0aXLCjzugNdLn7JDjRB1SRvbpv3WS8P2kvQDjytKSHeiLChokE7y2JTEDlW98+iWLAwtpzsde0fG9Q5f2ddNHKUaATETFdU1BfuFkHhePJeWvhESmGK48pIPumsCLoP9yd1PEhd+5JjHhiX9j%2Favx7TeZnXqctTzzz+eIOcwesYjAmIn3pB%2FcS2YpWoqzJCTRAkB0ObP1S0zl+7ND5dCW7vDLe1+Lp%2FYMPz0tHsUfqmPQXR4GjUiWi4akdNDGJTBIPNER7ru3EHyAYm2bxjI6Fi79KrCW7zdDrgUJUEtMkeP59WLfeA37X+M2h2jO1MX0Ldkg7Iq7svIpQoFi7tHCeG+ohMh2JhRjYpixDO2fIVf98q0+LdryscuB6AorEaq9E%2F7WyrAodpI7fV+OSa417MOeQqEcX4kdBDP+feMsrG8AYm2bxjI6Fiv5KsNWiUdH6SS+ZSX0gjPA0je6QBEoudOC6Fqsr0EbwlFVqHSnL0Yfle35t5%2FePl+nD4V%2F%2FJO9P7QBUVJ7a5Ki6ugMKcw8rFkvuVsqioOYZfKo5DHcMP+e7YlT54X8pDd4HWFemfR1xLSDniqrzYG=";
        try {
            //String encrypt = encryptStringToBase64(content);
            //String encrypt2 = encryptString(content,"12345678");
            String de = decryptString(content, "gpcms#credit#key%#");
            //log.info("加密结果：" + encrypt2);
            //log.info("解密结果：" + decryptStringToBase64(encrypt));
            log.info("解密结果：" + decryptString(test, "12345678"));
            log.info("test----{}" + de);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 加密字符串
     *
     * @param code 需要加密的字符串
     * @return 加密后的BASE64编码的字符串
     * @throws Exception
     */
    public static String encryptStringToBase64(String code,String desKey) throws Exception {
        byte[] keyByte = desKey.getBytes(charset);
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        DESKeySpec mDesKeySpec = new DESKeySpec(desKey.getBytes(charset));
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(mDesKeySpec);
        Cipher mCipher = Cipher.getInstance("DES");
        mCipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte encode[] = mCipher.doFinal(code.getBytes(charset));
        return new BASE64Encoder().encode(encode);

    }

    /**
     * 解密字符串
     *
     * @param code 需要解密的BASE64编码的密文
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public static String decryptStringToBase64(String code,String desKey) throws Exception {
        byte[] keyByte = desKey.getBytes(charset);
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        // 创建一个DESKeySpec对象
        DESKeySpec mDesKeySpec = new DESKeySpec(desKey.getBytes(charset));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(mDesKeySpec);
        Cipher mCipher = Cipher.getInstance("DES");
        mCipher.init(Cipher.DECRYPT_MODE, securekey);
        byte decode[] = mCipher.doFinal(new BASE64Decoder().decodeBuffer(code));
        return new String(decode, charset);
    }

    /**
     * 加密字符串
     *
     * @param code 需要加密的字符串
     * @return 加密后的BASE64编码的字符串
     * @throws Exception
     */
    public static String encryptString(String code, String desKey) throws Exception {
        byte[] keyByte = desKey.getBytes(charset);
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        DESKeySpec mDesKeySpec = new DESKeySpec(desKey.getBytes(charset));
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(mDesKeySpec);
        Cipher mCipher = Cipher.getInstance("DES");
        mCipher.init(Cipher.ENCRYPT_MODE, securekey);
        byte encode[] = mCipher.doFinal(code.getBytes(charset));
        return new BASE64Encoder().encode(encode);
    }

    /**
     * 解密字符串
     *
     * @param code 需要解密的BASE64编码的密文
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public static String decryptString(String code, String desKey) throws Exception {
        byte[] keyByte = desKey.getBytes(charset);
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        // 创建一个DESKeySpec对象
        DESKeySpec mDesKeySpec = new DESKeySpec(desKey.getBytes(charset));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(mDesKeySpec);
        Cipher mCipher = Cipher.getInstance("DES");
        mCipher.init(Cipher.DECRYPT_MODE, securekey);
        byte decode[] = mCipher.doFinal(new BASE64Decoder().decodeBuffer(code));
        return new String(decode, charset);
    }

    /**
     * 加密输入流，目前测试最大25M
     *
     * @param inputStream 需要加密的输入流
     * @param desKey      用于DES加密的密钥(目前测试使用的是56位字符串)
     * @return 加密后的输入流
     */
    public static InputStream encrypt(InputStream inputStream, String desKey) {
        byte[] keyByte = null;
        try {
            keyByte = desKey.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        Key key = new SecretKeySpec(byteTemp, "DES");
        Cipher mCipher;
        try {
            mCipher = Cipher.getInstance("DES");
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            log.info("DES开始加密！");
            CipherInputStream cis = new CipherInputStream(inputStream, mCipher);
            return cis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密输出流，目前测试最大25M
     *
     * @param filePath 需要解密的输出流
     * @param desKey   用于DES解密的密钥(目前测试使用的是56位字符串)
     * @return 解密后的输出流
     * @throws Exception
     */
    public static InputStream decrypt(String filePath, String desKey) throws Exception {
        FileInputStream keyFIS = null;
        byte[] keyByte = desKey.getBytes("utf-8");
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        Key key = new SecretKeySpec(byteTemp, "DES");
        Cipher cipherDecrypt;
        InputStream is = null;
        CipherOutputStream cos = null;
        try {
            log.info("开始解密");
            cipherDecrypt = Cipher.getInstance("DES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
            is = new FileInputStream(filePath);
            OutputStream out = new ByteArrayOutputStream();
            cos = new CipherOutputStream(out, cipherDecrypt);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
//			cos.close();
            // out.close();
//			is.close();
            log.info("解密完成");
            return outputStreamToInputStream(out);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                is.close();
            }
            if (null != cos) {
                cos.close();
            }
        }
        return null;
    }

    /**
     * outputStream转inputStream
     *
     * @param out
     * @return
     * @throws Exception
     */
    public static ByteArrayInputStream outputStreamToInputStream(OutputStream out) throws Exception {
        ByteArrayOutputStream baos;
        baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    /**
     * 解密
     *
     * @param s
     * @return
     */
    public static String decode(String s) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    /**
     * 加密
     *
     * @param s
     * @return
     */
    public static String encode(String s) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }
}
