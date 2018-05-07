package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GetHelpeeAPITask extends AsyncTask<String, Void, ArrayList<Helpee>> {


    @Override
    protected ArrayList<Helpee> doInBackground(String... params) {

        String id = params[0];

        GetHelpeeAPI client = new GetHelpeeAPI();

        ArrayList<Helpee> w = client.getJson(id);

        return w;
    }
}
