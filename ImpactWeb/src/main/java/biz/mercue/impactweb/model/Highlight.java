//package biz.mercue.impactweb.model;
//
//import com.fasterxml.jackson.annotation.JsonView;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "highlight")
//public class Highlight extends BaseBean {
//    @Id
//    @JsonView(View.Highlight.class)
//    private String highlight_id;
//
//    @JsonView(View.Highlight.class)
//    private String highlight_title;
//
//    @JsonView(View.Highlight.class)
//    private String highlight_subtitle;
//
//    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
//    @JoinColumn(name = "news_id")
//    private News news;
//
//    @OneToMany(mappedBy = "highlight", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonView(View.Highlight.class)
//    private List<ImageCollection> imageCollectionList;
//
//    public String getHighlight_id() {
//        return highlight_id;
//    }
//
//    public void setHighlight_id(String highlight_id) {
//        this.highlight_id = highlight_id;
//    }
//
//    public String getHighlight_title() {
//        return highlight_title;
//    }
//
//    public void setHighlight_title(String highlight_title) {
//        this.highlight_title = highlight_title;
//    }
//
//    public String getHighlight_subtitle() {
//        return highlight_subtitle;
//    }
//
//    public void setHighlight_subtitle(String highlight_subtitle) {
//        this.highlight_subtitle = highlight_subtitle;
//    }
//
//    public News getNews() {
//        return news;
//    }
//
//    public void setNews(News news) {
//        this.news = news;
//    }
//
//    public List<ImageCollection> getImageCollectionList() {
//        return imageCollectionList;
//    }
//
//    public void setImageCollectionList(List<ImageCollection> imageCollectionList) {
//        this.imageCollectionList = imageCollectionList;
//    }
//}
