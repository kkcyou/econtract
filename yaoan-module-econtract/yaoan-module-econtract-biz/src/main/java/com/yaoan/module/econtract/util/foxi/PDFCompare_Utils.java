package com.yaoan.module.econtract.util.foxi;

import com.foxit.sdk.PDFException;
import com.foxit.sdk.common.fxcrt.Matrix2D;
import com.foxit.sdk.common.fxcrt.PointF;
import com.foxit.sdk.common.fxcrt.PointFArray;
import com.foxit.sdk.common.fxcrt.RectF;
import com.foxit.sdk.pdf.GraphicsObjects;
import com.foxit.sdk.pdf.PDFDoc;
import com.foxit.sdk.pdf.PDFPage;
import com.foxit.sdk.pdf.TextPage;
import com.foxit.sdk.pdf.annots.*;
import com.foxit.sdk.pdf.graphics.*;
import com.foxit.sdk.pdf.objects.PDFArray;
import com.foxit.sdk.pdf.objects.PDFDictionary;
import com.foxit.sdk.pdf.objects.PDFObject;

import java.util.*;

// 单条差异记录数据结构
class SentenceDiff {
    public static final int TYPE_MATCH = 0;
    public static final int TYPE_DELETE = 1;
    public static final int TYPE_INSERT = 2;
    public static final int TYPE_REPLACE = 3;
    public static final int TYPE_STYLE_CHANGE = 4;

    int diffType;                 // 差异类型
    int start1;                   // 文档1差异起始索引（无则-1）
    int end1;                     // 文档1差异结束索引（无则-1）
    int start2;                   // 文档2差异起始索引（无则-1）
    int end2;                     // 文档2差异结束索引（无则-1）
    String content1;              // 文档1差异内容
    String content2;              // 文档2差异内容
    List<RectF> rects1;           // 文档1差异区域集合
    List<RectF> rects2;           // 文档2差异区域集合
    List<Integer> page1;                    // 文档1差异所在页码
    List<Integer> page2;                    // 文档2差异所在页码

    public SentenceDiff() {
        this.start1 = -1;
        this.end1 = -1;
        this.start2 = -1;
        this.end2 = -1;
        this.page1 =new ArrayList<>();                    // 文档1差异所在页码
        this. page2 =new ArrayList<>();
        this.rects1 = new ArrayList<>();
        this.rects2 = new ArrayList<>();
        this.content1 = "";
        this.content2 = "";
    }
}

// 差异结果集合管理类
class SentenceDiffResult {
    private List<SentenceDiff> diffList = new ArrayList<>();

    public void addDiff(SentenceDiff diff) {
        diffList.add(diff);
    }

    public List<SentenceDiff> getDiffList() {
        return diffList;
    }
}
public class PDFCompare_Utils {
    private static final int COLOR_REPLACE = 0x9999ff;
    private static final int COLOR_INSERT = 0xff9999;
    private static final int COLOR_CHANGE_STYLE = 0xff99ff;
    private static final int COLOR_DELETE_TIP = 0xff0000;
    private static final String TIP_INSERT = "插入";
    private static final String TIP_REPLACE = "替换";
    private static final String TIP_DELETE = "删除";


    // ContentInfo类
    public static class ContentInfo {
        // 内容类型枚举
        public enum ContentType {
            e_ContentTypeText, e_ContentTypeImage, e_ContentTypeTable, e_ContentTypePath
        }

        // 内容样式枚举
        public enum ContentStyle {
            e_ContentStyle_Normal, e_ContentStyle_Underline
        }

        // 成员变量
        public ContentType type;
        public String text;  // WString对应Java的String
        public int pageIndex;
        public RectF textPos;
        public List<Integer> charIndexArray;
        public List<RectF> charRects;
        public List<ContentStyle> styleArray;

        // 构造方法
        public ContentInfo() {
            charIndexArray = new ArrayList<>();
            charRects = new ArrayList<>();
            styleArray = new ArrayList<>();
        }

        public ContentInfo(ContentType contentType, String str, int pIndex, RectF rectF,
                           List<RectF> charRects1, List<ContentStyle> contentStyle) {
            this();
            this.type = contentType;
            this.text = str;
            this.pageIndex = pIndex;
            this.textPos = rectF;
            this.charRects = charRects1;
            this.styleArray = contentStyle;
        }
    }

    // DocInfos类
    public static class DocInfos {
        // 成员变量
        public PDFDoc doc;
        public ContentInfo temp;
        public List<ContentInfo> contentInfos;

        // 构造函数
        public DocInfos(PDFDoc doc1) {
            this.doc = doc1;
            this.temp = new ContentInfo();
            this.contentInfos = new ArrayList<>();
        }



        // 获取内容信息列表
        public List<ContentInfo> getContentInfos() {
            return contentInfos;
        }

        // 添加内容信息
        public void addContentInfo(ContentInfo contentInfo) {
            contentInfos.add(contentInfo);
        }


    }


    // DocInfosNew类
    public static class DocSentencesInfo {
        // 成员变量
        public PDFDoc doc;
        public List<String> sents;
        public List<List<Integer>> pageIndexArray;
        public List<List<Integer>> charPosArray;
        public List<List<RectF>> charRectArray;
        public List<List<ContentInfo.ContentStyle>> contentStyleArray;

        // 构造函数
        public DocSentencesInfo(PDFDoc doc1) {
            this.doc = doc1;
            this.sents = new ArrayList<>();
            this.pageIndexArray = new ArrayList<>();
            this.charPosArray = new ArrayList<>();
            this.charRectArray = new ArrayList<>();
            this.contentStyleArray = new ArrayList<>();
        }
    }

    // ProgressTracker类
    public static class ProgressTracker {
        // 处理阶段枚举
        public enum ProcessStage {
            state_0_DOCUMENT_STRUCTURE_PARSING,    // 文档结构解析
            state_1_SENTENCE_SEGMENTATION,         // 语句分段
            state_2_CHARACTER_COORDINATE_MAPPING,  // 字符坐标映射
            state_3_ROUGH_LINE_BY_LINE,            // 粗略比较
            state_4_SENTENCE_COMPARISON,           // 逐语句对比
            state_5_FINISHED,
            state_6_FALIER
        }

        // 成员变量
        public ProcessStage curStage;
        public int total;
        public int curProgress;

        // 构造函数
        public ProgressTracker() {
            curStage = ProcessStage.state_0_DOCUMENT_STRUCTURE_PARSING;
            curProgress = 0;
            total = 0;
        }
    }

    public static   void GetGraphicsObject(GraphicsObjects gobjs, PDFPage page, RectF rect, List<GraphicsObject> gobjList) throws PDFException {
        GraphicsObjectArray gArray= page.getGraphicsObjectsAtRectangle(rect, GraphicsObject.e_TypeText);

        for (int i = 0; i < gArray.getSize(); i++)
        {
            GraphicsObject gObj = gArray.getAt(i);
            //有的说明书字号很小，容易把上下两行误获取到，所以要进行二次判断。
            //不能指定一个值把RectF往内缩，因为字号不固定  有的12号，有的4号。
            RectF rectObj = gObj.getRect();
            float h = rectObj.height();
            rectObj.intersect(rect);
            float h2 = rectObj.height();
            if ((rectObj.height() / gObj.getRect().height()) > 0.8f)
            {
                gobjList.add(gArray.getAt(i));
            }
        }
        GraphicsObjectArray  gArrayFX = page.getGraphicsObjectsAtRectangle(rect, GraphicsObject.e_TypeFormXObject);
        for (int i = 0; i < gArrayFX.getSize(); i++)
        {
            GraphicsObject gObj = gArrayFX.getAt(i);
            GetGraphicsObjectFX(gObj.getFormXObject().getGraphicsObjects(),page, rect, gobjList);
        }
    }
    static void GetGraphicsObjectFX(GraphicsObjects gobjs, PDFPage page, RectF rect, List<GraphicsObject>gobjList) throws PDFException {
        //GraphicsObjectArray gArray = page.GetGraphicsObjectsAtRectangle(rect, GraphicsObject::Type::e_TypeFormXObject);

        for (int i = 0; i < gobjs.getGraphicsObjectCount(); i++)
        {
            GraphicsObject gObj = gobjs.getGraphicsObject(i);
            if (gObj.getType() == GraphicsObject.e_TypeFormXObject)
            {
                FormXObject fxObj = gObj.getFormXObject();
                GetGraphicsObjectFX(fxObj.getGraphicsObjects(), page,rect, gobjList);
            }
            else if (gObj.getType() == GraphicsObject.e_TypeText)
            {
                //有的说明书字号很小，容易把上下两行误获取到，所以要进行二次判断。
                //不能指定一个值把RectF往内缩，因为字号不固定  有的12号，有的4号。
                RectF rectObj = gObj.getRect();
                float h = rectObj.height();
                rectObj.intersect(rect);
                float h2 = rectObj.height();
                if ((rectObj.height() / gObj.getRect().height()) > 0.8f)
                {
                    gobjList.add(gObj);
                }
            }

        }

    }
    public static void GetTextPosInfo(
            PDFPage page,
            TextPage textPage,
            RectF rectF,
            DocInfos docInfo,
            ContentInfo.ContentType cType,
            boolean isTableCell
    ) throws PDFException {
        // 1. 获取矩形内文本并去除换行符
        String str = textPage.getTextInRect(rectF);
        str = str.replace("\n", ""); // 替换换行符
        str = str.replace("\r", ""); // 替换回车符

        // 文本长度>0才继续处理
        if (str.length() > 0) {

            //  调用辅助方法获取图形对象列表
            List<GraphicsObject> gobjs = new ArrayList<>();
            GetGraphicsObject(page, page, rectF, gobjs);

            // 对图形对象排序
            Collections.sort(gobjs, new Comparator<GraphicsObject>() {
                @Override
                public int compare(GraphicsObject obj1, GraphicsObject obj2) {
                    // 排序规则：(obj.rect.top - obj.rect.height/2) 降序
                    RectF rect1 = obj1.getRect();
                    RectF rect2 = obj2.getRect();
                    float midY1 = rect1.getTop() - (rect1.height() / 2);
                    float midY2 = rect2.getTop() - (rect2.height() / 2);
                    // 降序：midY1大的在前 → 若midY1>midY2，返回-1（obj1在前）
                    return Float.compare(midY2, midY1);
                }
            });

            // 5. 划分文本行为
            float curPosY = 0;
            List<List<GraphicsObject>> gobjsRows = new ArrayList<>(); // 行列表
            List<GraphicsObject> gRow = new ArrayList<>(); // 当前行


            for (int i = 0; i < gobjs.size(); i++) {
                GraphicsObject curObj = gobjs.get(i);
                TextObject tObj = curObj.getTextObject(); // 转换为文本对象

                // 计算当前文本高度
                TextState textState = tObj.getTextState(page);
                Matrix2D matrix = tObj.getMatrix();
                float h = textState.getFont_size()  * matrix.getA();
                RectF textRect = tObj.getRect(); // 获取文本矩形
                // 第一行初始化
                if (i == 0) {
                    curPosY = textRect.getBottom(); // 记录当前行底部Y坐标
                    gRow.add(tObj); // 加入当前行
                } else {
                    // 判断是否需要换行（Y差值>h*0.8）
                    float bottomDiff = Math.abs(curPosY - textRect.getBottom());
                    if (bottomDiff > h * 0.8) {
                        // 对当前行按left升序排序
                        Collections.sort(gRow, new Comparator<GraphicsObject>() {
                            @Override
                            public int compare(GraphicsObject obj1, GraphicsObject obj2) {
                                // 按矩形left升序：left小的在前
                                return Float.compare(
                                        obj1.getRect().getLeft(),
                                        obj2.getRect().getLeft()
                                );
                            }
                        });
                        // 当前行加入行列表，重置当前行
                        gobjsRows.add(gRow);
                        gRow = new ArrayList<>();
                        curPosY = textRect.getBottom();
                        gRow.add(tObj);
                    } else {
                        // 不换行，加入当前行
                        gRow.add(tObj);
                    }
                }

                // 最后一行处理
                if (i == gobjs.size() - 1) {
                    // 对最后一行排序
                    Collections.sort(gRow, new Comparator<GraphicsObject>() {
                        @Override
                        public int compare(GraphicsObject obj1, GraphicsObject obj2) {
                            return Float.compare(
                                    obj1.getRect().getLeft(),
                                    obj2.getRect().getLeft()
                            );
                        }
                    });
                    // 加入行列表
                    gobjsRows.add(gRow);
                }
            }

            // 提取字符坐标与样式
            String strSent = ""; // 最终文本
            List<RectF> charPosArray = new ArrayList<>(); // 字符矩形列表
            List<ContentInfo.ContentStyle> contentStyleArray = new ArrayList<>(); // 样式列表

            for (List<GraphicsObject> row : gobjsRows) {
                for (GraphicsObject obj : row) {
                    TextObject textObject = obj.getTextObject();
                    String text = textObject.getText();
                    String content = ""; // 临时内容（去空格）

                    // 遍历每个字符
                    for (int charIndex = 0; charIndex < textObject.getCharCount(); charIndex++) {
                        // 获取单个字符并去空格
                        String charStr = String.valueOf(text.charAt(charIndex)); // 辅助方法：获取指定索引字符
                        charStr = charStr.replace(" ", "");

                        // 跳过空字符
                        if (charStr.length() < 1) {
                            continue;
                        }

                        // 获取字符坐标与宽高
                        PointF charPos = textObject.getCharPos(charIndex);
                        float charWidth = textObject.getCharWidthByIndex(charIndex);
                        float charHeight = textObject.getCharHeightByIndex(charIndex);

                        // 创建字符矩形并与目标矩形相交
                        RectF charRect = new RectF(
                                charPos.getX(),
                                charPos.getY(),
                                charPos.getX() + charWidth,
                                charPos.getY() + charHeight
                        );
                        charRect.intersect(rectF); // 相交处理

                        // 有效矩形才保留
                        if (charRect.width() > 0 && charRect.height() > 0) {
                            // 重新创建原始字符矩形
                            charRect = new RectF(
                                    charPos.getX(),
                                    charPos.getY(),
                                    charPos.getX() + charWidth,
                                    charPos.getY() + charHeight
                            );
                            charPosArray.add(charRect); // 加入字符矩形列表

                            // 获取字符样式
                            ContentInfo.ContentStyle style = ContentInfo.ContentStyle.e_ContentStyle_Normal;
                            contentStyleArray.add(style); // 加入样式列表

                            content +=  text.charAt( charIndex); // 拼接有效字符
                        }
                    }
                    strSent += content; // 拼接最终文本
                }
            }

            //  表格单元格特殊处理
            if (isTableCell) {
                List<ContentInfo> contentInfos = docInfo.getContentInfos();
                // 若已有内容，且最后一个内容的文本末尾不是换行，添加换行
                if (!contentInfos.isEmpty()) {
                    ContentInfo lastInfo = contentInfos.get(contentInfos.size() - 1);
                    String lastText = lastInfo.text;
                    if (lastText != null && !lastText.endsWith("\n")) {
                        strSent = "\n" + strSent;
                    }
                }
                // 末尾添加换行
                strSent += "\n";
            }

            // 创建ContentInfo并加入DocInfos
            ContentInfo contentInfo = new ContentInfo(
                    cType,          // 内容类型
                    strSent,        // 文本
                    page.getIndex(),// 页面索引
                    rectF,          // 矩形
                    charPosArray,   // 字符矩形列表
                    contentStyleArray // 样式列表
            );
            docInfo.getContentInfos().add(contentInfo);
        }
    }
    static DocInfos DocParse(PDFDoc doc) throws PDFException {
        DocInfos docInfo =  new DocInfos(doc);

        // 遍历 PDF 所有页面
        for (int pageIndex = 0; pageIndex < doc.getPageCount(); pageIndex++) {
            // 获取当前页面
            PDFPage currentPage = doc.getPage(pageIndex);

            //  页面解析与标准化
            currentPage.startParse(0, null, true);
            currentPage.normalize();
            currentPage.startParse(0, null, true);

            RectF pageFullRect = new RectF(
                    0, 0, currentPage.getWidth(), currentPage.getHeight()
            );
            TextPage textPage = new TextPage(currentPage, 0);

            GetTextPosInfo(
                    currentPage,
                    textPage,
                    pageFullRect,
                    docInfo,
                    ContentInfo.ContentType.e_ContentTypeText,
                    false
            );
        }
        return docInfo;
    }
    static String GetStringInDocInfo(DocInfos docInfo) {

        int curPage = -1;
        String textContents = "";
        for (int i = 0; i < docInfo.contentInfos.size(); i++) {

            ContentInfo contentInfo = docInfo.contentInfos.get(i);
            String str = contentInfo.text;
            if (contentInfo.text != null) {
                if (contentInfo.type != ContentInfo.ContentType.e_ContentTypeTable)
                {
                    textContents = textContents + contentInfo.text;
                }
                else
                {
                    textContents = textContents + contentInfo.text;
                }
            }
        }
        return textContents;
    }
    public static List<String> splitAndKeepPeriod(String text) {
        List<String> segments = new ArrayList<>();
        // 空字符串直接返回
        if (text == null || text.isEmpty()) {
            return segments;
        }

        // 定义分隔符集合
        String delimiters = "";//""】。\n";
        int textLength = text.length(); // 文本总长度
        int start = 0; // 片段起始索引

        // 遍历文本字符
        for (int i = 0; i < textLength; i++) {
            char currentChar = text.charAt(i);
            // 检查当前字符是否为分隔符
            if (delimiters.indexOf(currentChar) != -1) {
                // 提取从 start 到当前位置（包含当前字符）的片段
                int length = i - start + 1;
                String segment = text.substring(start, start + length);

                // 过滤纯空白片段
                boolean isWhitespace = true;
                for (int j = 0; j < segment.length(); j++) {
                    if (!Character.isWhitespace(segment.charAt(j))) {
                        isWhitespace = false;
                        break;
                    }
                }

                if (!isWhitespace) {
                    // 移除片段中的换行符
                    String segmentWithoutNewline = segment.replace("\n", "");
                    // 非空片段加入结果
                    if (!segmentWithoutNewline.isEmpty()) {
                        segments.add(segmentWithoutNewline);
                    }
                }

                start = i + 1; // 移动到下一个字符，作为新片段的起始
            }
        }

        // 处理最后一个分隔符之后的剩余内容
        if (start < textLength) {
            String remaining = text.substring(start); // 提取剩余内容

            // 检查剩余内容是否为纯空白
            boolean isWhitespace = true;
            for (int j = 0; j < remaining.length(); j++) {
                if (!Character.isWhitespace(remaining.charAt(j))) {
                    isWhitespace = false;
                    break;
                }
            }

            // 非空白且非空的剩余内容加入结果
            if (!isWhitespace && !remaining.isEmpty()) {
                segments.add(remaining);
            }
        }

        return segments;
    }

    public static DocSentencesInfo GetSentences(DocInfos docInfos, String text) {
        // 初始化变量
        List<ContentInfo> contentInfos = docInfos.getContentInfos(); // 获取docInfos中的内容列表
        String text0 = text;
        List<String> sents = new ArrayList<>();

        // 初始化二维列表
        List<List<Integer>> pageIndexArray = new ArrayList<>();
        List<List<Integer>> charPosArray = new ArrayList<>();
        List<List<RectF>> charRectArray = new ArrayList<>();

        // 分割文本并保留句点
        sents = splitAndKeepPeriod(text0);

        // 初始化循环变量
        int charPosText = 0; // charPos_Text：当前ContentInfo的字符起始位置
        int curCInfoIndex = 0; //  curCInfoInex：当前遍历的ContentInfo索引
        //PageIndex：未实际使用，注释掉

        // 初始化结果列表
        List<List<RectF>> charPosArrayNew = new ArrayList<>(); // 字符矩形结果（二维列表）
        List<List<Integer>> pageIndexArrayNew = new ArrayList<>(); // 页面索引结果（二维列表）
        List<List<ContentInfo.ContentStyle>> contentStyleArrayNew = new ArrayList<>(); // 样式结果（二维列表）

        //  遍历每个句子
        for (int sentIndex = 0; sentIndex < sents.size(); sentIndex++) {
            String str = sents.get(sentIndex); // 当前句子
            int sentLen = str.length(); //
            int needCount = sentLen; // 还需获取的字符数量（当前句子未匹配的字符数）

            // 每个句子的临时存储：字符矩形、页面索引、样式
            List<RectF> charArray = new ArrayList<>(); // 单个句子的字符矩形列表
            List<Integer> pageIndexSubArray = new ArrayList<>(); // 单个句子的页面索引列表
            List<ContentInfo.ContentStyle> styleSubArray = new ArrayList<>(); // 单个句子的样式列表

            //  循环匹配字符
            while (needCount > 0 && curCInfoIndex >= 0) {
                // 边界判断：避免curCInfoIndex超出contentInfos范围（防止数组越界）
                if (curCInfoIndex >= contentInfos.size()) {
                    break; // 无更多ContentInfo可匹配，退出循环
                }

                // 获取当前ContentInfo
                ContentInfo curCInfo = contentInfos.get(curCInfoIndex);
                // 去除当前ContentInfo文本中的换行符
                String curText = curCInfo.text.replace("\n", "");

                // 获取当前ContentInfo的字符矩形和样式列表
                List<RectF> charCurArray = curCInfo.charRects;
                List<ContentInfo.ContentStyle> styleCurArray = curCInfo.styleArray;

                // 分支1：当前ContentInfo的剩余字符足够匹配当前句子
                if (charCurArray.size() - charPosText >= needCount) {
                    // 截取字符矩形子列表
                    List<RectF> subList = new ArrayList<>(charCurArray.subList(charPosText, charPosText + needCount));
                    // 截取样式子列表
                    List<ContentInfo.ContentStyle> styleSubList = new ArrayList<>(styleCurArray.subList(charPosText, charPosText + needCount));

                    // 将子列表加入当前句子的临时存储
                    charArray.addAll(subList);
                    styleSubArray.addAll(styleSubList);

                    // 获取当前ContentInfo的页面索引，并添加到页面索引列表
                    int pIndex = curCInfo.pageIndex;
                    for (int i = 0; i < needCount; i++) {
                        pageIndexSubArray.add(pIndex);
                    }

                    // 更新字符起始位置或ContentInfo索引
                    if (needCount < charCurArray.size() - charPosText) {
                        // 剩余字符未用完：更新charPosText
                        charPosText += subList.size();
                    } else {
                        // 剩余字符刚好用完：切换到下一个ContentInfo，重置charPosText
                        curCInfoIndex++;
                        charPosText = 0;
                    }



                    break; // 当前句子匹配完成，退出while循环
                }

                // 分支2：当前ContentInfo的剩余字符不足，需全部截取
                else {
                    // 截取当前ContentInfo的剩余字符矩形（从charPosText到末尾）
                    List<RectF> subList = new ArrayList<>(charCurArray.subList(charPosText, charCurArray.size()));
                    // 截取剩余样式列表
                    List<ContentInfo.ContentStyle> styleSubList = new ArrayList<>(styleCurArray.subList(charPosText, styleCurArray.size()));

                    // 加入当前句子的临时存储
                    charArray.addAll(subList);
                    styleSubArray.addAll(styleSubList);

                    // 添加页面索引
                    int pIndex = curCInfo.pageIndex;
                    int addCount = charCurArray.size() - charPosText; // 需添加的页面索引数量
                    for (int i = 0; i < addCount; i++) {
                        pageIndexSubArray.add(pIndex);
                    }

                    // 切换到下一个ContentInfo，重置charPosText，更新剩余需匹配字符数
                    curCInfoIndex++;

                    charPosText = 0;
                    needCount -= subList.size(); // 剩余需匹配字符数减少

                    continue;
                }
            }

            // 将当前句子的临时存储加入结果列表
            charPosArrayNew.add(charArray);
            pageIndexArrayNew.add(pageIndexSubArray);
            contentStyleArrayNew.add(styleSubArray);
        }

        // 创建DocInfosNew实例并赋值
        DocSentencesInfo docSentencesInfo = new DocSentencesInfo(docInfos.doc);
        docSentencesInfo.sents=sents; // 设置句子列表
        docSentencesInfo.pageIndexArray=pageIndexArrayNew; // 设置页面索引数组
        docSentencesInfo.charRectArray=charPosArrayNew; // 设置字符矩形数组
        docSentencesInfo.contentStyleArray=(contentStyleArrayNew); // 设置样式数组

        return docSentencesInfo;
    }
    public static int levenshteinDistance(String s1, String s2) {
        // 处理空字符串边界：若任一字符串为空，编辑距离为另一字符串长度
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int m = s1.length(); // 字符串1长度
        int n = s2.length(); // 字符串2长度

        // 优化1：始终让短字符串作为内层循环（减少数组长度，降低空间复杂度）
        if (m > n) {
            // 交换 s1 和 s2，同时交换长度 m 和 n
            String tempStr = s1;
            s1 = s2;
            s2 = tempStr;

            int tempLen = m;
            m = n;
            n = tempLen;
        }

        // 优化2：用一维数组滚动计算
        int[] prev = new int[n + 1]; // 上一行：长度为 n+1（s2 长度+1）
        int[] curr = new int[n + 1]; // 当前行：长度同 prev

        // 初始化第一行
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        // 滚动计算每一行
        for (int i = 1; i <= m; i++) {
            // 初始化当前行第一列
            curr[0] = i;

            // 计算当前行每一列
            for (int j = 1; j <= n; j++) {
                // 计算替换成本：字符相同则成本0，不同则成本1
                char charS1 = s1.charAt(i - 1); // s1 第 i 个字符（索引 i-1）
                char charS2 = s2.charAt(j - 1); // s2 第 j 个字符（索引 j-1）
                int cost = (charS1 == charS2) ? 0 : 1;

                // 编辑距离公式：取「左上角（替换）、上方（删除）、左侧（插入）」的最小值

                int replaceCost = prev[j - 1] + cost; // 替换：上一行前一列 + 成本
                int deleteCost = prev[j] + 1;         // 删除：上一行当前列 + 1（删除 s1 字符）
                int insertCost = curr[j - 1] + 1;     // 插入：当前行前一列 + 1（插入 s2 字符）
                curr[j] = Math.min(Math.min(replaceCost, deleteCost), insertCost);
            }

            // 交换 prev 和 curr
            int[] tempArr = prev;
            prev = curr;
            curr = tempArr;

            // 重置 curr
            for (int j = 0; j <= n; j++) {
                curr[j] = 0;
            }
        }

        // 最终结果存于 prev[n]
        return prev[n];
    }
    static double calculateSimilarity(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        double threshold = 0.3;
        // 长度差异过大时直接返回低相似度（提前过滤）
        if (Math.abs(len1 - len2) > Math.max(len1, len2) * (1 - threshold)) {
            return 0.0;
        }
        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(len1, len2);
        return maxLength == 0 ? 1.0 : 1.0 - (double)distance / maxLength;
    }  static Highlight CreatHighLightRect(PDFPage page, int color, QuadPointsArray quad_points_array, String csType,String diff) throws PDFException {

        Highlight highlight= new Highlight( page.addAnnot(Annot.e_Highlight, new RectF(0,0,0,0)));
        //This flag is used for printing annotations.
        highlight.setFlags(4);
        highlight.setTitle(csType);
        highlight.setQuadPoints(quad_points_array);
        //highlight.SetSubject(L"Highlight");
        //highlight.setTitle("Foxit SDK");
        highlight.setContent(diff);
        highlight.setBorderColor(color);
        // Appearance should be reset.
        highlight.resetAppearanceStream();
        return highlight;
//        PDFDictionary dict=highlight.getDict();
//        dict.setAtString("customEntries",anotherTips);
    }

    static QuadPoints RectToQuad(RectF item)
    {
        QuadPoints quad_points=new QuadPoints();
        quad_points.setFirst(new  PointF(item.getLeft(), item.getTop()));
        quad_points.setSecond(new  PointF(item.getRight(), item.getTop()));
        quad_points.setThird(new  PointF(item.getLeft(), item.getBottom()));
        quad_points.setFourth( new  PointF(item.getRight(), item.getBottom()));;
        return quad_points;
    }
    private static String RandomUID() {
        String uuid = "";
        String temp = "0123456789qwertyuiopasdfghjklzxcvbnm";

        for (int n = 0; n < 16; n++) {
            String uuid_temp;
            Random random = new Random();
            int b = random.nextInt() % 255;

            switch (n) {
                case 6:
                    uuid_temp = String.format("4%x", b % 15);
                    break;
                case 8:
                    int index = random.nextInt() % temp.length();
                    if (index < 0)
                        index = (-1) * index;
                    uuid_temp = String.format("%c%x", temp.charAt(index), b % 15);
                    break;
                default:
                    uuid_temp = String.format("%02x", b);
                    break;
            }
            uuid += uuid_temp;

            switch (n) {
                case 3:
                case 5:
                case 7:
                case 9:
                    uuid += '-';
                    break;
            }
        }
        return uuid;
    }
    public static PolyLine CreatCalloutRect1(PDFPage page, int color, PointF point, String content) throws PDFException {
        PolyLine polyline= new PolyLine(page.addAnnot(Annot.e_PolyLine,new  RectF(point.getX(), point.getY() - 10, point.getX() + 30, point.getY())));
        //This flag is used for printing annotations.
        polyline.setFlags(4);
        PointFArray vertexe_array=new PointFArray();
        vertexe_array.add(point);
        vertexe_array.add(new  PointF(point.getX()+3, (float) (point.getY()-3.5)));
        vertexe_array.add(new PointF(point.getX() - 3, (float) (point.getY() - 3.5)));
        vertexe_array.add(point);
        //   polyline.setUniqueID(UUID);
        polyline.setVertexes(vertexe_array);
        polyline.setSubject("PolyLine");
        polyline.setStyleFillColor(0xff0000);
        polyline.setTitle(TIP_DELETE);
        polyline.setContent(content);


        BorderInfo binfo= polyline.getBorderInfo();
        binfo.setWidth(1.5f);
        polyline.setBorderInfo(binfo);
        // Appearance should be reset.
        polyline.resetAppearanceStream();
        return  polyline;
    }
    public static void CompareSentences(int index1, int index2, DocSentencesInfo docInfos1, DocSentencesInfo docInfos2) throws PDFException {
        // 获取两个句子及关联数据
        String s1 = docInfos1.sents.get(index1);
        String s2 = docInfos2.sents.get(index2);
        PDFDoc doc1 = docInfos1.doc;
        PDFDoc doc2 = docInfos2.doc;

        // 需忽略的字符
        String charsToBeIgnored = "";

        // 获取字符矩形、页码、样式列表
        List<RectF> charRects1 = docInfos1.charRectArray.get(index1);
        List<RectF> charRects2 = docInfos2.charRectArray.get(index2);
        List<Integer> pageIndices1 = docInfos1.pageIndexArray.get(index1);
        List<Integer> pageIndices2 = docInfos2.pageIndexArray.get(index2);
        List<ContentInfo.ContentStyle> styles1 = docInfos1.contentStyleArray.get(index1);
        List<ContentInfo.ContentStyle> styles2 = docInfos2.contentStyleArray.get(index2);

        // 初始化字符级 DP 矩阵与操作矩阵
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        int[][] ops = new int[m + 1][n + 1];

        // 初始化DP矩阵边界
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        // 填充DP矩阵与操作矩阵
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char char1 = s1.charAt(i - 1);
                char char2 = s2.charAt(j - 1);
                int cost = (char1 == char2) ? 0 : 1;

                int matchCost = dp[i - 1][j - 1] + cost;
                int deleteCost = dp[i - 1][j] + 1;
                int insertCost = dp[i][j - 1] + 1;

                dp[i][j] = Math.min(Math.min(matchCost, deleteCost), insertCost);
                if (dp[i][j] == matchCost) {
                    ops[i][j] = (cost == 0) ? 0 : 3;
                } else if (dp[i][j] == deleteCost) {
                    ops[i][j] = 1;
                } else {
                    ops[i][j] = 2;
                }
            }
        }

        // 初始化差异结果对象
        SentenceDiffResult diffResult = new SentenceDiffResult();
        SentenceDiff currentDiff = null;

        // 回溯DP路径，收集差异数据
        int i = m;
        int j = n;
        while (i > 0 || j > 0) {
            // 处理文档2的页码切换
            if (j > 0 && pageIndices2.size() > j - 1) {
                int newPage2 = pageIndices2.get(j - 1);
                if (currentDiff != null && currentDiff.page2.size() != 0 && currentDiff.page2.get(currentDiff.page2.size()-1) != newPage2) {
                    diffResult.addDiff(currentDiff);
                    currentDiff = null;
                }
            }

            // 处理文档1的页码切换
            if (i > 0 && pageIndices1.size() > i - 1) {
                int newPage1 = pageIndices1.get(i - 1);
                if (currentDiff != null && currentDiff.page1.size()!= 0 && currentDiff.page1.get(currentDiff.page1.size()-1) != newPage1) {
                    diffResult.addDiff(currentDiff);
                    currentDiff = null;
                }
            }

            // 分支1：字符匹配（ops=0）
            if (i > 0 && j > 0 && ops[i][j] == 0) {
                // 提交当前临时差异
                if (currentDiff != null) {
                    diffResult.addDiff(currentDiff);
                    currentDiff = null;
                }

                // 检查样式差异
                if (!styles1.get(i - 1).equals(styles2.get(j - 1))) {
                    SentenceDiff styleDiff = new SentenceDiff();
                    styleDiff.diffType = SentenceDiff.TYPE_STYLE_CHANGE;
                    styleDiff.start1 = i - 1;
                    styleDiff.end1 = i - 1;
                    styleDiff.start2 = j - 1;
                    styleDiff.end2 = j - 1;
                    styleDiff.content1 = String.valueOf(s1.charAt(i - 1));
                    styleDiff.content2 = String.valueOf(s2.charAt(j - 1));
                    styleDiff.rects1.add(charRects1.get(i - 1));
                    styleDiff.rects2.add(charRects2.get(j - 1));
                    styleDiff.page1.add(pageIndices1.get(i - 1));
                    styleDiff.page2.add(pageIndices2.get(j - 1));
                    diffResult.addDiff(styleDiff);
                }

                i--;
                j--;
            }

            // 分支2：字符替换（ops=3）
            else if (i > 0 && j > 0 && ops[i][j] == 3) {
                char char1 = s1.charAt(i - 1);
                char char2 = s2.charAt(j - 1);

                // 忽略指定字符
                if (charsToBeIgnored.indexOf(char1) != -1 || charsToBeIgnored.indexOf(char2) != -1) {
                    i--;
                    j--;
                    continue;
                }

                // 初始化或延续替换差异
                if (currentDiff == null || currentDiff.diffType != SentenceDiff.TYPE_REPLACE) {
                    if (currentDiff != null) {
                        diffResult.addDiff(currentDiff);
                    }
                    currentDiff = new SentenceDiff();
                    currentDiff.diffType = SentenceDiff.TYPE_REPLACE;
                    currentDiff.page1.add( pageIndices1.get(i - 1));
                    currentDiff.page2.add( pageIndices2.get(j - 1));
                    currentDiff.start1 = i - 1;
                    currentDiff.end1 = i - 1;
                    currentDiff.start2 = j - 1;
                    currentDiff.end2 = j - 1;
                } else {
                    currentDiff.start1 = Math.min(currentDiff.start1, i - 1);
                    currentDiff.start2 = Math.min(currentDiff.start2, j - 1);
                }

                // 添加差异内容和位置
                currentDiff.content1 = char1 + currentDiff.content1;
                currentDiff.content2 = char2 + currentDiff.content2;
                currentDiff.rects1.add(charRects1.get(i - 1));
                currentDiff.rects2.add(charRects2.get(j - 1));

                i--;
                j--;
            }

            // 分支3：字符删除（ops=1）
            else if (i > 0 && (j == 0 || ops[i][j] == 1)) {
                char char1 = s1.charAt(i - 1);

                // 忽略指定字符
                if (charsToBeIgnored.indexOf(char1) != -1) {
                    i--;
                    continue;
                }

                // 初始化或延续删除差异
                if (currentDiff == null || currentDiff.diffType != SentenceDiff.TYPE_DELETE) {
                    if (currentDiff != null) {
                        diffResult.addDiff(currentDiff);
                    }
                    currentDiff = new SentenceDiff();
                    currentDiff.diffType = SentenceDiff.TYPE_DELETE;
                    currentDiff.page1.add( pageIndices1.get(i - 1));
                    currentDiff.page2.add(pageIndices2.get(j-1));
                    currentDiff.rects2.add(charRects2.get(j-1));
                    currentDiff.start1 = i - 1;
                    currentDiff.end1 = i - 1;
                } else {
                    currentDiff.start1 = Math.min(currentDiff.start1, i - 1);
                }

                // 添加差异内容和位置
                currentDiff.content1 = char1 + currentDiff.content1;
                currentDiff.rects1.add(charRects1.get(i - 1));

                i--;
            }

            // 分支4：字符插入（ops=2）
            else if (j > 0) {
                char char2 = s2.charAt(j - 1);

                // 忽略指定字符
                if (charsToBeIgnored.indexOf(char2) != -1) {
                    j--;
                    continue;
                }

                // 初始化或延续插入差异
                if (currentDiff == null || currentDiff.diffType != SentenceDiff.TYPE_INSERT) {
                    if (currentDiff != null) {
                        diffResult.addDiff(currentDiff);
                    }
                    currentDiff = new SentenceDiff();
                    currentDiff.diffType = SentenceDiff.TYPE_INSERT;
                    currentDiff.page2.add( pageIndices2.get(j - 1));
                    currentDiff.start2 = j - 1;
                    currentDiff.end2 = j - 1;
                    currentDiff.page1.add(pageIndices1.get(i-1));
                    currentDiff.rects1.add(charRects1.get(i-1));
                } else {
                    currentDiff.start2 = Math.min(currentDiff.start2, j - 1);
                }

                // 添加差异内容和位置
                currentDiff.content2 = char2 + currentDiff.content2;
                currentDiff.rects2.add(charRects2.get(j - 1));

                j--;
            }
        }

        // 提交最后一条临时差异
        if (currentDiff != null) {
            diffResult.addDiff(currentDiff);
        }

        // 根据差异结果执行标注
        processDiffsForAnnotation(diffResult, doc1, doc2);
    }

    /**
     * 根据差异结果集合执行PDF标注
     */
    private static void processDiffsForAnnotation(SentenceDiffResult result, PDFDoc doc1, PDFDoc doc2) throws PDFException {
        for (SentenceDiff diff : result.getDiffList()) {
            switch (diff.diffType) {
                case SentenceDiff.TYPE_DELETE:
                    processDeleteDiff(diff, doc1, doc2);
                    break;
                case SentenceDiff.TYPE_INSERT:
                    processInsertDiff(diff, doc1, doc2);
                    break;
                case SentenceDiff.TYPE_REPLACE:
                    processReplaceDiff(diff, doc1, doc2);
                    break;
                case SentenceDiff.TYPE_STYLE_CHANGE:
                    // processStyleChangeDiff(diff, doc2);
                    break;
            }
        }
    }

    /**
     * 处理删除差异标注
     */
    private static void processDeleteDiff(SentenceDiff diff, PDFDoc doc1, PDFDoc doc2) throws PDFException {
        if (diff.page1.size() == 0 || diff.rects1.isEmpty()) return;
        String diffTps1="";
        String diffTps2="";
        // 获取文档1的页面并标注删除内容
        PDFPage page1 = doc1.getPage(diff.page1.get(0));
        page1.startParse(0, null, true);
        page1.normalize();

        QuadPointsArray quads = new QuadPointsArray();
        for (RectF rect : diff.rects1) {
            quads.add(RectToQuad(rect));
        }
        RectF rectFirst=diff.rects1.get(0);

        diffTps2= String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page1.getIndex(), rectFirst.getLeft(),rectFirst.getTop());

        // 创建删除提示标注
        RectF firstRect = diff.rects2.get(0);
        PointF tipPos = new PointF(firstRect.getRight(), firstRect.getBottom());

        // 确定提示所在的文档2页面
        int tipPage2 = (diff.page2.size()!=0) ? diff.page2.get(0) : 0;
        if (tipPage2 > doc2.getPageCount()) tipPage2 = doc2.getPageCount();

        PDFPage page2 = doc2.getPage(tipPage2);
        diffTps1=String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page2.getIndex(), tipPos.getX(),tipPos.getY());

        Highlight annotInsert= CreatHighLightRect(page1, COLOR_INSERT, quads, TIP_INSERT,"插入："+diff.content1);
        {
            PDFDictionary dict = annotInsert.getDict();
            dict.setAtString("customEntries", diffTps1);
        }

        PolyLine annotDelete= CreatCalloutRect1(page2, COLOR_DELETE_TIP, tipPos, "删除: " + diff.content1);
        {
            PDFDictionary dict=annotDelete.getDict();
            dict.setAtString("customEntries",diffTps2);
        }
    }

    /**
     * 处理插入差异标注
     */
    private static void processInsertDiff(SentenceDiff diff, PDFDoc doc1, PDFDoc doc2) throws PDFException {
        if (diff.page2.size()==0 || diff.rects2.size()==0) return;

        // 获取文档2的页面并标注插入内容
        PDFPage page2 = doc2.getPage(diff.page2.get(0));
        page2.startParse(0, null, true);
        page2.normalize();

        QuadPointsArray quads = new QuadPointsArray();
        for (RectF rect : diff.rects2) {
            quads.add(RectToQuad(rect));
        }
        RectF rectFirst=diff.rects2.get(0);
        String diffTips1=String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page2.getIndex(), rectFirst.getLeft(),rectFirst.getTop());


        // 创建插入提示标注
        //  if (!diff.content2.isEmpty()) {
        RectF firstRect = diff.rects1.get(0);
        PointF tipPos = new PointF(firstRect.getRight(), firstRect.getBottom());

        // 确定提示所在的文档1页面
        int tipPage1 = (diff.page1.size()!=0) ? diff.page1.get(0) : 0;
        if (tipPage1 > doc1.getPageCount()) tipPage1 = doc1.getPageCount();

        PDFPage page1 = doc1.getPage(tipPage1);

        PolyLine annot1=  CreatCalloutRect1(page1, COLOR_DELETE_TIP, tipPos, "删除： " + diff.content2);
        String diffTips2=String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page1.getIndex(), tipPos.getX(),tipPos.getY());
        Highlight annot2=  CreatHighLightRect(page2, COLOR_INSERT, quads, TIP_INSERT,"插入："+diff.content2);

        {
            PDFDictionary dict=annot1.getDict();
            dict.setAtString("customEntries",diffTips1);
        }
        {

            PDFDictionary dict=annot2.getDict();
            dict.setAtString("customEntries",diffTips2);
        }
    }

    /**
     * 处理替换差异标注
     */
    private static void processReplaceDiff(SentenceDiff diff, PDFDoc doc1, PDFDoc doc2) throws PDFException {
        // 标注文档1中的替换内容
//        if (diff.page1.size()!=0 && diff.rects1.size()!=0)
//        {
        PDFPage page1 = doc1.getPage(diff.page1.get(0));
        page1.startParse(0, null, true);
        page1.normalize();

        QuadPointsArray quads1 = new QuadPointsArray();
        for (RectF rect : diff.rects1) {
            quads1.add(RectToQuad(rect));
        }

        //  }

        // 标注文档2中的替换内容
        // if (diff.page2.size()!=0  && !diff.rects2.isEmpty()) {
        PDFPage page2 = doc2.getPage(diff.page2.get(0));
        page2.startParse(0, null, true);
        page2.normalize();

        QuadPointsArray quads2 = new QuadPointsArray();
        for (RectF rect : diff.rects2) {
            quads2.add(RectToQuad(rect));
        }
        Highlight annot1= CreatHighLightRect(page1, COLOR_REPLACE, quads1, TIP_REPLACE,"基准:"+diff.content1+"\r\n"+ "对照: " + diff.content2);
        Highlight annot2=  CreatHighLightRect(page2, COLOR_REPLACE, quads2, TIP_REPLACE,"基准:"+diff.content2+"\r\n"+ "对照: " + diff.content1);
        RectF rectFirst=diff.rects1.get(0);
        String diffTips1=String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page1.getIndex(), rectFirst.getLeft(),rectFirst.getTop());
        rectFirst=diff.rects2.get(0);
        String diffTips2=String.format("{\"another_PageIndex\":\"%d\",\"Point_x\":\"%f\",\"Point_y\":\"%f\"}",page2.getIndex(), rectFirst.getLeft(),rectFirst.getTop());

        {
            PDFDictionary dict = annot1.getDict();
            dict.setAtString("customEntries", diffTips1);
        }
        {
            PDFDictionary dict = annot2.getDict();
            dict.setAtString("customEntries", diffTips2);
        }

//}
    }


    public static void CompareTexts(DocSentencesInfo docInfos1, DocSentencesInfo docInfos2) {
        // 获取两个文档的句子列表
        List<String> sents1 = docInfos1.sents;
        List<String> sents2 = docInfos2.sents;

        int m = sents1.size(); // 文档1的句子数
        int n = sents2.size(); // 文档2的句子数
        // 创建相似度矩阵
        double[][] similarityMatrix = new double[m][n];

        // 计算相似度矩阵
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int len1 = sents1.get(i).length();
                int len2 = sents2.get(j).length();
                int maxLen = Math.max(len1, len2);
                if (Math.abs(len1 - len2) > maxLen * 0.7) { // 差异>70%，相似度0
                    similarityMatrix[i][j] = 0.0;
                    continue;
                }
                similarityMatrix[i][j] = calculateSimilarity(sents1.get(i), sents2.get(j));
            }
        }

        // 动态规划：创建DP矩阵和路径矩阵
        int[][] dp = new int[m + 1][n + 1];
        int[][] path = new int[m + 1][n + 1]; // 0:对角线, 1:向上, 2:向左

        // 初始化DP矩阵和路径矩阵
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 0;
                    continue;
                }

                // 仅文档2有句子：向左移动（路径2）
                if (i == 0) {
                    dp[i][j] = dp[i][j - 1] + 1;
                    path[i][j] = 2;
                    continue;
                }

                // 仅文档1有句子：向上移动（路径1）
                if (j == 0) {
                    dp[i][j] = dp[i - 1][j] + 1;
                    path[i][j] = 1;
                    continue;
                }

                // 计算三种路径的成本
                double similarity = similarityMatrix[i - 1][j - 1];
                int diagCost = dp[i - 1][j - 1] + (int) (10 * (1 - similarity)); // 对角线成本
                int upCost = dp[i - 1][j] + 10; // 向上成本
                int leftCost = dp[i][j - 1] + 10; // 向左成本

                // 选择成本最小的路径
                if (diagCost <= upCost && diagCost <= leftCost) {
                    dp[i][j] = diagCost;
                    path[i][j] = 0;
                } else if (upCost <= leftCost) {
                    dp[i][j] = upCost;
                    path[i][j] = 1;
                } else {
                    dp[i][j] = leftCost;
                    path[i][j] = 2;
                }
            }
        }
        // 回溯路径：从(m,n)反向遍历到(0,0)
        int i = m;
        int j = n;
        while (i > 0 || j > 0) {
            try {

                // 分支1：对角线路径（两个句子匹配，对比句子）
                if (i > 0 && j > 0 && path[i][j] == 0) {
                    // 获取匹配的句子
                    String s1 = sents1.get(i - 1);
                    String s2 = sents2.get(j - 1);

                    // 对比句子
                    CompareSentences(i - 1, j - 1, docInfos1, docInfos2);
                    i--;
                    j--;
                    continue;
                }

                // 分支2：向上路径（文档1独有句子，标注删除）
                if (i > 0 && (j == 0 || path[i][j] == 1)) {
                    highlightDeletedSentence(docInfos1, i - 1, docInfos2, j);
                    i--;
                    continue;
                }

                // 分支3：向左路径（文档2独有句子，标注添加）
                if (j > 0 && (i == 0 || path[i][j] == 2)) {
                    // 标注添加的句子
                    highlightDeletedSentence(docInfos2, j - 1, docInfos1, i);
                    j--;
                    continue;
                }

                // 安全机制：防止无限循环
                if (i <= 0 && j <= 0) {
                    break;
                }
                if (i > 0) {
                    i--;
                } else if (j > 0) {
                    j--;
                }
            } catch (IndexOutOfBoundsException e) {
                System.err.println("越界访问错误 (i=" + i + ", j=" + j + "): " + e.getMessage());
                if (i > 0) i--;
                if (j > 0) j--;

            } catch (PDFException e) {
                throw new RuntimeException(e);
            }
        }
    }
    static void highlightDeletedSentence(DocSentencesInfo docInfos1, int sentIndex, DocSentencesInfo docInfos2, int j) throws PDFException {
        String s1 = docInfos1.sents.get( sentIndex);

        // 获取标注位置信息（增加边界检查）
        PointF deletePos;
        if (j > 0 && j - 1 < docInfos2.pageIndexArray.size() &&
                !docInfos2.charRectArray.get( j - 1).isEmpty()) {
            int pIndex = docInfos2.pageIndexArray.get(j - 1).get(0);
            if (pIndex < docInfos2.doc.getPageCount()) {
                PDFPage page2 = docInfos2.doc.getPage(pIndex);
                page2.startParse(0, null, true);
                List<RectF> rectArray = docInfos2.charRectArray.get(j - 1);
                RectF rectPos = rectArray.get(rectArray.size() - 1);
                deletePos =new PointF(rectPos.getRight(), rectPos.getBottom());
                CreatCalloutRect1(page2, 0xff0000, deletePos, s1);
            }
        }

        // 处理高亮标注
        QuadPointsArray quad_points_array=new QuadPointsArray();
        List<RectF> rectList = docInfos1.charRectArray.get( sentIndex);
        PDFPage page=docInfos1.doc.getPage(0);
        int lastPageIndex = -1;

        for (int rectIndex = 0; rectIndex < rectList.size(); rectIndex++) {
            // 增加数组边界检查
            if (rectIndex >= docInfos1.pageIndexArray.get( sentIndex).size()) {
                break;
            }

            int pIndex = docInfos1.pageIndexArray.get(sentIndex).get(rectIndex);
            // 检查页码有效性
            if (pIndex < 0 || pIndex >= docInfos1.doc.getPageCount()) {
                continue;
            }

            if (rectIndex == 0 || pIndex != lastPageIndex) {
                // 完成上一页的标注
                if (rectIndex > 0) {
                    //   CreatHighLightRect(page, COLOR_INSERT, quad_points_array, TIP_INSERT);
                    quad_points_array = new QuadPointsArray();
                }
                page = docInfos1.doc.getPage(pIndex);
                page.startParse(0, null, true);
                lastPageIndex = pIndex;
            }

            quad_points_array.add(RectToQuad(rectList.get(rectIndex)));
        }

        // 处理最后一页的标注
        if (quad_points_array.getSize()>0) {
            // CreatHighLightRect(page, COLOR_INSERT, quad_points_array, TIP_INSERT);
        }
    }
    // 新增：添加句子的高亮标注函数
    static    void HighlightAddedSentence(DocSentencesInfo docInfos2, int sentIndex) throws PDFException {
        String s2 = docInfos2.sents.get( sentIndex);
        QuadPointsArray quad_points_array=new QuadPointsArray();
        List<RectF> rectList = docInfos2.charRectArray.get( sentIndex);
        PDFPage page = docInfos2.doc.getPage(0);
        int lastPageIndex = -1;

        for (int rectIndex = 0; rectIndex < rectList.size(); rectIndex++) {
            // 增加边界检查
            if (rectIndex >= docInfos2.pageIndexArray.get( sentIndex).size()) {
                break;
            }

            int pIndex = docInfos2.pageIndexArray.get( sentIndex).get(rectIndex);
            if (pIndex < 0 || pIndex >= docInfos2.doc.getPageCount()) {
                continue;
            }

            if (rectIndex == 0 || pIndex != lastPageIndex) {
                if (rectIndex > 0) {
                    //       CreatHighLightRect(page, COLOR_INSERT, quad_points_array, TIP_INSERT);
                    quad_points_array = new QuadPointsArray();
                }
                page = docInfos2.doc.getPage(pIndex);
                page.startParse(0,null, true);
                lastPageIndex = pIndex;
            }

            quad_points_array.add(RectToQuad(rectList.get(rectIndex)));
        }
        int c= Math.toIntExact(quad_points_array.getSize());
        if (quad_points_array.getSize()!=0) {
            // CreatHighLightRect(page, COLOR_INSERT, quad_points_array, TIP_INSERT);
        }
    }
    public static void pdfCompare(String filePath1, String filePath2,String savePath1,String savePath2) throws PDFException {

        //带分栏的文件
        PDFDoc docBase =new PDFDoc(filePath1);
        int errorCode= docBase.load(null);
        DocInfos docInfoBase =  DocParse(docBase);
        //标准文件
        PDFDoc docNew = new PDFDoc(filePath2);
        errorCode= docNew.load(null);
        DocInfos docInfoNew = DocParse(docNew);


        String text1 = GetStringInDocInfo(docInfoBase);
        String text2 = GetStringInDocInfo(docInfoNew);
        //进行文本拆分和比较
        DocSentencesInfo doc11 = GetSentences(docInfoBase, text1);
        DocSentencesInfo doc22 = GetSentences(docInfoNew, text2);
        if (doc11.sents.size() == 0|| doc22.sents.size() == 0)
        {
            return;
        }
        CompareTexts(doc11, doc22);
        {
            docBase.saveAs( savePath1, 0);
            docNew.saveAs(savePath2, 0);
        }
    }
}
