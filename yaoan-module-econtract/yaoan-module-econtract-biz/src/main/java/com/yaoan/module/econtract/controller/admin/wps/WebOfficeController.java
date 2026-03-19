package com.yaoan.module.econtract.controller.admin.wps;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.econtract.dal.dataobject.wps.*;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.yaoan.module.econtract.enums.ModuleConstants.MODULE_MODEL;
import static com.yaoan.module.econtract.enums.StatusConstants.FILE_TYPE_DOC;
import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;

@Slf4j
@RestController
@RequestMapping("econtract/wps")
@Validated
@Tag(name = "wps-WebOffice", description = "wps WebOffice操作接口")
public class WebOfficeController {
    final static Integer PERMISSION_YES = 1;
    //// TODO: 参数传递最好不要出现中文等特殊字符，容易导致签名不过等问题，本例子用fileid与文件名做了一个映射，实际开发可以按情况处理
    private static Map<String, String> fileNameMap = new HashMap<String, String>();
    private final String tempPath = "/Users/doujiale/Desktop/";

    /**
     * wps下载路径
     */
    @Value("${wps.download_url}")
    private String download_url;


    @Resource
    private FileApi fileApi;

    private static String sha1(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(data.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateRFC1123() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        DateTimeFormatter rfc1123Formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        String formattedTime = now.format(rfc1123Formatter);
        return formattedTime;
    }

    @RequestMapping(value = "v3/3rd/files/{file_id}", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object fileInfo(@PathVariable String file_id) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        FileWpsDTO fileWpsDTO = fileApi.selectWpsDTOById(file_id);

        data.put("id", String.valueOf(file_id));
        data.put("name", String.valueOf(fileWpsDTO.getName()));
        data.put("version", 1);
        data.put("size", fileWpsDTO.getSize());
        data.put("create_time", (int) fileWpsDTO.getCreateTime().toEpochSecond(ZoneOffset.ofHours(8)));
        data.put("modify_time", (int) fileWpsDTO.getUpdateTime().toEpochSecond(ZoneOffset.ofHours(8)));
        data.put("creator_id", "406");
        data.put("modifier_id", "406");

        jsonObject.put("data", data);
        jsonObject.put("code", 0);

        return jsonObject.toString();
    }

    /**
     * 下载
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/download", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object download(@PathVariable String file_id) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            FileWpsDTO fileWpsDTO = fileApi.selectWpsDTOById(file_id);
//            data.put("url", StatusConstants.WPS_DOWNLOAD_URL + file_id);
            data.put("url", download_url + file_id);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/v3/3rd/files/{file_id}/permission", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object permission(@PathVariable String file_id) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            FileModel fileModel = new FileModel();
            PermissionModel permissionModel = new PermissionModel();
            FileWpsDTO localFile = fileApi.selectWpsDTOById(file_id);
            if (ObjectUtil.isNotNull(localFile)) {
                data.put("download", 0);
                data.put("history", 0);
                data.put("print", 0);
                data.put("read", 1);
                data.put("comment", 0);
                data.put("copy", 0);
                data.put("rename", 0);
                data.put("saveas", 0);
                data.put("update", 1);
                data.put("user_id", "406");
            }


            jsonObject.put("data", data);
            jsonObject.put("code", 0);
            System.out.println("data = " + data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 单阶段提交
     * 逻辑：
     *
     * @param file_id 用saveModel的信息更新fileId对应的数据
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/upload", method = RequestMethod.POST)
    @ResponseBody
    @PermitAll
    public Object upload(@PathVariable String file_id, SaveModel saveModel) throws Exception {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        request.getHeader("Content-Md5");
        request.getHeader("Authorization");
        FileWpsDTO fileWpsDTO = wpsDTO2FileDO(file_id, saveModel);

        return enhanceDTO(fileWpsDTO);
    }

    private Object enhanceDTO(FileWpsDTO fileWpsDTO) {
        //将wps传来的数据更新给老fileId
        fileApi.updateFileByWps(fileWpsDTO);
        //拿到最新的时间
        fileWpsDTO = fileApi.selectWpsDTOById(fileWpsDTO.getId());
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("id", fileWpsDTO.getId() + "");
            data.put("name", fileWpsDTO.getName());
            data.put("version", 1);
            data.put("size", (int) fileWpsDTO.getSize());
            data.put("create_time", (int) fileWpsDTO.getCreateTime().toEpochSecond(ZoneOffset.ofHours(8)));
            data.put("modify_time", (int) fileWpsDTO.getUpdateTime().toEpochSecond(ZoneOffset.ofHours(8)));
            data.put("creator_id", fileWpsDTO.getCreator() + "");
            data.put("modifier_id", fileWpsDTO.getUpdater() + "");
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private FileWpsDTO wpsDTO2FileDO(String fileId, SaveModel saveModel) throws IOException {
        FileWpsDTO newFile = getUploadInfo(fileId, saveModel);
        FileWpsDTO result = new FileWpsDTO()
                .setId(Long.valueOf(fileId))
                .setName(newFile.getName())
                .setSize(newFile.getSize())
                .setCreator(newFile.getCreator())
                .setUpdater(newFile.getUpdater())
                .setCreateTime(newFile.getCreateTime())
                .setUpdateTime(newFile.getUpdateTime())
                .setUrl(newFile.getUrl());
        return result;
    }

    /**
     * 参照KingGridFileResourceV2
     */
    private FileWpsDTO getUploadInfo(String fileId, SaveModel saveModel) throws IOException {
        MultipartFile file = saveModel.getFile();
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(fileId);
        Long newFileId = fileApi.uploadFile(saveModel.getName(), MODULE_MODEL + "/" + IdUtil.fastSimpleUUID() + "/" + saveModel.getName(), IoUtil.readBytes(file.getInputStream()));
        FileWpsDTO newFile = fileApi.selectWpsDTOById(newFileId);
        fileApi.updateFileInfo(new FileDTO().setId(fileDTO.getId()).setName(newFile.getName()).setPath(newFile.getPath()).setSize(newFile.getSize()).setType(newFile.getType()).setUrl(newFile.getUrl()));

        return newFile;
    }

    /**
     * 三阶段提交-  1. 准备上传阶段
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/upload/prepare", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object prepare(@PathVariable String file_id) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            FileModel fileModel = new FileModel();
            PermissionModel permissionModel = new PermissionModel();
            File localFile = new File(fileNameMap.get(file_id));
            fileModel.size = localFile.length();
            ArrayList<String> sign = new ArrayList<>();
            sign.add("md5");
            //// TODO: 下载链接中的参数如带中文等特殊字符，参数必须进行urlencode
            // data.put("download_url", fileModel.download_url + file_id);
            data.put("digest_types", sign);

            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 三阶段提交-  2. 获取上传地址
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/upload/address", method = RequestMethod.POST)
    @ResponseBody
    @PermitAll
    public Object address(@PathVariable String file_id, AddressModel addressModel) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            FileModel fileModel = new FileModel();
            PermissionModel permissionModel = new PermissionModel();
            File localFile = new File(fileNameMap.get(file_id));
            fileModel.size = localFile.length();
            ArrayList<String> sign = new ArrayList<>();
            sign.add("md5");
            //// TODO: 下载链接中的参数如带中文等特殊字符，参数必须进行urlencode
            // data.put("download_url", fileModel.download_url + file_id);
            data.put("method", "PUT");
            data.put("url", FileModel.download_url + file_id);

            //TODO:上传文档时需要信息
            HashMap<String, String> headers = new HashMap<>();
            HashMap<String, String> params = new HashMap<>();
            HashMap<String, String> send_back_params = new HashMap<>();

            data.put("headers", headers);
            data.put("params", params);
            data.put("send_back_params", send_back_params);

            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 三阶段提交-  3. 上传完成后，回调通知上传结果
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/upload/complete", method = RequestMethod.POST)
    @ResponseBody
    @PermitAll
    public Object complete(@PathVariable String file_id, AddressModel addressModel) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {

            FileModel fileModel = new FileModel();
            File localFile = new File(fileNameMap.get(file_id));
            fileModel.size = localFile.length();
            //// TODO: 文件的id应该唯一
            data.put("id", file_id);
            data.put("name", fileNameMap.get(file_id));
            //// TODO: 文件的版本控制
            fileModel.version++;
            data.put("version", fileModel.version);
            //// TODO: 必须返回文件真实大小，服务端会检查
            data.put("size", fileModel.size);
            data.put("create_time", fileModel.create_time);
            data.put("modify_time", fileModel.modify_time);
            data.put("creator_id", fileModel.creator_id);
            data.put("modifier_id", fileModel.modifier_id);

            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 用户信息
     *
     * @param user_ids
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/users", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object users(@RequestParam String user_ids) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<UserModel> data = new ArrayList<>();

        //  System.out.println("user_ids = " + user_ids);

        try {
            UserModel userModel = new UserModel();
            userModel.setId("406");
            userModel.setAvatar_url("https://picsum.photos/100/100/?image=1");
            userModel.setName("新版WebOffice");
            UserModel userModel2 = new UserModel();

            userModel2.setId("405");
            userModel2.setAvatar_url("https://picsum.photos/100/100/?image=1");
            userModel2.setName("6666");
            data.add(userModel);
            data.add(userModel2);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 重命名
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/name", method = RequestMethod.PUT)
    @ResponseBody
    public Object rename(@PathVariable String file_id, @RequestBody String name) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<UserModel> data = new ArrayList<>();
        try {
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 文档历史版本列表
     *
     * @param file_id
     * @param offset
     * @param limit
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/versions", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object fileVersionInfo(@PathVariable("file_id") Long file_id, Integer offset, Integer limit) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        ArrayList<FileModel> data = new ArrayList<>();
        try {
            FileModel fileModel = new FileModel();
            fileModel.setId(fileModel.id);
            fileModel.setName(fileModel.name);
            fileModel.setSize(fileModel.size);
            fileModel.setVersion(fileModel.version);
            fileModel.setCreate_time(fileModel.create_time);
            fileModel.setCreator_id(fileModel.creator_id);
            fileModel.setModifier_id(fileModel.modifier_id);
            fileModel.setModify_time(fileModel.modify_time);

            FileModel fileModel2 = new FileModel();
            fileModel2.setId(fileModel.id);
            fileModel2.setName(fileModel.name);
            fileModel2.setSize(fileModel.size);
            fileModel2.setVersion(fileModel.version++);
            fileModel2.setCreate_time(fileModel.create_time);
            fileModel2.setCreator_id(fileModel.creator_id);
            fileModel2.setModifier_id(fileModel.modifier_id);
            fileModel2.setModify_time(fileModel.modify_time);

            data.add(fileModel);
            data.add(fileModel2);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 历史版本预览，回滚等相关功能需要获取回滚的版本信息
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/versions/{version}", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object version(@PathVariable("file_id") String file_id, @PathVariable("version") Integer version) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            // 这里通过数据库查询对应的文件id 和版本信息
            FileModel fileModel = new FileModel();
            File localFile = new File(fileNameMap.get(file_id));
            fileModel.size = localFile.length();
            //// TODO: 文件的id应该唯一
            data.put("id", file_id);
            data.put("name", fileNameMap.get(file_id));
            //// TODO: 文件的版本控制
            data.put("version", fileModel.version);
            //// TODO: 必须返回文件真实大小，服务端会检查
            data.put("size", fileModel.size);
            data.put("create_time", fileModel.create_time);
            data.put("modify_time", fileModel.modify_time);
            data.put("creator_id", fileModel.creator_id);
            data.put("modifier_id", fileModel.modifier_id);

            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 获得文件
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/versions/{version}/download", method = RequestMethod.GET)
    @ResponseBody
    @PermitAll
    public Object download(@PathVariable("file_id") String file_id, @PathVariable("version") Integer version) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            // 这里通过数据库查询对应的文件id 和版本信息
            data.put("url", FileModel.download_url + file_id);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 水印
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/files/{file_id}/watermark", method = RequestMethod.GET)
    @ResponseBody
    public Object watermark(@PathVariable String file_id) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("type", 1);
            data.put("value", "谢绝拷贝\\n2023-02-07 13:18:11");
            data.put("fill_style", "rgba( 192, 192, 192, 0.6 )");
            data.put("font", "bold 20px Serif");
            data.put("horizontal", 50);
            data.put("vertical", 100);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * 事件通知
     *
     * @param file_id
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/v3/3rd/notify", method = RequestMethod.GET)
    @ResponseBody
    public Object notify(@PathVariable String file_id, SessionOpenContent sessionOpenContent) throws UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("type", 1);
            data.put("value", "谢绝拷贝\\n2023-02-07 13:18:11");
            data.put("fill_style", "rgba( 192, 192, 192, 0.6 )");
            data.put("font", "bold 20px Serif");
            data.put("horizontal", 50);
            data.put("vertical", 100);
            jsonObject.put("data", data);
            jsonObject.put("code", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //拿请求头方法
    private Map<String, Object> getHeads(HttpServletRequest request) {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        Enumeration<String> headers = request.getHeaderNames();
        //System.out.println("请求头信息");
        while (headers.hasMoreElements()) {
            String headName = (String) headers.nextElement();
            String headValue = request.getHeader(headName);
            System.out.println(headName + "：" + headValue);
            stringObjectHashMap.put(headName, headValue);
        }
        return stringObjectHashMap;
    }

    @GetMapping("/weboffice/getFile")
    @PermitAll
    public ResponseEntity<byte[]> getFile(@RequestParam("_w_fileid") String fileId) throws Exception {
        //下载文件
        FileWpsDTO fileWpsDTO = fileApi.selectWpsDTOById(fileId);
        String fileName = fileWpsDTO.getName();
        String uuid = IdUtil.fastSimpleUUID();
        String ready2UploadFolderPath = READY_TO_UPLOAD_PATH + "/" + uuid;
        FileUtil.mkdir(ready2UploadFolderPath);
        String pdfFilePath = ready2UploadFolderPath + "/" + fileWpsDTO.getName();
        ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(Long.valueOf(fileId)));
        File file = new File(pdfFilePath);
        // 将流的内容写入文件
        FileUtil.writeFromStream(byteArrayInputStream, file);
        InputStream inputStream = new FileInputStream(file);
        byte[] body = new byte[inputStream.available()];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + FILE_TYPE_DOC, "utf-8"));
        inputStream.read(body);
        //删除临时目录
        FileUtil.del(ready2UploadFolderPath);
        return new ResponseEntity(body, headers, HttpStatus.OK);
    }
}
