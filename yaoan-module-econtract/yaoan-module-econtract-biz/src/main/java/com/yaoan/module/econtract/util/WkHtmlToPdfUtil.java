package com.yaoan.module.econtract.util;


import cn.hutool.core.io.IoUtil;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.configurations.WrapperConfig;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WkHtmlToPdfUtil {
    private static final Logger log = LoggerFactory.getLogger(WkHtmlToPdfUtil.class);
    private static Param pageInfo = new Param("--footer-center", new String[]{new String(" ".getBytes(), Charset.forName("utf-8"))});
    private static Param encodingInfo = new Param("--encoding", new String[]{new String("UTF-8".getBytes(), Charset.forName("utf-8"))});
    private static Param cssInfo = new Param("--custom-header", new String[]{new String("Content-Style".getBytes(), Charset.forName("utf-8")),new String("input[type=\"radio\"]{-webkit-appearance: none;width: 12px;height: 12px;border: 1px solid #000;border-radius: 50%;}".getBytes(), Charset.forName("utf-8"))});

    public WkHtmlToPdfUtil() {
    }

    public static void main(String[] args) throws IOException {
        WrapperConfig config = new WrapperConfig("D:\\soft\\wkHtmlToPdf\\bin\\wkhtmltopdf.exe");
        String path = "C:\\Users\\lls\\Desktop\\22\\货物类.html";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        File targetFile = new File("C:\\Users\\lls\\Desktop\\22\\货物2.pdf");
        WkHtmlToPdfUtil.htmlToPdfFromString(config, content, "C:\\Users\\lls\\Desktop\\22\\货物2.pdf");
    }
    public static void htmlToPdfFromString(WrapperConfig config, String sourceString, String targetFile) {
        try {
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            if(sourceString!=null && !"".equals(sourceString)) {
                sourceString = sourceString.replaceAll("placeholder=\"[^\"]*\"", "");
            }
            pdf.addPageFromString(sourceString);
            //pdf.addPageFromString(sourceString);
            //pdf.addParam(pageInfo, new Param[0]);
            pdf.addParam(encodingInfo, new Param[0]);
            pdf.addParam(cssInfo, new Param[0]);
            pdf.saveAsDirect(targetFile);
        } catch (InterruptedException | IOException var4) {
            log.error("wkHtmlToPdf发生异常：{}", var4);
        }

    }

    public static void htmlToPdfFromUrl(WrapperConfig config, String sourceUrl, String targetFile) {
        try {
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromUrl(sourceUrl);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(targetFile);
        } catch (InterruptedException | IOException var4) {
            log.error("wkHtmlToPdf发生异常：{}", var4);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var4.getMessage());
        }
    }

    public static void htmlToPdfFromFile(WrapperConfig config, String sourceFile, String targetFile) {
        try {
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromFile(sourceFile);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(targetFile);
        } catch (InterruptedException | IOException var7) {
            log.error("wkHtmlToPdf发生异常：{}", var7);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var7.getMessage());
        } finally {
            ;
        }
    }

    public static byte[] htmlToPdfFromString(WrapperConfig config, String sourceString) {
        String tempPath = "";
        boolean var15 = false;

        byte[] var7;
        try {
            var15 = true;
            File temp = File.createTempFile("wkhtmltopdf" + UUID.randomUUID().toString(), ".pdf");
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromString(sourceString);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(temp.getPath());
            FileInputStream inputSteam = IoUtil.toStream(temp);
            byte[] returnBytes = IoUtil.readBytes(inputSteam);
            IoUtil.close(inputSteam);
            var7 = returnBytes;
            var15 = false;
        } catch (InterruptedException | IOException var18) {
            log.error("wkHtmlToPdf发生异常：{}", var18);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var18.getMessage());
        } finally {
            if (var15) {
                try {
                    Path p = Paths.get(tempPath);
                    log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
                } catch (IOException var16) {
                    log.warn("Couldn't delete temp file " + tempPath);
                }

            }
        }

        try {
            Path p = Paths.get(tempPath);
            log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
        } catch (IOException var17) {
            log.warn("Couldn't delete temp file " + tempPath);
        }

        return var7;
    }

    public static byte[] htmlToPdfFromUrl(WrapperConfig config, String sourceUrl, Param param, Param... params) {
        String tempPath = "";
        boolean var17 = false;

        byte[] var9;
        try {
            var17 = true;
            File temp = File.createTempFile("wkhtmltopdf" + UUID.randomUUID().toString(), ".pdf");
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromUrl(sourceUrl);
            pdf.addParam(param, params);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(temp.getPath());
            tempPath = temp.getPath();
            FileInputStream inputSteam = IoUtil.toStream(temp);
            byte[] returnBytes = IoUtil.readBytes(inputSteam);
            IoUtil.close(inputSteam);
            var9 = returnBytes;
            var17 = false;
        } catch (InterruptedException | IOException var20) {
            log.error("wkHtmlToPdf发生异常：{}", var20);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var20.getMessage());
        } finally {
            if (var17) {
                try {
                    Path p = Paths.get(tempPath);
                    log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
                } catch (IOException var18) {
                    log.warn("Couldn't delete temp file " + tempPath);
                }

            }
        }

        try {
            Path p = Paths.get(tempPath);
            log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
        } catch (IOException var19) {
            log.warn("Couldn't delete temp file " + tempPath);
        }

        return var9;
    }

    public static byte[] htmlToPdfFromUrl(WrapperConfig config, String sourceUrl) {
        String tempPath = "";
        boolean var15 = false;

        byte[] var7;
        try {
            var15 = true;
            File temp = File.createTempFile("wkhtmltopdf" + UUID.randomUUID().toString(), ".pdf");
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromUrl(sourceUrl);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(temp.getPath());
            tempPath = temp.getPath();
            FileInputStream inputSteam = IoUtil.toStream(temp);
            byte[] returnBytes = IoUtil.readBytes(inputSteam);
            IoUtil.close(inputSteam);
            var7 = returnBytes;
            var15 = false;
        } catch (InterruptedException | IOException var18) {
            log.error("wkHtmlToPdf发生异常：{}", var18);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var18.getMessage());
        } finally {
            if (var15) {
                try {
                    Path p = Paths.get(tempPath);
                    log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
                } catch (IOException var16) {
                    log.warn("Couldn't delete temp file " + tempPath);
                }

            }
        }

        try {
            Path p = Paths.get(tempPath);
            log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
        } catch (IOException var17) {
            log.warn("Couldn't delete temp file " + tempPath);
        }

        return var7;
    }

    public static byte[] htmlToPdfFromFile(WrapperConfig config, String sourceFile) {
        String tempPath = "";
        boolean var15 = false;

        byte[] var7;
        try {
            var15 = true;
            File temp = File.createTempFile("wkhtmltopdf" + UUID.randomUUID().toString(), ".pdf");
            Pdf pdf = new Pdf(config);
            pdf.setAllowMissingAssets();
            pdf.addPageFromFile(sourceFile);
            pdf.addParam(pageInfo, new Param[0]);
            pdf.saveAs(temp.getPath());
            tempPath = temp.getPath();
            FileInputStream inputSteam = IoUtil.toStream(temp);
            byte[] returnBytes = IoUtil.readBytes(inputSteam);
            IoUtil.close(inputSteam);
            var7 = returnBytes;
            var15 = false;
        } catch (InterruptedException | IOException var18) {
            log.error("wkHtmlToPdf发生异常：{}", var18);
            Thread.currentThread().interrupt();
            throw new RuntimeException("wkHtmlToPdf发生异常：" + var18.getMessage());
        } finally {
            if (var15) {
                try {
                    Path p = Paths.get(tempPath);
                    log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
                } catch (IOException var16) {
                    log.warn("Couldn't delete temp file " + tempPath);
                }

            }
        }

        try {
            Path p = Paths.get(tempPath);
            log.debug("Delete temp file at: " + tempPath + " " + Files.deleteIfExists(p));
        } catch (IOException var17) {
            log.warn("Couldn't delete temp file " + tempPath);
        }

        return var7;
    }
}
