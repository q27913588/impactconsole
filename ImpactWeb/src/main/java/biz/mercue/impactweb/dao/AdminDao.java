package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Admin;



public interface AdminDao {
	
	Admin getById(String id);
	

	
	Admin getByEmail(String email);
	
	void createAdmin(Admin bean);

	void deleteAdmin(Admin bean);

	List<Admin> getAdminList(int page,int pageSize);
	
	int getAdminCount();


}
