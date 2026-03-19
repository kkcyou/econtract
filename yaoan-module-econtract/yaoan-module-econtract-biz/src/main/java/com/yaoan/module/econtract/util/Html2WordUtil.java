package com.yaoan.module.econtract.util;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.SaveFormat;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * Html 转 Word 工具类
 * </p>
 *
 * @author zhaojie
 * @description
 * @date 2020/11/23 16:00
 */
@Slf4j
public class Html2WordUtil {

    /**
     * `html` 转 `word` 【 注：本地图片不支持显示！！！ 需转换成在线图片 】
     *
     * @param htmlBytes: html字节码
     * @return word文件路径
     * @author zhengqing
     * @date 2020/11/24 11:52
     */
    @SneakyThrows(Exception.class)
    public static byte[] htmlBytes2WordBytes(byte[] htmlBytes) {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.insertHtml(new String(htmlBytes));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doc.save(outputStream, SaveFormat.DOCX);
        return outputStream.toByteArray();
    }

    /**
     * `html` 转 `word` 【 注：本地图片不支持显示！！！ 需转换成在线图片 】
     *
     * @param htmlBytes:    html字节码
     * @param wordFilePath: 待生成的word文件路径
     * @return word文件数据
     * @author zhengqing
     * @date 2020/11/24 11:52
     */
    @SneakyThrows(Exception.class)
    public static File htmlBytes2WordFile(byte[] htmlBytes, String wordFilePath) {
        String htmlString = new String(htmlBytes,"utf-8");
        
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.insertHtml(new String(htmlBytes));
        doc.save(wordFilePath, SaveFormat.DOCX);
        return new File(wordFilePath);
    }
    @SneakyThrows(Exception.class)
    public static File htmlString2WordFile(String htmlString, String wordFilePath) {
      
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.insertHtml(htmlString);
        doc.save(wordFilePath, SaveFormat.DOCX);
        return new File(wordFilePath);
    }
    @SneakyThrows(Exception.class)
    public static File convertHtml2Doc(byte[] htmlBytes, String wordFilePath) {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.insertHtml(new String(htmlBytes));
        doc.save(wordFilePath, SaveFormat.DOC);
        return new File(wordFilePath);
    }

//    @SneakyThrows(Exception.class)
//    public static File htmlBytes2WordFile(byte[] htmlBytes, String wordFilePath) {
//        // 创建一个新的文档
//        Document doc = new Document();
//        DocumentBuilder builder = new DocumentBuilder(doc);
//
//        // 插入HTML内容
//        builder.insertHtml(new String(htmlBytes));
//
//        // 遍历文档中的所有段落
//        for (Paragraph paragraph : (Iterable<Paragraph>) doc.getChildNodes(NodeType.PARAGRAPH, true)) {
//            for (Run run : (Iterable<Run>) paragraph.getRuns()) {
//                // 获取文本的样式
//                Font font = run.getFont();
//
//                // 如果文本有背景颜色，将其转换为对应的前景色
//                if (font.getShading().getBackgroundPatternColor() != Color.WHITE) {
//                    font.setColor(font.getShading().getBackgroundPatternColor());
//                    font.getShading().clearBackgroundPatternColor();
//                }
//            }
//        }
//
//        // 遍历文档中的所有形状
//        for (Shape shape : (Iterable<Shape>) doc.getChildNodes(NodeType.SHAPE, true)) {
//            // 如果形状是复选框
//            if (shape.getOleFormat() != null && shape.getOleFormat().getProgId().equals("Forms.CheckBox.1")) {
//                // 设置复选框的勾选状态
//                shape.setChecked(true);
//            }
//        }
//
//        // 保存文档为Word格式
//        doc.save(wordFilePath, SaveFormat.DOCX);
//
//        // 返回生成的Word文件
//        return new File(wordFilePath);
//    }
//
//    public static byte[] readHtmlFile(String filePath) throws IOException {
//        return Files.readAllBytes(Paths.get(filePath));
//    }

    /**
     * `html` 转 `word` 【注`doc`生成的html中的图片路径中中文是被转义处理过的，再生成word时图片便看不了，需单独做处理，`docx`无此问题】 【 注：此方式会丢失一定格式 】
     *
     * @param html:         html内容
     * @param fileRootPath: 文件根位置
     * @param wordFileName: 待生成的word文件名
     * @return word文件路径
     * @author zhengqing
     * @date 2020/11/23 16:04
     */
    @SneakyThrows(Exception.class)
    public static String html2Word(String html, String fileRootPath, String wordFileName) {
        final String wordFilePath = fileRootPath + "/" + wordFileName;
        byte htmlBytes[] = html.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlBytes);
        POIFSFileSystem poifs = new POIFSFileSystem();
        DirectoryEntry directory = poifs.getRoot();
        DocumentEntry documentEntry = directory.createDocument("WordDocument", inputStream);
        FileOutputStream outputStream = new FileOutputStream(wordFilePath);
        poifs.writeFilesystem(outputStream);
        inputStream.close();
        outputStream.close();
        return wordFilePath;
    }

    public static void main(String[] args) throws Exception {
        // 指定输入和输出文件路径
        String inputFilePath = "E:\\user\\Desktop\\脚本\\1.html";
        String outputFilePath = "E:\\user\\Desktop\\脚本\\5.docx";

        // 读取本地HTML文件
        byte[] htmlBytes = readHtmlFile(inputFilePath);

        // 将HTML字节码转换为Word文件
        htmlBytes2WordFile(htmlBytes, outputFilePath);

        System.out.println("Document saved as " + outputFilePath);
    }

    public static byte[] readHtmlFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }
    public static void docToDocx(String docPath, String docxPath) {
        File inputWord = new File(docPath);
        File outputFile = new File(docxPath);
        try (InputStream docInputStream = new FileInputStream(inputWord);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            IConverter converter = LocalConverter.builder().build();
            boolean flag = false;
//            if("docx".equals(fileType)){// docx转doc
            flag = converter.convert(docInputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.DOCX).execute();
//            } else if ("doc".equals(fileType)){ //
//                flag = converter.convert(docxInputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
//            }
            if (flag) {
                converter.shutDown();
            }
            System.out.println("转换成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}