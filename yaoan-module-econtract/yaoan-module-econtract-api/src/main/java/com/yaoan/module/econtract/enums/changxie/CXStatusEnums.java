package com.yaoan.module.econtract.enums.changxie;

import lombok.Getter;

/**
 * @description: 畅写保存回调接口状态枚举
 * @author: Pele
 * @date: 2024/4/25 11:00
 */
@Getter
public enum CXStatusEnums {
    /**
     * status 1 接收到每个用户连接或从文档协同编辑断开连接。回调保存需返回{"error":0}
     * status 2 文档关闭收到，代表最关闭文档用户。回调保存需返回{"error":0}
     * status 3 文档关闭时并且畅写服务器保存文档处理出错。回调保存需返回{"error":-1}。 注：不返回{"error":-1}，编辑的文档内容会丢失
     * status 4 用户关闭文档,且未做任何更改。回调保存需返回{"error":0}
     * status 6 执行强制保存。回调保存需返回{"error":0}
     * status 7 执行强制保存并且畅写服务器保存文档处理出错。回调保存需返回{"error":-1}。注：不返回{"error":-1}，编辑的文档内容会丢失
     */
    /**
     * 是否 的枚举
     */
    COOPERATE_DISCONNECT(1, "接收到每个用户连接或从文档协同编辑断开连接", 0),
    DISCONNECT(2, "文档关闭收到，代表最关闭文档用户", 0),
    DISCONNECT_CX_SERVE_ERROR(3, "文档关闭时并且畅写服务器保存文档处理出错", -1),
    CLIENT_NO_CHANGE_DISCONNECT(4, "用户关闭文档,且未做任何更改", 0),
    FORCE_SAVE(6, "执行强制保存", 0),
    FORCE_SAVE_CX_SERVER_ERROR(7, "执行强制保存并且畅写服务器保存文档处理出错", -1),

    ;

    private final Integer code;
    private final String info;
    private final Integer error;

    CXStatusEnums(Integer code, String info, Integer error) {
        this.code = code;
        this.info = info;
        this.error = error;
    }

    public static CXStatusEnums getInstance(Integer code) {
        for (CXStatusEnums value : CXStatusEnums.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
