package com.yaoan.module.econtract.util;

import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WkHtmlToPdfManager {
    @Value("${contract.wkpath}")
    private String wkPath;


    public void fileAuthHtmlToPdfFromstring(String sourcestring, String targetFile) {
        WrapperConfig config = new WrapperConfig(this.wkPath);
        WkHtmlToPdfUtil.htmlToPdfFromString(config, sourcestring, targetFile);
        //return this.wkHtmlToPdfHandlerProvider.handler(targetFile,true);
    }
}
