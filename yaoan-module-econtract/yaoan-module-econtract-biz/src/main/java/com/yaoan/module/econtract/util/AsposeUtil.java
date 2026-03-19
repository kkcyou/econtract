package com.yaoan.module.econtract.util;

import com.aspose.words.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class AsposeUtil {


    public static void main(String[] args) throws Exception {
        docx2Pdf("/Users/doujiale/Desktop/采购合同.docx","/Users/doujiale/Desktop/采购合同.pdf");
        docx2Pdf("/Users/doujiale/Desktop/电梯维修和保养服务合同.docx","/Users/doujiale/Desktop/电梯维修和保养服务合同.pdf");
        docx2Pdf("/Users/doujiale/Desktop/互联网接入服务合同.docx","/Users/doujiale/Desktop/互联网接入服务合同.pdf");
        docx2Pdf("/Users/doujiale/Desktop/物业服务合同.docx","/Users/doujiale/Desktop/物业服务合同.pdf");
        docx2Pdf("/Users/doujiale/Desktop/LED显示屏采购合同.docx","/Users/doujiale/Desktop/LED显示屏采购合同.pdf");
//        docx2Html("/Users/doujiale/Desktop/电梯维修和保养服务合同.docx","/Users/doujiale/Desktop/321.html");
//        PdfConvertHtmlUtil.pdf2html("/Users/doujiale/Desktop/采购合同PM.pdf","/Users/doujiale/Desktop/321.html");
//        String s1 = FileUtil.readString("/Users/doujiale/Desktop/321.html", Charset.defaultCharset());
//        System.out.println(s1);

    }

    public static void docx2Pdf(String srcPath, String desPath) throws Exception {
       // InputStream stream = new FileInputStream(srcPath);
       // InputStreamReader isr = new InputStreamReader(stream,"utf-8");
//        Document convertDoc = new Document(srcPath);
//        FontSettings fontSettings = new FontSettings();

//        // 设置编码，比如加载一个可能有中文的文档，指定UTF-8编码
        LoadOptions loadOptions = new LoadOptions();
        loadOptions.setEncoding(StandardCharsets.UTF_8);
//        Document convertDoc = new Document(stream, loadOptions);
        Document convertDoc = new Document(srcPath, loadOptions);


//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")) {
//            fontSettings.setFontsFolder("C:\\Windows\\Fonts" + File.separator, true);
//        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
//            fontSettings.setFontsFolder("/usr/share/fonts/windows" + File.separator, true);
//        } else {
//            System.out.println("Cannot recognize the operating system.");
//        }
//        convertDoc.setFontSettings(fontSettings);
        convertDoc.save(desPath, SaveFormat.PDF);
        //stream.close();
    }

    public static void docx2Html(String srcPath, String desPath) throws Exception {
        InputStream stream = new FileInputStream(srcPath);
        Document convertDoc = new Document(stream);
        convertDoc.save(desPath, SaveFormat.HTML);
    }

    public static void docxStream2Html(InputStream stream, String desPath) throws Exception {
        Document convertDoc = new Document(stream);
        convertDoc.save(desPath, SaveFormat.HTML);
    }

    public static String toHtmlString(File file) throws IOException {
        PDDocument doc = PDDocument.load(file);

        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(doc);

        org.jsoup.nodes.Document htmlDoc = Jsoup.parse(text);
        return htmlDoc.html();
    }
}
