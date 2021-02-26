package com.blackstrawai.controller.onboarding;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.blackstrawai.common.BaseController;
import com.blackstrawai.common.BaseResponse;
import com.blackstrawai.common.CommonConstants;
import com.blackstrawai.common.Constants;
import com.blackstrawai.helper.OnBoardingConvertToVoHelper;
import com.blackstrawai.keycontact.dropdowns.UserDropDownVo;
import com.blackstrawai.onboarding.UserService;
import com.blackstrawai.onboarding.user.InviteActionVo;
import com.blackstrawai.onboarding.user.InviteUserVo;
import com.blackstrawai.onboarding.user.ListSentInvitesVo;
import com.blackstrawai.onboarding.user.ManageInvitesVo;
import com.blackstrawai.onboarding.user.UserVo;
import com.blackstrawai.onboarding.user.WithdrawInviteVo;
import com.blackstrawai.request.JSONObject;
import com.blackstrawai.request.onboarding.user.InviteActionRequest;
import com.blackstrawai.request.onboarding.user.InviteUserRequest;
import com.blackstrawai.request.onboarding.user.UserRequest;
import com.blackstrawai.response.onboarding.organization.UserDropDownResponse;
import com.blackstrawai.response.onboarding.user.InviteActionResponse;
import com.blackstrawai.response.onboarding.user.InviteUserResponse;
import com.blackstrawai.response.onboarding.user.ListInvitesResponse;
import com.blackstrawai.response.onboarding.user.ListUserResponse;
import com.blackstrawai.response.onboarding.user.ManageInvitesResponse;
import com.blackstrawai.response.onboarding.user.UserResponse;
import com.blackstrawai.response.onboarding.user.WithdrawInviteResponse;

@RestController
@CrossOrigin
public class UserController extends BaseController {

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	// For Creating User
	@RequestMapping(value = "/v1/users", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> createUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<UserRequest> userRequest) {
		logger.info("Entry into method:createUser");
		BaseResponse response = new UserResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(userRequest));
			UserVo userVo = OnBoardingConvertToVoHelper.getInstance().convertUserVoFromUserRequest(userRequest.getData());
			userVo = userService.createUser(userVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			userVo.setKeyToken(null);
			userVo.setValueToken(null);
			((UserResponse) response).setData(userVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_CREATED,
					Constants.SUCCESS_USER_CREATED, Constants.USER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (ApplicationException e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_CREATED,
						e.getCause().getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_CREATED,
						e.getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Updating User
	@RequestMapping(value = "/v1/users", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> updateUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<UserRequest> userRequest) {
		logger.info("Entry into method:updateUser");
		BaseResponse response = new UserResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(userRequest));
			UserVo userVo = OnBoardingConvertToVoHelper.getInstance().convertUserVoFromUserRequest(userRequest.getData());
			userVo = userService.updateUser(userVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			userVo.setKeyToken(null);
			userVo.setValueToken(null);
			((UserResponse) response).setData(userVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_UPDATED,
					Constants.SUCCESS_USER_UPDATED, Constants.USER_UPDATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_UPDATED, e.getMessage(),
					Constants.USER_UPDATED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Deleting User
	@RequestMapping(value = "/v1/users/{organizationId}/{organizationName}/{userId}/{id}/{roleName}/{status}", method = RequestMethod.DELETE)
	public ResponseEntity<BaseResponse> deleteUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable int id, @PathVariable String roleName, @PathVariable String status) {
		logger.info("Entry into method:deleteUser");
		BaseResponse response = new UserResponse();
		try {
			UserVo userVo = userService.deleteUser(id, status, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			userVo.setKeyToken(null);
			userVo.setValueToken(null);
			((UserResponse) response).setData(userVo);
			if (status.equals(CommonConstants.STATUS_AS_ACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_ACTIVATED,
						Constants.SUCCESS_USER_ACTIVATED, Constants.USER_DELETED_SUCCESSFULLY);
			} else if (status.equals(CommonConstants.STATUS_AS_INACTIVE)) {
				response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_DEACTIVATED,
						Constants.SUCCESS_USER_DEACTIVATED, Constants.USER_DELETED_SUCCESSFULLY);
			}
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_DELETED, e.getMessage(),
					Constants.USER_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// For Fetching all User details belonging to an Organization
	@RequestMapping(value = "/v1/users/organizations/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllUsersOfAnOrganizationUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into method:getAllUsersOfAnOrganization");
		BaseResponse response = new ListUserResponse();
		try {
			List<UserVo> listAllUsers = userService.getAllUsersOfAnOrganizationUserAndRole(organizationId,
					organizationName, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListUserResponse) response).setData(listAllUsers);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_FETCH,
					Constants.SUCCESS_USER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching a particular user
	@RequestMapping(value = "/v1/users/{organizationId}/{organizationName}/{userId}/{roleName}/{id}")
	public ResponseEntity<BaseResponse> getUserDetails(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable String roleName, @PathVariable int id) {
		logger.info("Entry into method:getUserDetails");
		BaseResponse response = new UserResponse();
		try {
			UserVo data = userService.getUserDetails(organizationId, id);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((UserResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_FETCH,
					Constants.SUCCESS_USER_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/dropsdowns/users")
	public ResponseEntity<BaseResponse> getUserDropDown(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.info("Entry into getUserDropDown");
		BaseResponse response = new UserDropDownResponse();
		try {
			UserDropDownVo data = userService.getUserDropDownData();
			setTokens(response, httpServletRequest.getHeader("keyToken"), httpServletRequest.getHeader("valueToken"));
			((UserDropDownResponse) response).setData(data);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_DROP_DOWN_FETCH,
					Constants.SUCCESS_DROP_DOWN_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);

		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_PO_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = "/v1/users/invite", method = RequestMethod.POST)
	public ResponseEntity<BaseResponse> inviteUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@RequestBody JSONObject<InviteUserRequest> userRequest) {
		logger.info("Entry into method: inviteUser");
		BaseResponse response = new InviteUserResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(userRequest));
			InviteUserVo inviteUserVo = OnBoardingConvertToVoHelper.getInstance()
					.convertInviteUserVoFromInviteUserRequest(userRequest.getData());
			inviteUserVo = userService.createUserInvite(inviteUserVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			inviteUserVo.setKeyToken(null);
			inviteUserVo.setValueToken(null);
			((InviteUserResponse) response).setData(inviteUserVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_USER_INVITED,
					Constants.SUCCESS_USER_INVITED, Constants.USER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_INVITED,
						e.getCause().getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_INVITED,
						e.getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Fetching all User Invite details belonging to an Organization
	@RequestMapping(value = "/v1/users/invite/{organizationId}/{organizationName}/{userId}/{roleName}")
	public ResponseEntity<BaseResponse> getAllUserInvitesOfAnOrganizationUserAndRole(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse, @PathVariable int organizationId, @PathVariable String organizationName,
			@PathVariable String userId, @PathVariable String roleName) {
		logger.info("Entry into getAllUserInvitesOfAnOrganizationUserAndRole");
		BaseResponse response = new ListInvitesResponse();
		try {
			List<ListSentInvitesVo> listAllInvites = userService.getAllUserInvitesOfAnOrganizationUserAndRole(organizationId,
					organizationName, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ListInvitesResponse) response).setData(listAllInvites);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_INVITES_FETCH,
					Constants.SUCCESS_INVITES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_INVITES_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// For Withdrawing invite
	@RequestMapping(value = "/v1/users/invite/{organizationId}/{organizationName}/{userId}/{id}/{roleName}", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> withdrawUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
			@PathVariable int organizationId, @PathVariable String organizationName, @PathVariable String userId,
			@PathVariable int id, @PathVariable String roleName) {
		logger.info("Entry into method: withdrawUser");
		BaseResponse response = new WithdrawInviteResponse();
		try {
			WithdrawInviteVo withdrawVo = userService.withdrawInvite(id, userId, roleName);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			withdrawVo.setKeyToken(null);
			withdrawVo.setValueToken(null);
			((WithdrawInviteResponse) response).setData(withdrawVo);

			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_WITHDRAW_INVITE,
					Constants.SUCCESS_WITHDRAW_INVITE, Constants.USER_DELETED_SUCCESSFULLY);

			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_WITHDRAW_INVITE, e.getMessage(),
					Constants.USER_DELETED_UNSUCCESSFULLY);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// User manage invites list
	@RequestMapping(value = "/v1/users/invite/manage/{emailId}")
	public ResponseEntity<BaseResponse> listManageInvitesDetails(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse,@PathVariable String emailId) {
		logger.info("Entry into listManageInvitesDetails");
		BaseResponse response = new ManageInvitesResponse();
		try {
			List<ManageInvitesVo> listAllInvites = userService.getManageInvitesDetails(emailId);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			((ManageInvitesResponse) response).setData(listAllInvites);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_INVITES_FETCH,
					Constants.SUCCESS_INVITES_FETCH, Constants.SUCCESS_DURING_GET);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_INVITES_FETCH, e.getMessage(),
					Constants.FAILURE_DURING_GET);
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	// Accept or Reject Invite
	@RequestMapping(value = "/v1/users/invite/manage", method = RequestMethod.PUT)
	public ResponseEntity<BaseResponse> inviteActionUser(HttpServletRequest httpRequest, HttpServletResponse httpResponse,			
			@RequestBody JSONObject<InviteActionRequest> userRequest) {
		logger.info("Entry into method: inviteActionUser");
		BaseResponse response = new InviteActionResponse();
		try {
			logger.info("Request Payload:" + generateRequestAndResponseLogPaylod(userRequest));
			InviteActionVo inviteActionVo = OnBoardingConvertToVoHelper.getInstance()
					.convertInviteActionVoFromInviteActionRequest(userRequest.getData());
			inviteActionVo = userService.takeInviteAction(inviteActionVo);
			setTokens(response, httpRequest.getHeader("keyToken"), httpRequest.getHeader("valueToken"));
			inviteActionVo.setKeyToken(null);
			inviteActionVo.setValueToken(null);
			((InviteActionResponse) response).setData(inviteActionVo);
			response = constructResponse(response, Constants.SUCCESS, Constants.SUCCESS_ACTION_TAKEN,
					Constants.SUCCESS_ACTION_TAKEN, Constants.USER_CREATED_SUCCESSFULLY);
			logger.info("Response Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			if (e.getCause() != null) {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_INVITED,
						e.getCause().getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			} else {
				response = constructResponse(response, Constants.FAILURE, Constants.FAILURE_USER_INVITED,
						e.getMessage(), Constants.USER_CREATED_UNSUCCESSFULLY);
			}
			logger.error("Error Payload:" + generateRequestAndResponseLogPaylod(response));
			return new ResponseEntity<BaseResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
