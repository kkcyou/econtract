package com.yaoan.module.infra.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author by doujl
 * Create Date: 2023年09月5日11:44:18
 * Description:二个元素的元组
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TwoTuple<F,S> implements Serializable {

    private static final long serialVersionUID = 8940481206954092214L;

    private F first;
    private S second;

}
