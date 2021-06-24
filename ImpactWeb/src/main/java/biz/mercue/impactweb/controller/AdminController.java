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

import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminService;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.ListResponseBody;
import biz.mercue.impactweb.util.MapResponseBody;
import biz.mercue.impactweb.util.StringUtils;





@Controller
public class AdminController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	@RequestMapping(value="/api/adminlogin", method = {RequestMethod.POST},consumes = Constants.CONTENT_TYPE_JSON, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String login(HttpServletRequest request,@RequestBody String receiveJSONString) {
		MapResponseBody response = new MapResponseBody();
		log.info("receiveJSONString:"+receiveJSONString);
		JSONObject reqJSON = new JSONObject(receiveJSONString);
		String email = reqJSON.optString("admin_email");
		String password = reqJSON.optString("admin_password");
		
		log.info("email:"+email);
		log.info("password:"+password);
		
		int handleResult = adminService.login(email, password);

		switch (handleResult) {
		case Constants.INT_SUCCESS:

			Admin adminBean = adminService.getByEmail(email);
			AdminToken tokenBean = adminTokenService.generateToken(adminBean.getAdmin_id());

			log.info("tokenBean:" + JacksonJSONUtils.mapObjectWithView(tokenBean, View.Token.class));
			log.info("token :" + tokenBean.getAdmin_token_id());
			log.info("admin :" + tokenBean.getAdmin().getAdmin_id());

			response.setCode(Constants.INT_SUCCESS);
			response.setData(Constants.JSON_TOKEN, tokenBean.getAdmin_token_id());
			break;

		case Constants.INT_CANNOT_FIND_DATA:
			response.setCode(Constants.INT_CANNOT_FIND_DATA);
			break;

		case Constants.INT_NO_PERMISSION:
			response.setCode(Constants.INT_NO_PERMISSION);
			break;
			
		case Constants.INT_ACCOUNT_UNAVAILABLE:
			response.setCode(Constants.INT_ACCOUNT_UNAVAILABLE);
			break;
			
		case Constants.INT_PASSWORD_ERROR:
			response.setCode(Constants.INT_PASSWORD_ERROR);
			break;

		case Constants.INT_SYSTEM_PROBLEM:
			response.setCode(Constants.INT_SYSTEM_PROBLEM);
			break;
		default:
			response.setCode(Constants.INT_SYSTEM_PROBLEM);
			break;
		}
		return response.getJacksonString(View.Public.class);

	}
	
	
	@RequestMapping(value="/api/adminlogout", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String logout(HttpServletRequest request) {
		MapResponseBody response = new MapResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		if(tokenBean != null && tokenBean.getAdmin() != null){
			String adminId = tokenBean.getAdmin().getAdmin_id();
			adminTokenService.logout(adminId);						
			response.setCode(Constants.INT_SUCCESS);
		}else {
			response.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return response.getJacksonString(View.Public.class);
	}
	
	// 管理者清單
	@RequestMapping(value="/api/getadminlist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		
		if(token !=null) {
			
			ListQueryForm adminForm = adminService.getAdminList(page);
				
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(adminForm);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		
		return responseBody.getJacksonString(View.Admin.class);
	}
	@RequestMapping(value="/api/addadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			
			taskResult = adminService.createAdmin(admin);
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
	
		return responseBody.getJacksonString(View.Admin.class);
	}
		
	
	@RequestMapping(value="/api/getadmininfo", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAdminInfo(HttpServletRequest request) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		if(tokenBean !=null) {
			
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setBean(tokenBean.getAdmin());
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		
		return responseBody.getJacksonString(View.Admin.class);
	}
	
	@RequestMapping(value="/api/updateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			
			taskResult = adminService.updateAdmin(admin);
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
		return responseBody.getJacksonString(View.Admin.class);
	}
	
	
//	@RequestMapping(value="/api/forgetpassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String forgetPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
//		BeanResponseBody responseBody = new BeanResponseBody();
//
//		Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
//
//		if(admin != null) {
//			int taskResult = adminService.forgetPassword(admin);
//			responseBody.setCode(taskResult);
//			
//		}else {
//			responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
//			
//		}
//	
//		return responseBody.getJacksonString(View.Public.class);
//	}

	@RequestMapping(value = "/api/updatepassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePassword(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (token != null) {
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String adminId = reqJSON.optString("admin_id");
			String password = reqJSON.optString("password");
			String repassword = reqJSON.optString("repassword");
			if (!StringUtils.isNULL(password) && password.equals(repassword)) {
				int taskResult = adminService.updatePassword(adminId, password);
				responseBody.setCode(taskResult);
			} else {
				responseBody.setCode(Constants.INT_PASSWORD_ERROR);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Public.class);
	}

	@RequestMapping(value="/api/modifypassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String modifyPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
	
		
		if(token !=null) {
	
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			if(admin!=null) {
				if(!StringUtils.isNULL(admin.getAdmin_password()) && admin.getAdmin_password().equals(admin.getRe_admin_password())) {

					int taskResult = adminService.updatePassword(admin.getAdmin_id(), admin.getAdmin_password());
					if(taskResult == Constants.INT_SUCCESS) {
						responseBody.setCode(Constants.INT_SUCCESS);
					}else if(taskResult == Constants.INT_CANNOT_FIND_DATA){
						responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
					}else {
						responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
					}
				}else {
					responseBody.setCode(Constants.INT_PASSWORD_ERROR);
				}
			}else {
				responseBody.setCode(Constants.INT_DATA_ERROR);
			}
			
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Public.class);
	}
	
//	@RequestMapping(value="/api/resetpassword", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String resetPassword(HttpServletRequest request,@RequestBody String receiveJSONString) {
//		BeanResponseBody responseBody = new BeanResponseBody();
//
//		JSONObject reqJSON = new JSONObject(receiveJSONString);
//		String token = reqJSON.optString("token");
//		String password = reqJSON.optString("password");
//		String repassword = reqJSON.optString("repassword");
//
//		if (!StringUtils.isNULL(password) && password.equals(repassword)) {
//			Admin admin = new Admin();
//			admin.setToken(token);
//			admin.setAdmin_password(password);
//			int taskResult = adminService.resetPassword(admin);
//			if (taskResult == Constants.INT_SUCCESS) {
//				responseBody.setCode(Constants.INT_SUCCESS);
//
//			} else if (taskResult == Constants.INT_CANNOT_FIND_DATA) {
//				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
//			} else {
//				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
//			}
//		} else {
//			responseBody.setCode(Constants.INT_PASSWORD_ERROR);
//		}
//
//		return responseBody.getJacksonString(View.Public.class);
//	}
	

	@RequestMapping(value="/api/invalidateadmin", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String invalidateAdmin(HttpServletRequest request,@RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		int taskResult = -1;
		if(token !=null) {
			
			Admin admin = (Admin) JacksonJSONUtils.readValue(receiveJSONString, Admin.class);
			admin.setAvailable(false);
			
			taskResult = adminService.updateAdmin(admin);
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
		
		return responseBody.getJacksonString( View.Public.class);
	}
}
