package biz.mercue.impactweb.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mysql.cj.api.log.Log;

import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.util.StringUtils;


@Repository("accountDao")
public class AccountDaoImpl extends AbstractDao<String,  Account> implements AccountDao {

	
	private Logger log = Logger.getLogger(this.getClass().getName());
	@Override
	public Account getById(String id) {

		return getByKey(id);
	}
	
	@Override
	public Account getByEmail(String email) {
		
		Criteria criteria = createEntityCriteria();	
		criteria.add(Restrictions.eq("account_email", email));
		return (Account) criteria.uniqueResult();
	}
	
	@Override
	public void createAccount(Account bean) {

		persist(bean);
	}
	
	@Override
	public void deleteAccount(Account bean) {

		delete(bean);
	}
	
	
	@Override
	public List<Account> getAccountList(int page,int pageSize){
		Criteria crit = createEntityCriteria();	
		crit.setFirstResult((page - 1) * pageSize);
		crit.setMaxResults(pageSize);
		return crit.list();
	}

	@Override
	public int getAccountCount() {
		Criteria crit = createEntityCriteria();	
		crit.setProjection(Projections.rowCount());
		long count = (long)crit.uniqueResult();
		return (int)count;
	}




}
