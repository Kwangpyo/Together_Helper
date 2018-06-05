package design.ws.com.Together_Helper.params;

import java.io.Serializable;

public class ReserveParam implements Serializable {

    private double lat;
    private double lon;
    private String count;

    public ReserveParam(double lat, double lon, String count) {
        this.lat = lat;
        this.lon = lon;
        this.count = count;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
