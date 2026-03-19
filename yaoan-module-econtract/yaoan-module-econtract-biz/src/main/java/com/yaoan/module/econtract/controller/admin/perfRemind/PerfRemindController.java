package com.yaoan.module.econtract.controller.admin.perfRemind;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.perfRemind.vo.PerfRemindVO;
import com.yaoan.module.econtract.service.perfRemind.PerfRemindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 履约提醒设置
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@RestController
@RequestMapping("econtract/remind")
@Tag(name = "履约提醒设置", description = "履约提醒设置")
public class PerfRemindController {
    @Resource
    private PerfRemindService perfRemindService;
    /**
     * 履约提醒设置
     * @param
     * @return
     */
    @PutMapping(value = "/set")
    @Operation(summary = "履约提醒设置", description = "履约提醒设置")
    @OperateLog(logArgs = false)
    public CommonResult createPerfRemind(@RequestBody PerfRemindVO perfRemindVO) {
        perfRemindService.createPerfRemind(perfRemindVO);
        return success(true);
    }
    /**
     * 查看履约提醒设置
     * @param
     * @return
     */
    @GetMapping(value = "/query")
    @Operation(summary = "查看履约提醒设置", description = "查看履约提醒设置")
    @OperateLog(logArgs = false)
    public CommonResult<PerfRemindVO> queryPerfRemind() {
        return success( perfRemindService.queryPerfRemind());
    }

}
