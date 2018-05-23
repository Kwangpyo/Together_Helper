package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GETCustomHelpAPITask extends AsyncTask<ParamsForCustom, Void, ArrayList<Help>> {

    @Override
    protected ArrayList<Help> doInBackground(ParamsForCustom... params) {

        GetCustomHelpAPI client = new GetCustomHelpAPI();

        ArrayList<Help> w = client.getJson(params[0]);

        return w;
    }
}
