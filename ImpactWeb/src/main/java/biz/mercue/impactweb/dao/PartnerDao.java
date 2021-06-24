package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Partner;

public interface PartnerDao {

	Partner getById(String id);

	Partner getByName(String name);
	
	void createPartner(Partner bean);

	void deletePartner(Partner bean);

	List<Partner> getPartnerList(int page, int pageSize);
	
	List<Partner> getAll();
	
	List<Partner> getAvailable();
	
	int getPartnerCount();

}
