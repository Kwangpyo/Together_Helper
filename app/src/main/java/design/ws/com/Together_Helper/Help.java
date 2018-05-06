package design.ws.com.Together_Helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Help implements Serializable {

    Helpee helpee;
    Integer helpeeId;
    double lon;
    double lat;
    int hour;
    int minute;
    int duration;
    int year;
    int month;
    int day;
    String type;
    String match_status;
    String start_status;
    String content;

    public Help(String type) {
        this.type = type;
    }

    public Help(Integer helpeeId, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, String match_status, String start_status, String content) {
        this.helpeeId = helpeeId;
        this.lon = lon;
        this.lat = lat;
        this.hour = hour;
        this.minute = minute;
        this.duration = duration;
        this.year = year;
        this.month = month;
        this.day = day;
        this.type = type;
        this.match_status = match_status;
        this.start_status = start_status;
        this.content = content;
    }


    public Integer getHelpeeId() {
        return helpeeId;
    }

    public void setHelpeeId(Integer helpeeId) {
        this.helpeeId = helpeeId;
    }

    public Helpee getHelpee() {
        return helpee;
    }

    public void setHelpee(Helpee helpee) {
        this.helpee = helpee;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMatch_status() {
        return match_status;
    }

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
    }

    public String getStart_status() {
        return start_status;
    }

    public void setStart_status(String start_status) {
        this.start_status = start_status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
