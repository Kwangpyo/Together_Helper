package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GetHelperAPITask extends AsyncTask<String, Void, ArrayList<Helper>> {


    @Override
    protected ArrayList<Helper> doInBackground(String... params) {

        String id = params[0];

        GetHelperAPI client = new GetHelperAPI();

        ArrayList<Helper> w = client.getJson(id);

        return w;
    }
}
