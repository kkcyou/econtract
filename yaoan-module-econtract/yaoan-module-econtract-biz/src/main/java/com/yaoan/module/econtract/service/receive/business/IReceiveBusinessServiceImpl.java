package com.yaoan.module.econtract.service.receive.business;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.purchasing.IProjectPurchasingApi;
import com.yaoan.module.econtract.api.purchasing.dto.BusinessInfoDTO;
import com.yaoan.module.econtract.api.purchasing.dto.ProjectPurchasingDTO;
import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;
import com.yaoan.module.econtract.dal.dataobject.alert.AlertDO;
import com.yaoan.module.econtract.dal.dataobject.purchasing.ReceiveBusinessesDO;
import com.yaoan.module.econtract.dal.mysql.alert.AlertMapper;
import com.yaoan.module.econtract.dal.mysql.receive.business.ReceiveBusinessesMapper;
import com.yaoan.module.econtract.enums.BusinessEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.process.ContractProcessStageEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@Service
public class IReceiveBusinessServiceImpl implements IReceiveBusinessService {
    /**
     * 虚拟系统对接api
     */
    @Resource
    private IProjectPurchasingApi projectPurchasingApi;
    /**
     * 虚拟系统数据标识存放表mapper
     */
    @Resource
    private ReceiveBusinessesMapper receiveBusinessesMapper;
    @Resource
    private AlertMapper alertMapper;

    @Override
    public void sendId(String id, Integer type, String tenantId, String deptId, String createUser) {
        //接收传递参数
        ReceiveBusinessesDO receiveBusinessesDO = (ReceiveBusinessesDO) new ReceiveBusinessesDO().setProjectPurchasingId(id).setType(type).setTenantId(Long.valueOf(tenantId)).setCreator(createUser);
        receiveBusinessesDO.setDeptId(Long.valueOf(deptId));
        //根据传递参数查询是否重复
        ReceiveBusinessesDO purchasingDO = receiveBusinessesMapper.selectOne(new LambdaQueryWrapperX<ReceiveBusinessesDO>()
                .eqIfPresent(ReceiveBusinessesDO::getProjectPurchasingId, id)
                .eqIfPresent(ReceiveBusinessesDO::getType, type));
        if (BeanUtil.isEmpty(purchasingDO)) {
            //不重复-新增
            receiveBusinessesMapper.insert(receiveBusinessesDO);
            //插入待办提醒记录
            insertAlertDO(receiveBusinessesDO);

        } else {
            //重复提示此数据已推送，请勿重复推送
            throw exception(ErrorCodeConstants.SEND_EXIST_ERROR);
        }
    }

    /**
     * 插入待办提醒记录
     */
    private void insertAlertDO(ReceiveBusinessesDO receiveBusinessesDO) {
        String resultStr = "";
        BusinessEnums businessEnums = BusinessEnums.getInstance(receiveBusinessesDO.getType());
        List<String> ids = new ArrayList<String>();
        ids.add(receiveBusinessesDO.getProjectPurchasingId());
        ReqIdsDTO reqIdsDTO = new ReqIdsDTO().setIds(ids);
        if (BusinessEnums.PROJECT_PURCHASING == businessEnums) {
            //去虚拟系统获取项目采购的信息
            resultStr = projectPurchasingApi.queryPurchasingByIds(reqIdsDTO.setType(receiveBusinessesDO.getType()));
            CommonResult<String> commonResult = (CommonResult) JSON.parseObject(resultStr, CommonResult.class);
            JSONArray dataJsonArray = new JSONArray(commonResult.getData());
            // 将JSONArray转换为List
            List<ProjectPurchasingDTO> list = JSON.parseArray(dataJsonArray.toString(), ProjectPurchasingDTO.class);
            if (CollectionUtil.isNotEmpty(list)) {
                ProjectPurchasingDTO projectPurchasingDTO = list.get(0);
                AlertDO alertDO = (AlertDO) new AlertDO().setBusinessId(projectPurchasingDTO.getId())
                        .setBusinessName(projectPurchasingDTO.getName())
                        .setFlowStage(ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CREATE.getCode())
                        .setTenantId(Long.valueOf(projectPurchasingDTO.getTenantId()))
                        .setCreator(projectPurchasingDTO.getCreateUser());

                alertMapper.insert(alertDO);
            } else {
                log.error("insertAlertDO：待办提醒信息插入异常");
            }

        }
        if (BusinessEnums.FRAMEWORK_AGREEMENT == businessEnums) {
            resultStr = projectPurchasingApi.queryFrameworkByIds(new ReqIdsDTO().setId(receiveBusinessesDO.getId()).setType(receiveBusinessesDO.getType()));
        }
        if (BusinessEnums.ELECTRONICS_STORE == businessEnums) {
            resultStr = projectPurchasingApi.queryElectronicsStoreByIds(new ReqIdsDTO().setId(receiveBusinessesDO.getId()).setType(receiveBusinessesDO.getType()));
        }


    }

    @Override
    public JSONArray queryPurchasingByIds(ReqIdsDTO reqIdsDTO) {
        //根据id标识调api批量查询项目采购信息
        String str = projectPurchasingApi.queryPurchasingByIds(reqIdsDTO);
        JSONObject jsonObj = new JSONObject(str);
        //获取列表数据进行返回
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        return jsonArray;
    }

    /**
     * 获得每个业务的id和名称
     */
    private BusinessInfoDTO getBusinessInfo(JSONArray jsonArray) {
        try {
            // 遍历 JSONArray 中的 JSONObject，并获取每个 JSONObject 中 "name" 字段的值
            BusinessInfoDTO businessInfoDTO = new BusinessInfoDTO();
            JSONObject obj = jsonArray.getJSONObject(0);
            String name = String.valueOf(obj.get("name"));
            String id = String.valueOf(obj.get("id"));
            businessInfoDTO.setBusinessId(id);
            businessInfoDTO.setBusinessName(name);
            return businessInfoDTO;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public JSONArray queryFrameworkByIds(ReqIdsDTO reqIdsDTO) {
        //根据id标识调api批量查询项目采购信息
        String str = projectPurchasingApi.queryFrameworkByIds(reqIdsDTO);
        JSONObject jsonObj = new JSONObject(str);
        //获取列表数据进行返回
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        return jsonArray;
    }

    @Override
    public JSONArray queryElectronicsStoreByIds(ReqIdsDTO reqIdsDTO) {
        //根据id标识调api批量查询项目采购信息
        String str = projectPurchasingApi.queryElectronicsStoreByIds(reqIdsDTO);
        JSONObject jsonObj = new JSONObject(str);
        //获取列表数据进行返回
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        return jsonArray;
    }
}
