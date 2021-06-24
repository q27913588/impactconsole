package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mysql.cj.api.log.Log;

import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.util.StringUtils;


@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao<String,  Admin> implements AdminDao {

	
	private Logger log = Logger.getLogger(this.getClass().getName());
	@Override
	public Admin getById(String id) {

		return getByKey(id);
	}
	
	@Override
	public Admin getByEmail(String email) {
		
		Criteria criteria = createEntityCriteria();	
		criteria.add(Restrictions.eq("admin_email", email));
		return (Admin) criteria.uniqueResult();
	}
	
	@Override
	public void createAdmin(Admin bean) {

		persist(bean);
	}
	
	@Override
	public void deleteAdmin(Admin bean) {

		delete(bean);
	}
	
	
	@Override
	public List<Admin> getAdminList(int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}

	@Override
	public int getAdminCount() {
		Criteria crit = createEntityCriteria();	
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}




}
