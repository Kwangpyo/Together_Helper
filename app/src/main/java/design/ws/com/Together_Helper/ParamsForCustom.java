package design.ws.com.Together_Helper;

public class ParamsForCustom {

    int fromYear;
    int fromMonth;
    int fromDay;
    int fromHour;
    int fromMin;
    int toYear;
    int toMonth;
    int toDay;
    int toHour;
    int toMin;
    double latitude;
    double longitude;
    String volunteerType;


    public ParamsForCustom(int fromYear, int fromMonth, int fromDay, int fromHour, int fromMin, int toYear, int toMonth, int toDay, int toHour, int toMin, double latitude, double longitude, String volunteerType) {
        this.fromYear = fromYear;
        this.fromMonth = fromMonth;
        this.fromDay = fromDay;
        this.fromHour = fromHour;
        this.fromMin = fromMin;
        this.toYear = toYear;
        this.toMonth = toMonth;
        this.toDay = toDay;
        this.toHour = toHour;
        this.toMin = toMin;
        this.latitude = latitude;
        this.longitude = longitude;
        this.volunteerType = volunteerType;
    }

    public int getFromYear() {
        return fromYear;
    }

    public void setFromYear(int fromYear) {
        this.fromYear = fromYear;
    }

    public int getFromMonth() {
        return fromMonth;
    }

    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    public int getFromDay() {
        return fromDay;
    }

    public void setFromDay(int fromDay) {
        this.fromDay = fromDay;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public int getFromMin() {
        return fromMin;
    }

    public void setFromMin(int fromMin) {
        this.fromMin = fromMin;
    }

    public int getToYear() {
        return toYear;
    }

    public void setToYear(int toYear) {
        this.toYear = toYear;
    }

    public int getToMonth() {
        return toMonth;
    }

    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }

    public int getToDay() {
        return toDay;
    }

    public void setToDay(int toDay) {
        this.toDay = toDay;
    }

    public int getToHour() {
        return toHour;
    }

    public void setToHour(int toHour) {
        this.toHour = toHour;
    }

    public int getToMin() {
        return toMin;
    }

    public void setToMin(int toMin) {
        this.toMin = toMin;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getVolunteerType() {
        return volunteerType;
    }

    public void setVolunteerType(String volunteerType) {
        this.volunteerType = volunteerType;
    }
}
