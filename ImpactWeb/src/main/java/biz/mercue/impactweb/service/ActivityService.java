package biz.mercue.impactweb.service;

import java.util.List;

import biz.mercue.impactweb.model.ListQueryForm;
import biz.mercue.impactweb.model.Activity;

public interface ActivityService {
	public Activity getById(String id);

	public int createActivity(Activity news);
	
	public int updateActivity(Activity news);
	
	public int updateActivityList(List<Activity> list);
	
	public int deleteActivity(Activity news);

	public List<Activity> getAll();

	public List<Activity> getAvailable();

	public ListQueryForm getActivityList(int page);
}
