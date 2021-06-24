package biz.mercue.impactweb.service;



import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;

import biz.mercue.impactweb.dao.AccountDao;
import biz.mercue.impactweb.dao.CSVTaskDao;
import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.CSVTask;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.util.CSVUtils;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.FileUtils;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.TuyaService;






@Service("csvTaskService")
@Transactional
public class CSVTaskServiceImpl implements CSVTaskService{
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private CSVTaskDao dao;
	
	
	@Autowired
	private AccountDao accountDao;
	
	@Override
	public CSVTask getById(String id) {
		return dao.getById(id);
	}
	
	@Override
	public List<CSVTask> getByAdmin(String adminId){
		return dao.getByAdmin(adminId);
	}
	
	@Override
	public List<CSVTask> getNotFinishByAdmin(String adminId){
		return dao.getNotFinishByAdmin(adminId);
	}
	
	@Override
	public ListQueryForm getCSVTaskList(int page){
		log.info("in getCSVTaskList");
		int cout = dao.getCSVTaskCount();
		log.info(cout);
		List list = dao.getCSVTaskList(page,Constants.SYSTEM_PAGE_SIZE);
		log.info(list);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		
		return form;
	}
	
	
	@Override
	public CSVTask addTaskByFile(File excel , Admin admin) throws IOException {
		log.info("addTaskByFile");
		CSVTask task = new CSVTask();
		task.setCSV_task_id(KeyGeneratorUtils.generateRandomString());
		try {
			

			if(excel != null) {
				log.info("file :"+excel.getName());
				task.setAdmin(admin);
				task.setIs_finish(false);
				task.setTask_file_name(excel.getName());
				dao.create(task);
				
				List<Account> listAccount = CSVUtils.readCSVFile(excel.getAbsolutePath());
				for(Account account : listAccount) {
					TuyaService.getInstance().rigisterUser(account);
					if("200".equals(account.getResult_code())) {

						account.setAvailable(true);
						Account dbAccount = accountDao.getById(account.getAccount_id());
						if(dbAccount == null) {
							account.setCreate_date(new Date());
							account.setUpdate_date(new Date());
							account.setOperation("C");
							accountDao.createAccount(account);
						}else {
							account.setOperation("U");
							dbAccount.setAccount_tuya_id(account.getAccount_tuya_id());
							dbAccount.setAccount_email(account.getAccount_email());
							dbAccount.setAccount_name(account.getAccount_name());
							dbAccount.setAccount_password(account.getAccount_password());
							dbAccount.setUpdate_date(new Date());
						}

					}
				}
				CSVUtils.writeCSVFile(listAccount, excel.getAbsolutePath());
			}
			
		

		} catch (Exception e) {
			log.error("Exception :"+e.getMessage());
		}finally{

		}
		return task;
	}
}
