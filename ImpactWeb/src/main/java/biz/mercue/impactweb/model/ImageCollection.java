package biz.mercue.impactweb.model;

import biz.mercue.impactweb.util.Constants;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;

@Entity
@Table(name = "image_collection")
public class ImageCollection extends BaseBean {
    @Id
    @JsonView(View.News.class)
    private String image_id;

    @JsonView(View.News.class)
    private String image_file;

    @JsonView(View.News.class)
    private Boolean is_main_image = false;

    @ManyToOne
    @JoinColumn(name = "news_id")
    private News news;

    @Transient
    @JsonView(View.News.class)
    private Boolean is_delete = false;

    @JsonView(View.News.class)
    @Transient
    private String f_image_image_url;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public Boolean getIs_main_image() {
        return is_main_image;
    }

    public void setIs_main_image(Boolean is_main_image) {
        this.is_main_image = is_main_image;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Boolean getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Boolean is_delete) {
        this.is_delete = is_delete;
    }

    public String getF_image_image_url() {
        return Constants.IMAGE_LOAD_URL + this.image_file;
    }
}
