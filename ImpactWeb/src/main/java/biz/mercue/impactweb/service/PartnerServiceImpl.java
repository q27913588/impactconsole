package biz.mercue.impactweb.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.PartnerDao;
import biz.mercue.impactweb.model.Partner;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Service("partnerService")
@Transactional
public class PartnerServiceImpl implements PartnerService {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PartnerDao partnerDao;

	@Override
	public Partner getById(String id) {
		Partner Partner = partnerDao.getById(id);
		return Partner;
	}

	@Override
	public ListQueryForm getPartnerList(int page) {
		int cout = partnerDao.getPartnerCount();
		List list = partnerDao.getPartnerList(page, Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);

		return form;
	}

	@Override
	public int createPartner(Partner partner) {
		try {

			partner.setPartner_id(KeyGeneratorUtils.generateRandomString());
			partner.setAvailable(true);

			int count = partnerDao.getPartnerCount();
			int partner_order = count;
			partner.setPartner_order(partner_order);
			partner.setCreate_date(new Date());
			partner.setUpdate_date(new Date());
			partnerDao.createPartner(partner);

			return Constants.INT_SUCCESS;

		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updatePartner(Partner partner) {
		Partner dbBean = partnerDao.getById(partner.getPartner_id());
		try {
			if (dbBean != null) {
				dbBean.setPartner_name(partner.getPartner_name());
				dbBean.setPartner_url(partner.getPartner_url());
				dbBean.setPartner_order(partner.getPartner_order());
				dbBean.setPartner_image_file(partner.getPartner_image_file());
				dbBean.setUpdate_date(new Date());
				dbBean.setAvailable(partner.isAvailable());

				return Constants.INT_SUCCESS;
			} else {

				return Constants.INT_CANNOT_FIND_DATA;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}

	}

	@Override
	public int updatePartnerList(List<Partner> list) {

		for (Partner partner : list) {
			updatePartner(partner);
		}
		return 0;
	}

	@Override
	public int deletePartner(Partner Partner) {
		Partner dbBean = partnerDao.getById(Partner.getPartner_id());
		if (dbBean != null) {

			partnerDao.deletePartner(dbBean);

			// reset rest partner_order value
			reorderPartnerList();

			return Constants.INT_SUCCESS;
		} else {

			return Constants.INT_CANNOT_FIND_DATA;
		}

	}

	// reorder partner_order value
	private void reorderPartnerList() {
		List<Partner> partnerList = getAll();
		int index = 0;
		for (Partner partner : partnerList) {
			partner.setPartner_order(index);
			updatePartner(partner);
			index++;
		}
	}

	@Override
	public List<Partner> getAll() {
		return partnerDao.getAll();
	}

	@Override
	public List<Partner> getAvailable() {
		return partnerDao.getAvailable();
	}

}
