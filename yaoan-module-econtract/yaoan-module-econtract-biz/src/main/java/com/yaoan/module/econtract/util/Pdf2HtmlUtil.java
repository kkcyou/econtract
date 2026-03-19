package com.yaoan.module.econtract.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.fit.pdfdom.PDFDomTreeConfig;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * @author doujiale
 */
public class Pdf2HtmlUtil {

    public static String parseWithPdfDomTree(InputStream is, int startPage, int endPage, PDFDomTreeConfig config)
            throws IOException, ParserConfigurationException {
        PDDocument pdf = PDDocument.load(is);
        PDFDomTree parser = new PDFDomTree(config);
        parser.setStartPage(startPage);
        parser.setEndPage(endPage);
        Writer output = new StringWriter();
        parser.writeText(pdf, output);
        pdf.close();
        return output.toString();
    }

    public static String convert2HtmlStr(InputStream inputStream) throws Exception {
        PDFDomTreeConfig config = PDFDomTreeConfig.createDefaultConfig();
        config.setImageHandler(PDFDomTreeConfig.saveToDirectory(new File("/Users/doujiale/Desktop/mnt/res/")));
        config.setFontHandler(config.getImageHandler());
        return parseWithPdfDomTree(inputStream, 0, 10, config);
    }
}