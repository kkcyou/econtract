package com.yaoan.module.econtract.controller.admin.cx.vo;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 10:51
 */
@Data
public class CXCallbackReqVO {

    /**
     * 当用户对文档执行操作时接收的对象
     */
    private List<Object> actions;

    /**
     * 强制保存请求执行.
     * 支持的值如下：
     * 0 - 后端强制保存接口触发
     * 1 - 点编辑器界面上保存按键触发 仅当forcesave 参数设置为true时
     * 2 - 畅写服务器后端定时触发
     * 只有当status为6或7时才会出现forcesavetype。
     */
    private String forcesavetype;

    /**
     * 打开或者编辑的文档唯一标记
     */
    private String key;

    /**
     * 文档的状态。 可能有以下值: 
     * 1 - 文档打开编辑 
     * 2 - 最后一个用户已关闭文档，将编辑后的URL发给业务系统存储文档 
     * 3 - 关闭文档时保存文档出错 
     * 4 - 文档未修改关闭 
     * 6- 文档正在编辑过程中用户或者业务系统触发强制保存 
     * 7- 强制保存文档时发生错误
     */
    private Integer status;

    /**
     * 指向畅写在线后端转换好的文档URL，业务系统通过这个URL下载文档保存到存储系统中
     */
    private String url;

    /**
     * 通过调用后端强制保存接口，自定义信息(如果该自定义信息存在于请求中)
     * 文件id（我方保存的）
     */
    private String userdata;

    /**
     * 打开文档进行编辑用户的标识符列表; 当文档被更改时，将返回最后一次编辑文档用户的标识符(当status 为2或status为6)
     */
    private List<String> users;

    /**
     * 保留参数,先暂时不关注
     */
    private Object history;

    /**
     * 保留参数,先暂时不关注
     */
    private String changesurl;

    /**
     * 保留参数,先暂时不关注
     */
    private List<Object> changeshistory;
}
