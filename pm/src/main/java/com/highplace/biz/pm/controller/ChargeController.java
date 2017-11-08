package com.highplace.biz.pm.controller;

import com.highplace.biz.pm.domain.charge.Bill;
import com.highplace.biz.pm.domain.charge.Subject;
import com.highplace.biz.pm.domain.ui.PageBean;
import com.highplace.biz.pm.service.ChargeService;
import com.highplace.biz.pm.service.util.SecurityUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
public class ChargeController {

    public static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private ChargeService chargeService;

    //////////////////费用科目管理/////////////////////
    @RequestMapping(path = "/charge/subject", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/subject;GET','/charge/subject;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeSubject(PageBean pageBean, Principal principal) throws Exception {
        return chargeService.querySubject(SecurityUtils.getCurrentProductInstId(principal),pageBean, false);
    }

    @RequestMapping(path = "/charge/subject", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/subject;POST','/charge/subject;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Subject creatChargeSubject(@Valid @RequestBody Subject subject,
                                Principal principal) throws Exception {

        //插入记录
        int rows = chargeService.insertSubject(SecurityUtils.getCurrentProductInstId(principal), subject);
        logger.debug("subject insert return num:" + rows);
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
        logger.debug("subjectId delete return num:" + rows);
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
        logger.debug("post subject:" + subject.toString());
        if (rows != 1) {
            throw new Exception("change failed, effected num:" + rows);
        }
        return subject;
    }

    //////////////////账单类型管理/////////////////////
    @RequestMapping(path = "/charge/bill", method = RequestMethod.GET)
    @PreAuthorize("hasAnyAuthority('/charge/bill;GET','/charge/bill;ALL','/charge/**;GET','/charge/**;ALL','ADMIN')")
    public Map<String, Object> getChargeBillType(PageBean pageBean, Principal principal) throws Exception {
        return chargeService.queryBillType(SecurityUtils.getCurrentProductInstId(principal),pageBean, false);
    }

    @RequestMapping(path = "/charge/bill", method = RequestMethod.POST)
    @PreAuthorize("hasAnyAuthority('/charge/bill;POST','/charge/POST;ALL','/charge/**;POST','/charge/**;ALL','ADMIN')")
    public Bill createChargeBillType(@Valid @RequestBody Bill bill, Principal principal) throws Exception {
        //插入记录
        int rows = chargeService.insertBillType(SecurityUtils.getCurrentProductInstId(principal), bill);
        logger.debug("bill insert return num:" + rows);
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
        if (bill.getBillSubjectRelList() == null || bill.getBillSubjectRelList().size()==0) throw new Exception("billSubjectRelList is null");

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
        logger.debug("subjectId delete return num:" + rows);
        if (rows != 1) {
            throw new Exception("delete failed, effected num:" + rows);
        }
    }


}
