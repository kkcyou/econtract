package com.yaoan.module.econtract.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;

/**
 * @author Pele
 */
public class PDF2JPGConverter {

    public void convertAll(String pdfPath, String outPath, int dpi) {
        try {
            PDDocument document;
            document = PDDocument.load(new File(pdfPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                try {
                    String filePath = Paths.get(outPath, page + "_" + removeSuffix(new File(pdfPath).getName()) + ".jpg").toString();
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, dpi, ImageType.RGB);
                    System.out.println("Dumping page " + page + " " + filePath);
                    ImageIOUtil.writeImage(bim, filePath, dpi);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
            document.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void convertSingle(String pdfPath, String outPath, int dpi, int pageNo) {
        try {
            PDDocument document;
            document = PDDocument.load(new File(pdfPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                if (page != pageNo) {
                    continue;
                }
                String filePath = Paths.get(outPath, page + "_" + removeSuffix(new File(pdfPath).getName()) + ".jpg").toString();
                try {
                    BufferedImage bim = pdfRenderer.renderImageWithDPI(page, dpi, ImageType.RGB);
                    System.out.println("Dumping page " + pageNo + " " + filePath);
                    ImageIOUtil.writeImage(bim, filePath, dpi);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
            document.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void convertMulti(String pdfPath, String outPath, int dpi, String pageNos) {
        try {
            PDDocument document;
            document = PDDocument.load(new File(pdfPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (String page : pageNos.split(",")) {
                try {
                    int pageNo = Integer.parseInt(page);
                    String filePath = Paths.get(outPath, page + "_" + new File(pdfPath).getName() + ".jpg").toString();

                    BufferedImage bim = pdfRenderer.renderImageWithDPI(pageNo, dpi, ImageType.RGB);
                    System.out.println("Dumping page " + pageNo + " " + filePath);
                    ImageIOUtil.writeImage(bim, filePath, dpi);
                } catch (Exception e) {
                    System.out.print(e);
                }
            }
            document.close();
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    /**
     * 去掉后缀
     */
    public String removeSuffix(String pdfPath) {

        // 去掉文件扩展名
        int dotIndex = pdfPath.lastIndexOf(".");
        if (dotIndex != -1) {
            String filePathWithoutExtension = pdfPath.substring(0, dotIndex);
            return filePathWithoutExtension;
        }
        return pdfPath;
    }
}