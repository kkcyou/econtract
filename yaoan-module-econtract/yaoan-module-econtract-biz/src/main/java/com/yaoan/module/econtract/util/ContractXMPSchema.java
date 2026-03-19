package com.yaoan.module.econtract.util;

import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.XMPSchema;
import org.apache.xmpbox.type.TextType;

public class ContractXMPSchema extends XMPSchema {
    /**
     * 创建
     * @param metadata
     * @param namespaceURI
     * @param prefix
     */
    public ContractXMPSchema(XMPMetadata metadata, String namespaceURI, String prefix) {
        super(metadata, namespaceURI, prefix);
    }

    /**
     * 设置
     * @param propertyName
     * @param base64ContractXml
     */
    public void setData(String propertyName,String base64ContractXml) {
        // ✅ 直接用 TextType 构造方法
        TextType text = new TextType(getMetadata(), getNamespace(), getPrefix(), propertyName, base64ContractXml);
        addProperty(text);
    }
}
