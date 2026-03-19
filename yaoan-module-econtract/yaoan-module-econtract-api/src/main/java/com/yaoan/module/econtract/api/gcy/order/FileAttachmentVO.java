package com.yaoan.module.econtract.api.gcy.order;


import lombok.Data;

/**
 * @description: 订单的附件信息
 * @author: Pele
 * @date: 2023/11/29 11:46
 */
@Data
public class FileAttachmentVO  {

    private static final long serialVersionUID = -2166341090635560652L;
    /**
     * id主键
     */
    private String id;
    /**
     * 附件名称
     */
    private String fileName;
    /**
     * 附件路径
     */
    private String path;
}
