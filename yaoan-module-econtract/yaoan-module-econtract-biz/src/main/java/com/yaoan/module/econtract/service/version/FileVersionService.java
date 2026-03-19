package com.yaoan.module.econtract.service.version;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.version.vo.FileVersionSaveReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageRespVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 11:44
 */
public interface FileVersionService {

    /**
     * 生成版本
     */
    Long save(FileVersionSaveReqVO reqVO) throws Exception;

    /**
     * 分页
     */
    PageResult<FileVersionPageRespVO> page(FileVersionPageReqVO reqVO);
}
