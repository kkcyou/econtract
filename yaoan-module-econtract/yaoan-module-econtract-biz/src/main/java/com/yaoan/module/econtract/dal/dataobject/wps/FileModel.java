package com.yaoan.module.econtract.dal.dataobject.wps;

public class FileModel {
    public String id = "111";
    public String name = "中文.doc";
    public long version = 3;
    public long size = 2000;
    public long create_time = 1670218748;
    public long modify_time = 1670328304;
    public String creator_id = "405";
    public String modifier_id = "405";
    public static String download_url = "";
    // public static String download_url = "https://ksc-bj.ag.kdocs.cn/api/object/2_19975ce2b6d24b2381c96b944f6847d1/compatible?response-content-disposition=attachment%3Bfilename%2A%3Dutf-8%27%27%25E6%25B5%258B%25E8%25AF%2595.docx&KSSAccessKeyId=AKLTU7N1__ODQ4KKf-YhOK9iwg&Expires=1675328392&Signature=74iRos0NFoxL2ukyyYC5FOOVo48%3D";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public long getModify_time() {
        return modify_time;
    }

    public void setModify_time(long modify_time) {
        this.modify_time = modify_time;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getModifier_id() {
        return modifier_id;
    }

    public void setModifier_id(String modifier_id) {
        this.modifier_id = modifier_id;
    }

    public static String getDownload_url() {
        return download_url;
    }

    public static void setDownload_url(String download_url) {
        FileModel.download_url = download_url;
    }
}
