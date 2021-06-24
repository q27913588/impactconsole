package biz.mercue.impactweb.controller;

import java.io.File;
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

import com.fasterxml.jackson.core.type.TypeReference;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Partner;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.PartnerService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.ImageUtils;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.ListResponseBody;
import biz.mercue.impactweb.util.MapResponseBody;

@Controller
public class PartnerController {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	PartnerService partnerService;

	@Autowired
	AdminTokenService adminTokenService;

	@RequestMapping(value = "/api/getpartnerlist", method = {
			RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPartnerList(HttpServletRequest request,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (token != null) {

			ListQueryForm partnerForm = partnerService.getPartnerList(page);

			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(partnerForm);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}

		return responseBody.getJacksonString(View.Partner.class);
	}

	@RequestMapping(value = "/api/getavailablepartnerlist", method = {
			RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getAvailablePartnerList(HttpServletRequest request) {
		log.info("getAvailablePartnerList ");
		ListResponseBody responseBody = new ListResponseBody();

		List<Partner> list = partnerService.getAvailable();

		responseBody.setCode(Constants.INT_SUCCESS);
		responseBody.setMessage(Constants.MSG_SUCCESS);
		responseBody.setList(list);
		return responseBody.getJacksonString(View.Partner.class);
	}

	@RequestMapping(value = "/api/addpartner", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
					"multipart/form-data" })
	@ResponseBody
	public String addPartner(HttpServletRequest request, @RequestParam("data") String receiveJSONString,
			@RequestPart("file") MultipartFile[] files) {
		log.info("addPartner");
		MapResponseBody responseBody = new MapResponseBody();

		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			try {
				log.info("receiveJSONString:" + receiveJSONString);
				Partner partner = (Partner) JacksonJSONUtils.readValue(receiveJSONString, Partner.class);
				String partnerId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
				partner.setPartner_id(partnerId);
				log.info("files.length :" + files.length);
				for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

					MultipartFile file = files[fileIndex];
					if (file != null && !file.getOriginalFilename().isEmpty()) {
						String imageName = file.getOriginalFilename();
						String extendName = FilenameUtils.getExtension(imageName);
						String finalFileName = partnerId + "." + extendName;

						File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

						if (ImageUtils.writeFile(file, finalFile)) {
							partner.setPartner_image_file(finalFileName);
						}
					}
				}

				partnerService.createPartner(partner);
				responseBody.setCode(Constants.INT_SUCCESS);
			} catch (Exception e) {
				log.error("Exception :" + e.getMessage());
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}
		return responseBody.getJacksonString(View.Partner.class);

	}

	@RequestMapping(value = "/api/getpartnerinfo", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getPartnerInfo(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (tokenBean != null) {
			log.info("receiveJSONString:" + receiveJSONString);
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String partnerId = reqJSON.optString("partner_id");

			Partner partner = partnerService.getById(partnerId);
			if (partner != null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(partner);
			} else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Partner.class);
	}

	@RequestMapping(value = "/api/updatepartner", produces = Constants.CONTENT_TYPE_JSON, consumes = { "multipart/mixed",
			"multipart/form-data" })
	@ResponseBody
	public String updatePartner(HttpServletRequest request, @RequestParam("data") String receiveJSONString,
			@RequestPart("file") MultipartFile[] files) {
		log.info("updatePartner");
		MapResponseBody responseBody = new MapResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			try {

				Partner partner = (Partner) JacksonJSONUtils.readValue(receiveJSONString, Partner.class);
				String partnerId = partner.getPartner_id();
				for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

					MultipartFile file = files[fileIndex];
					if (file != null && !file.getOriginalFilename().isEmpty()) {
						String imageName = file.getOriginalFilename();
						String extendName = FilenameUtils.getExtension(imageName);
						String finalFileName = partnerId + "." + extendName;

						File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

						if (ImageUtils.writeFile(file, finalFile)) {
							partner.setPartner_image_file(finalFileName);
						}
					}
				}

				partnerService.updatePartner(partner);
				responseBody.setCode(Constants.INT_SUCCESS);
			} catch (Exception e) {
				log.error("Exception :" + e.getMessage());
				responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}

		return responseBody.getJacksonString(View.Partner.class);
	}

	@RequestMapping(value = "/api/deletepartner", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deletePartner(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("deletePartner");

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			Partner partner = (Partner) JacksonJSONUtils.readValue(receiveJSONString, Partner.class);
			log.info("partner :" + partner.getPartner_id());

			// TODO: delete partner_image_file
			partnerService.deletePartner(partner);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Partner.class);
	}

	@RequestMapping(value = "/api/updatepartnerorder", method = {
			RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updatePartnerOrder(HttpServletRequest request, @RequestBody String receiveJSONString) {

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			List<Partner> list = (List<Partner>) JacksonJSONUtils.readValue(receiveJSONString,
					new TypeReference<List<Partner>>() {
					});
			int resultCode = partnerService.updatePartnerList(list);
			responseBody.setCode(Constants.INT_SUCCESS);
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Partner.class);
	}

}
