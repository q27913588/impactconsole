package biz.mercue.impactweb.service;

import java.util.List;

import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Rss;

public interface RssService {
	public Rss getById(String id);

	public int createRss(Rss Rss);
	
	public int updateRss(Rss Rss);
	
	public int updateRssList(List<Rss> list);
	
	public int deleteRss(Rss Rss);

	public List<Rss> getAll();

	public List<Rss> getAvailable();

	public ListQueryForm getRssList(int page);

}
