package com.yaoan.module.econtract.util;

import com.yh.scofd.agent.ConvertException;
import com.yh.scofd.agent.HTTPAgent;
import com.yh.scofd.agent.wrapper.Const;
import com.yh.scofd.agent.wrapper.PackException;
import com.yh.scofd.agent.wrapper.model.MarkPosition;
import com.yh.scofd.agent.wrapper.model.TextInfo;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class YhAgentUtil {
    public static void main(String[] args) throws PackException, ConvertException, IOException {

        System.out.println(officeToOFD("/Users/doujiale/Desktop/采购合同.docx", "/Users/doujiale/Desktop/1234.ofd"));
    }

    public static String officeToOFD(String sourcePath, String targetPath) throws IOException, PackException, ConvertException {
        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
        File srcFile = new File(sourcePath);
        FileOutputStream out = null;
        out = new FileOutputStream(targetPath);
        agent.officeToOFD(srcFile, out);
        agent.close();
        return "done";
    }

    public static String officeToPDF(String sourcePath, String targetPath) throws IOException, PackException, ConvertException {
        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
        File srcFile = new File(sourcePath);
        FileOutputStream out = null;
        out = new FileOutputStream(targetPath);
        agent.officeToPDF(srcFile, out);
        agent.close();
        return "done";
    }

    /**
     * 文件转为 PDF 添加文字水印
     * @param srcFile ：源文件
     * @param out ：输出流
     * @param textinfo ：文本属性
     * text String 水印内容
     * fontName String 水印字体
     * fontSize float 水印字号
     * color String 水印的透明度及颜色
     * rotate int 旋转度数，只支持 45 度的倍数
     * xAlign Const.XAlign 水平定位方向-使用常量加
     * Const.XAlign.Center
     * yAlign Const.YAlign 垂直定位方向-使用常量加
     * Const.YAlign.Middle
     * tiled boolean 是否平铺
     * @param mk ：水印的必要的一些属性，详细属性见 MarkPosition
     * x float 填充物横坐标
     * y float 填充物纵坐标
     * width float 填充物宽度,单位毫米
     * height float 填充物高度，单位毫米
     * pages Pair<Integer,Integer>[ ] 需添加的页码范围，如 2~4
     * index int[ ] 需添加填充物的页码，如 2,3,4
     * INDEX_ALL static final int[ ] new int[0]，属性 index 默认值，便于使用
     * @param printable ：是否可打印
     * @param visible ：是否可显示
     * @throws IOException
     * @throws PackException
     * @throws ConvertException
     */
    public static void convertAndAddTextWatermarkToPDF(File srcFile, OutputStream out, TextInfo textinfo, MarkPosition mk, boolean printable, boolean visible) throws IOException, PackException, ConvertException{
        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
        try {
            //旋转角度是 45 的倍数。颜色后 2 位可调整文字透明度 00-FF。
//            TextInfo textinfo = new TextInfo(" 我 是 水 印 ", " 宋 体 ", 18, "#00ff0033", 45, Const.XAlign.Center, Const.YAlign.Middle);
//            MarkPosition mk = new MarkPosition(10, 10, 100, 100, new int[]{1, 3});
//            boolean printable = true;
//            boolean visible = true;
            agent.addTextMarkToPDF(srcFile, out, textinfo, mk, printable, visible);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            agent.close();
        }
    }

}
