package design.ws.com.Together_Helper;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Helpee implements Serializable {

    Bitmap image;
    String token;
    double lat;
    double lon;
    int feedback;
    String name;
    String id;
    String phonenumber;


    public Helpee(String token, double lat, double lon, int feedback, String id, String phonenumber) {
        this.token = token;
        this.lat = lat;
        this.lon = lon;
        this.feedback = feedback;
        this.id = id;
        this.phonenumber = phonenumber;
    }

    public Helpee(Bitmap image, String token, double lat, double lon, int feedback, String name, String id) {
        this.image = image;
        this.token = token;
        this.lat = lat;
        this.lon = lon;
        this.feedback = feedback;
        this.name = name;
        this.id = id;
    }

    public Helpee(String name, String id) {
        this.name = name;
        this.id = id;
    }


    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
