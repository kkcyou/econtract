package com.yaoan.module.econtract.controller.admin.cx.vo.save;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/26 9:34
 */
@Data
public class CXSaveReqVO {
    /**
     * 设置命令的类型，支持命令如下：
     * · drop用于将用户踢出当前文档（用户只可查看不可编辑）
     * · forcesave 强制保存当前编辑的文档并且不关闭文档（调用此命令后用户可继续编辑文档，因此文档可能不是最终的版本），此命令会触发集成方实现的保存回调。
     * · info 返回文档状态
     */
    private String c;

    /**
     * 用户自定义数据，可用于集成方传递数据到保存回调
     */
    private String userdata;
    /**
     * 文档的唯一标识（即当前编辑或编辑过的文档ID）
     */
    private String key;
    /**
     * 标识用户列表，结合c=drop使用，用于将用户踢出文档编辑
     */
    private List<String> users;
}
