package biz.mercue.impactweb.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.model.CSVTask;
import biz.mercue.impactweb.model.ListQueryForm;




public interface CSVTaskService {

	

	CSVTask getById(String id);


	List<CSVTask> getByAdmin(String adminId);
	
	List<CSVTask> getNotFinishByAdmin(String adminId);
		

	ListQueryForm getCSVTaskList(int page);
	
	
	CSVTask addTaskByFile(File file, Admin admin)throws IOException;
}
