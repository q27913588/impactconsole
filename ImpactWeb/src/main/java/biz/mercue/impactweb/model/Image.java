package biz.mercue.impactweb.model;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.impactweb.util.Constants;

public class Image extends BaseBean {
	@JsonView({ View.Image.class })
	private String image_file;

	@JsonView({ View.Image.class, View.CkEditorUploadImage.class })
	private String image_url;
	
	public String getImage_file() {
		return image_file;
	}

	public void setImage_file(String image_file) {
		this.image_file = image_file;
	}

	public String getImage_url() {
		return Constants.IMAGE_LOAD_URL + this.image_file;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}
