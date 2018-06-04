package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

public class GETPhotoURLAPITask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {

        String id = params[0];

        GETPhotoURLAPI client = new GETPhotoURLAPI();

        String w = client.getJson(id);

        return w;
    }
}
