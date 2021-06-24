package biz.mercue.impactweb.model;

import biz.mercue.impactweb.util.Constants;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "news")
public class News extends BaseBean {
    @Id
    @JsonView({View.News.class, View.NewsFrontEnd.class, View.NewsBackEnd.class})
    private String news_id;

    @JsonView({View.News.class, View.NewsFrontEnd.class, View.NewsBackEnd.class})
    private String news_title;

    @JsonView({View.News.class})
    private String news_subtitle;

    @JsonView({View.News.class})
    private String news_content;

    @JsonView({View.News.class})
    private String news_image_file;

    @JsonView({View.News.class})
    private String news_iframe;

    @JsonView({View.News.class})
    private String news_custom_tag; // 使用者自定義tag

    @JsonView({View.News.class, View.NewsBackEnd.class})
    private boolean available;

    @JsonView({View.News.class})
    private Date create_date;

    @JsonView({View.News.class})
    private Date update_date;

    @JsonView({View.News.class, View.NewsFrontEnd.class})
    private Date published_date;

    @JsonView({View.News.class})
    private Integer news_language;

    @JsonView({View.News.class, View.NewsFrontEnd.class})
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "news_tag", joinColumns = {@JoinColumn(name = "news_id")}, inverseJoinColumns = {
            @JoinColumn(name = "tag_id")})
    private List<Tag> listTag;

    @JsonView({View.News.class})
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attachment> listAttachment;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonView(View.News.class)
    private List<ImageCollection> listImageCollection;

    @JsonView({View.News.class, View.NewsFrontEnd.class, View.NewsBackEnd.class})
    @Transient
    private String f_news_image_url;

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_subtitle() {
        return news_subtitle;
    }

    public void setNews_subtitle(String news_subtitle) {
        this.news_subtitle = news_subtitle;
    }

    public String getNews_content() {
        return news_content;
    }

    public void setNews_content(String news_content) {
        this.news_content = news_content;
    }

    public String getNews_iframe() {
        return news_iframe;
    }

    public void setNews_iframe(String news_iframe) {
        this.news_iframe = news_iframe;
    }

    public String getNews_image_file() {
        return news_image_file;
    }

    public void setNews_image_file(String news_image_file) {
        this.news_image_file = news_image_file;
    }

    public String getNews_custom_tag() {
        return news_custom_tag;
    }

    public void setNews_custom_tag(String news_custom_tag) {
        this.news_custom_tag = news_custom_tag;
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

    public Integer getNews_language() {
        return news_language;
    }

    public void setNews_language(Integer news_language) {
        this.news_language = news_language;
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

    public List<ImageCollection> getListImageCollection() {
        if (listImageCollection == null) {
            listImageCollection = new ArrayList<>();
        }
        return listImageCollection;
    }

    public void setListImageCollection(List<ImageCollection> listImageCollection) {
        this.listImageCollection = listImageCollection;
    }

    public void setF_news_image_url(String f_news_image_url) {
        this.f_news_image_url = f_news_image_url;
    }

    public String getF_news_image_url() {
        return Constants.IMAGE_LOAD_URL + this.news_image_file;
    }
}
