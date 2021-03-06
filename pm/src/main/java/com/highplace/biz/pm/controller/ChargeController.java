package com.highplace.biz.pm.controller;

import com.highplace.biz.pm.domain.charge.Bill;
import com.highplace.biz.pm.domain.charge.Charge;
import com.highplace.biz.pm.domain.charge.Subject;
import com.highplace.biz.pm.domain.ui.ChargeDetailSearchBean;
import com.highplace.biz.pm.domain.ui.ChargeSearchBean;
import com.highplace.biz.pm.domain.ui.PageBean;
import com.highplace.biz.pm.domain.ui.PropertyBillDetail;
import com.highplace.biz.pm.service.ChargeService;
import com.highplace.biz.pm.service.common.MQService;
import com.highplace.biz.pm.service.common.TaskStatusService;
import com.highplace.biz.pm.service.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class ChargeController {

    public static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private MQService mqService;

    //////////////////费用科目管理/////////////////////
    @RequestMapping(path = "/charge/subject", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/subject;GET','/charge/subject;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeSubject(PageBean pageBean, Principal principal) throws Exception {
        return chargeService.querySubject(SecurityUtils.getCurrentProductInstId(principal), pageBean, false);
    }

    @RequestMapping(path = "/charge/subject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/subject;POST','/charge/subject;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Subject createChargeSubject(@Valid @RequestBody Subject subject,
                                       Principal principal) throws Exception {

        //插入记录
        int rows = chargeService.insertSubject(SecurityUtils.getCurrentProductInstId(principal), subject);
        logger.debug("subject insert return num:{}" ,rows);
        if (rows != 1) {
            throw new Exception("create failed, effected num:" + rows);
        }
        return subject;
    }

    @RequestMapping(path = "/charge/subject", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('/charge/subject;DELETE','/charge/subject;ALL','/charge/**;DELETE','/charge/**;ALL','ADMIN')")
    public void deleteChargeSubject(@RequestParam(value = "subjectId", required = true) Long subjectId,
                                    Principal principal) throws Exception {

        //删除记录
        int rows = chargeService.deleteSubject(SecurityUtils.getCurrentProductInstId(principal), subjectId);
        logger.debug("subjectId delete return num:{}" ,rows);
        if (rows != 1) {
            throw new Exception("delete failed, effected num:" + rows);
        }
    }

    @RequestMapping(path = "/charge/subject", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('/charge/subject;PUT','/charge/subject;ALL','/charge/**;PUT','/charge/**;ALL','ADMIN')")
    public Subject changeChargeSubject(@RequestBody Subject subject,
                                       Principal principal) throws Exception {

        if (subject.getSubjectId() == null) throw new Exception("subjectId is null");

        //更新记录
        int rows = chargeService.updateSubject(SecurityUtils.getCurrentProductInstId(principal), subject);
        logger.debug("post subject:{}" ,subject.toString());
        if (rows != 1) {
            throw new Exception("change failed, effected num:" + rows);
        }
        return subject;
    }

    //////////////////账单类型管理/////////////////////
    @RequestMapping(path = "/charge/bill/catalog", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/bill/catalog;GET','/charge/bill/catalog;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getBillTypeCatalog(Principal principal) {

        return chargeService.rapidSearchBillType(SecurityUtils.getCurrentProductInstId(principal));
    }

    @RequestMapping(path = "/charge/bill", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/bill;GET','/charge/bill;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeBillType(PageBean pageBean, Principal principal) throws Exception {
        return chargeService.queryBillType(SecurityUtils.getCurrentProductInstId(principal), pageBean, false);
    }

    @RequestMapping(path = "/charge/bill", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/bill;POST','/charge/bill;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Bill createChargeBillType(@Valid @RequestBody Bill bill, Principal principal) throws Exception {
        //插入记录
        int rows = chargeService.insertBillType(SecurityUtils.getCurrentProductInstId(principal), bill);
        logger.debug("bill insert return num: {}" ,rows);
        if (rows != 1) {
            throw new Exception("create failed, effected num:" + rows);
        }
        return bill;
    }

    @RequestMapping(path = "/charge/bill", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('/charge/bill;PUT','/charge/bill;ALL','/charge/**;PUT','/charge/**;ALL','ADMIN')")
    public Bill changeChargeBillType(@RequestBody Bill bill,
                                     Principal principal) throws Exception {

        if (bill.getBillId() == null) throw new Exception("billId is null");
        if (bill.getBillSubjectRelList() == null || bill.getBillSubjectRelList().size() == 0)
            throw new Exception("billSubjectRelList is null");

        //插入记录
        int rows = chargeService.updateBillType(SecurityUtils.getCurrentProductInstId(principal), bill);
        if (rows != 1)
            throw new Exception("change failed, effected num:" + rows);

        return bill;
    }

    @RequestMapping(path = "/charge/bill", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('/charge/bill;DELETE','/charge/bill;ALL','/charge/**;DELETE','/charge/**;ALL','ADMIN')")
    public void deleteChargeBillType(@RequestParam(value = "billId", required = true) Long billId,
                                     Principal principal) throws Exception {

        //删除记录
        int rows = chargeService.deleteBillType(SecurityUtils.getCurrentProductInstId(principal), billId);
        logger.debug("subjectId delete return num: {}" ,rows);
        if (rows != 1) {
            throw new Exception("delete failed, effected num:" + rows);
        }
    }


    //////////////////费用出账/////////////////////
    //查询已出账单
    @RequestMapping(path = "/charge", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge;GET','/charge;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getCharge(ChargeSearchBean chargeSearchBean, Principal principal) throws Exception {
        return chargeService.queryCharge(SecurityUtils.getCurrentProductInstId(principal), chargeSearchBean, false);
    }

    //创建出账单(未导入仪表数据)
    @RequestMapping(path = "/charge", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge;POST','/charge;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Charge initialCharge(@Valid @RequestBody Charge charge, Principal principal) throws Exception {

        //可以传入chargeId，也可以不传入
        return chargeService.insertCharge(SecurityUtils.getCurrentProductInstId(principal), charge);
    }

    //获取需要导入的仪表数据
    @RequestMapping(path = "/charge/importInfo", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/importInfo;GET','/charge/importInfo;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeImportInfo(@RequestParam(value = "chargeId", required = true) Long chargeId,
                                                   Principal principal) throws Exception {

        return chargeService.getChargeImportInfo(SecurityUtils.getCurrentProductInstId(principal), chargeId);
    }

    //修改出账单信息(导入仪表数据之后,设置状态,并提交消息队列处理进行费用计算)
    //(仪表数据导入完成后设置为1,收费中设置为3)
    @RequestMapping(path = "/charge", method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('/charge;PUT','/charge;ALL','/charge/**;PUT','/charge/**;ALL','ADMIN')")
    public Charge updateCharge(@RequestBody Charge charge, Principal principal) throws Exception {

        if (charge.getChargeId() == null) throw new Exception("chargeId is null");
        String productInstId = SecurityUtils.getCurrentProductInstId(principal);
        Integer status = charge.getStatus();
        if (status != null) {
            if (status != 1 && status != 3) throw new Exception("出账单状态只能修改为1和3");
        }

        int rows = chargeService.updateCharge(productInstId, charge);
        if (rows == -1) {
            throw new Exception("chargeId not exists");
        } else if (rows == -2) {
            throw new Exception("仪表用量数据未导入完成");
        } else if (rows == -3) {
            throw new Exception("出账单当前状态必须为出账完成");
        } else if (rows != 1) {
            throw new Exception("change failed, effected num:" + rows);
        }

        //如果修改状态为1,则提交消息队列,进行出账计算
        if (status != null && status == 1) {
            mqService.sendChargeMessage(UUID.randomUUID().toString(), productInstId, charge.getChargeId(), null);
        }
        return charge;
    }

    //删除出账单,必须为出账中状态才能删除,会同时删除对应的仪表用量信息
    @RequestMapping(path = "/charge", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyAuthority('/charge;DELETE','/charge;ALL','/charge/**;DELETE','/charge/**;ALL','ADMIN')")
    public void deleteCharge(@RequestParam(value = "chargeId", required = true) Long chargeId,
                             Principal principal) throws Exception {

        //删除记录
        int rows = chargeService.deleteCharge(SecurityUtils.getCurrentProductInstId(principal), chargeId);
        if (rows == -1) {
            throw new Exception("chargeId is error");
        } else if (rows == -2) {
            throw new Exception("非出账中状态,不能删除");
        } else if (rows != 1) {
            throw new Exception("delete failed, effected num:" + rows);
        }
    }

    //导入仪表用量信息,异步任务
    @RequestMapping(path = "/charge/import", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/import;POST','/charge/import;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Map<String, Object> importRequest(@RequestParam(value = "chargeId", required = true) Long chargeId,
                                             @RequestParam(value = "feeDataType", required = true) Integer feeDataType,
                                             @RequestParam(value = "fileUrl", required = true) String fileUrl,
                                             @RequestParam(value = "vendor", required = false) Integer vendor,
                                             Principal principal) {

        //对象存储服务供应商 0: 腾讯云 1:阿里云 ，默认为0
        if (vendor == null) vendor = new Integer(0);

        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put(ChargeService.MAP_CHARGE_ID, chargeId);
        extraMap.put(ChargeService.MAP_FEE_DATA_TYPE, feeDataType);

        return taskStatusService.sendTaskToMQ(TaskStatusService.TaskTargetEnum.CHARGE,
                TaskStatusService.TaskTypeEnum.IMPORT,
                SecurityUtils.getCurrentProductInstId(principal),
                fileUrl,
                vendor,
                extraMap);

    }

    //查询导入结果
    @RequestMapping(path = "/charge/import", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/import;GET','/charge/import;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<Object, Object> getImportTaskResult(@RequestParam(value = "taskId", required = true) String taskId,
                                                   Principal principal) {

        return taskStatusService.getTaskStatus(TaskStatusService.TaskTargetEnum.CHARGE,
                TaskStatusService.TaskTypeEnum.IMPORT,
                SecurityUtils.getCurrentProductInstId(principal),
                taskId);
    }

    ///////////////////////////出账单明细////////////////////////
    @RequestMapping(path = "/charge/detail", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/detail;GET','/charge/detail;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeDetail(ChargeDetailSearchBean chargeDetailSearchBean, Principal principal) throws Exception {
        return chargeService.queryChargeDetail(SecurityUtils.getCurrentProductInstId(principal), chargeDetailSearchBean, false);
    }

    //查询chargeId下对应房产的收费明细
    @RequestMapping(path = "/charge/{chargeId}/{propertyId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge;GET','/charge;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public PropertyBillDetail getChargePropertyBillDetail(@PathVariable("chargeId") Long chargeId,
                                                          @PathVariable("propertyId") Long propertyId,
                                                          Principal principal) throws Exception {
        return chargeService.rapidGetChargePropertyBillDetail(SecurityUtils.getCurrentProductInstId(principal), chargeId, propertyId);
    }

    @RequestMapping(path = "/charge/detail/export", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/detail/export;POST','/charge/detail/export;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Map<String, Object> exportChargeDetail(ChargeDetailSearchBean chargeDetailSearchBean, Principal principal) {

        //对象存储服务供应商 0: 腾讯云 1:阿里云 ，默认为0
        if (chargeDetailSearchBean.getVendor() == null) chargeDetailSearchBean.setVendor(0);

        return taskStatusService.sendTaskToMQ(TaskStatusService.TaskTargetEnum.CHARGE_DETAIL,
                TaskStatusService.TaskTypeEnum.EXPORT,
                SecurityUtils.getCurrentProductInstId(principal),
                null,
                chargeDetailSearchBean.getVendor(),
                chargeDetailSearchBean.toMap());
    }

    @RequestMapping(path = "/charge/detail/export", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/detail/export;GET','/charge/detail/export;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<Object, Object> getExportTaskResult(@RequestParam(value = "taskId", required = true) String taskId,
                                                   Principal principal) {

        return taskStatusService.getTaskStatus(TaskStatusService.TaskTargetEnum.CHARGE_DETAIL,
                TaskStatusService.TaskTypeEnum.EXPORT,
                SecurityUtils.getCurrentProductInstId(principal),
                taskId);
    }
}
