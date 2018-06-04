package design.ws.com.Together_Helper.domain;

import java.io.Serializable;

public class Help implements Serializable {

    Helpee helpee;
    Helper helper;
    String helpeeId;
    String helperId;
    double lon;
    double lat;
    int hour;
    int minute;
    int duration;
    int year;
    int month;
    int day;
    String type;
    int match_status;
    int start_status;
    String content;
    int helpId;
    String accept_status;

    public Help(String type) {
        this.type = type;
    }

    public Help(String helpeeId, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, int match_status, int start_status, String content) {
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

    public Help(String helpeeId, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, int match_status, int start_status, String content, int helpId,String helperid) {
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
        this.helpId = helpId;
        this.helperId = helperid;
    }

    public Help(String helpeeId, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, int match_status, int start_status, String content, int helpId,String helperid, String accept_status) {
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
        this.helpId = helpId;
        this.helperId = helperid;
        this.accept_status = accept_status;
    }

    public String getHelperId() {
        return helperId;
    }

    public void setHelperId(String helperId) {
        this.helperId = helperId;
    }

    public int getHelpId() {
        return helpId;
    }

    public void setHelpId(int helpId) {
        this.helpId = helpId;
    }

    public String getHelpeeId() {
        return helpeeId;
    }

    public void setHelpeeId(String helpeeId) {
        this.helpeeId = helpeeId;
    }

    public Helpee getHelpee() {
        return helpee;
    }

    public void setHelpee(Helpee helpee) {
        this.helpee = helpee;
    }

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
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

    public int getMatch_status() {
        return match_status;
    }

    public void setMatch_status(int match_status) {
        this.match_status = match_status;
    }

    public int getStart_status() {
        return start_status;
    }

    public void setStart_status(int start_status) {
        this.start_status = start_status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAccept_status() {
        return accept_status;
    }

    public void setAccept_status(String accept_status) {
        this.accept_status = accept_status;
    }
}
