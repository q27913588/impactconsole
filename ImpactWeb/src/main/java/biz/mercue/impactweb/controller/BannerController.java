package biz.mercue.impactweb.controller;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
import biz.mercue.impactweb.model.Banner;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.BannerService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.ImageUtils;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.ListResponseBody;

@Controller
public class BannerController {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	BannerService bannerService;

	@Autowired
	AdminTokenService adminTokenService;

	@RequestMapping(value = "/api/getbannerlist", method = { RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBannerList(HttpServletRequest request,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (token != null) {

			ListQueryForm bannerForm = bannerService.getBannerList(page);

			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(bannerForm);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}

		return responseBody.getJacksonString(View.Banner.class);
	}

	@RequestMapping(value = "/api/addbanner", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
	"multipart/form-data" })
	@ResponseBody
	public String addBanner(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {

			Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);

			log.info("receiveJSONString:" + receiveJSONString);
			String bannerId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
			banner.setBanner_id(bannerId);
			log.info("files.length :" + files.length);
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

				MultipartFile file = files[fileIndex];
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = bannerId + "." + extendName;

					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

					if (ImageUtils.writeFile(file, finalFile)) {
						banner.setBanner_image_file(finalFileName);
					}
				}
			}
			
			taskResult = bannerService.createBanner(banner);
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

		return responseBody.getJacksonString(View.Banner.class);
	}

	@RequestMapping(value = "/api/getbannerinfo", method = { RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getBannerInfo(HttpServletRequest request) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));

		/**
		 * 需求：前端call api時只需要回傳單筆數據，而非array形式
		 * 作法：修改service新增方法，成功時回傳單筆model
		 */
		
		if (tokenBean != null) {
			Banner banner = bannerService.getOneBanner();
			if (banner != null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(banner);
			} else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
		return responseBody.getJacksonString(View.Banner.class);
	}

	@RequestMapping(value = "/api/updatebanner", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
	"multipart/form-data" })
	@ResponseBody
	public String updateBanner(HttpServletRequest request, @RequestParam("data") String receiveJSONString, @RequestPart("file") MultipartFile[] files) {

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {

			Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);

			String bannerId = banner.getBanner_id();
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

				MultipartFile file = files[fileIndex];
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String finalFileName = bannerId + "." + extendName;

					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

					if (ImageUtils.writeFile(file, finalFile)) {
						banner.setBanner_image_file(finalFileName);
					}
				}
			}
			
			taskResult = bannerService.updateBanner(banner);

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
		return responseBody.getJacksonString(View.Banner.class);
	}

	@RequestMapping(value = "/api/deletebanner", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deleteBanner(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("deleteBanner");

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			Banner banner = (Banner) JacksonJSONUtils.readValue(receiveJSONString, Banner.class);
			log.info("banner :" + banner.getBanner_id());
			bannerService.deleteBanner(banner);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Banner.class);
	}
}
