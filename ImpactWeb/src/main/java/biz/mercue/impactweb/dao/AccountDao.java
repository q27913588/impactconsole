package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Account;



public interface AccountDao {
	
	Account getById(String id);
	

	
	Account getByEmail(String email);
	
	void createAccount(Account bean);
	

	void deleteAccount(Account bean);

	List<Account> getAccountList(int page,int pageSize);
	
	int getAccountCount();


}
