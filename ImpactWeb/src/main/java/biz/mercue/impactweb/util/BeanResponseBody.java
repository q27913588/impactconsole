package biz.mercue.impactweb.util;

import biz.mercue.impactweb.model.BaseBean;
import biz.mercue.impactweb.model.View;
import com.fasterxml.jackson.annotation.JsonView;

public class BeanResponseBody extends ResponseBody {
    @JsonView(View.Public.class)
    BaseBean data;

    public BaseBean getBean() {
        return data;
    }

    public void setBean(BaseBean data) {
        this.data = data;
    }
}
