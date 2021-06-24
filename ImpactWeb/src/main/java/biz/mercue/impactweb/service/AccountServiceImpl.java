package biz.mercue.impactweb.service;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.AccountDao;
import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;





@Service("accountService")
@Transactional
public class AccountServiceImpl implements AccountService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private AccountDao dao;
	
	@Override
	public Account getById(String id) {
		Account account = dao.getById(id);
		return account;
	}
	
	@Override
	public ListQueryForm getAccountList(int page){
		int cout = dao.getAccountCount();
		List list = dao.getAccountList(page,Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		
		return form;
	}

	@Override
	public int createAccount(Account account) {
		Account dbBean = dao.getByEmail(account.getAccount_email());
		try{
			if(dbBean == null){
	
				account.setAccount_id(KeyGeneratorUtils.generateRandomString());
				
				account.setCreate_date(new Date());
				account.setUpdate_date(new Date());
				
				dao.createAccount(account);
				return Constants.INT_SUCCESS;
			}else{
				log.info("e-mail:"+dbBean.getAccount_email());
				return Constants.INT_USER_DUPLICATE;
			}
		}catch(Exception e){
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}
	
	@Override
	public int updateAccount(Account account) {
		Account dbBean = dao.getById(account.getAccount_id());
		try{
			if(dbBean != null){
				dbBean.setAccount_email(account.getAccount_email());
				dbBean.setAccount_name(account.getAccount_name());
				dbBean.setAccount_password(account.getAccount_password());
				dbBean.setUpdate_date(new Date());
				dbBean.setAvailable(account.isAvailable());
				
				return Constants.INT_SUCCESS;
			}else{
				
				return Constants.INT_CANNOT_FIND_DATA;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}

	@Override
	public int deleteAccount(Account account) {
		Account dbBean = dao.getById(account.getAccount_id());
		if(dbBean != null){
		
			dao.deleteAccount(dbBean);
			return Constants.INT_SUCCESS;
		}else{
			
			return Constants.INT_CANNOT_FIND_DATA;
		}

	}
	
	@Override
	public Account getByEmail(String email){
		Account account = dao.getByEmail(email);
		return account;
	}

}
