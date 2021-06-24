package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Rss;

public interface RssDao {
	Rss getById(String id);
	
	void createRss(Rss bean);
	
	void deleteRss(Rss bean);
	
	List<Rss> getRssList(int page, int pageSize);
	
	List<Rss> getAll();
	
	List<Rss> getAvailable();
	
	int getRssCount();
}
