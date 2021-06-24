package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import biz.mercue.impactweb.model.News;
import biz.mercue.impactweb.model.SuccessCase;

@Repository("successCaseDao")
public class SuccessCaseDaoImpl extends AbstractDao<String, SuccessCase> implements SuccessCaseDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public SuccessCase getById(String id) {
		return getByKey(id);
	}

	@Override
	public void createSuccessCase(SuccessCase bean) {
		persist(bean);
	}

	@Override
	public void deleteSuccessCase(SuccessCase bean) {
		delete(bean);
	}

	@Override
	public List<SuccessCase> getSuccessCaseList(int page, int pageSize) {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.desc("published_date"));
		criteria.setFirstResult((page - 1) * pageSize);
		criteria.setMaxResults(pageSize);
		return criteria.list();
	}

	@Override
	public List<SuccessCase> getAll() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.desc("published_date"));
		criteria.addOrder(Order.asc("successCase_order"));
		return criteria.list();
	}

	@Override
	public List<SuccessCase> getAvailable() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.desc("published_date"));
		criteria.add(Restrictions.eq("available", true));
		return criteria.list();
	}

	@Override
	public int getSuccessCaseCount() {
		Criteria crit = createEntityCriteria();
		crit.setProjection(Projections.rowCount());
		long count = (long) crit.uniqueResult();
		return (int) count;
	}
	
	@Override
	public void deleteAttachment(String successCaseId) {
		String hql = "Delete From Attachment a where a.success_case.success_case_id = :successCaseId";
		Session session = getSession();
		Query query = session.createQuery(hql);
		query.setParameter("successCaseId", successCaseId);
		query.executeUpdate();
	}
}
