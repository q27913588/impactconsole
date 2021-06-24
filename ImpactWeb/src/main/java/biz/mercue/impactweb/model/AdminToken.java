package biz.mercue.impactweb.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;



@Entity
@Table(name="admin_token")
public class AdminToken  extends BaseBean{
	
	@Id
	@JsonView(View.Public.class)
	private String admin_token_id;
	
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="admin_id")
	@JsonView(View.Public.class)
	private Admin admin;
	
	
	private Date login_date;
	
	@JsonView(View.Public.class)
	private Date expire_date;
	
	private Date create_date;
	
	private Date update_date;
	
	private boolean available = false;
	
	public String getAdmin_token_id() {
		return admin_token_id;
	}

	public void setAdmin_token_id(String admin_token_id) {
		this.admin_token_id = admin_token_id;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Date getLogin_date() {
		return login_date;
	}

	public void setLogin_date(Date login_date) {
		this.login_date = login_date;
	}

	public Date getExpire_date() {
		return expire_date;
	}

	public void setExpire_date(Date expire_date) {
		this.expire_date = expire_date;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
