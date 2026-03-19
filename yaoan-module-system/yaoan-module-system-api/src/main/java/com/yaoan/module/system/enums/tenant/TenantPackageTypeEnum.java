package com.yaoan.module.system.enums.tenant;

import cn.hutool.core.util.ArrayUtil;
import com.yaoan.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 *
 * @author 芋道源码
 */
@Getter
@AllArgsConstructor
public enum TenantPackageTypeEnum implements IntArrayValuable {

    RTF(0, "RTF"),
    WPS(1, "WPS");

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(TenantPackageTypeEnum::getType).toArray();

    /**
     * 状态值
     */
    private final Integer type;
    /**
     * 状态名
     */
    private final String name;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static TenantPackageTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
