package biz.mercue.impactweb.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.impactweb.util.Constants;

@Entity
@Table(name = "partner")
public class Partner extends BaseBean {

	@Id
	@JsonView({ View.Partner.class })
	private String partner_id;

	@JsonView({ View.Partner.class })
	private String partner_name;

	@JsonView({ View.Partner.class })
	private String partner_url;

	@JsonView({ View.Partner.class })
	private String partner_image_file;

	@JsonView(View.Partner.class)
	@Transient
	private String f_partner_image_url;
	
	@JsonView({ View.Partner.class })
	private int partner_order;

	@JsonView({ View.Partner.class })
	private boolean available;

	
	private Date create_date;

	private Date update_date;

	// Getters and Setters
	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public String getPartner_url() {
		return partner_url;
	}

	public void setPartner_url(String partner_url) {
		this.partner_url = partner_url;
	}

	public String getPartner_image_file() {
		return partner_image_file;
	}

	public void setPartner_image_file(String partner_image_file) {
		this.partner_image_file = partner_image_file;
	}

	public int getPartner_order() {
		return partner_order;
	}

	public void setPartner_order(int partner_order) {
		this.partner_order = partner_order;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
	public String getF_partner_image_url() {
		return Constants.IMAGE_LOAD_URL + this.partner_image_file;
	}
}
