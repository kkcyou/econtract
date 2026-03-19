package com.yaoan.module.bpm.dal.dataobject.act;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;
import java.util.Date;

@TableName("act_ge_bytearray")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActGeByteArray {

    @TableId(value = "ID_", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField(value = "REV_")
    private Integer rev;

    @TableField(value = "NAME_")
    private String name;

    @TableField(value = "DEPLOYMENT_ID_")
    private String deploymentId;

    @TableField(value = "BYTES_")
    private byte[] bytes;

    @TableField(value = "GENERATED_")
    private byte[] generated;
}
