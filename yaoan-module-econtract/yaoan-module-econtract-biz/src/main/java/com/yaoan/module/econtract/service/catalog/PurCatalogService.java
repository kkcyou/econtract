package com.yaoan.module.econtract.service.catalog;


import cn.hutool.core.lang.tree.Tree;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogShowVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogVO;
import com.yaoan.module.econtract.dal.dataobject.catalog.PurCatalogDO;

import java.util.HashMap;
import java.util.List;

public interface PurCatalogService {
    List<ModelIdVO> getModelIdByOrderCode(String orderId, String code, String type, String templateId, String purchaserOrg, String purchaserOrgGuid);


    List<PurCatalogShowVO> getPurCatalogByCode(PurCatalogVO purCatalogVO);


}
