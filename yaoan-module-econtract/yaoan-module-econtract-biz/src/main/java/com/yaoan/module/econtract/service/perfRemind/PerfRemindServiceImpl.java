package com.yaoan.module.econtract.service.perfRemind;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.perfRemind.vo.PerfRemindVO;
import com.yaoan.module.econtract.convert.remind.RemindConverter;
import com.yaoan.module.econtract.dal.dataobject.perfRemind.relative.PerfRemindDO;
import com.yaoan.module.econtract.dal.mysql.perfRemind.PerRemindMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class PerfRemindServiceImpl implements PerfRemindService {
@Resource
private PerRemindMapper perRemindMapper;
    @Override
    public String createPerfRemind(PerfRemindVO perfRemindVO) {
        PerfRemindDO perfRemindDO = new PerfRemindDO().setStatus(perfRemindVO.getStatus());
        if(StringUtils.isNotBlank(perfRemindVO.getId())){
        //修改
            perfRemindDO.setId(perfRemindVO.getId());
            perRemindMapper.updateById(perfRemindDO)  ;
        }else {
            //一个账号只允许存在一条数据
            Long count = perRemindMapper.selectCount(new LambdaQueryWrapperX<PerfRemindDO>().eqIfPresent(PerfRemindDO::getCreator, WebFrameworkUtils.getLoginUserId()));
            if(count>0){
                throw exception(ErrorCodeConstants.CREATE_REMIND_ERROR);
            }
            perRemindMapper.insert(perfRemindDO);
        }
        return perfRemindDO.getId();
    }

    @Override
    public PerfRemindVO queryPerfRemind() {
        PerfRemindDO perfRemindDO = perRemindMapper.selectOne(new LambdaQueryWrapperX<PerfRemindDO>().eqIfPresent(PerfRemindDO::getCreator, WebFrameworkUtils.getLoginUserId()));
        return RemindConverter.INSTANCE.tovo(perfRemindDO);
    }
}
