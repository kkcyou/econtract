package com.yaoan.module.econtract.service.param;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.param.vo.*;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.convert.param.ParamConverter;
import com.yaoan.module.econtract.dal.dataobject.category.ParamCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.dataobject.paramModel.ParamModel;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.TermParam;
import com.yaoan.module.econtract.dal.mysql.category.ParamCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.dal.mysql.term.ModelTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermParamMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.ModelTypeEnum;
import com.yaoan.module.econtract.enums.ParamTypeEnums;
import com.yaoan.module.econtract.enums.WordlengthEnums;
import com.yaoan.module.econtract.service.paramModel.ParamModelMapper;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR_V2;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
public class ParamServiceImpl implements ParamService {
    @Resource
    private ParamModelMapper paramModelMapper;
    @Resource
    private ParamCategoryMapper paramCategoryMapper;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ParamConverter paramConverter;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private TermParamMapper termParamMapper;

    @Override
    public String saveParamV2(ParamReqVO paramReqVO)  {
        if (ParamTypeEnums.TXT.getCode().equals(paramReqVO.getType())) {
       //文本类型不可为空
            if(ObjectUtil.isEmpty(paramReqVO.getMaxLength())){
                throw exception(ErrorCodeConstants.MAXLENTH_IS_NULL);
            }
        }
        //1.生成entity
        Param entity = paramConverter.toEntity(paramReqVO);
        //2.校验名称是否重复
        if (nameExist(entity.getId(), entity.getName())) {
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        }
        //3.校验编码是否重复
        if (codeExist(entity.getId(), entity.getCode())) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }
        if (ObjectUtil.isNotEmpty(entity.getId())) {
            //7.修改參數信息
            paramMapper.updateById(entity);
        } else {
            //3 执行插入操作
            paramMapper.insert(entity);
        }
        return entity.getId();
    }

    @Override
    public List<String> removeByIds(ReqIdsVO paramByIdsVO) throws Exception {
        //1.校验是否被调用
        List<String> idlist = queryParamInfoByIds(paramByIdsVO);
        if (ObjectUtil.isEmpty(idlist)) {
            //2.没有调用则根据id查询对应的图片存放路径
            List<Param> params = queryParam(null, null, paramByIdsVO.getIds(), null);
            if (CollUtil.isNotEmpty(params)) {
                List<String> ids = paramByIdsVO.getIds();
                //2.刪除參數信息
                paramMapper.deleteBatchIds(ids);
                //3.循环删除minio中的图片
                for (Param param : params) {
                    try {
                        FileDTO fileDTO = fileApi.selectById(param.getIconId());
                        fileApi.deleteFile(param.getIconId());
                    } catch (Exception e) {
                    }
                }
            }
        }
        return idlist;
    }

    @Override
    public PageResult<ParamRespVO> queryAllParam(ParamPageReqVO paramPageReqVO) {
        PageResult<Param> paramPageResult = paramMapper.selectSentPage(paramPageReqVO);
        PageResult<ParamRespVO> paramRespVOPageResult = paramConverter.convertPage(paramPageResult);
        if (CollUtil.isNotEmpty(paramRespVOPageResult.getList())) {
        //获取所有分类信息
        List<Integer> paramCategoryIds = paramRespVOPageResult.getList().stream().map(ParamRespVO::getCategoryId).collect(Collectors.toList());
        List<ParamCategory> paramCategories = paramCategoryMapper.selectList(new QueryWrapper<ParamCategory>().in("id",paramCategoryIds));
        Map<Integer, ParamCategory> categoryMap = CollectionUtils.convertMap(paramCategories, ParamCategory::getId);
        //获取用户信息
        List<String> userIdsStr = paramRespVOPageResult.getList().stream().map(ParamRespVO::getCreator).collect(Collectors.toList());
        //将userids转成list<long>类型
        List<Long> userIdsLong = userIdsStr.stream().map(Long::parseLong).collect(Collectors.toList());
        List<AdminUserRespDTO> userList1 = adminUserApi.getUserList(userIdsLong);
        Map<Long, AdminUserRespDTO> creatorMap = CollectionUtils.convertMap(userList1, AdminUserRespDTO::getId);
        List<Long> pathIds = new ArrayList<>();
            //设置参数分类名称  优化  转成map key 为id
            for (ParamRespVO paramDTO : paramRespVOPageResult.getList()) {
                //1.设置参数分类名
                ParamCategory category = categoryMap.get(paramDTO.getCategoryId());
                paramDTO.setCategoryName(category == null ? null : category.getName());
                //2.设置创建人姓名
                AdminUserRespDTO adminUserRespDTO = creatorMap.get(Long.valueOf(paramDTO.getCreator()));
                paramDTO.setCreatorName(adminUserRespDTO == null ? null : adminUserRespDTO.getNickname());
                //3.设置修改人姓名
                AdminUserRespDTO adminUserRespDTO2 = creatorMap.get(Long.valueOf(paramDTO.getUpdater()));
                paramDTO.setUpdaterName(adminUserRespDTO2 == null ? null : adminUserRespDTO2.getNickname());
                pathIds.add(paramDTO.getIconId());
                //4.设置参数类型
                if (BeanUtil.isNotEmpty(ParamTypeEnums.getInstance(paramDTO.getType()))) {
                    paramDTO.setTypeName( ParamTypeEnums.getInstance(paramDTO.getType()).getInfo());
                }
                //5.字数长度要求
                if (BeanUtil.isNotEmpty(WordlengthEnums.getInstance(paramDTO.getMaxLength()))) {
                    paramDTO.setMaxLengthName( WordlengthEnums.getInstance(paramDTO.getMaxLength()).getInfo());
                }
                //7.设置合同类型
                ContractType contractType = contractTypeMapper.selectById(paramDTO.getContractType());
                paramDTO.setContractTypeName(contractType==null?null:contractType.getName());
            }
            //查询minio存储图片的地址   http://120.53.0.50:9000/ecms-econtract/2023-08-08\param\1691477307651-z.jpg
            List<FileDTO> fileDTOS = fileApi.selectBatchIds(pathIds);
            Map<Long, FileDTO> fileMap = CollectionUtils.convertMap(fileDTOS, FileDTO::getId);
            for (ParamRespVO paramDTO : paramRespVOPageResult.getList()) {
                        paramDTO.setIconPath(fileMap.get(paramDTO.getIconId())==null?null:fileMap.get(paramDTO.getIconId()).getUrl());
            }
        }
        return paramRespVOPageResult;
    }

    @Override
    public ParamRespVO queryParamById(String id) throws Exception {
        //1.根据参数id查询参数信息
        Param param = paramMapper.selectById(id);
        //2.转换成Relative
        ParamRespVO respVO = paramConverter.toVO(param);
        if(BeanUtil.isNotEmpty(respVO)){
            //3.设置参数分类名称
            String name = paramCategoryMapper.selectById(respVO.getCategoryId())==null?null:paramCategoryMapper.selectById(respVO.getCategoryId()).getName();
            respVO.setCategoryName(name);
            //4.设置参数类型
            if (BeanUtil.isNotEmpty(ParamTypeEnums.getInstance(respVO.getType()))) {
                respVO.setTypeName( ParamTypeEnums.getInstance(respVO.getType()).getInfo());
            }
            //5.字数长度要求
            if (BeanUtil.isNotEmpty(WordlengthEnums.getInstance(respVO.getMaxLength()))) {
                respVO.setMaxLengthName( WordlengthEnums.getInstance(respVO.getMaxLength()).getInfo());
            }
            //6.查看图片url
            String url = fileApi.getURL(respVO.getIconId());
            respVO.setIconPath(url);
            //7.设置合同类型
            ContractType contractType = contractTypeMapper.selectById(respVO.getContractType());
            respVO.setContractTypeName(contractType==null?null:contractType.getName());
        }

        return respVO;
    }

    /**
     * 根据ids查询对应的调用信息
     *
     * @param paramByIdsVO
     */
    public List<String> queryParamInfoByIds(ReqIdsVO paramByIdsVO) {
        List<String> ids = paramByIdsVO.getIds();
        Set<String> set1=new HashSet<>();
        Set<String> set2=new HashSet<>();
        // 1.查询条款表中是否用到该参数
        List<TermParam> termParams = termParamMapper.selectList(new LambdaQueryWrapperX<TermParam>().inIfPresent(TermParam::getParamId, ids));
        if (CollUtil.isNotEmpty(termParams)) {
             set1 = termParams.stream().map(TermParam::getParamId).collect(Collectors.toSet());
        }
        //2.查询模板表中是否用到该参数
        List<ParamModel> paramModels = paramModelMapper.selectList(new LambdaQueryWrapperX<ParamModel>().inIfPresent(ParamModel::getParamId, ids));
        //存在返回false，不存在返回true
        if (CollUtil.isNotEmpty(paramModels)) {
           set2 = paramModels.stream().map(ParamModel::getParamId).collect(Collectors.toSet());
        }
        List<String> paramIds = Stream.concat(set1.stream(), set2.stream()).distinct().collect(Collectors.toList());
        return paramIds;
    }

    @Override
    public List<ParamListRespVO> queryParamAndCategory(BaseParamVO baseParamVO) {
        List<Integer> ids = new ArrayList<>();
        List<Long> pathIds = new ArrayList<>();
        List<Param> params = queryParam(baseParamVO.getName(), baseParamVO.getCategoryId(), null, baseParamVO.getContractType());
        List<ParamListRespVO> paramCategoryDTOS = new ArrayList<>();
        if (CollUtil.isNotEmpty(params)) {
            for (Param param : params) {
                Integer categoryId = param.getCategoryId();
                if (!ids.contains(categoryId)) {
                    ids.add(categoryId);
                }
                if (!pathIds.contains(param.getIconId())) {
                    pathIds.add(param.getIconId());
                }
            }
            List<ParamCategory> paramCategories = paramCategoryMapper.selectList(new LambdaQueryWrapperX<ParamCategory>().inIfPresent(ParamCategory::getId, ids));
            //查询minio存储图片的地址   http://120.53.0.50:9000/ecms-econtract/2023-08-08\param\1691477307651-z.jpg
            List<FileDTO> fileDTOS = fileApi.selectBatchIds(pathIds);
            Map<Long, FileDTO> fileMap = CollectionUtils.convertMap(fileDTOS, FileDTO::getId);
            paramCategoryDTOS = categoryConverter.paramToList(paramCategories);
            for (ParamListRespVO paramCategory : paramCategoryDTOS) {
                List<Map<String, Object>> list = new ArrayList<>();
                for (Param param : params) {
                    Map<String, Object> map = new HashMap<>();
                    if (paramCategory.getId().equals(param.getCategoryId())) {
                        map.put("name", param.getName());
                        map.put("id", param.getId());
                        map.put("code", param.getCode());
                        map.put("placeholder", param.getPlaceholder());
                        map.put("maxLength", param.getMaxLength());
                        FileDTO fileDTO = fileMap.get(param.getIconId());
                        map.put("iconPath", fileDTO==null?null:fileDTO.getUrl());
                        map.put("type", param.getType());
                        if(ParamTypeEnums.TBLE.getCode().equals(param.getType())){
                            //表格
                            map.put("tableSet", param.getTableSet());
                            map.put("lineNum", param.getLineNum());
                            map.put("columnNum", param.getColumnNum());
                            map.put("tableDirection", param.getTableDirection());
                            map.put("lineHigh", param.getLineHigh());
                            map.put("totalLine", param.getTotalLine());
                            map.put("alignment", param.getAlignment());
                        }
                        if(ParamTypeEnums.DORP_DOWN.getCode().equals(param.getType())){
                            //下拉框
                            map.put("optionSet", param.getOptionSet());
                            map.put("optionNum", param.getOptionNum());
                        }
                        list.add(map);
                    }
                }
                paramCategory.setParams(list);
            }
        }
        return paramCategoryDTOS;
    }

    private List<Param> queryParam(String name, Integer categoryId, List<String> Ids, String contractType) {
        List<Param> params = paramMapper.selectList(name, categoryId, Ids, contractType);
        return params;
    }

    public Boolean nameExist(String id, String name) {
        return paramMapper.nameExist(id, name);
    }

    public Boolean codeExist(String id, String code) {
        return paramMapper.codeExist(id, code);
    }



    @Resource
    private TermMapper termMapper;
    @Resource
    private ModelTermMapper modelTermMapper;
    @Resource
    private ModelMapper modelMapper;

    /**
     * 如果不是范本制作的模板，则返回空集合
     */
    @Override
    public List<TermV2RespVO> queryParamByModelId(String id) {
        Model model = modelMapper.selectById(id);
        if (ObjectUtil.isNull(model)) {
            throw exception(EMPTY_DATA_ERROR_V2, "模板");
        }
        //如果不是参考文本制作的模板，则直接返回空
        if (!Objects.equals(ModelTypeEnum.TEMPLATE.getCode(), model.getType())) {
            return Collections.emptyList();
        }
        List<TermV2RespVO> result = new ArrayList<TermV2RespVO>();
        //找到模板关联的条款
        List<Term> termList = termMapper.selectListByModelId(id);
        if (CollectionUtil.isEmpty(termList)) {
            throw exception(EMPTY_DATA_ERROR_V2, "条款");
        }
        List<String> termIds = termList.stream().map(Term::getId).collect(Collectors.toList());
        List<TermParam> termParamList = termParamMapper.selectParamByTermIds(termIds);
        if (CollectionUtil.isEmpty(termParamList)) {
            throw exception(EMPTY_DATA_ERROR_V2, "条款参数关系");
        }
        List<String> paramIds = termParamList.stream().map(TermParam::getParamId).collect(Collectors.toList());
        List<Param> paramList = paramMapper.selectList(Param::getId, paramIds);
        if (CollectionUtil.isEmpty(paramList)) {
            throw exception(EMPTY_DATA_ERROR_V2, "参数");
        }
        Map<String, Param> paramMap = CollectionUtils.convertMap(paramList, Param::getId);
        Map<String, List<TermParam>> termParamMap = new HashMap<String, List<TermParam>>();
        termParamMap = termParamList.stream()
                .collect(Collectors.groupingBy(
                        TermParam::getTermId)); // 根据termId进行分组

        List<ModelTerm> modelTermList = modelTermMapper.selectList(ModelTerm::getModelId, id);
        if (CollectionUtil.isEmpty(modelTermList)) {
            throw exception(EMPTY_DATA_ERROR_V2, "模板条款关系");
        }
        Map<String, ModelTerm> modelTermMap = CollectionUtils.convertMap(modelTermList, ModelTerm::getTermId);
        //找到条款关联的参数
        for (Term term : termList) {
            String termId = term.getId();
            TermV2RespVO termV2RespVO = new TermV2RespVO();
            termV2RespVO.setTermId(termId);

            ModelTerm modelTerm = modelTermMap.get(termId);
            if (ObjectUtil.isNotNull(modelTerm)) {
                termV2RespVO.setTermNum(modelTerm.getTermNum());
                termV2RespVO.setTitle(modelTerm.getTitle());
            }
            List<ParamByTermRespVO> paramByTermRespVOList = new ArrayList<>();
            List<TermParam> termParams = termParamMap.get(term.getId());
            if (CollectionUtil.isNotEmpty(termParams)) {
                for (TermParam termParam : termParams) {
                    Param param = paramMap.get(termParam.getParamId());
                    if (ObjectUtil.isNotNull(param)) {
                        ParamByTermRespVO paramByTermRespVO = ParamConverter.INSTANCE.do2Resp(param);
                        paramByTermRespVO.setParamNum(termParam.getParamNum());


                        paramByTermRespVOList.add(paramByTermRespVO);
                    }
                }
                List<ParamByTermRespVO> sortedParamByTermRespVOList = paramByTermRespVOList.stream()
                        .sorted(Comparator.comparing(ParamByTermRespVO::getParamNum))
                        .collect(Collectors.toList());
                termV2RespVO.setParamByTermRespVOList(sortedParamByTermRespVOList);
            }
            result.add(termV2RespVO);
        }
        result = result.stream()
                .sorted(Comparator.comparing(TermV2RespVO::getTermNum))
                .collect(Collectors.toList());
        return result;
    }



}
