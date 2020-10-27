package entityUtil;

import java.io.Serializable;

/**
 * @author songbin
 * @date 2020/10/12
 */
public class BlogsForm implements Serializable {

    private String title;
    private int typeId;
    private boolean recommend;

    public BlogsForm(String title, int typeId, boolean recommend) {
        this.title = title;
        this.typeId = typeId;
        this.recommend = recommend;
    }

    @Override
    public String toString() {
        return "BlogsForm{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                ", recommend=" + recommend +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}
