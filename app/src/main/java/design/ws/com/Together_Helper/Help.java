package design.ws.com.Together_Helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Help implements Serializable {

    String title;
    String detail;
    Helpee helpee;
    String location;
    String time;
    String match_status;
    String start_status;
    String content;

    public Help(String title, String detail, Helpee helpee, String location, String time, String match_status, String start_status,String content) {
        this.title = title;
        this.detail = detail;
        this.helpee = helpee;
        this.location = location;
        this.time = time;
        this.match_status = match_status;
        this.start_status = start_status;
        this.content = content;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public Helpee getHelpee() {
        return helpee;
    }

    public void setHelpee(Helpee helpee) {
        this.helpee = helpee;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
