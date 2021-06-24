package biz.mercue.impactweb.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.impactweb.util.Constants;

@Entity
@Table(name="banner")
public class Banner extends BaseBean {
	@Id
	@JsonView({View.Banner.class})
	private String banner_id;
	
	@JsonView({ View.Banner.class })
	private String banner_image_file;
	
	@JsonView({View.Banner.class})
	private boolean available;
	
	@JsonView(View.Banner.class)
	@Transient
	private String f_banner_image_url;

	public String getBanner_id() {
		return banner_id;
	}

	public void setBanner_id(String banner_id) {
		this.banner_id = banner_id;
	}

	public String getBanner_image_file() {
		return banner_image_file;
	}

	public void setBanner_image_file(String banner_image_file) {
		this.banner_image_file = banner_image_file;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getF_banner_image_url() {
		return Constants.IMAGE_LOAD_URL + this.banner_image_file;
	}
}
