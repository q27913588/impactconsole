package biz.mercue.impactweb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;

import biz.mercue.impactweb.util.Constants;

@Entity
@Table(name = "success_case")
public class SuccessCase extends BaseBean {

	@Id
	@JsonView({ View.SuccessCase.class })
	private String success_case_id;

	@JsonView({ View.SuccessCase.class })
	private String success_case_title;

	@JsonView({ View.SuccessCase.class })
	private String success_case_subtitle;

	@JsonView({ View.SuccessCase.class })
	private String success_case_content;

	@JsonView({ View.SuccessCase.class })
	private String success_case_image_file;

	@JsonView({ View.SuccessCase.class })
	private String success_case_iframe;

	@JsonView({ View.SuccessCase.class })
	private boolean available;

	@JsonView({ View.SuccessCase.class })
	private Date create_date;

	@JsonView({ View.SuccessCase.class })
	private Date update_date;

	@JsonView({ View.SuccessCase.class })
	private Date published_date;

	@JsonView({ View.SuccessCase.class })
	private Integer success_case_language;

	@JsonView({ View.SuccessCase.class })
	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(name = "success_case_tag", joinColumns = {
			@JoinColumn(name = "success_case_id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id") })
	private List<Tag> listTag;

	@JsonView({ View.SuccessCase.class })
	@OneToMany(mappedBy = "success_case", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Attachment> listAttachment;

	@JsonView(View.SuccessCase.class)
	@Transient
	private String f_success_case_image_url;

	// Getters and Setters
	public String getSuccess_case_id() {
		return success_case_id;
	}

	public void setSuccess_case_id(String success_case_id) {
		this.success_case_id = success_case_id;
	}

	public String getSuccess_case_title() {
		return success_case_title;
	}

	public void setSuccess_case_title(String success_case_title) {
		this.success_case_title = success_case_title;
	}

	public String getSuccess_case_subtitle() {
		return success_case_subtitle;
	}

	public void setSuccess_case_subtitle(String success_case_subtitle) {
		this.success_case_subtitle = success_case_subtitle;
	}

	public String getSuccess_case_content() {
		return success_case_content;
	}

	public void setSuccess_case_content(String success_case_content) {
		this.success_case_content = success_case_content;
	}

	public String getSuccess_case_image_file() {
		return success_case_image_file;
	}

	public void setSuccess_case_image_file(String success_case_image_file) {
		this.success_case_image_file = success_case_image_file;
	}

	public String getSuccess_case_iframe() {
		return success_case_iframe;
	}

	public void setSuccess_case_iframe(String success_case_iframe) {
		this.success_case_iframe = success_case_iframe;
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

	public Date getPublished_date() {
		return published_date;
	}

	public void setPublished_date(Date published_date) {
		this.published_date = published_date;
	}

	public Integer getSuccess_case_language() {
		return success_case_language;
	}

	public void setSuccess_case_language(Integer success_case_language) {
		this.success_case_language = success_case_language;
	}

	public List<Tag> getListTag() {
		return listTag;
	}

	public void setListTag(List<Tag> listTag) {
		this.listTag = listTag;
	}

	public List<Attachment> getListAttachment() {
		return listAttachment;
	}

	public void setListAttachment(List<Attachment> listAttachment) {
		this.listAttachment = listAttachment;
	}

	public String getF_success_case_image_url() {
		return Constants.IMAGE_LOAD_URL + this.success_case_image_file;
	}
}
