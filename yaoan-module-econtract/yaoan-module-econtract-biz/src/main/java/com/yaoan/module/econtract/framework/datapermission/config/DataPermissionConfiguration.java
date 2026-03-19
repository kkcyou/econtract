package com.yaoan.module.econtract.framework.datapermission.config;

import com.yaoan.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.BatchPlanInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import com.yaoan.module.econtract.dal.dataobject.model.MyCollectModel;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskUserDO;
import com.yaoan.module.econtract.dal.dataobject.riskalert.RiskAlertDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer contractDeptDataPermissionRuleCustomizer() {
        return rule -> {
            // dept
            rule.addDeptColumn(EcmsDemo.class);
//            rule.addDeptColumn(Model.class);

            /**
             * 供应商
             */
//            rule.addSupplyColumn(DraftOrderInfoDO.class, "supplier_guid");
//            rule.addSupplyColumn(ContractDO.class, "supplier_id");
            rule.addSupplyColumn(PackageInfoDO.class, "supplier_ids");

            /**
             * 采购人
             */
            rule.addOrgColumn(DraftOrderInfoDO.class, "purchaser_org_guid");
//            rule.addOrgColumn(ContractDO.class, "buyer_org_id");
            rule.addOrgColumn(BatchPlanInfoDO.class, "purchaser_id");
            rule.addOrgColumn(PackageInfoDO.class, "purchaser_org_ids");

            /**
             * 部门
             * */
            rule.addDeptColumn(ContractDO.class);
            rule.addDeptColumn(PaymentScheduleDO.class);
            rule.addDeptColumn(PaymentApplicationDO.class);
            rule.addDeptColumn(ContractInvoiceManageDO.class);
            rule.addDeptColumn(PaymentApplScheRelDO.class);

            rule.addDeptColumn(BpmContract.class);
            rule.addDeptColumn(ContractBorrowBpmDO.class);
            rule.addDeptColumn(ContractPerformanceDO.class);
//            rule.addDeptColumn(ContractTemplate.class);
            rule.addDeptColumn(TemplateBpmDO.class);
//            rule.addDeptColumn(ContractType.class);
            rule.addDeptColumn(Ledger.class);
            rule.addDeptColumn(ModelBpmDO.class);
            rule.addDeptColumn(MyCollectModel.class);
//            rule.addDeptColumn(Param.class);
            rule.addDeptColumn(PerfTaskDO.class);
            rule.addDeptColumn(PerfTaskUserDO.class);
//            rule.addDeptColumn(Relative.class);
            rule.addDeptColumn(RiskAlertDO.class);
//            rule.addDeptColumn(Term.class);

            /**
             * 用户
             * */
            rule.addUserColumn(EcmsDemo.class, "creator");
//            rule.addUserColumn(Model.class, "creator");

//            rule.addUserColumn(ContractDO.class, "creator");
            rule.addUserColumn(PaymentScheduleDO.class, "creator");
            rule.addUserColumn(PaymentApplicationDO.class, "creator");
            rule.addUserColumn(PaymentApplScheRelDO.class, "creator");

            rule.addUserColumn(BpmContract.class, "creator");
            rule.addUserColumn(ContractBorrowBpmDO.class, "creator");
            rule.addUserColumn(ContractPerformanceDO.class, "creator");
//            rule.addUserColumn(ContractTemplate.class, "creator");
            rule.addUserColumn(TemplateBpmDO.class, "creator");
//            rule.addUserColumn(ContractType.class, "creator");
            rule.addUserColumn(Ledger.class, "creator");
            rule.addUserColumn(ModelBpmDO.class, "creator");
            rule.addUserColumn(MyCollectModel.class, "creator");
//            rule.addUserColumn(Param.class, "creator");
            rule.addUserColumn(PerfTaskDO.class, "creator");
            rule.addUserColumn(PerfTaskUserDO.class, "creator");
//            rule.addUserColumn(Relative.class, "creator");
            rule.addUserColumn(RiskAlertDO.class, "creator");
//            rule.addUserColumn(Term.class, "creator");
//            rule.addCompanyColumn(ContractSignetDO.class);


//            rule.addSupplyColumn(ContractDO.class, "supplier_guid");
//            rule.addSupplyColumn(PackageInfoDO.class, "supplier_ids");


        };
    }

}
