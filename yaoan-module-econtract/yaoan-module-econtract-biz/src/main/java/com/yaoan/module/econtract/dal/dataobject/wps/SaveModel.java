package com.yaoan.module.econtract.dal.dataobject.wps;

import com.yaoan.module.econtract.controller.admin.model.vo.ModelCreateReqVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Pele
 */
public class SaveModel {
    public MultipartFile file;
    public String name;
    public String size;
    public String sha1;
    public String is_manual;
    public String attachment_size;
    public String content_type;


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getIs_manual() {
        return is_manual;
    }

    public void setIs_manual(String is_manual) {
        this.is_manual = is_manual;
    }

    public String getAttachment_size() {
        return attachment_size;
    }

    public void setAttachment_size(String attachment_size) {
        this.attachment_size = attachment_size;
    }


    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
}
