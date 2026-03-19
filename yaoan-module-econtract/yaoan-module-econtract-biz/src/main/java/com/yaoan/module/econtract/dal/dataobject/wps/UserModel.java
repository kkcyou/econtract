package com.yaoan.module.econtract.dal.dataobject.wps;

public class UserModel {
    public String id ;
    public String name;
    public String avatar_url = "https://picsum.photos/100/100/?image=1";


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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
