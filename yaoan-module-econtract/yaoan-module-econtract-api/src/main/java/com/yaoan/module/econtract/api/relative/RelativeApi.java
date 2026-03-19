package com.yaoan.module.econtract.api.relative;

import com.yaoan.module.econtract.api.relative.dto.RelativeCompanyDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RelativeApi {

    public List<RelativeDTO> getRelative(String id);

    List<RelativeContactDTO> getRelativeUserId(String id,Long deptId);

    List<RelativeContactDTO> getRelativeContactByUserIds(List<Long> ids);

    public String saveRelative(RelativeDTO relativeDTO);

    public String updateRelative(RelativeDTO relativeDTO);

    void saveRelativeContact(String mobile, String relativeId);

    List<RelativeContactDTO> getContacts4RelativeId(String relativeId);

    void saveRelativeContacts(List<RelativeContactDTO> contactDTOList);

    List<Long> calculateUsers4RelativeExpression(String contractId, Integer signatureType);

    List<Long> calculateRelativesExpression(String contractId, Integer containCreatorFlag);

    // 获取用户所在相对方联系人id
    Long getRelativeContactId();

    List<Long> calculateUsers4SortedRelativeExpression(String contractId, Integer containCreatorFlag);

    List<Long> getExistRelativeVirtualIds(Set<Long> virtualIds);

    Map<Long, String> getRelativeNameVirtualIds(Set<Long> virtualIds);

    void saveRelativeContacts4Saas(List<RelativeContactDTO> contactDTOList);

    List<RelativeCompanyDTO> getCompanyIds4login();

    Long getVirtualId4User(Long userId);

    RelativeDTO getOneByCreditCode(String creditCode);

    RelativeDTO getOneByMoble4Individual(String mobile);

    RelativeDTO getIndividualRelativeUserId(Long loginUserId);

    List<RelativeContactDTO> getRelativeContactByCompanyIds(List<Long> companyIds);
}
