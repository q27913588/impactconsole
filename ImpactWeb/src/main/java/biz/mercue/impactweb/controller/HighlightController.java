//package biz.mercue.impactweb.controller;
//
//import biz.mercue.impactweb.model.AdminToken;
//import biz.mercue.impactweb.model.Highlight;
//import biz.mercue.impactweb.model.View;
//import biz.mercue.impactweb.service.AdminTokenService;
//import biz.mercue.impactweb.service.HighlightService;
//import biz.mercue.impactweb.util.*;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//public class HighlightController {
//    @Autowired
//    private AdminTokenService adminTokenService;
//
//    @Autowired
//    private HighlightService highlightService;
//
//    @PostMapping(value = "/api/addhighlight")
//    public String createHighlight(HttpServletRequest request,
//                                  @RequestParam("data") String receiveJSONString,
//                                  @RequestPart("file") MultipartFile[] files) throws Exception {
//        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//        if (token == null) throw new CustomException.TokenNullException();
//
//        BeanResponseBody responseBody = new BeanResponseBody();
//        Highlight highlight = (Highlight) JacksonJSONUtils.readValue(receiveJSONString, Highlight.class);
//        highlightService.create(highlight, files);
//
//        responseBody.setBean(highlight);
//        return responseBody.getJacksonString(View.Highlight.class);
//    }
//
//    @PostMapping(value = "/api/gethighlightinfo", produces = Constants.CONTENT_TYPE_JSON)
//    public String getHighlight(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
//        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//        if (token == null) throw new CustomException.TokenNullException();
//
//
//        BeanResponseBody responseBody = new BeanResponseBody();
//        JSONObject jsonObject = new JSONObject(receiveJSONString);
//        String highlightId = jsonObject.optString("highlight_id");
//        Highlight highlight = highlightService.getById(highlightId);
//
//        responseBody.setBean(highlight);
//        return responseBody.getJacksonString(View.Highlight.class);
//    }
//
//    @PostMapping(value = "/api/updatehighlight", produces = Constants.CONTENT_TYPE_JSON)
//    public String updateHighlight(HttpServletRequest request,
//                                  @RequestParam("data") String receiveJSONString,
//                                  @RequestPart("file") MultipartFile[] files) throws Exception {
//        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//        if (token == null) throw new CustomException.TokenNullException();
//
//        BeanResponseBody responseBody = new BeanResponseBody();
//        Highlight highlight = (Highlight) JacksonJSONUtils.readValue(receiveJSONString, Highlight.class);
//        highlightService.update(highlight, files);
//
//        responseBody.setBean(highlight);
//        return responseBody.getJacksonString(View.Highlight.class);
//    }
//
//    @PostMapping(value = "/api/deletehighlight", produces = Constants.CONTENT_TYPE_JSON)
//    public String deleteHighlight(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
//        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
//        if (token == null) throw new CustomException.TokenNullException();
//
//        BeanResponseBody responseBody = new BeanResponseBody();
//        Highlight highlight = (Highlight) JacksonJSONUtils.readValue(receiveJSONString, Highlight.class);
//        highlightService.delete(highlight);
//
//        return responseBody.getJacksonString(View.Highlight.class);
//    }
//}
