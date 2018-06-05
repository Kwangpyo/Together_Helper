package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.params.ReserveParam;

public class GETReservationCheckAPITask extends AsyncTask<String, Void, ReserveParam> {


    @Override
    protected ReserveParam doInBackground(String... str) {

        GETReservationCheckAPI client = new GETReservationCheckAPI();

        ReserveParam w = client.getJson(str[0]);

        return w;
    }
}
