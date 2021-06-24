package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.CSVTask;




@Repository("csvTaskDao")
public class CSVTaskDaoImpl extends AbstractDao<String,  CSVTask> implements CSVTaskDao {

	private Logger log = Logger.getLogger(this.getClass().getName());
		
	
	@Override
	public void create(CSVTask task) {
		persist(task);
		
	}
	
	@Override
	public CSVTask getById(String id){
		return getByKey(id);
	}
	
	
	@Override
	public List<CSVTask> getByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public List<CSVTask> getNotFinishByAdmin(String adminId){
		Criteria criteria =  createEntityCriteria();
		criteria.createAlias("admin","admin");
		criteria.add(Restrictions.eq("admin.admin_id", adminId));
		criteria.add(Restrictions.eq("is_finish", false));
		criteria.addOrder(Order.desc("create_date"));
		return criteria.list();
	}
	
	@Override
	public List<CSVTask> getCSVTaskList(int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}
	
	@Override
	public int getCSVTaskCount() {
		Criteria crit = createEntityCriteria();	
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}
}
