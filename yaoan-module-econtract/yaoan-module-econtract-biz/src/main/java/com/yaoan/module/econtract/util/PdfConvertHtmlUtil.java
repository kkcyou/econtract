package com.yaoan.module.econtract.util;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.css.apply.impl.DefaultCssApplierFactory;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * PDF文档转HTML文档
 *
 * @author LXW
 * @date 2020/6/17 16:45
 */
public class PdfConvertHtmlUtil {
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(PdfConvertHtmlUtil.class);

    /**
     * PDF文档流转Png
     *
     * @param pdfFileInputStream
     * @return BufferedImage
     */
    public static BufferedImage pdfStreamToPng(InputStream pdfFileInputStream) {
        PDDocument doc = null;
        PDFRenderer renderer = null;
        try {
            doc = PDDocument.load(pdfFileInputStream);
            renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            BufferedImage image = null;
            for (int i = 0; i < pageCount; i++) {
                if (image != null) {
                    image = combineBufferedImages(image, renderer.renderImageWithDPI(i, 144));
                }

                if (i == 0) {
                    image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                }
                // BufferedImage srcImage = resize(image, 240, 240);//产生缩略图

            }
            return combineBufferedImages(image);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * BufferedImage拼接处理，添加分割线
     *
     * @param images
     * @return BufferedImage
     */
    public static BufferedImage combineBufferedImages(BufferedImage... images) {
        int height = 0;
        int width = 0;
        for (BufferedImage image : images) {
            //height += Math.max(height, image.getHeight());
            height += image.getHeight();
            width = image.getWidth();
        }
        BufferedImage combo = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = combo.createGraphics();
        int x = 0;
        int y = 0;
        for (BufferedImage image : images) {
            //int y = (height - image.getHeight()) / 2;
            g2.setStroke(new BasicStroke(2.0f));// 线条粗细
            g2.setColor(new Color(193, 193, 193));// 线条颜色
            g2.drawLine(x, y, width, y);// 线条起点及终点位置

            g2.drawImage(image, x, y, null);
            //x += image.getWidth();
            y += image.getHeight();

        }
        return combo;
    }

    /**
     * 通过Base64创建HTML文件并输出html文件
     *
     * @param base64
     * @param htmlPath html保存路径
     */
    public static void createHtmlByBase64(String base64, String htmlPath) {
        StringBuilder stringHtml = new StringBuilder();
        PrintStream printStream = null;
        try {
            // 打开文件
            printStream = new PrintStream(new FileOutputStream(htmlPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 输入HTML文件内容
        stringHtml.append("<html><head>");
        stringHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        stringHtml.append("<title></title>");
        stringHtml.append("</head>");
        stringHtml.append(
                "<body style=\"\r\n" + "    text-align: center;\r\n" + "    background-color: #C1C1C1;\r\n" + "\">");
        stringHtml.append("<img src=\"data:image/png;base64," + base64 + "\" />");
        stringHtml.append("<a name=\"head\" style=\"position:absolute;top:0px;\"></a>");
        //添加锚点用于返回首页
        stringHtml.append("<a style=\"position:fixed;bottom:10px;right:10px\" href=\"#head\">回到首页</a>");
        stringHtml.append("</body></html>");
        try {
            // 将HTML文件内容写入文件中
            printStream.println(stringHtml.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printStream != null) {
                printStream.close();
            }
        }

    }

    /**
     * bufferedImage 转为 base64编码
     *
     * @param bufferedImage
     * @return
     */
    public static String bufferedImageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String png_base64 = "";
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);// 写入流中
            byte[] bytes = byteArrayOutputStream.toByteArray();// 转换成字节
            BASE64Encoder encoder = new BASE64Encoder();
            // 转换成base64串 删除 \r\n
            png_base64 = encoder.encodeBuffer(bytes).trim()
                    .replaceAll("\n", "")
                    .replaceAll("\r", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return png_base64;
    }

//    public static void main(String[] args) {
//        File file = new File("/Users/doujiale/Desktop/电梯维修和保养服务合同PM.pdf");
//        String htmlPath = "/Users/doujiale/Desktop/电梯维修和保养服务合同PM.html";
//        InputStream inputStream = null;
//        BufferedImage bufferedImage = null;
//        try {
//            inputStream = new FileInputStream(file);
//            bufferedImage = pdfStreamToPng(inputStream);
//            String base64_png = bufferedImageToBase64(bufferedImage);
//            createHtmlByBase64(base64_png, htmlPath);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void pdf2html(String srcPath, String desPath) throws Exception {
        File file = new File(srcPath);
        InputStream inputStream = null;
        BufferedImage bufferedImage = null;
        try {
            inputStream = new FileInputStream(file);
            bufferedImage = pdfStreamToPng(inputStream);
            String base64_png = bufferedImageToBase64(bufferedImage);
            createHtmlByBase64(base64_png, desPath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pdf转html
     */
    public static void pdfStream2html(InputStream inputStream, String desPath) throws Exception {

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = pdfStreamToPng(inputStream);
            String base64_png = bufferedImageToBase64(bufferedImage);
            createHtmlByBase64(base64_png, desPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * string类型的富文本转换成pdf
     *
     * @param htmlFile    需要转换成pdf的富文本文件
     * @param pdfFilePath 存放pdf文件的路径
     * @throws Exception
     */
    public static void rtf2Pdf(String htmlFile, String pdfFilePath) throws Exception {
        // 创建PDF文件
        File file = new File(pdfFilePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        // 创建转换属性
        ConverterProperties converterProperties = new ConverterProperties();
        FontProvider fontProvider = new DefaultFontProvider();
        fontProvider.addSystemFonts();
        converterProperties.setFontProvider(fontProvider);
//        converterProperties
        try {
            // 将HTML字符串转换为PDF文件
            HtmlConverter.convertToPdf(htmlFile, outputStream, converterProperties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭输出流
            outputStream.close();
        }
    }
    /**
     * string类型的富文本转换成pdf
     *
     * @param htmlFile    需要转换成pdf的富文本文件
     * @param pdfFilePath 存放pdf文件的路径
     * @throws Exception
     */
    public static void rtf2Pdf1(String htmlFile, String pdfFilePath) throws Exception {
        // 创建PDF文件
        File file = new File(pdfFilePath);
        FileOutputStream outputStream = new FileOutputStream(file);
        htmlFile = htmlFile.replace("\\", "");
        try {
            // 创建PdfWriter对象，并将其与输出流关联
            PdfWriter writer = new PdfWriter(outputStream);

            // 创建转换属性
            ConverterProperties converterProperties = new ConverterProperties();
            converterProperties.setBaseUri(new File(".").getAbsolutePath());
            // 设置页面尺寸为A4大小
            PageSize pageSize = PageSize.A4;
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputStream));
            pdfDoc.setDefaultPageSize(pageSize);
            // 设置页面边距
            Document document = new Document(pdfDoc);
            document.setMargins(10, 10, 10, 10);
            // 启用CSS解析
            converterProperties.setCssApplierFactory(new DefaultCssApplierFactory());
            // 设置字体提供程序，以确保正确的字体被应用
            FontProvider fontProvider = new FontProvider();
            fontProvider.addStandardPdfFonts();
            fontProvider.addSystemFonts();
            converterProperties.setFontProvider(fontProvider);
            // 将HTML字符串转换为PDF文件，并传递ConverterProperties对象和PdfWriter对象
            HtmlConverter.convertToPdf(htmlFile, writer, converterProperties);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭输出流
            outputStream.close();
        }
    }
    public static void main(String[] args) throws Exception {
//        String content = "<div contenteditable=\"false\"><div><div><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; border-bottom:0.75pt solid #000000; padding-bottom:1pt; font-size:9pt\"><span style=\"width:140.25pt; display:inline-block; -aw-tabstop-align:left; -aw-tabstop-pos:140.25pt\">&nbsp;</span><span style=\"font-family:宋体\">政府采购供应商投诉书范本</span></p></div><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; widows:0; orphans:0; font-size:22pt\"><span style=\"font-family:宋体; font-weight:bold\">投诉书范本</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体\">一、投诉相关主体基本情况</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">投诉人：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">地</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">址：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">邮编：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">法定代表人/主要负责人：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">联系电话：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">授权代表：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">联系电话</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">地</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">址：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">邮编：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">被投诉人1：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">地</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">址：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">邮编：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">联系人：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">联系电话：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">被投诉人2</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">……</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">相关供应商：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">地</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">址：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">邮编：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">联系人：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">联系电话：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体\">二、投诉项目基本情况</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">采购项目名称：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">采购项目编号：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">包号：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">采购人名称：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">代理机构名称：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">采购文件公告:</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">是/否 </span><span style=\"font-family:仿宋_GB2312\">公告期限：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">采购结果公告:</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">是/否 </span><span style=\"font-family:仿宋_GB2312\">公告期限：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体\">三、质疑基本情况</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">投诉人于</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">年</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">月</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp; </span><span style=\"font-family:仿宋_GB2312\">日,向</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">提出质疑，质疑事项为：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:24pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">采购人/代理机构</span><span style=\"font-family:仿宋_GB2312\">于</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">年</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">月</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">日,就质疑事项作出了答复/没有在法定期限内作出答复。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体\">四、投诉事项具体内容</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">投诉事项 1：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">事实依据：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">法律依据：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">投诉事项2</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">……</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体\">五、与投诉事项相关的投诉请求</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">请求：</span><span style=\"font-family: 仿宋_GB2312; text-decoration-line: underline;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">签字(签章)：</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312\">公章：</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">日期：</span><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp; </span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:justify; widows:0; orphans:0; font-size:16pt\"><span style=\"font-family:黑体; font-weight:bold\">投诉书制作说明：</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; text-align:justify; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">1.投诉人提起投诉时，应当提交投诉书和必要的证明材料，并按照被投诉人和与投诉事项有关的供应商数量提供投诉书副本。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">2.投诉人若委托代理人进行投诉的，投诉书应按照要求列明“授权代表”的有关内容，并在附件中提交由</span><span style=\"font-family:仿宋_GB2312\">投诉人签署的授权委托书。授权委托书应当载明代理人的姓名或者名称、代理事项、具体权限、期限和相关事项。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">3.投诉人若对项目的某一分包进行投诉，投诉书应列明具体分包号。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">4.投诉书应简要列明质疑事项，质疑函、质疑答复等作为附件材料提供。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">5.投诉书的投诉事项应具体、明确，并有必要的事实依据和法律依据。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">6.投诉书的投诉请求应与投诉事项相关。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-indent:32pt; font-size:16pt\"><span style=\"font-family:仿宋_GB2312\">7.投诉人为自然人的，投诉书应当由本人签字；投诉人为法人或者其他组织的，投诉书应当由法定代表人、主要负责人，或者其授权代表签字或者盖章，并加盖公章。</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; widows:0; orphans:0; font-size:15pt\"><span style=\"font-family:仿宋_GB2312; -aw-import:spaces\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:仿宋_GB2312; font-weight:bold; -aw-import:spaces\">&nbsp;</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; widows:0; orphans:0; font-size:15pt\"><span style=\"font-family:仿宋_GB2312; font-weight:bold; -aw-import:ignore\">&nbsp;</span></p><div><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; widows:0; orphans:0; font-size:9pt\"><span style=\"font-family:'Times New Roman'\">1</span></p><p style=\"margin-top:0pt; margin-bottom:0pt; text-align:center; widows:0; orphans:0; font-size:9pt\"><span style=\"font-family:'Times New Roman'; -aw-import:ignore\">&nbsp;</span></p></div></div></div>";
//        rtf2Pdf(content, "/Users/doujiale/Desktop/123.pdf");
//        String s = Pdf2HtmlUtil.convert2HtmlStr(new FileInputStream("/Users/doujiale/Desktop/123.pdf"));
//        System.out.println(s);
//        String s = PoiUtil.pdfToHtml(new FileInputStream("/Users/doujiale/Desktop/123.pdf"));
//        System.out.println(s);
        String path = "C:\\Users\\lls\\Desktop\\货物.html";
        String content = new String(Files.readAllBytes(Paths.get(path)));
        rtf2Pdf(content, "C:\\Users\\lls\\Desktop\\货物.pdf");

        String path2 = "C:\\Users\\lls\\Desktop\\服务.html";
        String content2 = new String(Files.readAllBytes(Paths.get(path2)));
        rtf2Pdf(content2, "C:\\Users\\lls\\Desktop\\服务.pdf");

    }

}

