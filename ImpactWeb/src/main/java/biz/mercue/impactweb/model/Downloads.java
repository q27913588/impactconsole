package biz.mercue.impactweb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "downloads")
public class Downloads extends BaseBean {

	@Id
	@JsonView({ View.Downloads.class })
	private String downloads_id;

	@JsonView({ View.Downloads.class })
	private String downloads_title;

	@JsonView({ View.Downloads.class })
	private String downloads_url;
	
	@JsonView({ View.Downloads.class })
	private int downloads_order;

	@JsonView({ View.Downloads.class })
	private boolean available;

	@JsonView({ View.Downloads.class })
	@Column(updatable = false)
	private Date create_date;

	@JsonView({ View.Downloads.class })
	private Date published_date;

	@JsonView({ View.Downloads.class })
	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name ="downloads_category",
		joinColumns = {@JoinColumn(name="downloads_id")},
		inverseJoinColumns = {@JoinColumn(name="category_id")})
	private Category category;

	public String Downloads() {
		return downloads_id;
	}

	public String getDownloads_id() {
		return downloads_id;
	}

	public void setDownloads_id(String downloads_id) {
		this.downloads_id = downloads_id;
	}

	public String getDownloads_title() {
		return downloads_title;
	}

	public void setDownloads_title(String downloads_title) {
		this.downloads_title = downloads_title;
	}

	public String getDownloads_url() {
		return downloads_url;
	}

	public void setDownloads_url(String downloads_url) {
		this.downloads_url = downloads_url;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getDownloads_order() {
		return downloads_order;
	}

	public void setDownloads_order(int downloads_order) {
		this.downloads_order = downloads_order;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Date getPublished_date() {
		return published_date;
	}

	public void setPublished_date(Date published_date) {
		this.published_date = published_date;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
