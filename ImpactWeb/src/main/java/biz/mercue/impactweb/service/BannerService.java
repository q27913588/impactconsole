package biz.mercue.impactweb.service;

import java.util.List;

import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Banner;

public interface BannerService {
	public Banner getById(String id);

	public int createBanner(Banner news);
	
	public int updateBanner(Banner news);
	
	public int updateBannerList(List<Banner> list);
	
	public int deleteBanner(Banner news);

	public List<Banner> getAll();

	public List<Banner> getAvailable();

	public ListQueryForm getBannerList(int page);

	public Banner getOneBanner();
}
