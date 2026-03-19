package com.yaoan.module.econtract.util;

import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;

public class HtmlToWordConverter {
//    public static void main(String[] args) throws IOException {
//        // 1. 创建一个新的Word文档
//        XWPFDocument document = new XWPFDocument();
//
//        // 2. 读取本地HTML文件
//        FileInputStream fis = new FileInputStream("E:\\user\\Desktop\\脚本\\1.html");
//        Document htmlDoc = Jsoup.parse(fis, "UTF-8", "");
//
//        // 3. 解析HTML内容并写入Word文档
//        Elements paragraphs = htmlDoc.select("p"); // 假设HTML中的内容都在<p>标签中
//        for (Element paragraph : paragraphs) {
//            XWPFParagraph wordParagraph = document.createParagraph();
//            XWPFRun run = wordParagraph.createRun();
//            run.setText(paragraph.text());
//        }
//
//        // 4. 保存Word文档到指定路径
//        FileOutputStream out = new FileOutputStream("E:\\user\\Desktop\\脚本\\6.docx");
//        document.write(out);
//        out.close();
//        document.close();
//        fis.close();
//        System.out.println("转换完成！");
//    }

    private static void addTableToDocument(XWPFDocument document, Element tableElement) {
        XWPFTable table = document.createTable();
        table.setWidth(Integer.parseInt("100%"));
        table.setCellMargins(50, 50, 50, 50);

        for (Element rowElement : tableElement.select("tr")) {
            XWPFTableRow row = table.createRow();
            for (Element cellElement : rowElement.select("td, th")) {
                XWPFTableCell cell = row.getCell(row.getTableCells().size());
                cell.setText(cellElement.text());
                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                // 设置单元格边框
                CTTcPr cellProps = cell.getCTTc().addNewTcPr();
                CTTcBorders borders = cellProps.addNewTcBorders();
                borders.addNewTop().setVal(STBorder.SINGLE);
                borders.addNewBottom().setVal(STBorder.SINGLE);
                borders.addNewLeft().setVal(STBorder.SINGLE);
                borders.addNewRight().setVal(STBorder.SINGLE);
            }
        }
    }

    // 视图模式
    public void convertHtmlToWord(String htmlContent, String outputPath) throws IOException {
        // 创建一个XWPFDocument对象，代表一个Word文档
        XWPFDocument document = new XWPFDocument();

        // 使用XWPFParagraph和XWPFRun来添加内容
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        // 设置该run的文本为HTML内容（这里简单处理，实际情况可能需要更细致的解析）
        run.setText(htmlContent);

        // 将生成的文档写入文件
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            document.write(out);
        }

        document.close();
    }

    public static void main(String[] args) throws IOException {
        HtmlToWordConverter converter = new HtmlToWordConverter();

        String inputPath = "E:\\user\\Desktop\\脚本\\1.html"; // 输入路径
        String outputPath = "E:\\user\\Desktop\\脚本\\6.docx"; // 输出路径

        try {
            // 读取HTML内容
            FileInputStream fis = new FileInputStream(inputPath);
            Document htmlDoc = Jsoup.parse(fis, "UTF-8", "");
            String htmlContent = htmlDoc.html();
            fis.close();

            // 步骤1：转换HTML到Word文档
            converter.convertHtmlToWord(htmlContent, outputPath);

            // 步骤2：设置Word文档的视图模式\
            try (XWPFDocument document = new XWPFDocument(new FileInputStream(outputPath))) {
                // 通过XML信息设置视图模式
                // 这个功能可能需要根据POI的最新版本进行特殊定制
                // 例如设置为"Web"模式（使用POI的Visual基础设置）
                // 实际实现可能根据业务需求和版本要求变化
//                document.getDocument().getBody().getBodyType().setVal(STBodyType.WEB);
//                document.getDocument().getBody()

                // 将更改写回文件
                try (FileOutputStream out = new FileOutputStream(outputPath)) {
                    document.write(out);
                }
            }

            System.out.println("文档已生成并设置视图模式。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
