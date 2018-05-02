package design.ws.com.Together_Helper;

import java.io.Serializable;

public class Helpee implements Serializable {

    String name;

    public Helpee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
