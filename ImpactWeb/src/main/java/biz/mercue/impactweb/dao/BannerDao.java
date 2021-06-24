package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Banner;

public interface BannerDao {
	Banner getById(String id);
	
	void createBanner(Banner bean);
	
	void deleteBanner(Banner bean);
	
	List<Banner> getBannerList(int page, int pageSize);
	
	List<Banner> getAll();
	
	List<Banner> getAvailable();
	
	int getBannerCount();
}
