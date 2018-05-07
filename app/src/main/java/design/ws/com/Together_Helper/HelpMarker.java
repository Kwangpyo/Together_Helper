package design.ws.com.Together_Helper;

import com.google.android.gms.maps.model.Marker;

public class HelpMarker {

    Marker marker;
    Help help;

    public HelpMarker(Marker marker, Help help) {
        this.marker = marker;
        this.help = help;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Help getHelp() {
        return help;
    }

    public void setHelp(Help help) {
        this.help = help;
    }
}
