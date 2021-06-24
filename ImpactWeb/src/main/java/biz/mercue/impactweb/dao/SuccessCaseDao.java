package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.SuccessCase;

public interface SuccessCaseDao {
	SuccessCase getById(String id);
	
	void createSuccessCase(SuccessCase bean);
	
	void deleteSuccessCase(SuccessCase bean);
	
	List<SuccessCase> getSuccessCaseList(int page, int pageSize);
	
	List<SuccessCase> getAll();
	
	List<SuccessCase> getAvailable();
	
	int getSuccessCaseCount();
	
	void deleteAttachment(String successCaseId);
}
