package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.impactweb.model.Activity;

@Repository("activityDao")
public class ActivityDaoImpl extends AbstractDao<String, Activity> implements ActivityDao {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Activity getById(String id) {
		return getByKey(id);
	}

	@Override
	public void createActivity(Activity bean) {
		persist(bean);
	}

	@Override
	public void deleteActivity(Activity bean) {
		delete(bean);
	}

	@Override
	public List<Activity> getActivityList(int page, int pageSize) {
		Criteria crit = createEntityCriteria();
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}

	@Override
	public List<Activity> getAll() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("activity_order"));
		return criteria.list();
	}

	@Override
	public List<Activity> getAvailable() {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		return criteria.list();
	}

	@Override
	public int getActivityCount() {
		Criteria crit = createEntityCriteria();
		crit.setProjection(Projections.rowCount());
		long count = (long) crit.uniqueResult();
		return (int) count;
	}
}
