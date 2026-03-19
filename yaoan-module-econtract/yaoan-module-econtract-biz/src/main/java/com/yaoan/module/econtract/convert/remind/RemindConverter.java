package com.yaoan.module.econtract.convert.remind;

import com.yaoan.module.econtract.controller.admin.perfRemind.vo.PerfRemindVO;
import com.yaoan.module.econtract.dal.dataobject.perfRemind.relative.PerfRemindDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper(componentModel = "spring")
public interface RemindConverter {

    RemindConverter INSTANCE = Mappers.getMapper(RemindConverter.class);

    PerfRemindVO tovo(PerfRemindDO bean);


}
