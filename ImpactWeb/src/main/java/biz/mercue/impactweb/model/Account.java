package biz.mercue.impactweb.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.opencsv.bean.CsvBindByPosition;


@Entity
@Table(name="account")
public class Account extends BaseBean {
	
	
	@Id
	@CsvBindByPosition(position = 0)
	@JsonView({View.Account.class})
	private String account_id;
	

	@JsonView({View.Account.class})
	private String account_tuya_id;
	

	@JsonView({View.Account.class})
	private String account_name;
	
	@CsvBindByPosition(position = 1)
	@JsonView({View.Account.class})
	private String account_email;
	
	
	@JsonView({View.Account.class})
	private String account_country_code;
	
//	@JsonView({View.Account.class})
//	private String account_mobile;
	
	@CsvBindByPosition(position = 2)
	private String account_password;
	
	@Transient
	private String re_account_password;
	
	
	@CsvBindByPosition(position = 3)
	@JsonView({View.Account.class})
	@Transient
	private String operation;
	
	@JsonView({View.Account.class})
	private boolean available;
	
	
	@Transient
	@CsvBindByPosition(position = 4)
	@JsonView({View.Account.class})
	private String  result_code;
	
	@Transient
	@CsvBindByPosition(position = 5)
	@JsonView({View.Account.class})
	private String  result;
	
	private Date create_date;
	
	private Date update_date;
	
	
	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_email() {
		return account_email;
	}

	public void setAccount_email(String account_email) {
		this.account_email = account_email;
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

	public String getAccount_password() {
		return account_password;
	}

	public void setAccount_password(String account_password) {
		this.account_password = account_password;
	}

	public String getAccount_country_code() {
		return account_country_code;
	}

	public void setAccount_country_code(String account_country_code) {
		this.account_country_code = account_country_code;
	}
//
//	public String getAccount_mobile() {
//		return account_mobile;
//	}
//
//	public void setAccount_mobile(String account_mobile) {
//		this.account_mobile = account_mobile;
//	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}


	public String getAccount_tuya_id() {
		return account_tuya_id;
	}

	public void setAccount_tuya_id(String account_tuya_id) {
		this.account_tuya_id = account_tuya_id;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getRe_account_password() {
		return re_account_password;
	}

	public void setRe_account_password(String re_account_password) {
		this.re_account_password = re_account_password;
	}

	

}
