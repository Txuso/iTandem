package Classes;

import android.media.Image;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josurubio on 19/03/15.
 */
public class TandemUser {

    private String name;
    private int age;
    private ArrayList<String>langKnown;
    private ArrayList<String>langLearn;
    private String image;
    private String extract;
    private String longitude;
    private String latitude;
    private int distance;
    private String newMessageValue;

    public TandemUser(){
        name = "";
        age = 0;
        langKnown = new ArrayList<String>();
        langLearn = new ArrayList<String>();
        image = "";
        extract = "";
        longitude = "";
        latitude = "";
        distance = 50;
        this.newMessageValue = "";
    }

    public TandemUser (String name, int age,String image, ArrayList<String>langKnown, ArrayList<String>langLearn, String extract, String longitude, String latitude, int distance,String newMessageValue){

        this.name = name;
        this.image = image;
        this.langKnown = langKnown;
        this.langLearn = langLearn;
        this.age = age;
        this.extract = extract;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.newMessageValue = newMessageValue;

    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public void setImages(ArrayList<Image> images) {
        this.image = image;
    }

    public void setLangKnown(ArrayList<String> langKnown) {
        this.langKnown = langKnown;
    }

    public void setLangLearn(ArrayList<String> langLearn) {
        this.langLearn = langLearn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public ArrayList<String> getLangKnown() {
        return langKnown;
    }

    public ArrayList<String> getLangLearn() {
        return langLearn;
    }

    public String getExtract() {
        return extract;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public String getNewMessageValue() {
        return newMessageValue;
    }

    public void setNewMessageValue(String newMessageValue) {
        this.newMessageValue = newMessageValue;
    }
}
