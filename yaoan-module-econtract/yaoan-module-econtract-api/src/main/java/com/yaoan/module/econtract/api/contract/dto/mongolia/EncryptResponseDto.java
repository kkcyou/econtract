package com.yaoan.module.econtract.api.contract.dto.mongolia;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * EncryptResponseDTO
 * @author doujiale
 */
@lombok.Data
public class EncryptResponseDto {

    private String data;
    private String errorInfoDesc;
    private String mac;
    private String message;
    private PageInfo pageInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime responseTime;
    private String sourcePlatform;
    private String sourceStatus;
    private String status;
}