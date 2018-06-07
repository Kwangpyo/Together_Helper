package design.ws.com.Together_Helper.API.GET;

import android.os.AsyncTask;

public class GETDeviceKeyAPITask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String id = params[0];

        GETDeviceKeyAPI client = new GETDeviceKeyAPI();

        String w = client.getJson(id);

        return w;
    }
}
