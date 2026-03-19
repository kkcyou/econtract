package com.yaoan.module.econtract.api.changxie;

import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterDTO;
import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXAddReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.cleandraft.CXCleanDraftReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.GetDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.InsertDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.watermark.CXWaterMarkReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 16:38
 */
@FeignClient(url = "${feign.client.changxie}", name = "CXServer")
public interface ChangXieApi {

    @PostMapping(value = "/converter")
    CXFileConverterRespDTO converter(@RequestBody CXFileConverterDTO reqDTO);

    @PostMapping(value = "/addtocontentcontrol")
    String addtocontentcontrol(@RequestBody CXAddReqDTO dto);

    @PostMapping(value = "/insertwatermark")
    String insertwatermark(@RequestBody CXWaterMarkReqDTO dto);

    @PostMapping(value = "/coauthoring/CommandService.ashx")
    CXSaveRespDTO cxSave(@RequestBody CXSaveReqDTO dto);

    @PostMapping(value = "/getDocContent")
    String getDocContent(@RequestBody GetDocContentReqDTO dto);

    @PostMapping(value = "/insertContentToDoc")
    String insertContentToDoc(@RequestBody InsertDocContentReqDTO dto);

    @PostMapping(value = "/cleandraft")
    String cleandraft(@RequestBody CXCleanDraftReqDTO dto);
}
