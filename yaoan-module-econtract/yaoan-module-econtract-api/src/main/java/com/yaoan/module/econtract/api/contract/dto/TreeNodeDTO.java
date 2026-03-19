package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;

import java.util.List;

/**
 * @author doujiale
 */
@Data
public class TreeNodeDTO {

    /**
     * 品目id
     */
    private String id;
    /**
     * 品目父id
     */
    private String pid;
    /**
     * 品目名称
     */
    private String name;
    /**
     * 是否选中
     */
    private Boolean checked;
    /**
     * 品目是否有效
     */
    private Integer valid;
    /**
     * 子节点
     */
    private List<TreeNodeDTO> children;


}
