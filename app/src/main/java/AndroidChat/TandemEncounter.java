package AndroidChat;

import java.sql.Date;

/**
 * Created by josurubio on 15/04/15.
 */
public class TandemEncounter {

    private String author;
    private String receptor;
    private String date;
    private String receptorName;
    private boolean created;

    public TandemEncounter (String author, String receptor, String receptorName, String date, boolean created){
        this.author = author;
        this.receptor = receptor;
        this.receptorName = receptorName;
        this.date = date;
        this.created = created;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getAuthor() {
        return author;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean getCreated() {
        return created;
    }

    public void setReceptorName(String receptorName) {
        this.receptorName = receptorName;
    }

    public String getReceptorName() {
        return receptorName;
    }
}

