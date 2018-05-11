package design.ws.com.Together_Helper;

import java.io.Serializable;

public class Helper implements Serializable {

    String name;
    int feedback;
    String id;
    String password;
    String phone_number;
    String token;


    public Helper(String name, int feedback, String id, String password, String phone_number, String token) {
        this.name = name;
        this.feedback = feedback;
        this.id = id;
        this.password = password;
        this.phone_number = phone_number;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFeedback() {
        return feedback;
    }

    public void setFeedback(int feedback) {
        this.feedback = feedback;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
