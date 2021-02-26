package com.blackstrawai.controller.ap;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackstrawai.ApplicationException;
import com.blackstrawai.ApplicationRuntimeException;
import com.blackstrawai.ap.ExpenseService;
import com.blackstrawai.ap.dropdowns.BasicCustomerVo;
import com.blackstrawai.ap.dropdowns.BasicEmployeeVo;
import com.blackstrawai.ap.dropdowns.BasicVendorVo;
import com.blackstrawai.ap.dropdowns.ExpenseDropdownVo;
import com.blackstrawai.ap.expense.ExpenseFilterVO;
import com.blackstrawai.ap.expense.ExpensesVO;
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.ApConvertToVoHelper;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.ap.expense.ExpenseFilterRequest;
import com.blackstrawai.request.ap.expense.ExpenseRequest;
import com.blackstrawai.response.ap.expense.BasicCustomerResponse;
import com.blackstrawai.response.ap.expense.BasicEmployeeResponse;
import com.blackstrawai.response.ap.expense.BasicVendorResponse;
import com.blackstrawai.response.ap.expense.ExpensesDropDownResponse;
import com.blackstrawai.response.ap.expense.ExpensesResponse;
import com.blackstrawai.response.ap.expense.SingleExpenseResponse;
import com.blackstrawai.response.keycontact.employee.EmployeeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
@RequestMapping("/decifer/ap/expense")
public class ExpenseController extends BaseController {
    private Logger logger = Logger.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

 /*   @RequestMapping(value = "/v1/spendingnaturelist", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllSpendingNature(HttpServletRequest httpRequest,
                                                             HttpServletResponse httpResponse) {
        logger.info("Method to get Nature of spending: getAllSpendingNature");
        BaseResponse response = new NatureOfSpendingExpenseResponse();
        try {
            List<NatureOfSpendingVO> natureOfSpendingVOList = expenseService.getAllNatureOfSpendingList();
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            ((NatureOfSpendingExpenseResponse) response).setData(natureOfSpendingVOList);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSES_NATURE_OF_SPENDING_FETCH,
                    Constants.SUCCESS_EXPENSES_NATURE_OF_SPENDING_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSES_NATURE_OF_SPENDING_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/
    /*@RequestMapping(value = "/v1/expensestauses", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllExpenseStatus(HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse) {
        logger.info("Method to get ExpenseStatus: getAllExpenseStatus");
        BaseResponse response = new ExpenseStatusResponse();
        try {
            List<StatusVO> statusVOList = expenseService.getAllExpenseStatus();
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            ((ExpenseStatusResponse) response).setData(statusVOList);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSES_STATUS_FETCH,
                    Constants.SUCCESS_EXPENSES_STATUS_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSES_STATUS_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/


 /*   @RequestMapping(value = "/v1/acctypes", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllAccountType(HttpServletRequest httpRequest,
                                                          HttpServletResponse httpResponse) {
        logger.info("Method to get ExpenseStatus: getAllExpenseStatus");
        BaseResponse response = new ExpenseAccountTypeResponse();
        try {
            List<AccountTypeVO> accountTypeVOList = expenseService.getAllAccountType();
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            ((ExpenseAccountTypeResponse) response).setData(accountTypeVOList);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSES_ACCOUNT_TYPES_FETCH,
                    Constants.SUCCESS_EXPENSES_ACCOUNT_TYPES_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSES_ACCOUNT_TYPES_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // For Creating Employee
    @RequestMapping(value = "/v1/expense", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> createExpense(HttpServletRequest httpRequest,
                                                      HttpServletResponse httpResponse,
                                                      @RequestBody JSONObject<ExpenseRequest> expenseRequest) {
        logger.info("Entry into method: createExpense");
        BaseResponse response = new ExpensesResponse();
        try {
            logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(expenseRequest));
            ExpensesVO expensesVO = ApConvertToVoHelper.getInstance()
                    .getExpenseVOFromExpenseRequest(expenseRequest.getData());
            logger.info("Request expensesVO:" + expensesVO);
            expenseService.createExpense(expensesVO);
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_CREATED,
                    Constants.SUCCESS_EXPENSE_CREATED, Constants.EXPENSE_CREATED_SUCCESSFULLY);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            if (e.getCause() != null) {
                response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_CREATED,
                        e.getCause().getMessage(), Constants.EXPENSE_CREATED_UNSUCCESSFULLY);
            } else {
                response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_CREATED,
                        e.getMessage(), Constants.EXPENSE_CREATED_UNSUCCESSFULLY);
            }
            logger.error("Error ", e);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void setTokens(BaseResponse response, String keyToken, String valueToken) {
        response.setKeyToken(keyToken);
        response.setValueToken(valueToken);
    }

    public String generateRequestAndResponseLogPaylod(Object o) throws ApplicationRuntimeException {
        try {
            ObjectMapper Obj = new ObjectMapper();
            String jsonStr;
            jsonStr = Obj.writeValueAsString(o);
            return jsonStr;
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            throw new ApplicationRuntimeException(e);
        }
    }


    @RequestMapping(value = "/v1/allexpenses", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllExpensesByOrganization(HttpServletRequest httpRequest,
                                                                     HttpServletResponse httpResponse,
                                                                     @QueryParam("organizationId") Integer organizationId) {
        logger.info("Method to get All the Expenses : getAllExpensesByOrganization");
        BaseResponse response = new ExpensesResponse();
        try {
            List<ExpensesVO> expensesVOList = expenseService.getAllExpenses(organizationId);
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            ((ExpensesResponse) response).setData(expensesVOList);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_FETCH,
                    Constants.SUCCESS_EXPENSE_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/v1/expense", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getExpenseByOrgIdExpenseId(HttpServletRequest httpRequest,
                                                                   HttpServletResponse httpResponse,
                                                                   @QueryParam("organizationId") Integer organizationId,
                                                                   @QueryParam("expenseId") Integer expenseId) {
        logger.info("Method to get All the Expenses : getExpenseByOrgIdExpenseId");
        BaseResponse response = new SingleExpenseResponse();
        if (organizationId != null && expenseId != null) {
            try {

                ExpensesVO expensesVOList = expenseService.getAExpenseByExpenseId(organizationId, expenseId);
                setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
                ((SingleExpenseResponse) response).setData(expensesVOList);
                response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_FETCH,
                        Constants.SUCCESS_EXPENSE_FETCH, Constants.SUCCESS_DURING_GET);
                logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
                return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
            } catch (ApplicationException e) {
                response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_FETCH,
                        e.getMessage(), Constants.FAILURE_DURING_GET);
                logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
                logger.info(response);
                return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<BaseResponse>(response, HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/v1/vendorOrCustomer", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getVendorOrCustomerByOrganizationID(HttpServletRequest httpRequest,
                                                                            HttpServletResponse httpResponse,
                                                                            @QueryParam("organizationId") int organizationId,
                                                                            @QueryParam("isVendor") Boolean isVendor) {
        logger.info("Method to get All the Expenses : getVendorOrCustomerByOrganizationID");
        BaseResponse response = null;
        try {
            if (isVendor) {
                response = new BasicVendorResponse();
                List<BasicVendorVo> basicVendorVoList = expenseService.getVendor(organizationId);
                setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
                ((BasicVendorResponse) response).setData(basicVendorVoList);
            } else {
                response = new BasicEmployeeResponse();
                List<BasicCustomerVo> basicCustomerVoList = expenseService.getCustomer(organizationId);
                setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
                ((BasicCustomerResponse) response).setData(basicCustomerVoList);
            }
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_CUSTOMER_VENDOR_FETCH,
                    Constants.SUCCESS_CUSTOMER_VENDOR_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_CUSTOMER_VENDOR_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/v1/dropsdowns/expenses/{organizationId}")
    public ResponseEntity<BaseResponse> getExpenseDropDown(HttpServletRequest httpServletRequest,
                                                           HttpServletResponse httpServletResponse,
                                                           @PathVariable int organizationId) {
        logger.info("Entry into getExpenseDropDown");
        BaseResponse response = new ExpensesDropDownResponse();
        try {
            ExpenseDropdownVo data = expenseService.getExpenseDropDownData(organizationId);
            setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
            ((ExpensesDropDownResponse) response).setData(data);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
                    Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_DROP_DOWN_FETCH, e.getMessage(),
                    Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/v1/expenses/employers/{organizationId}", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getAllEmployers(HttpServletRequest httpServletRequest,
                                                        HttpServletResponse httpServletResponse,
                                                        @PathVariable int organizationId) {
        logger.info("Entry into getAllEmployers");
        BaseResponse response = new BasicEmployeeResponse();
        try {
            List<BasicEmployeeVo> data = expenseService.getEmployee(organizationId);
            setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
            ((BasicEmployeeResponse) response).setData(data);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_EMPLOYEE_FETCH,
                    Constants.SUCCESS_EXPENSE_EMPLOYEE_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

        } catch (Exception e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_EMPLOYEE_FETCH, e.getMessage(),
                    Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/v1/expense/{organizationId}/{id}/{status}", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> deleteExpense(HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse,
                                                            @PathVariable Integer id,
                                                            @PathVariable String status) {
        logger.info("Entry into method: deleteExpense");
        BaseResponse response = new EmployeeResponse();
        try {
            expenseService.updateExpenseStatus(id, status);
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            
            if(status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_ACTIVATED,
						Constants.SUCCESS_EXPENSE_ACTIVATED, Constants.EXPENSE_DELETED_SUCCESSFULLY);
			}else if(status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_DEACTIVATED,
					Constants.SUCCESS_EXPENSE_DEACTIVATED, Constants.EXPENSE_DELETED_SUCCESSFULLY);			
			}			

           logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));

            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_DELETED,
                    e.getMessage(), Constants.EXPENSE_DELETED_UNSUCCESSFULLY);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/v1/filterexpense/{organizationId}", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> getFilteredExpenses(HttpServletRequest httpRequest,
                                                            HttpServletResponse httpResponse,
                                                            @RequestBody JSONObject<ExpenseFilterRequest> expenseFilterRequest,
                                                            @PathVariable Integer organizationId) {
        logger.info("Method to get All the Expenses : getFilteredExpenses");
        BaseResponse response = new ExpensesResponse();
        try {
            logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(expenseFilterRequest));
            logger.info("Request organizationId:" + organizationId);
            ExpenseFilterVO expenseFilterVO = ApConvertToVoHelper.getInstance()
                    .getExpenseFilterVOFromExpenseFilterRequest(expenseFilterRequest.getData());
            logger.info("Request expenseFilterVO:" + expenseFilterVO);
            List<ExpensesVO> expensesVOList = expenseService.getAllExpensesByFilter(expenseFilterVO,organizationId);
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            ((ExpensesResponse) response).setData(expensesVOList);
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_FETCH,
                    Constants.SUCCESS_EXPENSE_FETCH, Constants.SUCCESS_DURING_GET);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_FETCH,
                    e.getMessage(), Constants.FAILURE_DURING_GET);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            logger.info(response);
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/v1/expense", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> updateExpense(HttpServletRequest httpRequest,
                                                      HttpServletResponse httpResponse,
                                                      @RequestBody JSONObject<ExpenseRequest> expenseRequest) {
        logger.info("Entry into method: createExpense");
        BaseResponse response = new ExpensesResponse();
        try {
            logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(expenseRequest));
            ExpensesVO expensesVO = ApConvertToVoHelper.getInstance()
                    .getExpenseVOFromExpenseRequest(expenseRequest.getData());
            logger.info("Request expensesVO:" + expensesVO);
            expenseService.updateExpense(expensesVO);
            setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
            response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_EXPENSE_UPDATED,
                    Constants.SUCCESS_EXPENSE_UPDATED, Constants.EXPENSE_UPDATED_SUCCESSFULLY);
            logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
        } catch (ApplicationException e) {
            if (e.getCause() != null) {
                response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_UPDATED,
                        e.getCause().getMessage(), Constants.EXPENSE_UPDATED_SUCCESSFULLY);
            } else {
                response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_EXPENSE_UPDATED,
                        e.getMessage(), Constants.EXPENSE_UPDATED_SUCCESSFULLY);
            }
            logger.error("Error ", e);
            logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
            return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
