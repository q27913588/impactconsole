package biz.mercue.impactweb.dao;


import java.util.List;

import biz.mercue.impactweb.model.AdminToken;


public interface AdminTokenDao {
	
	int addToken(AdminToken token);
	AdminToken getById(String token);
	List<AdminToken> getByUserId(String userId);
	List<AdminToken> getByAvailableUserId(String userId);

}
