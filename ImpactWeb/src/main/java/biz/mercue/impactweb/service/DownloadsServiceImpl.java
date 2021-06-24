package biz.mercue.impactweb.service;

import biz.mercue.impactweb.dao.DownloadsDao;
import biz.mercue.impactweb.model.Downloads;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.CustomException;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("downloadsService")
@Transactional
public class DownloadsServiceImpl implements DownloadsService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private DownloadsDao downloadsDao;

    @Override
    public Downloads getById(String id) {
        Downloads dbDownloads = downloadsDao.getById(id);
        return dbDownloads;
    }

    @Override
    public ListQueryForm getDownloadsList(String categoryId, String text, int page) {
        List<Downloads> list = downloadsDao.getDownloadsList(categoryId, text, page, Constants.SYSTEM_PAGE_SIZE);
        int count = downloadsDao.getDownloadsListCount(categoryId, text);

        for (Downloads downloads : list) {
            downloads.getCategory().getListDownloads().size();
        }

        return new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);
    }

    @Override
    public List<Downloads> getAll() {
        return downloadsDao.getAll();
    }

    @Override
    public List<Downloads> getAvailable() {
        return downloadsDao.getAvailable();
    }

    @Override
    public int createDownloads(Downloads editDownloads) {
        int count = downloadsDao.getDownloadsCount();
        editDownloads.setDownloads_id(KeyGeneratorUtils.generateRandomString());
        editDownloads.setDownloads_order(count);
        editDownloads.setCreate_date(new Date());
        editDownloads.setPublished_date(new Date());
        downloadsDao.createDownloads(editDownloads);
        return Constants.INT_SUCCESS;
    }

    @Override
    public int updateDownloads(Downloads editDownloads) {
        Downloads dbBean = downloadsDao.getById(editDownloads.getDownloads_id());

        if (dbBean == null) {
            throw new CustomException.CanNotFindDataException();
        }

        dbBean.setDownloads_title(editDownloads.getDownloads_title());
        dbBean.setDownloads_url(editDownloads.getDownloads_url());
        dbBean.setDownloads_order(editDownloads.getDownloads_order());
        dbBean.setAvailable(editDownloads.isAvailable());
        dbBean.setCreate_date(new Date());
        dbBean.setPublished_date(editDownloads.getPublished_date());
        dbBean.setCategory(editDownloads.getCategory());
        return Constants.INT_SUCCESS;
    }

    @Override
    public int updateDownloadsList(List<Downloads> list) {
        for (Downloads downloads : list) {
            updateDownloads(downloads);
        }
        return Constants.INT_SUCCESS;
    }

    @Override
    public int deleteDownloads(Downloads editDownloads) {
        Downloads dbBean = downloadsDao.getById(editDownloads.getDownloads_id());
        if (dbBean != null) {
            downloadsDao.deleteDownloads(dbBean);
            return Constants.INT_SUCCESS;
        } else {
            return Constants.INT_CANNOT_FIND_DATA;
        }
    }

}
