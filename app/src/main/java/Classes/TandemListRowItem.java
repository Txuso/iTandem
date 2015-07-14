package Classes;

import android.graphics.drawable.Drawable;

/**
 * Created by josurubio on 08/04/15.
 * Class to manage tandem list items
 */
public class TandemListRowItem {

    private Drawable imageId;
    private String nameAge;
    private String knownLang;
    private String speakLang;
    private String id;

    public TandemListRowItem(Drawable imageId, String nameAge, String knownLang, String speakLang, String id) {
        this.imageId = imageId;
        this.nameAge = nameAge;
        this.knownLang = knownLang;
        this.speakLang = speakLang;
        this.id = id;
    }
    public Drawable getImageId() {
        return imageId;
    }
    public void setImageId(Drawable imageId) {
        this.imageId = imageId;
    }
    public String getknownLang() {
        return knownLang;
    }
    public void setknownLang(String knownLang) {
        this.knownLang = knownLang;
    }
    public String getnameAge() {
        return nameAge;
    }
    public void setnameAge(String nameAge) {
        this.nameAge = nameAge;
    }
    @Override
    public String toString() {
        return nameAge + "\n" + knownLang;
    }

    public void setSpeakLang(String speakLang) {
        this.speakLang = speakLang;
    }

    public String getSpeakLang() {
        return speakLang;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
