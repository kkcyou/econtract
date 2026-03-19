package com.yaoan.module.econtract.controller.admin.demo;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoPageReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoRespVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.convert.demo.DemoConverter;
import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEvent;
import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEventPublisher;
import com.yaoan.module.econtract.service.demo.EcmsDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 测试功能模块
 */
@Slf4j
@RestController
@RequestMapping("econtract/demo")
@Validated
@Tag(name = "测试功能模块", description = "测试功能模块操作接口")
public class EcmsDemoController {

    @Resource
    private EcmsDemoService ecmsDemoService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private EcmsContractResultEventPublisher ecmsContractResultEventPublisher;

    /**
     * 获得demo分页列表
     * @param reqVO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "获得demo分页列表")
    public CommonResult<PageResult<DemoRespVO>> getPostPage(@Validated DemoPageReqVO reqVO) {
        System.out.println("开始执行");
//        ecmsContractResultEventPublisher.sendContractResultEvent(new EcmsContractResultEvent(this).setResult(1).setId(""));
        log.info("测试一条数据");
        return success(DemoConverter.INSTANCE.convertPage(ecmsDemoService.getDemoPage(reqVO)));
    }


    /**
     * 创建demo
     * @param reqVO
     * @return
     */
    @PostMapping("create")
    @Operation(summary = "创建demo")
    public CommonResult<String> createDept(@Valid @RequestBody DemoCreateReqVO reqVO) {
        String deptId = ecmsDemoService.createDemo(reqVO);
        return success(deptId);
    }

    /**
     * 更新demo
     * @param reqVO
     * @return
     */
    @PutMapping("update")
    @Operation(summary = "更新demo")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DemoUpdateReqVO reqVO) {
        ecmsDemoService.updateDemo(reqVO);
        return success(true);
    }

    /***
     * 删除demo
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @Operation(summary = "删除demo")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteDept(@RequestParam("id") String id) {
        ecmsDemoService.deleteDemo(id);
        return success(true);

    }
}
