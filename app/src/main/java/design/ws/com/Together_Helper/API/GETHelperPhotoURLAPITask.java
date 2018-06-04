package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

public class GETHelperPhotoURLAPITask extends AsyncTask<String, Void, String> {


    @Override
    protected String doInBackground(String... params) {

        String id = params[0];

        GETHelperPhotoURLAPI client = new GETHelperPhotoURLAPI();

        String w = client.getJson(id);

        return w;
    }
}
