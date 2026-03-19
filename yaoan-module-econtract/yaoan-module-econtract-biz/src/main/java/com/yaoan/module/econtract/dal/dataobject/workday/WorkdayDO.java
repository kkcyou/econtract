package com.yaoan.module.econtract.dal.dataobject.workday;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_workday")
public class WorkdayDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 8214799163881461240L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;


    private String cYear;

    private String cMonth;
    private String cDay;
    private String cDate;
    private String cWeek;

    /**
     * 0=工作日
     * 1=节假日
     */
    private String dateType;
    /**
     * 备注
     */
    private String remark;


}
