package com.blackstrawai.ap;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.ap.dropdowns.BasicVendorVo;
import com.blackstrawai.ap.dropdowns.ExpenseDropdownVo;
import com.blackstrawai.ap.expense.ExpenseFilterVO;
import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.attachments.AttachmentService;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.common.BaseService;

@Service
public class ExpenseService extends BaseService {

    @Autowired
    private ExpenseDao expenseDao;

    @Autowired
    private AttachmentService attachmentService;

    

  /*  @Autowired
    private DropDownDao dropDownDao;
*/
    private Logger logger = Logger.getLogger(ExpenseService.class);

 /*   public List<NatureOfSpendingVO> getAllNatureOfSpendingList() throws ApplicationException {
        logger.info("Entry into method: getAllNatureOfSpendingList");
        List<NatureOfSpendingVO> natureOfSpendingVOList = expenseDao.getAllNatureOfSpending();
        Collections.reverse(natureOfSpendingVOList);
        return natureOfSpendingVOList;
    }*/

  /*  public List<StatusVO> getAllExpenseStatus() throws ApplicationException {
        logger.info("Entry into method: getAllExpenseStatus");
        List<StatusVO> statusVOList = expenseDao.getAllExpenseStatus();
        Collections.reverse(statusVOList);
        return statusVOList;
    }*/

    /*public List<AccountTypeVO> getAllAccountType() throws ApplicationException {
        logger.info("Entry into method: getAllAccountType");
        List<AccountTypeVO> accountTypeVOList = expenseDao.getAllAccountType();
        Collections.reverse(accountTypeVOList);
        return accountTypeVOList;
    }
*/
    public Boolean createExpense(ExpensesVO expensesVO) throws ApplicationException {
        logger.info("Entry into expensesVO");
        boolean isTxnSuccess = false;
        try {
            isTxnSuccess = expenseDao.createExpense(expensesVO);
            if (isTxnSuccess && !expensesVO.getAttachments().isEmpty()) {
                logger.info("Entry into upload");
                attachmentService.upload(AttachmentsConstants.MODULE_TYPE_EXPENSES, expensesVO.getId(), expensesVO.getOrganizationId(), expensesVO.getAttachments());
                logger.info("Upload Successfull");
            }
        } catch (ApplicationException e1) {
            throw e1;
        } catch (Exception e) {
            try {
                expenseDao.deleteExpenseEntries(expensesVO.getId(),expensesVO.getUserId(),expensesVO.getRoleName());
            } catch (Exception e1) {
                logger.info("Error in Expense entries delete in service layer ");
                throw new ApplicationException(e);
            }
            throw new ApplicationException(e);
        }
        logger.info("Expense created Successfully in service layer ");
        return isTxnSuccess;

    }

    public Boolean updateExpense(ExpensesVO expensesVO) throws ApplicationException {
        logger.info("Entry into expensesVO");
        boolean isTxnSuccess = false;
        try {
            isTxnSuccess = expenseDao.updateExpense(expensesVO);
            if (isTxnSuccess && !expensesVO.getAttachments().isEmpty()) {
                logger.info("Entry into upload");
                attachmentService.upload(AttachmentsConstants.MODULE_TYPE_EXPENSES, expensesVO.getId(), expensesVO.getOrganizationId(), expensesVO.getAttachments());
                logger.info("Upload Successfull");
            }
        } catch (ApplicationException e1) {
            throw e1;
        } catch (Exception e) {
            try {
                expenseDao.deleteExpenseEntries(expensesVO.getId(),expensesVO.getUserId(),expensesVO.getRoleName());
            } catch (Exception e1) {
                logger.info("Error in Expense entries delete in service layer ");
                throw new ApplicationException(e);
            }
            throw new ApplicationException(e);
        }
        logger.info("Expense created Successfully in service layer ");
        return isTxnSuccess;

    }


    public List<ExpensesVO> getAllExpenses(Integer organizationId) throws ApplicationException {
        logger.info("Entry into method: getAllExpenses");
        List<ExpensesVO> expensesVOList = expenseDao.getAllExpenses(organizationId);
        Collections.reverse(expensesVOList);
        return expensesVOList;
    }

    public List<ExpensesVO> getAllExpensesByFilter(ExpenseFilterVO expenseFilterVO, Integer organizationId) throws ApplicationException {
        logger.info("Entry into method: getAllExpenses");
        List<ExpensesVO> expensesVOList = expenseDao.getAllFilteredExpenses(expenseFilterVO, organizationId);
        Collections.reverse(expensesVOList);
        return expensesVOList;
    }

    public ExpensesVO getAExpenseByExpenseId(Integer organizationId, Integer expenseId) throws ApplicationException {
        logger.info("Entry into method: getAExpenseByExpenseId");
        ExpensesVO expensesVO = expenseDao.getExpenseByExpenseId(organizationId, expenseId);
        if (expensesVO != null && expensesVO.getAttachments().size() > 0 && expensesVO.getId() != null) {
            attachmentService.encodeAllFiles(expensesVO.getOrganizationId(), expensesVO.getId(), AttachmentsConstants.MODULE_TYPE_EXPENSES, expensesVO.getAttachments());
        }
        return expensesVO;
    }

    public List<BasicVendorVo> getVendor(Integer organizationId) throws ApplicationException {
        logger.info("Entry into method: get Vendor");
        List<BasicVendorVo> vendorVos = null;
        try {
            vendorVos = expenseDao.getVendor(organizationId);
        } catch (Exception e) {
            logger.error("Exception while getting Vendor", e);
        }
        Collections.reverse(vendorVos);
        return vendorVos;
    }

    public List<BasicEmployeeVo> getEmployee(Integer organizationId) throws ApplicationException {
        logger.info("Entry into method:  getEmployee");
        List<BasicEmployeeVo> basicEmployeeVoList = null;
        try {
            basicEmployeeVoList = expenseDao.getAllEmployeeByOrgId(organizationId);
        } catch (Exception e) {
            logger.error("Exception while getting getEmployee", e);
        }
        Collections.reverse(basicEmployeeVoList);
        return basicEmployeeVoList;
    }

    public List<BasicCustomerVo> getCustomer(Integer organizationId) throws ApplicationException {
        logger.info("Entry into method:  getCustomer");
        List<BasicCustomerVo> basicCustomerVoList = null;
        try {
            basicCustomerVoList = expenseDao.getAllCustomerByOrgId(organizationId);
        } catch (Exception e) {
            logger.error("Exception while getting getEmployee", e);
        }
        Collections.reverse(basicCustomerVoList);
        return basicCustomerVoList;
    }

    public ExpenseDropdownVo getExpenseDropDownData(int organizationId) throws ApplicationException {
        logger.info("Entry into method: getExpenseDropDownData");
        //return dropDownDao.getExpenseDropDownData(organizationId);
        return null;
    }

    public void updateExpenseStatus(int id, String status) throws ApplicationException {
        logger.info("Entry into method: deleteEmployee");
        try {
            expenseDao.updateExpenseStatus(id, status);
        } catch (Exception e) {
            logger.error("Error while Updating the status ", e);
        }
    }
}
