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

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Rss;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.RssService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.JacksonJSONUtils;
import biz.mercue.impactweb.util.ListResponseBody;

@Controller
public class RssController {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	RssService rssService;

	@Autowired
	AdminTokenService adminTokenService;

	@RequestMapping(value = "/api/getrsslist", method = { RequestMethod.GET }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getRssList(HttpServletRequest request,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		ListResponseBody responseBody = new ListResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (token != null) {

			ListQueryForm rssForm = rssService.getRssList(page);

			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setListQuery(rssForm);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);

		}

		return responseBody.getJacksonString(View.Rss.class);
	}

	@RequestMapping(value = "/api/addrss", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String addRss(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {
			Rss rss = (Rss) JacksonJSONUtils.readValue(receiveJSONString, Rss.class);
			log.info("receiveJSONString:" + receiveJSONString);
			taskResult = rssService.createRss(rss);
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

		return responseBody.getJacksonString(View.Rss.class);
	}

	@RequestMapping(value = "/api/getrssinfo", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String getRssInfo(HttpServletRequest request, @RequestBody String receiveJSONString) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));

		if (tokenBean != null) {
			log.info("receiveJSONString:" + receiveJSONString);
			JSONObject reqJSON = new JSONObject(receiveJSONString);
			String rssId = reqJSON.optString("rss_id");

			Rss rss = rssService.getById(rssId);
			if (rss != null) {
				responseBody.setCode(Constants.INT_SUCCESS);
				responseBody.setBean(rss);
			} else {
				responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Rss.class);
	}

	@RequestMapping(value = "/api/updaterss", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON, consumes = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String updateRss(HttpServletRequest request, @RequestBody String receiveJSONString) {

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

		int taskResult = -1;
		if (token != null) {

			Rss rss = (Rss) JacksonJSONUtils.readValue(receiveJSONString, Rss.class);
			taskResult = rssService.updateRss(rss);

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
		return responseBody.getJacksonString(View.Rss.class);
	}

	@RequestMapping(value = "/api/deleterss", method = { RequestMethod.POST }, produces = Constants.CONTENT_TYPE_JSON)
	@ResponseBody
	public String deleteRss(HttpServletRequest request, @RequestBody String receiveJSONString) {
		log.info("deleteRss");

		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
		if (tokenBean != null) {
			Rss rss = (Rss) JacksonJSONUtils.readValue(receiveJSONString, Rss.class);
			log.info("rss :" + rss.getRss_id());
			rssService.deleteRss(rss);
			responseBody.setCode(Constants.INT_SUCCESS);
			responseBody.setMessage(Constants.MSG_SUCCESS);

		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}

		return responseBody.getJacksonString(View.Rss.class);
	}

}
