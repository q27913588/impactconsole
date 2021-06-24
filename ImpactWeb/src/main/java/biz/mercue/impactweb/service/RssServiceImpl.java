package biz.mercue.impactweb.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.RssDao;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Rss;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Service("rssService")
@Transactional
public class RssServiceImpl implements RssService {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private RssDao rssDao;

	@Override
	public Rss getById(String id) {
		Rss Rss = rssDao.getById(id);
		return Rss;
	}

	@Override
	public ListQueryForm getRssList(int page) {
		int cout = rssDao.getRssCount();
		List list = rssDao.getRssList(page, Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);

		return form;
	}

	@Override
	public int createRss(Rss rss) {
		try {
			rss.setRss_id(KeyGeneratorUtils.generateRandomString());
			rss.setCreate_date(new Date());
			rssDao.createRss(rss);
			
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updateRss(Rss Rss) {
		Rss dbBean = rssDao.getById(Rss.getRss_id());
		try {
			if (dbBean != null) {
				dbBean.setRss_title(Rss.getRss_title());
				dbBean.setRss_url(Rss.getRss_url());
				dbBean.setAvailable(Rss.isAvailable());

				return Constants.INT_SUCCESS;
			} else {

				return Constants.INT_CANNOT_FIND_DATA;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}

	@Override
	public int updateRssList(List<Rss> list) {

		int resultCode = 0;
		for (Rss rss : list) {
			resultCode = updateRss(rss);
			log.info(resultCode);
		}
		return 0;
	}

	@Override
	public int deleteRss(Rss Rss) {
		Rss dbBean = rssDao.getById(Rss.getRss_id());
		if (dbBean != null) {

			rssDao.deleteRss(dbBean);

			return Constants.INT_SUCCESS;
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}

	}

	@Override
	public List<Rss> getAll() {
		return rssDao.getAll();
	}

	@Override
	public List<Rss> getAvailable() {
		return rssDao.getAvailable();
	}
}
