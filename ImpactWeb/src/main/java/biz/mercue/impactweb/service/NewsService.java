package biz.mercue.impactweb.service;

import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.News;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface NewsService {
    public News getById(String id);

    public void createNews(News news);

    public void updateNews(News news) throws Exception;

    public void updateNewsList(List<News> list) throws Exception;

    public void deleteNews(News news) throws Exception;

    public List<News> getAll();

    public List<News> getAvailable();

    public ListQueryForm getNewsList(int page);

    public ListQueryForm getNewsListByTag(String tagId, int page);

    public ListQueryForm getNewsListFrontEnd(int page);

    public ListQueryForm getNewsListBackEnd(int page);

    public void multipartFile(News news, MultipartFile[] newFiles, MultipartFile[] imageFiles);

    void deleteOriginFile(News news) throws IOException;
}
