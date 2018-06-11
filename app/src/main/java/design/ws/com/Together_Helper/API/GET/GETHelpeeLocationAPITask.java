package design.ws.com.Together_Helper.API.GET;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Helper;

public class GETHelpeeLocationAPITask extends AsyncTask<String, Void, LatLng> {

    @Override
    protected LatLng doInBackground(String... params) {

        String id = params[0];

        GETHelpeeLocationAPI client  = new GETHelpeeLocationAPI();

        LatLng w = client.getJson(id);

        return w;
    }
}
