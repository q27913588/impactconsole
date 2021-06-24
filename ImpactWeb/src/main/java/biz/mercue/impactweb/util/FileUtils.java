package biz.mercue.impactweb.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;





public class FileUtils {
	
	private static Logger log = Logger.getLogger(FileUtils.class.getName());
	
	
	public static final File MultipartFile2File(MultipartFile mpFile, String fileName) throws IOException {
		log.info("MultipartFile2File");
		log.info("Constants.FILE_UPLOAD_PATH :"+Constants.FILE_UPLOAD_PATH);
		File convFile = null;
		InputStream inputStream = null;

		try {
			CommonsMultipartFile cFile = (CommonsMultipartFile) mpFile;
			DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
			inputStream = fileItem.getInputStream();
			String orginalFileName = mpFile.getOriginalFilename();
			log.info("orginalFileName:"+orginalFileName);
			if(StringUtils.isNULL(fileName)) {
				fileName = orginalFileName;
			}
			log.info("fileName:"+fileName);
			//String extensionName = FilenameUtils.getExtension(orginalFileName);
			String finalFileName = Constants.FILE_UPLOAD_PATH + File.separator + fileName ;
			log.info("finalFileName:"+finalFileName);
			convFile = new File(finalFileName);
			mpFile.transferTo(convFile);
			
		} catch (Exception e) {
			log.error("Exception:" + e.getMessage());
		} finally {
			if(inputStream != null) {
				inputStream.close();
			}
		}

		return convFile;
	}
	
	public static final String readHtml(String filePath) {
		StringBuilder contentBuilder = new StringBuilder();
		try {
		    BufferedReader in = new BufferedReader(new FileReader(filePath));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str);
		    }
		    in.close();
		} catch (IOException e) {
			log.error("read file error");	
			return null;
		}
		
		return  contentBuilder.toString();
	}
	

}
