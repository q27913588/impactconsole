package biz.mercue.impactweb.model;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "tag")
public class Tag extends BaseBean {

	@Id
	@JsonView({View.Tag.class, View.News.class, View.SuccessCase.class})
	private String tag_id;

	@JsonView({View.Tag.class, View.News.class, View.SuccessCase.class})
	private String tag_name;

	@JsonView({View.Tag.class, View.News.class, View.SuccessCase.class})
	@OrderBy
	private String tag_order;

	@JsonView({View.Tag.class, View.News.class, View.SuccessCase.class })
	private String tag_from;

	public String getTag_id() {
		return tag_id;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

	public String getTag_name() {
		return tag_name;
	}

	public void setTag_name(String tag_name) {
		this.tag_name = tag_name;
	}

	public String getTag_order() {
		return tag_order;
	}

	public void setTag_order(String tag_order) {
		this.tag_order = tag_order;
	}

	public String getTag_from() {
		return tag_from;
	}

	public void setTag_from(String tag_from) {
		this.tag_from = tag_from;
	}
}
