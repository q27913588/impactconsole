package biz.mercue.impactweb.service;

import java.util.Date;
import java.io.File;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import biz.mercue.impactweb.model.Admin;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.DateUtils;

@Component
public class SyncTaskServiceImp implements SyncTaskService {
	private Logger log = Logger.getLogger(SyncTaskServiceImp.class);

	@Autowired
	CSVTaskService csvTaskService;

	// @Scheduled(cron = "0 30 19 * * *")
	@Scheduled(cron = "0 0 0 * * *")
	public void syncCSVFile() {
		log.info("syncCSVFile ");

		try {
			DirectoryScanner scanner = new DirectoryScanner();
			scanner.setIncludes(new String[] { "tuya_" + DateUtils.getSimpleDateFormatDate(new Date()) + "*.csv" });
			scanner.setBasedir(Constants.SYNC_FILE_UPLOAD_PATH);
			scanner.setCaseSensitive(false);
			scanner.scan();
			String[] files = scanner.getIncludedFiles();

			Admin admin = new Admin();
			admin.setAdmin_id("176ecd908775c1bdeb72ea87e56cffdd");

			for (String fileName : files) {
				log.info("fileName:" + fileName);
				File file = new File(Constants.SYNC_FILE_UPLOAD_PATH+ File.separator + fileName);
				csvTaskService.addTaskByFile(file, admin);
			}
		} catch (Exception e) {
			log.error("Exception:"+e.getMessage());
		}

	}


}
