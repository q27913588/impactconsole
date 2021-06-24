package biz.mercue.impactweb.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
public class Category extends BaseBean {
    @Id
    @JsonView({View.Downloads.class, View.Category.class})
    private String category_id;

    @JsonView({View.Downloads.class, View.Category.class})
    private String category_name;

    @JsonView({View.Downloads.class, View.Category.class})
    private Integer category_order = 0;

    @JsonView({View.Category.class})
    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinTable(name = "downloads_category"
            , joinColumns = {@JoinColumn(name = "category_id")}
            , inverseJoinColumns = {@JoinColumn(name = "downloads_id")})
    private List<Downloads> listDownloads;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getCategory_order() {
        return category_order;
    }

    public void setCategory_order(Integer category_order) {
        this.category_order = category_order;
    }

    public List<Downloads> getListDownloads() {
        return listDownloads;
    }

    public void setListDownloads(List<Downloads> listDownloads) {
        this.listDownloads = listDownloads;
    }
}
