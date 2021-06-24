package biz.mercue.impactweb.service;

import biz.mercue.impactweb.model.AdminToken;

public interface AdminTokenService {
	
	
	public AdminToken getById(String token);
	
	public AdminToken generateToken(String adminId);
	
	public void updateToken(AdminToken bean);
	
	public int logout(String adminId);
	
	public AdminToken checkAvailable(String token);
	
}
