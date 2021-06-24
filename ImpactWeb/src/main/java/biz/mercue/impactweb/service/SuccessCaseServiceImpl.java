package biz.mercue.impactweb.service;

import java.util.Date;
import java.util.List;

import biz.mercue.impactweb.model.Attachment;
import biz.mercue.impactweb.model.News;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.SuccessCaseDao;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.SuccessCase;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Service("successCaseService")
@Transactional
public class SuccessCaseServiceImpl implements SuccessCaseService {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private SuccessCaseDao successCaseDao;

	@Override
	public SuccessCase getById(String id) {
		SuccessCase dbSuccessCase = successCaseDao.getById(id);

		if (dbSuccessCase != null) {
			dbSuccessCase.getListTag().size();
			dbSuccessCase.getListAttachment().size();
		}

		return dbSuccessCase;
	}

	@Override
	public ListQueryForm getSuccessCaseList(int page) {
		List<SuccessCase> list = successCaseDao.getSuccessCaseList(page, Constants.SYSTEM_PAGE_SIZE);
		int count = successCaseDao.getSuccessCaseCount();
		ListQueryForm form = new ListQueryForm(count, Constants.SYSTEM_PAGE_SIZE, list);

		if (!list.isEmpty()) {
			for (SuccessCase dbSuccessCase : list) {
				dbSuccessCase.getListTag().size();
				dbSuccessCase.getListAttachment().size();
			}
		}

		return form;
	}

	@Override
	public int createSuccessCase(SuccessCase editSuccessCase) {
		try {
			editSuccessCase.setAvailable(true);
			editSuccessCase.setCreate_date(new Date());
			editSuccessCase.setUpdate_date(new Date());
			successCaseDao.createSuccessCase(editSuccessCase);
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updateSuccessCase(SuccessCase editSuccessCase) {
		SuccessCase dbSuccess = successCaseDao.getById(editSuccessCase.getSuccess_case_id());
		try {
			if (dbSuccess == null) {
				return Constants.INT_CANNOT_FIND_DATA;
			}
			dbSuccess.setSuccess_case_title(editSuccessCase.getSuccess_case_title());
			dbSuccess.setSuccess_case_subtitle(editSuccessCase.getSuccess_case_subtitle());
			dbSuccess.setSuccess_case_content(editSuccessCase.getSuccess_case_content());
			dbSuccess.setSuccess_case_image_file(editSuccessCase.getSuccess_case_image_file());
			dbSuccess.setSuccess_case_iframe(editSuccessCase.getSuccess_case_iframe());
			dbSuccess.setAvailable(editSuccessCase.isAvailable());
			dbSuccess.setCreate_date(editSuccessCase.getCreate_date());
			dbSuccess.setUpdate_date(new Date());
			dbSuccess.setPublished_date(editSuccessCase.getPublished_date());
			dbSuccess.setSuccess_case_language(editSuccessCase.getSuccess_case_language());
			dbSuccess.setListTag(editSuccessCase.getListTag());

			// many to many
			dbSuccess.setListAttachment(editSuccessCase.getListAttachment());

			// one to many
			handleAttachment(dbSuccess, editSuccessCase);
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updateSuccessCaseList(List<SuccessCase> list) {
		int resultCode = Constants.INT_SYSTEM_PROBLEM;
		for (SuccessCase successCase : list) {
			resultCode = updateSuccessCase(successCase);
		}
		return resultCode;
	}

	private void handleAttachment(SuccessCase dbSuccess, SuccessCase editSuccess) {
		if (editSuccess.getListAttachment() != null && !editSuccess.getListAttachment().isEmpty()) {
			List<Attachment> listAttachment = editSuccess.getListAttachment();
			for (Attachment attachment : listAttachment) {
				attachment.setAttachment_id(KeyGeneratorUtils.generateRandomString());
				attachment.setSuccess_case(dbSuccess);
			}
			successCaseDao.deleteAttachment(dbSuccess.getSuccess_case_id());
			dbSuccess.setListAttachment(editSuccess.getListAttachment());
		} else {
			if (dbSuccess.getListAttachment() != null) {
				successCaseDao.deleteAttachment(dbSuccess.getSuccess_case_id());
			}
		}
	}

	@Override
	public int deleteSuccessCase(SuccessCase editSuccessCase) {
		SuccessCase dbSuccess = successCaseDao.getById(editSuccessCase.getSuccess_case_id());
		if (dbSuccess == null) {
			return Constants.INT_CANNOT_FIND_DATA;
		}
		successCaseDao.deleteSuccessCase(dbSuccess);
		return Constants.INT_SUCCESS;
	}

	@Override
	public List<SuccessCase> getAll() {
		return successCaseDao.getAll();
	}

	@Override
	public List<SuccessCase> getAvailable() {
		return successCaseDao.getAvailable();
	}
}
