package biz.mercue.impactweb.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.SuccessCase;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.SuccessCaseService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.ImageUtils;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.ListResponseBody;

@Controller
public class SuccessCaseController {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	SuccessCaseService successCaseService;

	@Autowired
	AdminTokenService adminTokenService;

	@RequestMapping(value = "/api/getsuccesscaselist", method = { RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getSuccessCaseList(HttpServletRequest request,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		try {
			if (token != null) {
				ListQueryForm form = successCaseService.getSuccessCaseList(page);
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setListQuery(form);
			} else {
				responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseBody.getJacksonString(View.SuccessCase.class);
	}

	@RequestMapping(value = "/api/addsuccesscase", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
	"multipart/form-data" })
	@ResponseBody
	public String addSuccessCase(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {

			SuccessCase successCase = (SuccessCase) JacksonJSONUtils.readValue(receiveJSONString, SuccessCase.class);

			log.info("receiveJSONString:" + receiveJSONString);
			String successCaseId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
			successCase.setSuccess_case_id(successCaseId);
			log.info("files.length :" + files.length);
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

				MultipartFile file = files[fileIndex];
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = successCaseId + "." + extendName;

					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

					if (ImageUtils.writeFile(file, finalFile)) {
						successCase.setSuccess_case_image_file(finalFileName);
					}
				}
			}
			
			taskResult = successCaseService.createSuccessCase(successCase);
			if (taskResult == Constants.INT_SUCCESS) {
				responseBody.setCode(Constants.INT_SUCCESS);

			} else if (taskResult == Constants.INT_DATA_DUPLICATE) {
				responseBody.setCode(Constants.INT_DATA_DUPLICATE);
			} else {
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.SuccessCase.class);
	}

	@RequestMapping(value = "/api/getsuccesscaseinfo", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getSuccessCaseInfo(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (tokenBean != null) {
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String successCaseId = reqJSON.optString("success_case_id");

			SuccessCase successCase = successCaseService.getById(successCaseId);
			if (successCase != null) {
				if (successCase.getListAttachment().isEmpty()) {
					successCase.setListAttachment(null);
				}
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(successCase);
			} else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.SuccessCase.class);
	}

	@RequestMapping(value = "/api/updatesuccesscase", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
	"multipart/form-data" })
	@ResponseBody
	public String updateSuccessCase(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {

			SuccessCase successCase = (SuccessCase) JacksonJSONUtils.readValue(receiveJSONString, SuccessCase.class);

			String successCaseId = successCase.getSuccess_case_id();
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

				MultipartFile file = files[fileIndex];
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = successCaseId + "." + extendName;

					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

					if (ImageUtils.writeFile(file, finalFile)) {
						successCase.setSuccess_case_image_file(finalFileName);
					}
				}
			}
			
			taskResult = successCaseService.updateSuccessCase(successCase);

			if (taskResult == Constants.INT_SUCCESS) {
				responseBody.setCode(Constants.INT_SUCCESS);
			} else if (taskResult == Constants.INT_CANNOT_FIND_DATA) {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			} else {
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.SuccessCase.class);
	}

	@RequestMapping(value = "/api/deletesuccesscase", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deleteSuccessCase(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("deleteSuccessCase");

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			SuccessCase successCase = (SuccessCase) JacksonJSONUtils.readValue(receiveJSONString, SuccessCase.class);
			successCaseService.deleteSuccessCase(successCase);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.SuccessCase.class);
	}
}
