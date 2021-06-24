package biz.mercue.impactweb.service;

import java.util.List;

import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.SuccessCase;

public interface SuccessCaseService {
	public SuccessCase getById(String id);

	public int createSuccessCase(SuccessCase successCase);
	
	public int updateSuccessCase(SuccessCase successCase);
	
	public int updateSuccessCaseList(List<SuccessCase> list);
	
	public int deleteSuccessCase(SuccessCase successCase);

	public List<SuccessCase> getAll();

	public List<SuccessCase> getAvailable();

	public ListQueryForm getSuccessCaseList(int page);
}
