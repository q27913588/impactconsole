package biz.mercue.impactweb.service;



import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.AdminDao;
import biz.mercue.impactweb.dao.AdminTokenDao;
import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.StringUtils;




@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService{
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private AdminDao dao;
	
	@Autowired
	private AdminTokenDao tokenDao;

	
	
	@Autowired
	PasswordEncoder encoder;
	
	
	@Override
	public Admin getById(String id) {
		Admin admin = dao.getById(id);
		return admin;
	}

	@Override
	public int login(String email, String password) {
		
		Admin dbBean = dao.getByEmail(email);
		
		if(dbBean != null){
			try {
				if (encoder.matches(password, dbBean.getAdmin_password())) {
					if (dbBean.isAvailable()) {
						log.info("isAvailable");
						return Constants.INT_SUCCESS;
					} else {
						log.info("unavailable");
						return Constants.INT_ACCOUNT_UNAVAILABLE;
					}
				} else {
					log.info("login fail, password error");
					return Constants.INT_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				log.error("Exception : " + e.getMessage());
			} 
			
		}else{
			return Constants.INT_CANNOT_FIND_DATA;
			
		}
		
		return Constants.INT_SYSTEM_PROBLEM;
	}

	@Override
	public int logout(String adminId) {
		List<AdminToken> list  = tokenDao.getByAvailableUserId(adminId);
		list.forEach(bean->{
			bean.setAvailable(false);
		});
		return Constants.INT_SUCCESS;
	}

	
	@Override
	public int checkPassword(String adminId, String password) {

		Admin dbBean = dao.getById(adminId);

		if (dbBean != null) {
			try {
				if (encoder.matches(password, dbBean.getAdmin_password())) {
					return Constants.INT_SUCCESS;
				} else {
					log.info("login fail, password error");
					return Constants.INT_PASSWORD_ERROR;
				}
			} catch (Exception e) {
				return Constants.INT_SYSTEM_PROBLEM;
			}
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}
	
	@Override
	public ListQueryForm getAdminList(int page){
		int cout = dao.getAdminCount();
		List list = dao.getAdminList(page,Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		
		return form;
	}

	@Override
	public int createAdmin(Admin admin) {
		Admin dbBean = dao.getByEmail(admin.getAdmin_email());
		try{
			if(dbBean == null){
	
				admin.setAdmin_id(KeyGeneratorUtils.generateRandomString());
				if(!StringUtils.isNULL(admin.getAdmin_password())) {
					admin.setAdmin_password(encoder.encode(admin.getAdmin_password()));
				}

				admin.setCreate_date(new Date());
				admin.setUpdate_date(new Date());
				
				dao.createAdmin(admin);
		
				return Constants.INT_SUCCESS;
			}else{
				log.info("e-mail:"+dbBean.getAdmin_email());
				return Constants.INT_USER_DUPLICATE;
			}
		}catch(Exception e){
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}
	
	@Override
	public int updateAdmin(Admin admin) {
		Admin dbBean = dao.getById(admin.getAdmin_id());
		try{
			if(dbBean != null){
			
				dbBean.setAdmin_email(admin.getAdmin_email());
				dbBean.setAdmin_name(admin.getAdmin_name());
				dbBean.setUpdate_date(new Date());
				dbBean.setAvailable(admin.isAvailable());
				
				if(!StringUtils.isNULL(admin.getAdmin_password())){
					dbBean.setAdmin_password(encoder.encode(admin.getAdmin_password()));
				}
	
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
	public int updatePassword(String adminId, String password) {
		Admin dbBean = dao.getById(adminId);
		try {
			if (dbBean == null) {
				return Constants.INT_CANNOT_FIND_DATA;
			}
			dbBean.setAdmin_password(encoder.encode(password));
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}
	

	@Override
	public int deleteAdmin(Admin admin) {
		Admin dbBean = dao.getById(admin.getAdmin_id());
		if(dbBean != null){
		
			dao.deleteAdmin(dbBean);
			return Constants.INT_SUCCESS;
		}else{
			
			return Constants.INT_CANNOT_FIND_DATA;
		}

	}
	
	@Override
	public Admin getByEmail(String email){
		Admin admin = dao.getByEmail(email);
		return admin;
	}

//	@Override
//	public int forgetPassword(Admin admin) {
//		Admin dbAdmin = dao.getByEmail(admin.getAdmin_email());
//		if(dbAdmin!=null) {
//			log.info("dbAdmin :"+dbAdmin.getAdmin_email());
//			createTempToken(dbAdmin);
//			new MailSender().sendForgetPassword(dbAdmin);
//			return Constants.INT_SUCCESS;
//		} else {
//			return Constants.INT_CANNOT_FIND_DATA;
//		}
//	}

//	@Override
//	public int resetPassword(Admin admin) {
//		TempToken tempBean = tempTokenDao.getById(admin.getToken());
//		if(tempBean!=null  && tempBean.getAdmin() !=null) {
//			if(tempBean.getExpire_date().after(new Date()) && tempBean.isAvailable() ) {
//				tempBean.setAvailable(false);
//				Admin dbAdmin = tempBean.getAdmin();
//				int taskResult = updatePassword(dbAdmin.getAdmin_id(), admin.getAdmin_password());
//				
//				return taskResult;
//			}else {
//				return Constants.INT_ACCESS_TOKEN_ERROR;
//			}
//		}else {
//			return Constants.INT_CANNOT_FIND_DATA;
//		}
//		
//	}
	
//	private void createTempToken(Admin admin) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.add(Calendar.DAY_OF_MONTH, 2);
//		TempToken token = new TempToken();
//		token.setToken_id(KeyGeneratorUtils.generateRandomString());
//		token.setAdmin(admin);
//		token.setAvailable(true);
//		token.setCreate_date(new Date());
//		token.setExpire_date(calendar.getTime());
//		tempTokenDao.createToken(token);
//	}
	
	

}
