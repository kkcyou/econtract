package com.yaoan.module.system.controller.admin.captcha;

import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.xingyuv.captcha.service.CaptchaService;
import com.yaoan.module.system.controller.admin.captcha.vo.Check;
import com.yaoan.module.system.util.ImgValidateCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 验证码")
@RestController("adminCaptchaController")
@RequestMapping("/system/captcha")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    @PostMapping({"/get"})
    @Operation(summary = "获得验证码")
    @PermitAll
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        assert request.getRemoteHost() != null;
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    @PostMapping("/check")
    @Operation(summary = "校验验证码")
    @PermitAll
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String ip = ServletUtils.getClientIP(request);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }
    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 生成图片验证码
     * @return
     */
    @PostMapping("/getImgCode")
    @PermitAll
    public CommonResult<Map<String, String>> getImgCode() {

        Map<String, String> result = new HashMap<>();

        try {
            // 获取 4位数验证码
            result= ImgValidateCodeUtil.getImgCodeBaseCode(4);
            // 将验证码存入redis 中（有效时长5分钟）
            cacheImgCode(result);
            // 移除 "imgCode" 键值对
            result.remove("imgCode");
        } catch (Exception e) {
            System.out.println(e);
        }
        return success(result);
    }

    /**
     * 校验图片验证码
     * @param check
     * @return
     */
    @PostMapping("/checkImgCode")
    @PermitAll
    public CommonResult<String> checkImgCode(@RequestBody Check check) {
        String cacheCode = redisTemplate.opsForValue().get(check.getImgCodeKey());
        if (null == cacheCode) {
            return success( "图片验证码已过期，请重新获取").setCode(555);
        }
        if (cacheCode.equals(check.getImgCode().toLowerCase())) {
            return success("验证码输入正确");
        }
        return success("验证码输入错误").setCode(555);

    }

    /**
     * 将验证码存入redis 中
     * @param result
     */
    public void cacheImgCode(Map<String, String> result) {
        String imgCode = result.get("imgCode");
        UUID randomUUID = UUID.randomUUID();
        String imgCodeKey = randomUUID.toString();
        System.out.println("imgCodeKey:" + imgCodeKey);
        // 图片验证码有效时间 ：5 分钟
        redisTemplate.opsForValue().set(imgCodeKey, imgCode, 5, TimeUnit.MINUTES);
        result.put("imgCodeKey", imgCodeKey);
    }
}
