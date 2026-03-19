package com.yaoan.module.econtract.controller.admin.foxi;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.addon.conversion.office2pdf.Office2PDF;
import com.foxit.sdk.common.Library;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareRespVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveRespVO;
import com.yaoan.module.econtract.service.foxi.FoxiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static com.foxit.sdk.common.Constants.e_ErrInvalidLicense;
import static com.foxit.sdk.common.Constants.e_ErrSuccess;
import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-5 16:20
 */
@Slf4j
@RestController
@RequestMapping("econtract/foxi")
@Validated
@Tag(name = "福昕鲲鹏功能模块", description = "福昕鲲鹏功能模块")
public class FoxiController {

    @Resource
    private FoxiService foxiService;

    /**
     * 福昕文件保存(demo测试)
     */
    @PostMapping(value = "/saveBackTest")
    @PermitAll
    public CommonResult<FoxiSaveRespVO> saveBackTest(@RequestBody @Validated FoxiSaveReqVO reqVO) {
        log.info("Foxi请求参数：{}", reqVO.toString());
        System.out.println("Foxi请求参数：" + reqVO.toString());
        return success(foxiService.saveBackTest(reqVO));
    }

    /**
     * 福昕文件保存
     */
    @PostMapping(value = "/saveBack")
    @PermitAll
    public CommonResult<FoxiSaveRespVO> saveBack(@RequestBody @Validated FoxiSaveReqVO reqVO) {
        return success(foxiService.saveBack(reqVO));
    }


    /**
     * 文件比对
     */
    @PostMapping(value = "/pdfCompare")
    @PermitAll
    public CommonResult<PdfCompareRespVO> pdfCompare(@RequestBody @Validated PdfCompareReqVO reqVO) {
        return success(foxiService.pdfCompare(reqVO));
    }

    static String key = "8f3g1sFttW8NAgfSNv+BtaJq6WX3+7iZs/PdPWkHlLz/s5ukbPWH69OXP4Na4EBsYIs8hVDsyzacr9E2PgCxGWk1bMXQUXHM7g9DivZhQpWv8pZithEUGPoIZVsu8GLSK1/6PzqNdGITP2hZfUJn4JUiazbGLXo+afiyEZR17Y3Gu9QaOIACWNFBGBhGt8CW43ui4D5kd2cXVjGYy4uf3KN6qb6sUU/4t2NBfTlJMMVwLofkED70Nmgb2jn04YDUBAUbUCY2Ut/Jg35PHQClO9U3IWzJHMrcrSdfCOAVX6NvuZit3a/laUvRoYXxbkowFdkbed3M1KPzlpSVrYV5/yoU9VTbCdV1NGzO8K+rfuOkjOfs1E+cUcN1gnFzYxcljmuPW7bvBGEigp5acH+n0BLiWtU5lcEhTh4Ew5MRfmEbjxiVjL5u10zd+f2zsIJbDaJjDsm1rKddx2//oSLKpV6pu0DkzVEKDKAAPOwbJNQh7CN+xxMmdSnnC1FKQd0N8pXA08JJYOmd142pND5V7RXknMEQcEwezBcooVPHOCz3Iv0xKrMlyKne2uPvsF8SP8bDNKma21h5VUdpsCpd2Etwh1pbQpxqc9W4G/rKVWihKY4HZ8lH2Se8Uxe9TxuIVbRbCBHI8lA8Nr853qIiGqfIHsr75m+TNtPrEQLTNbFKQSaZ/BU8/RHphJK/SqqNz1WWVqWRI2SX3dpJqvpg0gkZRjPfTou6shfLw16O8ISbHT1FRm7HP6VRrJJYJHZ3rvWdFqvF9e1puTB+vizC6Vk+/pIce2sDByXVvOKRYbjTQ4b7VzWATC8HJtYclz3BIadxdAShlcabn7Ur2jY1Dc+ioqKo8eoNBwTA1Ds9dws5YtL4P1Nv0VdCzmrRtpOUQroKtMXakP7VDTi49LnaU/XruKG04j4JSmo/1+Ze0+15vQvxVLy+iVPBSAjlkFDTdrpGoiTBLgPBhCenECwY2WtN8cS19njQEjEEUOeNg1/ldlTNAFA/7IQQ65Y1lOZutBdwn3oBS551pCcp48NcgUZChzs3t71QBAEvmmUJyWJbmV+S9e19wmR90NiAf8GI3okpWkLCMhEESsDI/0D3IU/m+mCMelwUNFIOirpomVzdh+1yUMWajyg9q+iaLJFjXNn6gvQqtTdKT8i/poRupolzHy7VeA7TstuZ1kUTHp3pt5sBXA7JXWjpLU+KypryGSe0EeAvUdItB4ywcRO7muyzGqWi4GYbntrLGHkFv1x3U240B3k0udpOge/zI1BVvurMGAbGSpOiR+NxQWIfKcHjDixhobq7vPXPW3hkywYhrtKQJL10sHLy2JsOA6QCBhs9kTxTccZ99VMdB+1wvYIxdCT/6DEUw++HhY/ygMO8gwi1+9E5fGjH84PpRZL/8DczKdtiNv3lpz3t8abBc5pX7tFGY/s3Qg==";
    static String sn = "SoYYRfoFW6eUuePKRWQhdux9MyUpGUOXf3mHZNtKfCV+otpcG1q5iw==";

    /**
     * 文件比对
     */
    @PostConstruct()
    @PermitAll
    public void initial() {
        // You can also use System.load("filename") instead. The filename argument must be an absolute path name.
        String os = System.getProperty("os.name").toLowerCase();
        String lib = "fsdk_java_";
        if (os.startsWith("win")) {
            lib += "win";
        } else if (os.startsWith("mac")) {
            lib += "mac";
        } else {
            lib += "linux";
        }
        if (System.getProperty("sun.arch.data.model").equals("64")) {
            if (System.getProperty("os.arch").equals("aarch64")) {
                lib += "arm";
            } else {
                lib += "64";
            }
        } else {
            lib += "32";
        }
        System.loadLibrary(lib);


        // Initialize library
        String v = Library.getVersion();

        int error_code = Library.initialize(sn, key);
        if (error_code != e_ErrSuccess) {
            if (e_ErrInvalidLicense == error_code)
                System.out.println("[Failed] Current used Foxit PDF SDK key information is invalid.");
            else
                System.out.println("Library Initialize Error: " + error_code);
            return;
        }

        //Initialize the Office2PDF module.
        try {
            Office2PDF.initialize(library_path);
        } catch (PDFException e) {
            log.error("[Failed] Foxit PDF Initialization Error: " + e.getMessage());
            throw new RuntimeException(e);
        }

        System.out.println("Convert Word file to PDF file.");
        log.info("foxi服务初始化完成");
    }
    @Value(value = "${foxi.library_path}")
    private String library_path;

}
