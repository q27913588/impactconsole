package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.impactweb.model.Banner;

@Repository("bannerDao")
public class BannerDaoImpl extends AbstractDao<String, Banner> implements BannerDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Banner getById(String id) {

		return getByKey(id);
	}

	@Override
	public void createBanner(Banner bean) {
		persist(bean);
	}

	@Override
	public void deleteBanner(Banner bean) {
		delete(bean);
	}

	@Override
	public List<Banner> getBannerList(int page, int pageSize) {
		Criteria crit = createEntityCriteria();
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}

	@Override
	public List<Banner> getAll() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("banner_order"));
		return criteria.list();
	}

	@Override
	public List<Banner> getAvailable() {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		return criteria.list();
	}

	@Override
	public int getBannerCount() {
		Criteria crit = createEntityCriteria();
		crit.setProjection(Projections.rowCount());
		long count = (long) crit.uniqueResult();
		return (int) count;
	}
}
