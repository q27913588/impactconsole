package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.impactweb.model.Rss;

@Repository("rssDao")
public class RssDaoimpl extends AbstractDao<String, Rss> implements RssDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Rss getById(String id) {

		return getByKey(id);
	}

	@Override
	public void createRss(Rss bean) {
		persist(bean);
	}

	@Override
	public void deleteRss(Rss bean) {
		delete(bean);
	}

	@Override
	public List<Rss> getRssList(int page, int pageSize) {
		Criteria crit = createEntityCriteria();
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}

	@Override
	public List<Rss> getAll() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("rss_order"));
		return criteria.list();
	}

	@Override
	public List<Rss> getAvailable() {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		return criteria.list();
	}

	@Override
	public int getRssCount() {
		Criteria crit = createEntityCriteria();
		crit.setProjection(Projections.rowCount());
		long count = (long) crit.uniqueResult();
		return (int) count;
	}
}
