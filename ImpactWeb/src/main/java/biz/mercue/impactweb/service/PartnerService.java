package biz.mercue.impactweb.service;

import java.util.List;

import biz.mercue.impactweb.model.Partner;
import biz.mercue.impactweb.model.ListQueryForm;

public interface PartnerService {

	public Partner getById(String id);

	public int createPartner(Partner partner);

	public int updatePartner(Partner partner);
	
	public int updatePartnerList(List<Partner> list);
	
	public int deletePartner(Partner partner);

	public List<Partner> getAll();

	public List<Partner> getAvailable();

	public ListQueryForm getPartnerList(int page);

}