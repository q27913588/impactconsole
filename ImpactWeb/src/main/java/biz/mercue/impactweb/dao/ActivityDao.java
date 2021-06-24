package biz.mercue.impactweb.dao;

import java.util.List;

import biz.mercue.impactweb.model.Activity;

public interface ActivityDao {
	Activity getById(String id);
	
	void createActivity(Activity bean);
	
	void deleteActivity(Activity bean);
	
	List<Activity> getActivityList(int page, int pageSize);
	
	List<Activity> getAll();
	
	List<Activity> getAvailable();
	
	int getActivityCount();
}
