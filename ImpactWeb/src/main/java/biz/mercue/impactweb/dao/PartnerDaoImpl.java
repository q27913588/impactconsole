package biz.mercue.impactweb.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.apache.log4j.Logger;
import com.mysql.cj.api.log.Log;

import biz.mercue.impactweb.model.Partner;
import biz.mercue.impactweb.util.StringUtils;

@Repository("partnerDao")
public class PartnerDaoImpl extends AbstractDao<String, Partner> implements PartnerDao {

	private Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public Partner getById(String id) {

		return getByKey(id);
	}

	@Override
	public Partner getByName(String name) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("partner_name", name));
		return (Partner) criteria.uniqueResult();
	}

	@Override
	public void createPartner(Partner bean) {
		persist(bean);
	}

	@Override
	public void deletePartner(Partner bean) {
		delete(bean);
	}

	@Override
	public List<Partner> getPartnerList(int page, int pageSize) {
		Criteria crit = createEntityCriteria();
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		crit.addOrder(Order.asc("partner_order"));
		return crit.list();
	}

	@Override
	public List<Partner> getAll() {
		Criteria criteria = createEntityCriteria();
		criteria.addOrder(Order.asc("partner_order"));
		return criteria.list();
	}

	@Override
	public List<Partner> getAvailable() {
		Criteria criteria =  createEntityCriteria();
		criteria.add(Restrictions.eq("available", true));
		criteria.addOrder(Order.asc("partner_order"));
		return criteria.list();
	}
	
	@Override
	public int getPartnerCount() {
		Criteria crit = createEntityCriteria();
		crit.setProjection(Projections.rowCount());
		long count = (long) crit.uniqueResult();
		return (int) count;
	}

}
