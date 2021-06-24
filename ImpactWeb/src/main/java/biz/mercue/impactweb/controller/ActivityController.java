//package biz.mercue.impactweb.controller;
//
//import java.io.File;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.io.FilenameUtils;
//import org.apache.log4j.Logger;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.multipart.MultipartFile;
//
//import biz.mercue.impactweb.model.Activity;
//import biz.mercue.impactweb.model.AdminToken;
//import biz.mercue.impactweb.model.ListQueryForm;
//import biz.mercue.impactweb.model.View;
//import biz.mercue.impactweb.service.ActivityService;
//import biz.mercue.impactweb.service.AdminTokenService;
//import biz.mercue.impactweb.util.BeanResponseBody;
//import biz.mercue.impactweb.util.Constants;
//import biz.mercue.impactweb.util.ImageUtils;
//import biz.mercue.impactweb.util.JWTUtils;
//import biz.mercue.impactweb.util.JacksonJSONUtils;
//import biz.mercue.impactweb.util.KeyGeneratorUtils;
//import biz.mercue.impactweb.util.ListResponseBody;
//
//@Controller
//public class ActivityController {
//	private Logger log = Logger.getLogger(this.getClass().getName());
//
//	@Autowired
//	ActivityService activityService;
//
//	@Autowired
//	AdminTokenService adminTokenService;
//
//	@RequestMapping(value = "/api/getactivitylist", method = { RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String getActivityList(HttpServletRequest request,
//			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
//		ListResponseBody responseBody = new ListResponseBody();
//		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//		if (token != null) {
//			ListQueryForm activityForm = activityService.getActivityList(page);
//			responseBody.setCode(Constants.INT_SUCCESS);
//			responseBody.setListQuery(activityForm);
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//		}
//
//		return responseBody.getJacksonString(View.Activity.class);
//	}
//
//	@RequestMapping(value = "/api/addactivity", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
//	"multipart/form-data" })
//	@ResponseBody
//	public String addActivity(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {
//		BeanResponseBody responseBody = new BeanResponseBody();
//		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//
//		int taskResult = -1;
//		if (token != null) {
//
//			Activity activity = (Activity) JacksonJSONUtils.readValue(receiveJSONString, Activity.class);
//
//			log.info("receiveJSONString:" + receiveJSONString);
//			String activityId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
//			activity.setActivity_id(activityId);
//			log.info("files.length :" + files.length);
//
//			// 試著寫寫看
//			List<String> list = new LinkedList();
//			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
//
//				MultipartFile file = files[fileIndex];
//				if (file != null && !file.getOriginalFilename().isEmpty()) {
//					String imageName = file.getOriginalFilename();
//					String extendName = FilenameUtils.getExtension(imageName);
//					String finalFileName = activityId + "." + extendName;
//
//					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
//
//					if (ImageUtils.writeFile(file, finalFile)) {
//						list.add(finalFileName);
//						activity.setActivity_image_file(finalFileName);
//					}
//				}
//			}
//
//			taskResult = activityService.createActivity(activity);
//			if (taskResult == Constants.INT_SUCCESS) {
//				responseBody.setCode(Constants.INT_SUCCESS);
//
//			} else if (taskResult == Constants.INT_DATA_DUPLICATE) {
//				responseBody.setCode(Constants.INT_DATA_DUPLICATE);
//			} else {
//				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
//			}
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//		}
//
//		return responseBody.getJacksonString(View.Activity.class);
//	}
//
//	@RequestMapping(value = "/api/getactivityinfo", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String getActivityInfo(HttpServletRequest request, @RequestBody String receiveJSONString) {
//		BeanResponseBody responseBody = new BeanResponseBody();
//		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
//
//		if (tokenBean != null) {
//			log.info("receiveJSONString:" + receiveJSONString);
//			JSONObject reqJSON = new JSONObject(receiveJSONString);
//			String activityId = reqJSON.optString("activity_id");
//
//			Activity activity = activityService.getById(activityId);
//			if (activity != null) {
//				responseBody.setCode(Constants.INT_SUCCESS);
//				responseBody.setBean(activity);
//			} else {
//				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
//			}
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//		}
//
//		return responseBody.getJacksonString(View.Activity.class);
//	}
//
//	@RequestMapping(value = "/api/updateactivity", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
//	"multipart/form-data" })
//	@ResponseBody
//	public String updateActivity(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {
//
//		BeanResponseBody responseBody = new BeanResponseBody();
//		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//
//		int taskResult = -1;
//		if (token != null) {
//
//			Activity activity = (Activity) JacksonJSONUtils.readValue(receiveJSONString, Activity.class);
//
//			String activityId = activity.getActivity_id();
//			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {
//
//				MultipartFile file = files[fileIndex];
//				if (file != null && !file.getOriginalFilename().isEmpty()) {
//					String imageName = file.getOriginalFilename();
//					String extendName = FilenameUtils.getExtension(imageName);
//					String finalFileName = activityId + "." + extendName;
//
//					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
//
//					if (ImageUtils.writeFile(file, finalFile)) {
//						activity.setActivity_image_file(finalFileName);
//					}
//				}
//			}
//
//			taskResult = activityService.updateActivity(activity);
//
//			if (taskResult == Constants.INT_SUCCESS) {
//				responseBody.setCode(Constants.INT_SUCCESS);
//			} else if (taskResult == Constants.INT_CANNOT_FIND_DATA) {
//				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
//			} else {
//				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
//			}
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//		}
//		return responseBody.getJacksonString(View.Activity.class);
//	}
//
//	@RequestMapping(value = "/api/deleteactivity", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
//	@ResponseBody
//	public String deleteActivity(HttpServletRequest request, @RequestBody String receiveJSONString) {
//		log.info("deleteActivity");
//
//		BeanResponseBody responseBody = new BeanResponseBody();
//		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
//		if (tokenBean != null) {
//			Activity activity = (Activity) JacksonJSONUtils.readValue(receiveJSONString, Activity.class);
//			log.info("activity :" + activity.getActivity_id());
//			activityService.deleteActivity(activity);
//			responseBody.setCode(Constants.INT_SUCCESS);
//			responseBody.setMessage(Constants.MSG_SUCCESS);
//
//		} else {
//			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
//		}
//
//		return responseBody.getJacksonString(View.Activity.class);
//	}
//}
