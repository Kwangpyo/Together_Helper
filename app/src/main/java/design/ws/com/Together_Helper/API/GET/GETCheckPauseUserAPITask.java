package design.ws.com.Together_Helper.API.GET;

import android.os.AsyncTask;

public class GETCheckPauseUserAPITask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... str) {

        GETCheckPauseUserAPI client = new GETCheckPauseUserAPI();

        String w = client.getJson(str[0]);

        return w;
    }
}

