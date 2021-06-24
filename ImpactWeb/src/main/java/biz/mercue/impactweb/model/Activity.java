package biz.mercue.impactweb.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.impactweb.util.Constants;

@Entity
@Table(name = "activity")
public class Activity extends BaseBean {

	@Id
	@JsonView({ View.Activity.class })
	private String activity_id;

	@JsonView({ View.Activity.class })
	private String activity_title;

	@JsonView({ View.Activity.class })
	private Date create_date;

	@JsonView({ View.Activity.class })
	private boolean available;
	
	@JsonView({ View.Activity.class })
	private String activity_image_file;

	@JsonView(View.Activity.class)
	@Transient
	private String f_activity_image_url;
	
	// Getter and Setter
	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		this.activity_id = activity_id;
	}

	public String getActivity_title() {
		return activity_title;
	}

	public void setActivity_title(String activity_title) {
		this.activity_title = activity_title;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getActivity_image_file() {
		return activity_image_file;
	}

	public void setActivity_image_file(String activity_image_file) {
		this.activity_image_file = activity_image_file;
	}
	
	public String getF_activity_image_url() {
		return Constants.IMAGE_LOAD_URL + this.activity_image_file;
	}
}
