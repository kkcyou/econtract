package com.yaoan.module.econtract.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.itextpdf.text.pdf.PdfReader;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.enums.gpx.GpxPlanIdTag;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.module.econtract.enums.StatusConstants.SUFFIX_OFD;
import static com.yaoan.module.econtract.enums.StatusConstants.SUFFIX_PDF;

/**
 * @description: 工具
 * @author: Pele
 * @date: 2023/8/17 11:25
 */
@Slf4j
public class EcontractUtil {

    static final int wdDoNotSaveChanges = 0;// 不保存待定的更改。
    static final int wdFormatPDF = 17;// PDF 格式


    /**
     * 远端目录命名方法
     *
     * @param moduleName 相关业务模块的名字;比如 合同、模板、参数
     */
    public static String getRemoteFolderPath(String moduleName, String uuid) {
//        return moduleName + "/" + DateUtil.today() + "/" + IdUtil.simpleUUID();
        return moduleName + "/" + DateUtil.today() + "/" + uuid;
    }

    /**
     * 得到上传文件的路径
     */
    public static String getSourcePath(MultipartFile file, String folderId) {
        return DateUtil.today() + "/" + folderId + "/" + file.getOriginalFilename();

    }

    public static LocalDateTime getDateFromStr(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            return dateTime;
            // 在这里可以使用转换后的Date对象进行后续操作
        } catch (Exception e) {
            e.printStackTrace();
            log.error("处理日期解析异常");
            // 处理日期解析异常
        }
        return null;
    }

    public static Date getRealDateFromStr(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString);
        Date result = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return result;
    }

    public static LocalDateTime getDateFromSimpleStr(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime;
    }

    /**
     * 将word文件docx装成PDF文件
     * 返回页数
     */
    public static int wordToPdf(String wordpath, String pdfpath) {
        System.out.println("启动Word...");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        try {
            //打开word应用程序
            app = new ActiveXComponent("Word.Application");
//                设置应用操作是文档不在明面上显示，只在后台静默处理。
            app.setProperty("Visible", false);
            //获得文档集合，用来操作我们需要处理的文档.
            Dispatch docs = app.getProperty("Documents").toDispatch();
            //打开word文档
            Dispatch doc = Dispatch.call(docs,//
                    "Open", //
                    wordpath,// FileName
                    false,// ConfirmConversions
                    true // ReadOnly
            ).toDispatch();

            File tofile = new File(pdfpath);
            //创建存放pdf的文件夹
            if (tofile.exists()) {
                tofile.delete();
            }
            //将word另存为pdf
            Dispatch.call(doc,//
                    "SaveAs", //
                    pdfpath, // FileName
                    wdFormatPDF);
            //关闭word文档
            Dispatch.call(doc, "Close", false);
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");
            return getFilePageFromPDF(pdfpath);
//            System.out.println(String.valueOf(getFilePageFromPDF(pdfpath)));
//            if (FileUtil.del(pdfpath)) {
//                System.out.println("pdf删除完成");
//            }
        } catch (Exception e) {
            log.error(e.getMessage());
//            System.out.println("========Error:文档转换失败：" + e.getMessage());
        } finally {
            if (app != null) {
                app.invoke("Quit", wdDoNotSaveChanges);
            }
        }

        return 0;
    }

    /**
     * 得到PDF文件的页数
     */
    public static int getFilePageFromPDF(String filePath) {
        try {
            PdfReader pdfReader = new PdfReader(new FileInputStream(new File(filePath)));
            //pdf页数
            int pdfPage = pdfReader.getNumberOfPages();
            System.out.println("总页数：" + pdfPage);
            return pdfPage;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取路径下所有 MultipartFile文件
     */
    public static List<MultipartFile> searchAllMultiFiles(String filePath, String regex) throws IOException {
        List<File> files = searchAllFiles(filePath, regex);
        return convert(files);
    }

    /**
     * 获取路径下所有 File文件
     */
    public static List<File> searchAllFiles(String filePath, String regex) {

        File folder = new File(filePath);

        // 使用.listFiles()方法列出文件夹内所有文件和子文件夹。
        File[] files = folder.listFiles();

        // 方法内部的sumFiles变量，当方法返回时只返回方法内声明的 sumFiles 变量，
        List<File> sumFiles = new CopyOnWriteArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    sumFiles.addAll(searchAllFiles(file.getAbsolutePath(), regex));
                } else {
                    if (regex.equals(regex)) {
                        sumFiles.add(file);
                    } else {
                        if (file.getName().matches(regex)) {
                            sumFiles.add(file);
                        }
                    }
                }
            }
        }

        // 返回 sumFiles 集合，其中包含按照指定要求筛选出来的所有文件。
        return sumFiles;
    }

    /**
     * 将File集合转换成File集合
     */
    public static List<MultipartFile> convert(List<File> files) throws IOException {
        List<MultipartFile> multipartFiles = new ArrayList<>();

        for (File file : files) {
            FileInputStream fileInputStream = new FileInputStream(file);

            // 创建MultipartFile对象，将File的内容复制到其中
            MultipartFile multipartFile = new MockMultipartFile(
                    file.getName(),
                    file.getName(),
                    null,
                    fileInputStream
            );

            multipartFiles.add(multipartFile);
        }

        return multipartFiles;
    }

    /**
     * 方法中传入的参数分别为
     * filePath:为生成的file文件地址，地址后要以\（File.separator）结尾，
     * fileName：为生成的file文件名称
     **/
    public static File bytesToFile(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {

            file = new File(filePath + fileName);
            if (!file.getParentFile().exists()) {
                //文件夹不存在 生成
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 截取序号
     */
    public static Integer getNumberFromFileName(String path) {
        try {
            StringBuilder number = new StringBuilder();
            String fileName = getFileName(path);
            for (int i = 0; i < fileName.length(); i++) {
                char c = fileName.charAt(i);
                if (Character.isDigit(c)) {
                    number.append(c);
                } else {
                    break;
                }
            }
            return Integer.parseInt(number.toString());
        } catch (Exception e) {
            log.error("范本图片数据发生异常");
            return 0;

        }

    }

    /**
     * 序号排序
     */
    public static void sortListByInteger(List<Map<Integer, Object>> list) {
        Collections.sort(list, new Comparator<Map<Integer, Object>>() {
            @Override
            public int compare(Map<Integer, Object> map1, Map<Integer, Object> map2) {
                Integer key1 = map1.keySet().iterator().next();
                Integer key2 = map2.keySet().iterator().next();
                return key1.compareTo(key2);
            }
        });
    }

    /**
     * 获取文件名称
     */
    public static String getFileName(String path) {
        Path filePath = Paths.get(path);
        return filePath.getFileName().toString();
    }

    /**
     * 将word文件的名字换成pdf
     */
    public static String exchangeName2pdf(String wordFileName) {
        int dotIndex = wordFileName.lastIndexOf(".");
        String substring = wordFileName.substring(0, dotIndex);
        return substring + "." + SUFFIX_PDF;
    }

    /**
     * MultipartFile 如何转成File
     */
    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile(multipartFile.getOriginalFilename(), ".tmp");
        multipartFile.transferTo(file);
        return file;
    }

    /**
     * 根据路径转成MultipartFile
     */
    public static MultipartFile getMfileFromPath(String filePath) throws IOException {
        File file = new File(filePath);
        DiskFileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());

        byte[] fileContent = Files.readAllBytes(file.toPath());
        fileItem.getOutputStream().write(fileContent);

        return new MockMultipartFile(fileItem.getFieldName(), fileItem.getName(), fileItem.getContentType(), fileItem.getInputStream());

    }

    /**
     * 统计pdf文件字符数量（包括标点符号）
     */
    public static int getPdfWordCount(String pdfFilePath) throws IOException {
        // 加载 PDF 文档
        PDDocument document = PDDocument.load(new File(pdfFilePath));
        // 创建 PDFTextStripper 对象
        PDFTextStripper stripper = new PDFTextStripper();
        // 从 PDF 文档中提取文本
        String text = stripper.getText(document).replace(" ", "").replace("\n", "").replace("\r", "");
        // 关闭 PDF 文档
        document.close();
        // 统计文本中的单词个数
        Integer count = text.length();
        return count;
    }

    /**
     * 将文件的名字换成pdf
     */
    public static String exchangeName2Ofd(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        String substring = fileName.substring(0, dotIndex);
        return substring + "." + SUFFIX_OFD;
    }

    /**
     * 将金额转成大写
     */
    public static String convertToChineseUpper(BigDecimal amount) {
        String[] upperNumbers = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String[] upperUnits = {"", "拾", "佰", "仟", "万", "亿"};

        StringBuilder result = new StringBuilder();
        String amountStr = amount.toPlainString();

        int index = amountStr.indexOf(".");
        if (index != -1) {
            String integerPart = amountStr.substring(0, index);
            String decimalPart = amountStr.substring(index + 1);
            result.append(convertIntegerPart(integerPart, upperNumbers, upperUnits));
            result.append(convertDecimalPart(decimalPart, upperNumbers));
        } else {
            result.append(convertIntegerPart(amountStr, upperNumbers, upperUnits));
        }

        result.append("元");

        return result.toString();
    }

    public static String convertIntegerPart(String integerPart, String[] upperNumbers, String[] upperUnits) {
        StringBuilder result = new StringBuilder();
        int length = integerPart.length();
        boolean isPreviousZero = false;  // 前一位是否为零

        for (int i = 0; i < length; i++) {
            int digit = Integer.parseInt(String.valueOf(integerPart.charAt(i)));
            int unitIndex = (length - i - 1) % 4;

            if (digit == 0) {
                isPreviousZero = true;
                continue;
            }

            if (isPreviousZero) {
                result.append(upperNumbers[0]);  // 添加零
            }

            result.append(upperNumbers[digit]);
            result.append(upperUnits[unitIndex]);

            isPreviousZero = false;
        }

        return result.toString();
    }

    public static String convertDecimalPart(String decimalPart, String[] upperNumbers) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < decimalPart.length(); i++) {
            int digit = Integer.parseInt(String.valueOf(decimalPart.charAt(i)));
            result.append(upperNumbers[digit]);
        }

        return result.toString();
    }

    /**
     * 根据名称和时间戳获得编号
     * 名称的拼音缩写（保留6位） + 当前年月日 + 6位随机
     */
    public static String getCodeAutoByTimestamp(String name) {
        if(StringUtils.isBlank(name)){
            return "默认";
        }
        LocalDateTime now = LocalDateTime.now();
        String month = now.getMonthValue() > 10 ? now.getMonthValue() + "" : "0" + now.getMonthValue();
        String day = now.getDayOfMonth() > 10 ? now.getDayOfMonth() + "" : "0" + now.getDayOfMonth();
        String pinYinHeader = getPinYinHeader(name);
        //如果不够6位，随机填充
        if (6 > pinYinHeader.length()) {
            pinYinHeader = (pinYinHeader + IdUtil.fastSimpleUUID()).substring(0, 6);
        }
        return pinYinHeader.toUpperCase() + "-" + now.getYear() + month + day + "-" + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
    }

    /**
     * 根据时间戳获得编号(用于托底)
     * 名称的拼音缩写（保留6位） + 当前年月日 + 6位随机
     */
    public static String getDefaultCodeAutoByTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        String month = now.getMonthValue() > 10 ? now.getMonthValue() + "" : "0" + now.getMonthValue();
        String day = now.getDayOfMonth() > 10 ? now.getDayOfMonth() + "" : "0" + now.getDayOfMonth();
        //如果不够6位，随机填充
        return "DEFAULT-" + now.getYear() + month + day + "-" + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
    }

    /**
     * 判断字符串是否为汉字
     */
    public static boolean isChinese(String str) {
        return str.matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 汉字转换为首字母缩写
     */
    public static String getPinYinHeader(String name) {
        StringBuilder sb = new StringBuilder();
        char[] chars = name.toCharArray();
        List<String> strList = new ArrayList<String>();
        for (char aChar : chars) {
            String[] temp = PinyinHelper.toHanyuPinyinStringArray(aChar);
            strList.add(temp[0]);
        }
        for (String str : strList) {
            sb.append(str);
        }

        String newSb = sb.toString();

        StringBuilder newStr = new StringBuilder();

        for (int i = 0; i < newSb.length(); i++) {
            char currentChar = newSb.charAt(i);

            if (i == 0 || Character.isDigit(currentChar)) {
                newStr.append(currentChar);

                if (Character.isDigit(currentChar) && i < newSb.length() - 1) {
                    newStr.append(newSb.charAt(i + 1));
                }
            }
        }
        String result = newStr.toString();
        String newResult = result.replaceAll("\\d", "");
        return newResult;
    }


    /**
     * 将processInstanceId相同的所有元素中组成新的List
     */
    public static List<BpmTaskAllInfoRespVO> distinctTask(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        if (CollectionUtil.isEmpty(taskAllInfoRespVOList)) {
            return taskAllInfoRespVOList;
        }
        List<BpmTaskAllInfoRespVO> filteredList = taskAllInfoRespVOList.stream()
                .collect(Collectors.groupingBy(BpmTaskAllInfoRespVO::getProcessInstanceId))
                .values().stream()
                .map(list -> list.stream()
                        .max(Comparator.comparing(BpmTaskAllInfoRespVO::getCreateTime))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return filteredList;
    }

    /**
     * 去重，保留处理过的任务（只保留endTime不为null的任务）
     */
    public static List<BpmTaskAllInfoRespVO> distinctTaskHaveEndTime(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        if (CollectionUtil.isEmpty(taskAllInfoRespVOList)) {
            return taskAllInfoRespVOList;
        }
        List<BpmTaskAllInfoRespVO> filteredList = taskAllInfoRespVOList.stream()
                .filter(task -> task.getEndTime() != null) // 筛选出endTime不为空的元素
                .collect(Collectors.groupingBy(BpmTaskAllInfoRespVO::getProcessInstanceId))
                .values().stream()
                .map(list -> list.stream()
                        .filter(task -> task.getCreateTime() != null) // 过滤掉createTime为空的元素
                        .max(Comparator.comparing(BpmTaskAllInfoRespVO::getCreateTime))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return filteredList;
    }

    /**
     * 去重，保留处理过的任务（只保留endTime最新的任务）
     */
    public static List<BpmTaskAllInfoRespVO> distinctDoneTaskLatestEndTime(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        if (CollectionUtil.isEmpty(taskAllInfoRespVOList)) {
            return taskAllInfoRespVOList;
        }
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        List<BpmTaskAllInfoRespVO> filteredList = new ArrayList<>(taskAllInfoRespVOList.stream()
                .filter(task -> Objects.equals(task.getAssigneeUserId(), userId)) // 筛选出 assigneeUserId == userId 的任务
                .filter(task -> task.getEndTime() != null) // 筛选出 endTime 不为空的任务
                .collect(Collectors.toMap(
                        BpmTaskAllInfoRespVO::getProcessInstanceId, // 以 processInstanceId 作为 key 进行分组
                        Function.identity(), // 当有重复 key 时，保留当前元素
                        (existing, replacement) -> {
                            if (existing.getEndTime().isAfter(replacement.getEndTime())) {
                                return existing; // 如果已有元素的 endTime 更晚，则保留已有元素
                            } else {
                                return replacement; // 否则保留当前元素
                            }
                        }
                ))
                .values());
        return filteredList;
    }


    /**
     * 去重，保留待处理的任务（只保留endTime为null的任务）
     */
    public static List<BpmTaskAllInfoRespVO> distinctTaskNullEndTime(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        if (CollectionUtil.isEmpty(taskAllInfoRespVOList)) {
            return taskAllInfoRespVOList;
        }
        List<BpmTaskAllInfoRespVO> filteredList = taskAllInfoRespVOList.stream()
                .filter(task -> task.getEndTime() == null) // 筛选出endTime为空的元素
                .collect(Collectors.groupingBy(BpmTaskAllInfoRespVO::getProcessInstanceId))
                .values().stream()
                .map(list -> list.stream()
                        .filter(task -> task.getCreateTime() != null) // 过滤掉createTime为空的元素
                        .max(Comparator.comparing(BpmTaskAllInfoRespVO::getCreateTime))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return filteredList;
    }

    /**
     * 去重，保留待处理的任务（只保留endTime为null的任务）
     */
    public static List<BpmTaskAllInfoRespVO> distinctTaskNullEndTimeByUserId(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList, Long userId) {
        if (CollectionUtil.isEmpty(taskAllInfoRespVOList)) {
            return taskAllInfoRespVOList;
        }
        List<BpmTaskAllInfoRespVO> filteredList = taskAllInfoRespVOList.stream()
                .filter(task -> task.getEndTime() == null) // 筛选出endTime为空的元素
                .filter(task -> task.getAssigneeUserId() != null && task.getAssigneeUserId().equals(userId))
                .collect(Collectors.groupingBy(BpmTaskAllInfoRespVO::getProcessInstanceId))
                .values().stream()
                .map(list -> list.stream()
                        .filter(task -> task.getCreateTime() != null) // 过滤掉createTime为空的元素
                        .max(Comparator.comparing(BpmTaskAllInfoRespVO::getCreateTime))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return filteredList;
    }

    public static String getNameAuto(String str) {
        return str + "采购合同";
    }


    /**
     * 合同名称 : “采购人+品目+交易方式”
     */
    public static String getContractNameAuto(String buyer, String goodClassName, String dealMethod) {
        return buyer + goodClassName + dealMethod;
    }

    /**
     * 去除已取消的任务
     */
    public static List<ContractProcessInstanceRelationInfoRespDTO> clearRepealTask(List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList) {
        if (CollectionUtil.isEmpty(processInstanceRelationInfoRespDTOList)) {
            return processInstanceRelationInfoRespDTOList;
        }
        return processInstanceRelationInfoRespDTOList.stream()
                .filter(dto -> !Objects.equals(BpmProcessInstanceResultEnum.CANCEL.getResult(), dto.getProcessResult()))  // 筛选出processResult不等于4的元素
                .collect(Collectors.toList());
    }

    public static String getRandomCodeAutoByTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        String month = now.getMonthValue() > 10 ? now.getMonthValue() + "" : "0" + now.getMonthValue();
        String day = now.getDayOfMonth() > 10 ? now.getDayOfMonth() + "" : "0" + now.getDayOfMonth();
        String pinYinHeader = generateRandomLetters(6);
        //如果不够6位，随机填充
        if (6 > pinYinHeader.length()) {
            pinYinHeader = (pinYinHeader + IdUtil.fastSimpleUUID()).substring(0, 6);
        }
        return pinYinHeader.toUpperCase() + "-" + now.getYear() + month + day + "-" + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase();
    }

    public static String generateRandomLetters(int length) {
        // 定义随机字母表
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        // 生成指定长度的随机字母串
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(letters.length());
            sb.append(letters.charAt(index));
        }

        return sb.toString();
    }

    /**
     * 将输入流中的内容读取到字节数组中的方法
     */
    public static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        // 使用 ByteArrayOutputStream 来缓存输入流的内容
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 设置缓冲区大小
        byte[] buffer = new byte[1024];
        int length;

        // 从输入流中读取数据，并写入到 ByteArrayOutputStream 中
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        // 关闭 ByteArrayOutputStream（这也会关闭其内部的输入流）
        byteArrayOutputStream.close();

        // 返回字节数组
        return byteArrayOutputStream.toByteArray();
    }

    // 从路径中提取文件名的方法
    public static String getFileNameFromPath(String path) {
        // 使用路径中最后一个斜杠（/）后的部分作为文件名
        int lastIndex = path.lastIndexOf('/');
        if (lastIndex >= 0 && lastIndex < path.length() - 1) {
            return path.substring(lastIndex + 1);
        }
        return "";
    }

    // 去除子字符串的方法
    private static String removeSubstring(String input, String substringToRemove) {
        // 使用 replace 方法将子字符串替换为空字符串
        return input.replace(substringToRemove, "");
    }

    public static String getTimeFolderPath() {
        LocalDateTime date = LocalDateTime.now();
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();

        return year + "/" + month + "/" + day + "/";
    }

    /**
     * 获取文件格式后缀 ( 例如：.docx )
     */
    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        // 检查是否存在点，且点不在最后一位（避免".gitignore"这类文件或"file."这种情况）
        if (lastDotIndex > -1 && lastDotIndex < fileName.length() - 1) {
            return "." + fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
    //    public static String getFileSuffix(String fileName) {
//        if(StringUtils.isBlank(fileName)){
//            return "";
//        }
//        String[] parts = fileName.split("\\.");
//        String result = (parts.length > 1) ? "." + parts[1] : "";
//
//        return result;
//    }
    /**
     * 获取简洁文件格式后缀( 例如：docx )
     */
    public static String getSimpleFileSuffix(String fileName) {
        if(StringUtils.isBlank(fileName)){
            return "";
        }
        String[] parts = fileName.split("\\.");
        return parts[1];
    }
    /**
     * 从字符串获取List
     */
    public List<String> convertStringToList(String idsString) {
        String[] idArray = StringUtils.split(idsString, ',');
        return Arrays.asList(idArray);
    }

    /**
     * 去掉采购计划的Tag
     */
    public static String cleanTag(String str) {
        String result = str;
        if (str.contains(GpxPlanIdTag.GPX_PLAN_PLAN_ID_TAG)) {
            result = StrUtil.subAfter(str, GpxPlanIdTag.GPX_PLAN_PLAN_ID_TAG, false);
        }
        return result;
    }

}


