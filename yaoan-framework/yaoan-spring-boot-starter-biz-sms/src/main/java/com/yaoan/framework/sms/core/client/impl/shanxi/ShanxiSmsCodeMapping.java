package com.yaoan.framework.sms.core.client.impl.shanxi;

import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.yaoan.framework.sms.core.client.SmsCodeMapping;
import com.yaoan.framework.sms.core.enums.SmsFrameworkErrorCodeConstants;

public class ShanxiSmsCodeMapping implements SmsCodeMapping {

    @Override
    public ErrorCode apply(String apiCode) {
        switch (apiCode) {
            case "200": return GlobalErrorCodeConstants.SUCCESS;
            default: return SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
        }

        //return Objects.equals(apiCode, "0") ? GlobalErrorCodeConstants.SUCCESS : SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
    }

}
