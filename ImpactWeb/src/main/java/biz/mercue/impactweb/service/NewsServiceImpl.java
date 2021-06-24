package biz.mercue.impactweb.service;

import biz.mercue.impactweb.dao.NewsDao;
import biz.mercue.impactweb.model.Attachment;
import biz.mercue.impactweb.model.ImageCollection;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.News;
import biz.mercue.impactweb.util.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("newsService")
@Transactional
public class NewsServiceImpl implements NewsService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void createNews(News editNews) {
        String newsId = editNews.getNews_id();
        if (StringUtils.isNULL(newsId)) {
            newsId = KeyGeneratorUtils.generateRandomString();
        }

        editNews.setNews_id(newsId);
        editNews.setAvailable(true);
        editNews.setCreate_date(new Date());
        editNews.setUpdate_date(new Date());
        editNews.setListTag(editNews.getListTag());
        newsDao.createNews(editNews);
    }

    @Autowired
    private NewsDao newsDao;

    @Override
    public News getById(String id) {
        News dbNews = newsDao.getById(id);

        if (dbNews != null) {
            dbNews.getListTag().size();
            dbNews.getListAttachment().size();
            dbNews.getListImageCollection().size();
        }

        return dbNews;
    }

    @Override
    public ListQueryForm getNewsList(int page) {
        List<News> list = newsDao.getNewsList(page, Constants.SYSTEM_PAGE_SIZE);
        int count = newsDao.getCountNewsList();
        ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);

        for (News news : list) {
            news.getListTag().size();
            news.getListAttachment().size();
            news.getListImageCollection().size();
        }

        return form;
    }

    @Override
    public ListQueryForm getNewsListByTag(String tagId, int page) {
        List<News> list = newsDao.getNewsListByTag(tagId, page, Constants.SYSTEM_PAGE_SIZE);
        int count = newsDao.getCountNewsListByTag(tagId);
        ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);

        for (News news : list) {
            news.getListTag().size();
            news.getListAttachment().size();
            news.getListImageCollection().size();
        }

        return form;
    }

    @Override
    public ListQueryForm getNewsListFrontEnd(int page) {
        List<News> list = newsDao.getNewsListFrontEnd(page, Constants.SYSTEM_PAGE_SIZE);
        int count = newsDao.getCountNewsListFrontEnd();
        ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);

        for (News news : list) {
            news.getListTag().size();
            news.getListAttachment().size();
            news.getListImageCollection().size();
        }

        return form;
    }

    @Override
    public ListQueryForm getNewsListBackEnd(int page) {
        List<News> list = newsDao.getNewsListBackEnd(page, Constants.SYSTEM_PAGE_SIZE);
        int count = newsDao.getCountNewsListBackEnd();
        ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);

        for (News news : list) {
            news.getListTag().size();
            news.getListAttachment().size();
            news.getListImageCollection().size();
        }

        return form;
    }

    @Override
    public void updateNews(News editNews) throws Exception {
        News dbNews = newsDao.getById(editNews.getNews_id());
        if (dbNews == null) throw new CustomException.CanNotFindDataException();

        dbNews.setNews_image_file(editNews.getNews_image_file());
        dbNews.setNews_title(editNews.getNews_title());
        dbNews.setNews_subtitle(editNews.getNews_subtitle());
        dbNews.setNews_content(editNews.getNews_content());
        dbNews.setNews_iframe(editNews.getNews_iframe());
        dbNews.setNews_custom_tag(editNews.getNews_custom_tag());
        dbNews.setPublished_date(editNews.getPublished_date());
        dbNews.setUpdate_date(new Date());
        dbNews.setAvailable(editNews.isAvailable());
        dbNews.setNews_language(editNews.getNews_language());

        // many to many
        dbNews.setListTag(editNews.getListTag());

        // one to many
        handleAttachment(dbNews, editNews);
        handleImageCollection(dbNews, editNews);
    }

    @Override
    public void updateNewsList(List<News> list) throws Exception {
        for (News news : list) {
            updateNews(news);
        }
    }

    @Override
    public void deleteNews(News News) throws CustomException.CanNotFindDataException {
        News dbBean = newsDao.getById(News.getNews_id());
        if (dbBean == null) throw new CustomException.CanNotFindDataException();

        newsDao.deleteNews(dbBean);
    }

    @Override
    public List<News> getAll() {
        return newsDao.getAll();
    }

    @Override
    public List<News> getAvailable() {
        return newsDao.getAvailable();
    }

    @Override
    public void multipartFile(News news, MultipartFile[] newFiles, MultipartFile[] imageFiles) {
        // news images
        String newsId = news.getNews_id();
        if (StringUtils.isNULL(newsId)) {
            newsId = KeyGeneratorUtils.generateRandomString();
        }

        for (MultipartFile file : newFiles) {
            if (file == null || StringUtils.isNULL(file.getOriginalFilename())) continue;

            String imageName = file.getOriginalFilename();
            String extendName = FilenameUtils.getExtension(imageName);
            String finalFileName = newsId + "." + extendName;

            File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
            if (ImageUtils.writeFile(file, finalFile)) news.setNews_image_file(finalFileName);
        }

        // highlight images
        List<ImageCollection> imageCollectionList = new ArrayList<>();
        for (MultipartFile file : imageFiles) {
            if (file == null || StringUtils.isNULL(file.getOriginalFilename())) {
                continue;
            }

            ImageCollection imageCollection = new ImageCollection();
            String imageId = KeyGeneratorUtils.generateRandomString();
            imageCollection.setImage_id(imageId);
            imageCollection.setNews(news);
            imageCollectionList.add(imageCollection);

            String fileName = KeyGeneratorUtils.generateRandomString();
            String imageName = file.getOriginalFilename();
            String extendName = FilenameUtils.getExtension(imageName);
            String finalFileName = fileName + "." + extendName;

            File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);
            if (ImageUtils.writeFile(file, finalFile)) imageCollection.setImage_file(finalFileName);
        }


        for (ImageCollection image : news.getListImageCollection()) {
            if (!image.getIs_delete()) imageCollectionList.add(image);
        }

        news.setListImageCollection(imageCollectionList);
    }

    @Override
    public void deleteOriginFile(News news) throws IOException {
        if (StringUtils.isNULL(news.getNews_id())) {
            return;
        }
        News dbNews = newsDao.getById(news.getNews_id());

        if (isNewsModify(dbNews, news)) {
            String newsImagePath = Constants.IMAGE_UPLOAD_PATH + File.separator + dbNews.getNews_image_file();
            Files.deleteIfExists(Paths.get(newsImagePath));
        }

        List<ImageCollection> imageList = news.getListImageCollection();
        for (ImageCollection image : imageList) {
            if (image.getIs_delete()) {
                String newsImagePath = Constants.IMAGE_UPLOAD_PATH + File.separator + image.getImage_file();
                Files.deleteIfExists(Paths.get(newsImagePath));
            }
        }
    }

    private void handleAttachment(News dbNews, News editNews) {
        if (editNews.getListAttachment() != null && !editNews.getListAttachment().isEmpty()) {
            List<Attachment> listAttachment = editNews.getListAttachment();
            for (Attachment attachment : listAttachment) {
                attachment.setAttachment_id(KeyGeneratorUtils.generateRandomString());
                attachment.setNews(dbNews);
            }
            newsDao.deleteAttachment(dbNews.getNews_id());
            dbNews.setListAttachment(editNews.getListAttachment());
        } else {
            if (dbNews.getListAttachment() != null) {
                newsDao.deleteAttachment(dbNews.getNews_id());
            }
        }
    }

    private void handleImageCollection(News dbNews, News editNews) {
        if (editNews.getListImageCollection() != null && !editNews.getListImageCollection().isEmpty()) {
            List<ImageCollection> listImageCollection = editNews.getListImageCollection();
            for (ImageCollection image : listImageCollection) {
                image.setImage_id(KeyGeneratorUtils.generateRandomString());
                image.setNews(dbNews);
            }
            newsDao.deleteImageCollection(dbNews.getNews_id());
            dbNews.setListImageCollection(editNews.getListImageCollection());
        } else {
            if (dbNews.getListImageCollection() != null) {
                newsDao.deleteImageCollection(dbNews.getNews_id());
            }
        }
    }

    private boolean isNewsModify(News dbNews, News news) {
        String dbNewsId = dbNews.getNews_id();
        String newsId = news.getNews_id();
        return !dbNewsId.equals(newsId);
    }
}
