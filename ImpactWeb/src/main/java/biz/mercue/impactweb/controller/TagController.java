package biz.mercue.impactweb.controller;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.Tag;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.NewsService;
import biz.mercue.impactweb.service.TagService;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.ListResponseBody;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TagController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    AdminTokenService adminTokenService;

    @Autowired
    NewsService newsService;

    @Autowired
    TagService tagService;

    @RequestMapping(value = "/api/gettaglist", method = RequestMethod.GET, produces = Constants.CONTENT_TYPE_JSON)
    @ResponseBody
    public String getTagList(HttpServletRequest request) {
        log.info("/api/gettaglist");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (token == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.News.class);
        }
        List<Tag> list = tagService.getTagList();
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(list);
        responseBody.setTotal_count(list.size());
        responseBody.setPage_size(list.size());
        return responseBody.getJacksonString(View.Tag.class);
    }
}
