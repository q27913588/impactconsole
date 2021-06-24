package biz.mercue.impactweb.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "rss")
public class Rss extends BaseBean {

	@Id
	@JsonView({ View.Rss.class })
	private String rss_id;

	@JsonView({ View.Rss.class })
	private String rss_title;

	@JsonView({ View.Rss.class })
	private String rss_url;
	
	@JsonView({ View.Rss.class })
	private boolean available;

	@JsonView({ View.Rss.class })
	private Date create_date;
	
	public String getRss_id() {
		return rss_id;
	}

	public void setRss_id(String rss_id) {
		this.rss_id = rss_id;
	}

	public String getRss_title() {
		return rss_title;
	}

	public void setRss_title(String rss_title) {
		this.rss_title = rss_title;
	}

	public String getRss_url() {
		return rss_url;
	}

	public void setRss_url(String rss_url) {
		this.rss_url = rss_url;
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
}
