package biz.mercue.impactweb.controller;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.News;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.NewsService;
import biz.mercue.impactweb.service.TagService;
import biz.mercue.impactweb.util.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class NewsController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    AdminTokenService adminTokenService;

    @Autowired
    NewsService newsService;

    @Autowired
    TagService tagService;

    @PostMapping(value = "/api/addnews", produces = Constants.CONTENT_TYPE_JSON, consumes = {"multipart/mixed",
            "multipart/form-data"})
    public String addNews(HttpServletRequest request,
                          @RequestParam("data") String receiveJSONString,
                          @RequestPart("news") MultipartFile[] newsFiles,
                          @RequestPart(value = "images", required = false) MultipartFile[] imageFiles) throws Exception {
        log.info("/api/addnews");
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        News news = (News) JacksonJSONUtils.readValue(receiveJSONString, News.class);
        newsService.multipartFile(news, newsFiles, imageFiles);
        newsService.createNews(news);

        responseBody.setBean(news);
        return responseBody.getJacksonString(View.News.class);
    }

    @PostMapping(value = "/api/getnewsinfo", produces = Constants.CONTENT_TYPE_JSON)
    public String getNewsInfo(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        log.info("/api/getnewsinfo");
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        JSONObject reqJSON = new JSONObject(receiveJSONString);
        String newsId = reqJSON.optString("news_id");

        News news = newsService.getById(newsId);
        if (news == null) throw new CustomException.CanNotFindDataException();
        if (news.getListAttachment().isEmpty()) news.setListAttachment(null);

        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(news);
        return responseBody.getJacksonString(View.News.class);
    }

    @GetMapping(value = "/api/getnewslist", produces = Constants.CONTENT_TYPE_JSON)
    public String getNewsList(HttpServletRequest request,
                              @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                              @RequestParam(value = "tag_id", required = false) String tagId) throws Exception {
        log.info("/api/getnewslist");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        ListQueryForm newsForm;
        if (StringUtils.isNULL(tagId)) {
            newsForm = newsService.getNewsList(page);
        } else {
            newsForm = newsService.getNewsListByTag(tagId, page);
        }

        responseBody.setListQuery(newsForm);
        return responseBody.getJacksonString(View.News.class);
    }

    @GetMapping(value = "/api/getnewslistfrontend", produces = Constants.CONTENT_TYPE_JSON)
    public String getNewsListFrontEnd(HttpServletRequest request,
                                      @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        log.info("/api/getnewslistfrontend");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        ListQueryForm newsForm = newsService.getNewsListFrontEnd(page);

        responseBody.setListQuery(newsForm);
        return responseBody.getJacksonString(View.NewsFrontEnd.class);
    }

    @GetMapping(value = "/api/getnewslistbackend", produces = Constants.CONTENT_TYPE_JSON)
    public String getNewsListBackEnd(HttpServletRequest request,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") int page) throws Exception {
        log.info("/api/getnewslistbackend");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        ListQueryForm newsForm = newsService.getNewsListBackEnd(page);

        responseBody.setListQuery(newsForm);
        return responseBody.getJacksonString(View.NewsBackEnd.class);
    }

    @PostMapping(value = "/api/updatenews", produces = Constants.CONTENT_TYPE_JSON, consumes = {"multipart/mixed",
            "multipart/form-data"})
    public String updateNews(HttpServletRequest request,
                             @RequestParam("data") String receiveJSONString,
                             @RequestPart("news") MultipartFile[] newsFiles,
                             @RequestPart("images") MultipartFile[] images) throws Exception {
        log.info("/api/updatenews");
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        News news = (News) JacksonJSONUtils.readValue(receiveJSONString, News.class);
        newsService.deleteOriginFile(news);
        newsService.multipartFile(news, newsFiles, images);
        newsService.updateNews(news);

        responseBody.setBean(news);
        return responseBody.getJacksonString(View.News.class);
    }

    @PostMapping(value = "/api/deletenews", produces = Constants.CONTENT_TYPE_JSON)
    public String deleteNews(HttpServletRequest request, @RequestBody String receiveJSONString) throws Exception {
        log.info("/api/deletenews");
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken adminToken = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (adminToken == null) throw new CustomException.TokenNullException();

        News news = (News) JacksonJSONUtils.readValue(receiveJSONString, News.class);
        newsService.deleteNews(news);

        return responseBody.getJacksonString(View.News.class);
    }
}
