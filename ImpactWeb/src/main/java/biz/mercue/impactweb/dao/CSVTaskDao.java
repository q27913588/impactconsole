package biz.mercue.impactweb.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import biz.mercue.impactweb.model.Account;
import biz.mercue.impactweb.model.CSVTask;


public interface CSVTaskDao {
	
	
	CSVTask getById(String id);
	

	List<CSVTask> getByAdmin(String adminId);
	
	List<CSVTask> getNotFinishByAdmin(String adminId);
	
	List<CSVTask> getCSVTaskList(int page,int pageSize);
	
	int getCSVTaskCount();
	
	
	void create(CSVTask task);
}
