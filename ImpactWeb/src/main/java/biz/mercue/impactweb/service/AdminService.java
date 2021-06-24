package biz.mercue.impactweb.service;


import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.ListQueryForm;




public interface AdminService {
	
	
	public Admin getById(String id);
	
	public Admin getByEmail(String email);
	
	public int login(String email,String password);
	
	public int logout(String adminId);
	
	public int checkPassword(String adminId,String password);
	

	
	public int createAdmin(Admin admin);

	public int updateAdmin(Admin admin);

	public int deleteAdmin(Admin admin);
	
	public int updatePassword(String adminId,String password);
	
//	public int forgetPassword(Admin admin);
	
//	public int resetPassword(Admin admin);
	
	public ListQueryForm getAdminList(int page);
}
