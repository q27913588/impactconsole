package biz.mercue.impactweb.controller;



import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.CSVTask;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.CSVTaskService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.FileUtils;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.ListResponseBody;






@Controller
public class CSVController {
	
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	
	@Autowired
	CSVTaskService csvTaskService;
	
	@Autowired
	AdminTokenService adminTokenService;
	
	
	// csv紀錄清單
	@RequestMapping(value="/api/getcsvtasklist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getCSVTaskList(HttpServletRequest request,@RequestParam(value ="page",required=false,defaultValue ="1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token =  adminTokenService.getById(JWTUtils.getJwtToken(request));
		
		
		if(token !=null) {
			
			ListQueryForm csvForm = csvTaskService.getCSVTaskList(page);
				
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(csvForm);
			
		}else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			
		}
		
		return responseBody.getJacksonString(View.CSVTask.class);
	}
	
	
	@RequestMapping(value = "/api/importcsv", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
					"multipart/form-data" })
	@ResponseBody
	public String importCSV(HttpServletRequest request, @RequestParam("file") MultipartFile mpFile) {
		log.info("importCSV");
		BeanResponseBody responseBody = new BeanResponseBody();
		try {
			AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
			if (tokenBean != null) {
				log.info("1");
				Admin admin = tokenBean.getAdmin();
				if (mpFile != null && !mpFile.getOriginalFilename().isEmpty()) {
					File excel = FileUtils.MultipartFile2File(mpFile, null);
					CSVTask task = csvTaskService.addTaskByFile(excel, admin);
					responseBody.setCode(Constants.INT_SUCCESS);
					responseBody.setBean(task);
				}

			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
			log.error("Exception :" + e.getMessage());
			responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
		}

		return responseBody.getJacksonString(View.CSVTask.class);
	}
}
