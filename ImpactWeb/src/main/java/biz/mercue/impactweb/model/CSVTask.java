package biz.mercue.impactweb.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name="csv_task")
public class CSVTask extends BaseBean {
	
	@Id
	@JsonView(View.CSVTask.class)
	private String csv_task_id;
	
	
	@JsonView(View.CSVTask.class)
	private String task_file_name;
	
	@OneToOne
	@JoinColumn(name="admin_id")
	@JsonView(View.CSVTask.class)
	private Admin admin;
	
	@JsonView(View.CSVTask.class)
	private Date create_date;
	
	@JsonView(View.CSVTask.class)
	private boolean is_finish;
	
	@JsonView(View.CSVTask.class)
	private int total_num;
	
	@JsonView(View.CSVTask.class)
	private int success_num;
	
	@JsonView(View.CSVTask.class)
	private int fail_num;

	public String getCSV_task_id() {
		return csv_task_id;
	}

	public void setCSV_task_id(String csv_task_id) {
		this.csv_task_id = csv_task_id;
	}

	public String getTask_file_name() {
		return task_file_name;
	}

	public void setTask_file_name(String task_file_name) {
		this.task_file_name = task_file_name;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public boolean isIs_finish() {
		return is_finish;
	}

	public void setIs_finish(boolean is_finish) {
		this.is_finish = is_finish;
	}
	
	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(int success_num) {
		this.success_num = success_num;
	}

	public int getFail_num() {
		return fail_num;
	}

	public void setFail_num(int fail_num) {
		this.fail_num = fail_num;
	}

}
