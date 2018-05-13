package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GETMyHelpAPITask extends AsyncTask<String, Void, ArrayList<Help>> {


    @Override
    protected ArrayList<Help> doInBackground(String... params) {

        String id = params[0];

        GETMyHelpAPI client = new GETMyHelpAPI();

        ArrayList<Help> w = client.getJson(id);

        return w;
    }
}
