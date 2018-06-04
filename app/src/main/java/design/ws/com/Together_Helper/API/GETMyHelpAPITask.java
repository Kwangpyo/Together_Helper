package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Help;

public class GETMyHelpAPITask extends AsyncTask<String, Void, ArrayList<Help>> {


    @Override
    protected ArrayList<Help> doInBackground(String... params) {

        String id = params[0];

        GETMyHelpAPI client = new GETMyHelpAPI();

        ArrayList<Help> w = client.getJson(id);

        return w;
    }
}
