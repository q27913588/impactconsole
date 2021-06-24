package biz.mercue.impactweb.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import biz.mercue.impactweb.dao.ActivityDao;
import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Activity;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Service("activityService")
@Transactional
public class ActivityServiceImpl implements ActivityService {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ActivityDao activityDao;

	@Override
	public Activity getById(String id) {
		Activity Activity = activityDao.getById(id);
		return Activity;
	}

	@Override
	public ListQueryForm getActivityList(int page) {
		int cout = activityDao.getActivityCount();
		List list = activityDao.getActivityList(page, Constants.SYSTEM_PAGE_SIZE);
		ListQueryForm form = new ListQueryForm(cout, Constants.SYSTEM_PAGE_SIZE, list);
		return form;
	}

	@Override
	public int createActivity(Activity activity) {
		try {
			activity.setActivity_id(KeyGeneratorUtils.generateRandomString());
			activity.setAvailable(true);
			activity.setCreate_date(new Date());
			activityDao.createActivity(activity);
			return Constants.INT_SUCCESS;
		} catch (Exception e) {
			log.error(e.getMessage());
			return Constants.INT_SYSTEM_PROBLEM;
		}
	}

	@Override
	public int updateActivity(Activity activity) {
		Activity dbBean = activityDao.getById(activity.getActivity_id());
		try {
			if (dbBean != null) {
				dbBean.setActivity_title(activity.getActivity_title());
				dbBean.setAvailable(activity.isAvailable());
				dbBean.setActivity_image_file(activity.getActivity_image_file());
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
	public int updateActivityList(List<Activity> list) {
		int resultCode = 0;
		for (Activity activity : list) {
			resultCode = updateActivity(activity);
			log.info(resultCode);
		}
		return 0;
	}

	@Override
	public int deleteActivity(Activity Activity) {
		Activity dbBean = activityDao.getById(Activity.getActivity_id());
		if (dbBean != null) {
			activityDao.deleteActivity(dbBean);
			return Constants.INT_SUCCESS;
		} else {
			return Constants.INT_CANNOT_FIND_DATA;
		}
	}

	@Override
	public List<Activity> getAll() {
		return activityDao.getAll();
	}

	@Override
	public List<Activity> getAvailable() {
		return activityDao.getAvailable();
	}
}
