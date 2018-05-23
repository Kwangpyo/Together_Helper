package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GETPhotoURLAPITask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {

        String id = params[0];

        GETPhotoURLAPI client = new GETPhotoURLAPI();

        String w = client.getJson(id);

        return w;
    }
}
