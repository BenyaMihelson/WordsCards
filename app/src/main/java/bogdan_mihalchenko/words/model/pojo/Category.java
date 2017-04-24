package bogdan_mihalchenko.words.model.pojo;

import java.io.Serializable;

/**
 * Created by Shipohvost on 27.09.2016.
 */

public class Category implements Serializable{

    private long id;
    private String categoryTitle;
    private boolean isComplete;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        this.isComplete = complete;
    }
}
