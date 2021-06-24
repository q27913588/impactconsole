package biz.mercue.impactweb.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.Image;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.util.BeanResponseBody;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.ImageUtils;
import biz.mercue.impactweb.util.JWTUtils;
import biz.mercue.impactweb.util.KeyGeneratorUtils;

@Controller
public class ImageController {
	private Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	AdminTokenService adminTokenService;
	
	@RequestMapping(value = "/api/ckeditoruploadimage", method = RequestMethod.POST, produces = Constants.CONTENT_TYPE_JSON, consumes = {
			"multipart/mixed", "multipart/form-data" })
	@ResponseBody
	public String CKEditorUploadImage(HttpServletRequest request, @RequestPart("file") MultipartFile[] files) {
		BeanResponseBody responseBody = new BeanResponseBody();
		AdminToken token = adminTokenService.getById(JWTUtils.getJwtToken(request));
		Image image = new Image();
		
		int taskResult = -1;
		if (token != null) {
			log.info("files.length :" + files.length);
			for (int fileIndex = 0; fileIndex < files.length; fileIndex++) {

				MultipartFile file = files[fileIndex];
				if (file != null && !file.getOriginalFilename().isEmpty()) {
					String imageName = file.getOriginalFilename();
					String extendName = FilenameUtils.getExtension(imageName);
					String imageRename = KeyGeneratorUtils.generateRandomString();
					String finalFileName = imageRename + "." + extendName;

					File finalFile = new File(Constants.IMAGE_UPLOAD_PATH + File.separator + finalFileName);

					if (ImageUtils.writeFile(file, finalFile)) {
						image.setImage_file(finalFileName);
						taskResult = Constants.INT_SUCCESS;
					}
				}

				if (taskResult == Constants.INT_SUCCESS) {
					responseBody.setCode(Constants.INT_SUCCESS);
				} else {
					responseBody.setCode(Constants.INT_SYSTEM_PROBLEM);
				}
			}
		} else {
			responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
		}
			
		responseBody.setBean(image);
		return responseBody.getJacksonString(View.CkEditorUploadImage.class);
	}
}
