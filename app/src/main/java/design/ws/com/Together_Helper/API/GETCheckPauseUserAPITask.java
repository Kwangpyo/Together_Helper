package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Help;

public class GETCheckPauseUserAPITask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... str) {

        GETCheckPauseUserAPI client = new GETCheckPauseUserAPI();

        String w = client.getJson(str[0]);

        return w;
    }
}

