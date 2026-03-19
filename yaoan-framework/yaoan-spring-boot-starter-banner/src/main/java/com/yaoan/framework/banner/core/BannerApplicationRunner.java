package com.yaoan.framework.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

import java.util.concurrent.TimeUnit;

/**
 * 项目启动成功后，提供文档相关的地址
 *
 * @author 芋道源码
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("\n" +
                "  启动成功      .-. .-')   \n" +
                "             \\  ( OO )  \n" +
                " .-'),-----. ,--. ,--.  \n" +
                "( OO'  .-.  '|  .'   /  \n" +
                "/   |  | |  ||      /,  \n" +
                "\\_) |  |\\|  ||     ' _) \n" +
                "  \\ |  | |  ||  .   \\   \n" +
                "   `'  '-'  '|  |\\   \\  \n" +
                "     `-----' `--' '--'  ");
    }

}
