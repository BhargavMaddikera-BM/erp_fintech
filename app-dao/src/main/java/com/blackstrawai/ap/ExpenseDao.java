package com.blackstrawai.ap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.ap.dropdowns.BasicVendorVo;
import com.blackstrawai.ap.expense.ExpenseFilterVO;
import com.blackstrawai.ap.expense.ExpensesAddedVo;
import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.ap.expense.NatureOfSpendingVO;
import com.blackstrawai.ap.expense.StatusVO;
import com.blackstrawai.attachments.AttachmentsConstants;
import com.blackstrawai.attachments.AttachmentsDao;
import com.blackstrawai.attachments.AttachmentsVo;
import com.blackstrawai.attachments.UploadFileVo;
import com.blackstrawai.common.BaseDao;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.keycontact.CustomerDao;
import com.blackstrawai.keycontact.EmployeeDao;
import com.blackstrawai.keycontact.VendorDao;
import com.blackstrawai.util.DaoUtil;


@Repository
public class ExpenseDao extends BaseDao {


    private Logger logger = Logger.getLogger(ExpenseDao.class);

    @Autowired
    private AttachmentsDao attachmentsDao;
    
    @Autowired
    private VendorDao vendorDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private EmployeeDao employeeDao;

    public List<NatureOfSpendingVO> getAllNatureOfSpending() throws ApplicationException {
        logger.info("Entry into method: getAllNatureOfSpending");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        List<NatureOfSpendingVO> natureOfSpendingVOList = new ArrayList<NatureOfSpendingVO>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_ALL_NATURE_OF_SPENDING;
            preparedStatement = con.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                NatureOfSpendingVO natureOfSpendingVO = new NatureOfSpendingVO();
                natureOfSpendingVO.setId(rs.getInt(1));
                natureOfSpendingVO.setSpendingDesc(rs.getString(2));
                natureOfSpendingVOList.add(natureOfSpendingVO);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return natureOfSpendingVOList;
    }


    public List<ExpensesVO> getAllExpenses(Integer organizationId) throws ApplicationException {
        logger.info("Entry into method: getAllExpenses");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        List<ExpensesVO> expensesVOList = new ArrayList<ExpensesVO>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_ALL_EXPENSES_BY_ORGANIZATION_ID;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, organizationId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ExpensesVO expensesVO = new ExpensesVO();
                //   e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status
                expensesVO.setId(rs.getInt(1));
                expensesVO.setExpenseTimeStamp(DaoUtil.getStringFromTimeStamp(rs.getTimestamp(2)));
                expensesVO.setNatureOfSpendingId(rs.getInt(3));
                expensesVO.setNatureOfSpendingDesc(rs.getString(4));
                expensesVO.setStatusId(rs.getInt(5));

                expensesVO.setExpenseStatusName(rs.getString(6));
                expensesVO.setCash(rs.getBoolean(7));
                expensesVO.setOrganizationId(rs.getInt(8));
                expensesVO.setEmployeeId(rs.getInt(9));

                expensesVO.setUserId(rs.getString(10));
                expensesVO.setSuperAdmin(rs.getBoolean(11));
                expensesVO.setStatus(rs.getString(12));
                expensesVO.setAmount(rs.getString(13));
                expensesVOList.add(expensesVO);
            }
        } catch (Exception e) {
            logger.error("Exception while Fetching Expenses", e);
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return expensesVOList;
    }


    public List<ExpensesVO> getAllFilteredExpenses(ExpenseFilterVO expenseFilterVO, Integer organizationId) throws ApplicationException {
        logger.info("Entry into method: getAllFilteredExpenses");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        List<ExpensesVO> expensesVOList = new ArrayList<ExpensesVO>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_ALL_FILTERED_EXPENSE_BASE_QUERY;
            int i = 1;
            if (expenseFilterVO != null) {
                if (expenseFilterVO.getAttachment()) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_ATTACHMENT_QUERY;
                } else {
                    query = query + ExpensesConstants.LEFT + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_ATTACHMENT_QUERY;
                }
                query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_WHERE_QUERY;
                if (!expenseFilterVO.getAttachment()) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_ATTACHMENT_WHERE_QUERY;
                }
                if (expenseFilterVO.getStatusId() != null) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_WHERE_STATUS_QUERY;
                }
                if (expenseFilterVO.getNatureOfSpendingId() != null) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_WHERE_NATURE_QUERY;
                }
                if (expenseFilterVO.getStartDateRange() != null && expenseFilterVO.getEndDateRange() != null) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_WHERE_DATE_QUERY;
                }

            }

            query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_GROUP_BY;
            if (expenseFilterVO != null) {
                if (expenseFilterVO.getMinAmountRange() != null && expenseFilterVO.getMaxAmountRange() != null) {
                    query = query + ExpensesConstants.GET_ALL_FILTERED_EXPENSE_HAVING_QUERY;
                }

            }
            logger.info("FinalQuery  : {}" + query);
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, organizationId);

            if (expenseFilterVO != null) {
                if (expenseFilterVO.getStatusId() != null) {
                    preparedStatement.setInt(++i, expenseFilterVO.getStatusId());
                }
                if (expenseFilterVO.getNatureOfSpendingId() != null) {
                    preparedStatement.setInt(++i, expenseFilterVO.getNatureOfSpendingId());
                }
                if (expenseFilterVO.getStartDateRange() != null && expenseFilterVO.getEndDateRange() != null) {
                    preparedStatement.setTimestamp(++i, expenseFilterVO.getStartDateRange());
                    preparedStatement.setTimestamp(++i, expenseFilterVO.getEndDateRange());
                }
                if (expenseFilterVO.getMinAmountRange() != null && expenseFilterVO.getMaxAmountRange() != null) {
                    preparedStatement.setString(++i, expenseFilterVO.getMinAmountRange());
                    preparedStatement.setString(++i, expenseFilterVO.getMaxAmountRange());
                }

            }

            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ExpensesVO expensesVO = new ExpensesVO();
                //   e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status
                expensesVO.setId(rs.getInt(1));
                expensesVO.setExpenseTimeStamp(DaoUtil.getStringFromTimeStamp(rs.getTimestamp(2)));
                expensesVO.setNatureOfSpendingId(rs.getInt(3));
                expensesVO.setNatureOfSpendingDesc(rs.getString(4));
                expensesVO.setStatusId(rs.getInt(5));

                expensesVO.setExpenseStatusName(rs.getString(6));
                expensesVO.setCash(rs.getBoolean(7));
                expensesVO.setOrganizationId(rs.getInt(8));
                expensesVO.setEmployeeId(rs.getInt(9));

                expensesVO.setUserId(rs.getString(10));
                expensesVO.setSuperAdmin(rs.getBoolean(11));
                expensesVO.setStatus(rs.getString(12));
                expensesVO.setAmount(rs.getString(13));
                expensesVOList.add(expensesVO);
            }
        } catch (Exception e) {
            logger.error("Exception while Fetching Expenses", e);
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return expensesVOList;
    }


    public ExpensesVO getExpenseByExpenseId(Integer organizationId, Integer expenseId) throws ApplicationException {
        logger.info("Entry into method: getAllNatureOfSpending");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        ResultSet addedExpensesRs = null;
        Connection con = null;
        ExpensesVO expensesVO = null;
        List<ExpensesAddedVo> expensesAddedVoList = new ArrayList<>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_EXPENSE_BY_EXPENSE_ID;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, organizationId);
            preparedStatement.setInt(2, expenseId);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                expensesVO = new ExpensesVO();
                //   e.id, e.create_ts, e.nature_of_spending_id,nos.spending_desc ,e.expense_status_id,es.status_desc ,e.is_cash, e.organization_id,e.employee_id, e.user_id,e.isSuperAdmin,e.status
                expensesVO.setId(rs.getInt(1));
                expensesVO.setExpenseTimeStamp(DaoUtil.getStringFromTimeStamp(rs.getTimestamp(2)));
                expensesVO.setNatureOfSpendingId(rs.getInt(3));
                expensesVO.setNatureOfSpendingDesc(rs.getString(4));
                expensesVO.setStatusId(rs.getInt(5));

                expensesVO.setExpenseStatusName(rs.getString(6));
                expensesVO.setCash(rs.getBoolean(7));
                expensesVO.setOrganizationId(rs.getInt(8));
                expensesVO.setEmployeeId(rs.getInt(9));

                expensesVO.setUserId(rs.getString(10));
                expensesVO.setSuperAdmin(rs.getBoolean(11));
                expensesVO.setStatus(rs.getString(12));
            }


            if (expensesVO != null && expensesVO.getId() != null) {
                query = ExpensesConstants.GET_ADDED_EXPENSE_BY_EXPENSE_ID;
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setInt(1, expensesVO.getId());
                addedExpensesRs = preparedStatement.executeQuery();
                while (addedExpensesRs.next()) {
                    ExpensesAddedVo expensesAddedVo = new ExpensesAddedVo();
                    // id, expenses_account_id,expenses_id,notes, amount,vendor_id,customer_id,status
                    expensesAddedVo.setId(addedExpensesRs.getInt(1));
                    expensesAddedVo.setExpensesAccountId(addedExpensesRs.getInt(2));
                    expensesAddedVo.setExpensesId(addedExpensesRs.getInt(3));
                    expensesAddedVo.setNotes(addedExpensesRs.getString(4));
                    expensesAddedVo.setAmount(addedExpensesRs.getString(5));
                    expensesAddedVo.setVendorId(addedExpensesRs.getInt(6));
                    expensesAddedVo.setCustomerId(addedExpensesRs.getInt(7));
                    expensesAddedVo.setStatus(addedExpensesRs.getString(8));
                    expensesAddedVo.setExpenseAccountName(addedExpensesRs.getString(9));
                    expensesAddedVo.setExpenseAccountLevel(addedExpensesRs.getString(10));
                    expensesAddedVo.setCustomerName(addedExpensesRs.getString(11));
                    expensesAddedVo.setVendorName(addedExpensesRs.getString(12));
                    if (expensesAddedVo.getVendorId() < 0) {
                        expensesAddedVo.setVendorIdOrCustomerId(expensesAddedVo.getCustomerId());
                        expensesAddedVo.setVendorId(null);
                        expensesAddedVo.setCustomer(true);
                    } else {
                        expensesAddedVo.setCustomer(false);
                        expensesAddedVo.setCustomerId(null);
                        expensesAddedVo.setVendorIdOrCustomerId(expensesAddedVo.getVendorId());
                    }
                    expensesAddedVoList.add(expensesAddedVo);
                }
                expensesVO.setExpensesAddedVoList(expensesAddedVoList);
                List<UploadFileVo> uploadFileVos = new ArrayList<UploadFileVo>();
                for (AttachmentsVo attachments : attachmentsDao.getAttachmentDetails(expensesVO.getId(), AttachmentsConstants.MODULE_TYPE_EXPENSES)) {
                    UploadFileVo uploadFileVo = new UploadFileVo();
                    uploadFileVo.setId(attachments.getId());
                    uploadFileVo.setName(attachments.getFileName());
                    uploadFileVo.setSize(attachments.getSize());
                    uploadFileVos.add(uploadFileVo);
                }
                expensesVO.setAttachments(uploadFileVos);
            }

        } catch (Exception e) {
            logger.error("Exception while Fetching Expenses", e);
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return expensesVO;
    }


    public List<StatusVO> getAllExpenseStatus() throws ApplicationException {
        logger.info("Entry into method: getAllNatureOfSpending");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        List<StatusVO> statusVOList = new ArrayList<StatusVO>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_ALL_EXPENSE_STATUS;
            preparedStatement = con.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                StatusVO statusVO = new StatusVO();
                statusVO.setId(rs.getInt(1));
                statusVO.setStatus(rs.getString(2));

                statusVOList.add(statusVO);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return statusVOList;
    }


    /*public List<AccountTypeVO> getAllAccountType() throws ApplicationException {
        logger.info("Entry into method: getAllNatureOfSpending");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        List<AccountTypeVO> accountTypeVOList = new ArrayList<AccountTypeVO>();
        try {
            con = getAccountsPayable();
            String query = ExpensesConstants.GET_ALL_ACCOUNT_TYPE;
            preparedStatement = con.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                AccountTypeVO accountTypeVO = new AccountTypeVO();
                accountTypeVO.setId(rs.getInt(1));
                accountTypeVO.setAccountType(rs.getString(2));

                accountTypeVOList.add(accountTypeVO);
            }
        } catch (Exception e) {
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return accountTypeVOList;
    }
*/

    public Boolean createExpense(ExpensesVO expensesVO) throws ApplicationException {
        logger.info("Entry into method: CreateExpense");
        boolean isCreatedSuccessfully = false;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getAccountsPayable();
            con.setAutoCommit(false);
            if (expensesVO != null) {
                expensesVO = createExpense(expensesVO, con);
                if (!expensesVO.getAttachments().isEmpty()) {
                    attachmentsDao.createAttachments(expensesVO.getOrganizationId(),expensesVO.getUserId(),expensesVO.getAttachments(), AttachmentsConstants.MODULE_TYPE_EXPENSES, expensesVO.getId(),expensesVO.getRoleName());
                }
            }
            con.commit();
            isCreatedSuccessfully = true;
        } catch (ApplicationException e1) {
            throw e1;
        } catch (Exception e) {
            logger.info("Error in createExpense:: ", e);
            try {
                con.rollback();
            } catch (Exception e1) {
                throw new ApplicationException(e1);
            }
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return isCreatedSuccessfully;
    }

    private ExpensesVO createExpense(ExpensesVO expensesVO, Connection con) throws ApplicationException {

        logger.info("Entry into method: createExpense");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = ExpensesConstants.INSERT_EXPENSES;
            preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, DaoUtil.getTimestampFromString(expensesVO.getExpenseTimeStamp()));
            preparedStatement.setInt(2, expensesVO.getNatureOfSpendingId());
            preparedStatement.setInt(3, expensesVO.getStatusId());
            preparedStatement.setBoolean(4, expensesVO.isCash());
            preparedStatement.setString(5, expensesVO.getUserId());
            preparedStatement.setInt(6, expensesVO.getOrganizationId());
            preparedStatement.setInt(7, expensesVO.getEmployeeId());
            preparedStatement.setBoolean(8, expensesVO.getSuperAdmin());
            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 1) {
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    expensesVO.setId(rs.getInt(1));
                }
            }
            expensesVO.setExpensesAddedVoList(createExpenseAddedList(expensesVO.getId(), expensesVO.getExpensesAddedVoList(), con));
        } catch (Exception e) {
            logger.error("Error While creating Expense ", e);
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }
        return expensesVO;
    }

    private List<ExpensesAddedVo> createExpenseAddedList(Integer expensesVOId, List<ExpensesAddedVo> expensesAddedVoList, Connection con) throws ApplicationException {

        logger.info("Entry into method: createExpense");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<ExpensesAddedVo> expensesAddedVos = new ArrayList<ExpensesAddedVo>();
        try {
            for (ExpensesAddedVo expensesAddedVo : expensesAddedVoList) {
                expensesAddedVos.add(createExpenseAdded(expensesVOId, expensesAddedVo, con));
            }

        } catch (Exception e) {
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }
        return expensesAddedVos;
    }

    private ExpensesAddedVo createExpenseAdded(Integer expensesVOId, ExpensesAddedVo expensesAddedVo, Connection con) throws ApplicationException {
        logger.info("Entry into method: createExpense");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = ExpensesConstants.INSERT_ADDED_EXPENSES;
            preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, expensesAddedVo.getExpensesAccountId());
            preparedStatement.setInt(2, expensesVOId);
            preparedStatement.setString(3, expensesAddedVo.getNotes());
            preparedStatement.setString(4, expensesAddedVo.getAmount());
            preparedStatement.setInt(5, expensesAddedVo.getCustomer() ? -1 : expensesAddedVo.getVendorIdOrCustomerId());
            preparedStatement.setInt(6, expensesAddedVo.getCustomer() ? expensesAddedVo.getVendorIdOrCustomerId() : -1);
            preparedStatement.setTimestamp(7, new Timestamp(new Date().getTime()));
            preparedStatement.setTimestamp(8, new Timestamp(new Date().getTime()));
            preparedStatement.setString(9, DaoUtil.getStatus(expensesAddedVo.getStatus()));
            preparedStatement.setString(10, expensesAddedVo.getExpenseAccountLevel());
            preparedStatement.setString(11, expensesAddedVo.getExpenseAccountName());

            int rowAffected = preparedStatement.executeUpdate();
            if (rowAffected == 1) {
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    expensesAddedVo.setId(rs.getInt(1));
                }
            }

        } catch (Exception e) {
            logger.error("Error while creating addedExpense ", e);
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }
        return expensesAddedVo;
    }


    public void deleteExpenseEntries(Integer id,String userId,String roleName) throws ApplicationException {
        logger.info("To deleteEmployeeEntries:: ");
        Connection connection = null;
        if (id != null) {
            try {
                connection = getAccountsPayable();
                changeStatusOfExpenseTable(id, CommonConstants.STATUS_AS_DELETE, connection, ExpensesConstants.MODIFY_EXPENSE_STATUS);
                attachmentsDao.changeStatusForAttachments(id, CommonConstants.STATUS_AS_DELETE, AttachmentsConstants.MODULE_TYPE_EXPENSES,userId,roleName);
                logger.info("Expense Deleted successfully ");
            } catch (Exception e) {
                logger.info("Error while  delete Expense Entries:: ", e);
                throw new ApplicationException(e);
            } finally {
            	closeResources(null, null, connection);
            }
        }

    }

    public void updateExpenseStatus(Integer id, String status) throws ApplicationException{
        logger.info("To deleteEmployeeEntries:: ");
        Connection connection = null;
        if (id != null) {
            try {
                connection = getAccountsPayable();
                changeStatusOfExpenseTable(id, status, connection, ExpensesConstants.MODIFY_EXPENSE_STATUS);
                changeStatusOfExpenseTable(id, status, connection, ExpensesConstants.MODIFY_EXPENSE_ADDED_STATUS);
             //   attachmentsDao.changeStatusForAttachments(id, status, AttachmentsConstants.MODULE_TYPE_EXPENSES);
                logger.info("Expense Deleted successfully ");
            } catch (Exception e) {
                logger.info("Error while  delete Expense Entries:: ", e);
                throw new ApplicationException(e);
            } finally {
            	closeResources(null, null, connection);
            }
        }

    }

    private void changeStatusOfExpenseTable(Integer id, String status, Connection con, String query) throws ApplicationException {
        logger.info("To Change the status with query :: " + query);
        Connection connection = null;
        if (query != null) {
            try (final PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setString(1, status);
                preparedStatement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setInt(3, id);
                preparedStatement.executeUpdate();
                logger.info("Status chaneged successfully ");
            } catch (Exception e) {
                logger.info("Error in changeStatusOfExpenseTable ", e);
                throw new ApplicationException(e);
            } finally {
            	closeResources(null, null, connection);
            }
        }
    }


    public List<BasicVendorVo> getVendor(Integer organizationID) throws ApplicationException {
        logger.info("To deleteEmployeeEntries:: ");
        Connection connection = null;
        List<BasicVendorVo> basicVendorVoList = new ArrayList<>();

        try {
            if (organizationID != null) {
                connection = getAccountsPayable();
                basicVendorVoList = vendorDao.getBasicVendor(organizationID, connection);
            }
        } catch (Exception e) {
            logger.info("Error in getVendor ", e);
            throw new ApplicationException(e);
        } finally {
        	closeResources(null, null, connection);
        }
        return basicVendorVoList;
    }


    public List<BasicEmployeeVo> getAllEmployeeByOrgId(Integer organizationID) throws ApplicationException {
        logger.info("To getAllEmployeeByOrgId:: ");
        Connection connection = null;
        List<BasicEmployeeVo> basicEmployeeVoList = new ArrayList<>();
        try {
            if (organizationID != null) {
                connection = getAccountsPayable();
                basicEmployeeVoList = employeeDao.getBasicEmployees(organizationID, connection);
            }
        } catch (Exception e) {
            logger.info("Error in get Employee ", e);
            throw new ApplicationException(e);
        } finally {
        	closeResources(null, null, connection);
        }
        return basicEmployeeVoList;
    }

    public List<BasicCustomerVo> getAllCustomerByOrgId(Integer organizationID) throws ApplicationException{
        logger.info("To getAllEmployeeByOrgId:: ");
        Connection connection = null;
        List<BasicCustomerVo> basicCustomerVoList = new ArrayList<>();
        try {
            if (organizationID != null) {
                connection = getAccountsReceivableConnection();
                basicCustomerVoList = customerDao.getBasicCustomer(organizationID, connection);
            }
        } catch (Exception e) {
            logger.info("Error in get Customer ", e);
            throw new ApplicationException(e);
        } finally {
        	closeResources(null, null, connection);
        }
        return basicCustomerVoList;
    }

    public Boolean updateExpense(ExpensesVO expensesVO) throws ApplicationException {
        logger.info("Entry into method: updateExpense");
        boolean isUpdateSuccessfully = false;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        Connection con = null;
        try {
            con = getAccountsPayable();
            con.setAutoCommit(false);
            if (expensesVO != null) {
                updateExpenseDetails(expensesVO, con);
                if (expensesVO.getAttachments() != null && expensesVO.getAttachments().size() > 0) {
                    attachmentsDao.createAttachments(expensesVO.getOrganizationId(),expensesVO.getUserId(),expensesVO.getAttachments(), AttachmentsConstants.MODULE_TYPE_EXPENSES, expensesVO.getId(),expensesVO.getRoleName());
                }
                if (expensesVO.getAttachmentsToRemove() != null && expensesVO.getAttachmentsToRemove().size() > 0) {
                    for (Integer attachmentId : expensesVO.getAttachmentsToRemove()) {
                        attachmentsDao.changeStatusForSingleAttachment(attachmentId, CommonConstants.STATUS_AS_DELETE,expensesVO.getUserId(),expensesVO.getRoleName());
                    }
                }
            }
            con.commit();
            isUpdateSuccessfully = true;
        } catch (ApplicationException e1) {
            throw e1;
        } catch (Exception e) {
            logger.info("Error in updateExpense:: ", e);
            try {
                con.rollback();
            } catch (Exception e1) {
                throw new ApplicationException(e1);
            }
            throw new ApplicationException(e);
        } finally {
            closeResources(rs, preparedStatement, con);
        }
        return isUpdateSuccessfully;
    }

    private void updateExpenseDetails(ExpensesVO expensesVO, Connection con) throws ApplicationException {
        logger.info("Entry into method: updateExpenseDetails");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            String sql = ExpensesConstants.UPDATE_EXPENSES;
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setTimestamp(1, DaoUtil.getTimestampFromString(expensesVO.getExpenseTimeStamp()));
            preparedStatement.setInt(2, expensesVO.getNatureOfSpendingId());
            preparedStatement.setInt(3, expensesVO.getStatusId());
            preparedStatement.setBoolean(4, expensesVO.isCash());
            preparedStatement.setString(5, expensesVO.getUserId());
            preparedStatement.setInt(6, expensesVO.getOrganizationId());
            preparedStatement.setInt(7, expensesVO.getEmployeeId());
            preparedStatement.setBoolean(8, expensesVO.getSuperAdmin());
            preparedStatement.setTimestamp(9, new Timestamp(new Date().getTime()));
            preparedStatement.setString(10, DaoUtil.getStatus(expensesVO.getStatus()));
            preparedStatement.setInt(11, expensesVO.getId());
            preparedStatement.executeUpdate();
            updateExpenseAddedList(expensesVO.getId(), expensesVO.getExpensesAddedVoList(), con);
        } catch (Exception e) {
            logger.error("Error While creating Expense ", e);
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }
    }

    private void updateExpenseAddedList(Integer expensesVOId, List<ExpensesAddedVo> expensesAddedVoList, Connection con) throws ApplicationException {

        logger.info("Entry into method: createExpense");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            for (ExpensesAddedVo expensesAddedVo : expensesAddedVoList) {
                updateExpenseAdded(expensesVOId, expensesAddedVo, con);
            }

        } catch (Exception e) {
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }

    }

    private void updateExpenseAdded(Integer expensesVOId, ExpensesAddedVo expensesAddedVo, Connection con) throws ApplicationException {

        logger.info("Entry into method: createExpense");
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            if (expensesAddedVo.getId() != null) {
                String sql = ExpensesConstants.UPDATE_ADDED_EXPENSES;
                // expenses_account_id = ? , expenses_id = ? , notes = ? , amount = ? , vendor_id = ? , customer_id = ? , create_ts = ? , update_ts = ? ,  status  = ? , expenses_account_level = ? , expenses_account_name = ?
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, expensesAddedVo.getExpensesAccountId());
                preparedStatement.setInt(2, expensesVOId);
                preparedStatement.setString(3, expensesAddedVo.getNotes());
                preparedStatement.setString(4, expensesAddedVo.getAmount());
                preparedStatement.setInt(5, expensesAddedVo.getCustomer() ? -1 : expensesAddedVo.getVendorIdOrCustomerId());
                preparedStatement.setInt(6, expensesAddedVo.getCustomer() ? expensesAddedVo.getVendorIdOrCustomerId() : -1);
                preparedStatement.setTimestamp(7, new Timestamp(new Date().getTime()));
                preparedStatement.setTimestamp(8, new Timestamp(new Date().getTime()));
                preparedStatement.setString(9, DaoUtil.getStatus(expensesAddedVo.getStatus()));
                preparedStatement.setString(10, expensesAddedVo.getExpenseAccountLevel());
                preparedStatement.setString(11, expensesAddedVo.getExpenseAccountName());
                preparedStatement.setInt(12, expensesAddedVo.getId());
                preparedStatement.setInt(13, expensesAddedVo.getExpensesId());


                preparedStatement.executeUpdate();

            } else {
                createExpenseAdded(expensesVOId, expensesAddedVo, con);
            }

        } catch (Exception e) {
            logger.error("Error while creating addedExpense ", e);
            throw new ApplicationException(e);

        } finally {
            closeResources(rs, preparedStatement, null);
        }

    }
}
