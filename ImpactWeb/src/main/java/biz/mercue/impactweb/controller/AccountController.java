package biz.mercue.impactweb.controller;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AccountService;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.ListResponseBody;
import biz.mercue.impactweb.util.TuyaService;





@Controller
public class AccountController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	// 使用者清單
	@RequestMapping(value="/api/getaccountlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAccountList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		
		if(token !=null) {
			
			ListQueryForm accountForm = accountService.getAccountList(page);
				
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(accountForm);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		
		return responseBody.getJacksonString(View.Account.class);
	}
	
	@RequestMapping(value="/api/addaccount", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addAccount(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Account account = (Account) JacksonJSONUtils.readValue(receiveJSONString, Account.class);
			
			taskResult = accountService.createAccount(account);
			if(taskResult == Constants.INT_SUCCESS) {
				responseBody.setCode(Constants.INT_SUCCESS);
				
			}else if(taskResult == Constants.INT_USER_DUPLICATE) {
				responseBody.setCode(Constants.INT_USER_DUPLICATE);
			}else {
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
	
		return responseBody.getJacksonString(View.Account.class);
	}
		
	
	@RequestMapping(value="/api/getaccountinfo", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAccountInfo(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		if(tokenBean !=null) {
			log.info("receiveJSONString:"+receiveJSONString);
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String accountId = reqJSON.optString("account_id");
			
			Account account = accountService.getById(accountId);
			if(account != null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(account);
			} else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.Account.class);
	}
	
	@RequestMapping(value="/api/updateaccount", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAccount(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Account account = (Account) JacksonJSONUtils.readValue(receiveJSONString, Account.class);
				
			taskResult = accountService.updateAccount(account);
			
			if(taskResult == Constants.INT_SUCCESS) {
				responseBody.setCode(Constants.INT_SUCCESS);
			}else if(taskResult == Constants.INT_CANNOT_FIND_DATA){
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}else {
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Account.class);
	}
	
	
	@RequestMapping(value="/api/updateaccountpwd", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAccountPwd(HttpServletRequest request,@RequestBody String receiveJSONString) {
		
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Account account = (Account) JacksonJSONUtils.readValue(receiveJSONString, Account.class);
				
			
			if (account != null && account.getAccount_password().equals(account.getRe_account_password())) {
				
				
				Account account2 = accountService.getById( account.getAccount_id());
				if (account2 != null) {
					
					TuyaService service = TuyaService.getInstance();
					account2.setAccount_password(account.getAccount_password());
					service.rigisterUser(account2);
					taskResult = accountService.updateAccount(account);

					if (taskResult == Constants.INT_SUCCESS) {
						responseBody.setCode(Constants.INT_SUCCESS);
					} else if (taskResult == Constants.INT_CANNOT_FIND_DATA) {
						responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
					} else {
						responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
					}
				}else {
					
				}
			}
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Account.class);
	}
	
	
	
	
}
