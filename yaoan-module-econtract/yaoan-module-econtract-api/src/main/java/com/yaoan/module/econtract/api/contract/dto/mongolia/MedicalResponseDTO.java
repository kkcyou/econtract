package com.yaoan.module.econtract.api.contract.dto.mongolia;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * EncryptResponseDTO
 * @author doujiale
 */
@lombok.Data
public class MedicalResponseDTO<T> {

    private HashMap data;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime responseTime;
    private String message;
    private String code;
    private String msg;
    private String   contractArchiveState;
}