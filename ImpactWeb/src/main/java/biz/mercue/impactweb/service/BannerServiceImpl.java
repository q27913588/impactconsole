package biz.mercue.impactweb.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.BannerDao;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Banner;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Service("bannerService")
@Transactional
public class BannerServiceImpl implements BannerService {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private BannerDao bannerDao;

	@Override
	public Banner getById(String id) {
		Banner Banner = bannerDao.getById(id);
		return Banner;
	}

	@Override
	public ListQueryForm getBannerList(int page) {
		int cout = bannerDao.getBannerCount();
		List list = bannerDao.getBannerList(page, Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);

		return form;
	}
	
	/**
	 * 只取出一個Banner
	 * @return Banner
	 */
	@Override
	public Banner getOneBanner() {
		List<Banner> list = bannerDao.getBannerList(1, Constants.SYSTEM_PAGE_SIZE);
		return list.get(0);
	}

	@Override
	public int createBanner(Banner banner) {
		try {
			// 需求：banner資料庫只需要留最新的一筆資料，也就是前端call api時只需要回傳最新一筆數據
			// 構想：在新增新的一筆banner數據前先判斷資料庫是否已存在數據，若有，則將banner table數據全部清空，再新增新的資料
			//     即banner table數據只會有最新一筆資料
			// 作法：
			//  1.先判斷資料庫是否存在數據（因為有可能第一次新增，完全沒有已存在的數據）
			//  2.若資料庫已存在新的數據，則get所有數據出來並封裝成List
			//  3.將list以for循環方式取出元素並分別執行刪除指令
			if (bannerDao.getBannerCount() != 0) {
				List<Banner> list = bannerDao.getBannerList(1, Constants.SYSTEM_PAGE_SIZE);
				for (Banner b : list) {
					bannerDao.deleteBanner(b);
				}
			}

			banner.setBanner_id(KeyGeneratorUtils.generateRandomString());
			banner.setAvailable(true);
			bannerDao.createBanner(banner);
			
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updateBanner(Banner Banner) {
		Banner dbBean = bannerDao.getById(Banner.getBanner_id());
		try {
			if (dbBean != null) {
				dbBean.setAvailable(Banner.isAvailable());
				dbBean.setBanner_image_file(Banner.getBanner_image_file());
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
	public int updateBannerList(List<Banner> list) {
		int resultCode = 0;
		for (Banner banner : list) {
			resultCode = updateBanner(banner);
			log.info(resultCode);
		}
		return 0;
	}

	@Override
	public int deleteBanner(Banner Banner) {
		Banner dbBean = bannerDao.getById(Banner.getBanner_id());
		if (dbBean != null) {

			bannerDao.deleteBanner(dbBean);

			return Constants.INT_SUCCESS;
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}

	}

	@Override
	public List<Banner> getAll() {
		return bannerDao.getAll();
	}

	@Override
	public List<Banner> getAvailable() {
		return bannerDao.getAvailable();
	}
}
