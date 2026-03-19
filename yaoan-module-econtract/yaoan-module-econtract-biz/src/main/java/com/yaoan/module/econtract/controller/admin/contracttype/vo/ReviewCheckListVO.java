package com.yaoan.module.econtract.controller.admin.contracttype.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class ReviewCheckListVO {

    /**
     * 审查清单id
     * */
    private String id;
    /**
     * 审查清单名称
     */
    private String name;

}
