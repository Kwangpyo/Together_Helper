package design.ws.com.Together_Helper.params;

import java.io.Serializable;

public class ParamsForHistory implements Serializable {

    int volunteerid;
    int helperScore;
    String helperFeedbackContent;

    public ParamsForHistory(int volunteerid, int helperScore, String helperFeedbackContent) {
        this.volunteerid = volunteerid;
        this.helperScore = helperScore;
        this.helperFeedbackContent = helperFeedbackContent;
    }

    public int getVolunteerid() {
        return volunteerid;
    }

    public void setVolunteerid(int volunteerid) {
        this.volunteerid = volunteerid;
    }

    public int getHelperScore() {
        return helperScore;
    }

    public void setHelperScore(int helperScore) {
        this.helperScore = helperScore;
    }

    public String getHelperFeedbackContent() {
        return helperFeedbackContent;
    }

    public void setHelperFeedbackContent(String helperFeedbackContent) {
        this.helperFeedbackContent = helperFeedbackContent;
    }
}
