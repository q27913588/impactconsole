package biz.mercue.impactweb.service;


import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.ListQueryForm;




public interface AccountService {
	
	
	public Account getById(String id);
	
	public Account getByEmail(String email);
	
	
	public int createAccount(Account account);

	public int updateAccount(Account account);

	public int deleteAccount(Account account);
	
	public ListQueryForm getAccountList(int page);
}
