package bogdan_mihalchenko.words.model.pojo;

import java.io.Serializable;

/**
 * Created by Shipohvost on 27.09.2016.
 */

public class Word implements Serializable {

    private long id;
    private String wordOrign;
    private String wordTranslate;
    private String wordTrancription;
    private boolean wordStatus;
    private int wordCategory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWordOrign() {
        return wordOrign;
    }

    public void setWordOrign(String wordOrign) {
        this.wordOrign = wordOrign;
    }

    public String getWordTranslate() {
        return wordTranslate;
    }

    public void setWordTranslate(String wordTranslate) {
        this.wordTranslate = wordTranslate;
    }

    public String getWordTrancription() {
        return wordTrancription;
    }

    public void setWordTrancription(String wordTrancription) {
        this.wordTrancription = wordTrancription;
    }

    public boolean isWordStatus() {
        return wordStatus;
    }

    public void setWordStatus(boolean wordStatus) {
        this.wordStatus = wordStatus;
    }

    public int getWordCategory() {
        return wordCategory;
    }

    public void setWordCategory(int wordCategory) {
        this.wordCategory = wordCategory;
    }
}
