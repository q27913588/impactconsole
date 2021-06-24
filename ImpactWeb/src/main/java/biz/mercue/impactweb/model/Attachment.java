package biz.mercue.impactweb.model;

import javax.naming.Name;
import javax.persistence.*;

import biz.mercue.impactweb.util.Constants;
import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "attachment")
public class Attachment extends BaseBean {
	@Id
	@JsonView({ View.Tag.class, View.News.class })
	private String attachment_id;

	@JsonView({ View.Tag.class, View.News.class })
	private String attachment_title;
	
	@JsonView({ View.Tag.class, View.News.class })
	private String attachment_url;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "news_id")
	private News news;

	@ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "success_case_id")
	private SuccessCase success_case;

	public String getAttachment_id() {
		return attachment_id;
	}

	public void setAttachment_id(String attachment_id) {
		this.attachment_id = attachment_id;
	}

	public String getAttachment_title() {
		return attachment_title;
	}

	public void setAttachment_title(String attachment_title) {
		this.attachment_title = attachment_title;
	}

	public String getAttachment_url() {
		return attachment_url;
	}

	public void setAttachment_url(String attachment_url) {
		this.attachment_url = attachment_url;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public SuccessCase getSuccess_case() {
		return success_case;
	}

	public void setSuccess_case(SuccessCase success_case) {
		this.success_case = success_case;
	}
}
