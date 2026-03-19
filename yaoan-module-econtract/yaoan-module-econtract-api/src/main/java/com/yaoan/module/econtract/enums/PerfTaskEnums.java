package com.yaoan.module.econtract.enums;

import lombok.Getter;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Getter
public enum PerfTaskEnums {

    PERFTASK_NO_START(1, "履约任务未开始"),
    WAIT_PERFORMANCE(2, "待履约"),
    IN_PERFORMANCE(3, "履约中"),
    PERFORMANCE_FINISH(4, "履约完成"),
    PERFORMANCE_PAUSE(5, "履约暂停"),
    PERFORMANCE_END(6, "履约结束"),
    PERFORMANCE_OVER_TIME(7, "履约超期"),
    OVER_TIME_PAUSE(8, "超期暂停"),
    OVER_TIME_END(9, "超期结束"),
    OVER_TIME_FINISH(10, "超期完成");


    private final Integer code;
    private final String desc;

    PerfTaskEnums(Integer code, String info) {
        this.code = code;
        this.desc = info;
    }

    public static PerfTaskEnums getInstance(Integer code) {
        for (PerfTaskEnums value : PerfTaskEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
