package biz.mercue.impactweb.controller;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.Downloads;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.DownloadsService;
import biz.mercue.impactweb.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class DownloadsController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    DownloadsService downloadsService;

    @Autowired
    AdminTokenService adminTokenService;

    @RequestMapping(value = "/api/getdownloadslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getDownloadsList(HttpServletRequest request,
                                   @RequestParam(value = "categoryId", required = false) String categoryId,
                                   @RequestParam(value = "searchText", required = false) String queryStr,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        log.info("getdownloadslist, categoryId: " + categoryId + ", queryStr: " + queryStr);
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));

        if (token != null) {
            ListQueryForm downloadsForm = downloadsService.getDownloadsList(categoryId, queryStr, page);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setListQuery(downloadsForm);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/getavailablebdownloadslist", method = {RequestMethod.GET}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getAvailableDownloadsList(HttpServletRequest request) {
        log.info("getAvailableDownloadsList ");
        ListResponseBody responseBody = new ListResponseBody();
        List<Downloads> list = downloadsService.getAvailable();
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setMessage(Constants.MSG_SUCCESS);
        responseBody.setList(list);
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/adddownloads", method = {
            RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON, consumes = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String addDownloads(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("addDownloads");
        MapResponseBody responseBody = new MapResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            try {
                log.info("receiveJSONString:" + receiveJSONString);
                Downloads downloads = (Downloads) JacksonJSONUtils.readValue(receiveJSONString, Downloads.class);
                String downloadsId = KeyGeneratorUtils.generateRandomString(Constants.SHORT_IMAGE_NAME_LENGTH);
                downloads.setDownloads_id(downloadsId);
                int result = downloadsService.createDownloads(downloads);
                // TODO: 再判斷一次 result
                responseBody.setCode(Constants.INT_SUCCESS);
            } catch (Exception e) {
                log.error("Exception :" + e.getMessage());
                responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
            }
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/getdownloadsinfo", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getDownloadsInfo(HttpServletRequest request, @RequestBody String receiveJSONString) {
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));

        if (tokenBean != null) {
            log.info("receiveJSONString:" + receiveJSONString);
            JSONObject reqJSON = new JSONObject(receiveJSONString);
            String downloadsId = reqJSON.optString("downloads_id");

            Downloads downloads = downloadsService.getById(downloadsId);
            if (downloads != null) {
                responseBody.setCode(Constants.INT_SUCCESS);
                responseBody.setBean(downloads);
            } else {
                responseBody.setCode(Constants.INT_CANNOT_FIND_DATA);
            }
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/updatedownloads", produces = Constants.CONTENT_TYPE_JSON, consumes = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String updateDownloads(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("updateDownloads");
        MapResponseBody responseBody = new MapResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            try {
                Downloads downloads = (Downloads) JacksonJSONUtils.readValue(receiveJSONString, Downloads.class);
                downloadsService.updateDownloads(downloads);
                responseBody.setCode(Constants.INT_SUCCESS);
            } catch (Exception e) {
                log.error("Exception :" + e.getMessage());
                responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
            }
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/deletedownloads", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String deleteDownloads(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("deleteDownloads");

        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            Downloads downloads = (Downloads) JacksonJSONUtils.readValue(receiveJSONString, Downloads.class);
            log.info("downloads :" + downloads.getDownloads_id());
            downloadsService.deleteDownloads(downloads);
            responseBody.setCode(Constants.INT_SUCCESS);
            responseBody.setMessage(Constants.MSG_SUCCESS);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }

    @RequestMapping(value = "/api/updatedownloadsorder", method = {RequestMethod.POST}, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String updateDownloadsOrder(HttpServletRequest request, @RequestBody String receiveJSONString) {

        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean != null) {
            List<Downloads> list = (List<Downloads>) JacksonJSONUtils.readValue(receiveJSONString, new TypeReference<List<Downloads>>() {
            });
            int resultCode = downloadsService.updateDownloadsList(list);
            responseBody.setCode(Constants.INT_SUCCESS);
        } else {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
        }
        return responseBody.getJacksonString(View.Downloads.class);
    }
}
