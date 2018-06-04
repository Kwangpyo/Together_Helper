package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GETHistoryAPITask extends AsyncTask<String, Void, ArrayList<Help>> {


    @Override
    protected ArrayList<Help> doInBackground(String... params) {

        String id = params[0];

        GETHistoryAPI client = new GETHistoryAPI();

        ArrayList<Help> w = client.getJson(id);

        return w;
    }
}
