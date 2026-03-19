package com.yaoan.framework.common.pojo;

import lombok.Data;

import java.io.Serializable;


@Data
public class TokenResult implements Serializable {
    private static final long serialVersionUID = 772122980209963931L;
    private String access_token;
    private String token_type;
    private String expires_in;
    private String scope;
    
}
