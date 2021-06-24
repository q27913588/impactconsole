package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.News;

import java.util.List;

public interface NewsDao {
    void createNews(News bean);

    News getById(String id);

    List<News> getNewsList(int page, int pageSize);

    int getCountNewsList();

    List<News> getNewsListByTag(String tagId, int page, int pageSize);

    int getCountNewsListByTag(String tagId);

    List<News> getNewsListFrontEnd(int page, int pageSize);

    int getCountNewsListFrontEnd();

    List<News> getNewsListBackEnd(int page, int pageSize);

    int getCountNewsListBackEnd();

    List<News> getAll();

    List<News> getAvailable();

    int getNewsCount();

    void deleteNews(News bean);

    void deleteAttachment(String newsId);

    void deleteImageCollection(String newsId);
}
