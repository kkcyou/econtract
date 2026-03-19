package com.yaoan.module.econtract.controller.admin.contract.vo;
import com.yaoan.module.econtract.controller.admin.model.vo.RecentUseModelVO;
import lombok.Data;

import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
public class RecentUseRespVO {
    private List<RecentUseModelVO> recentUseModelInfo;
    private List<RecentUseModelVO> myCollectModelInfo;
}
