package design.ws.com.Together_Helper;

import android.os.AsyncTask;

import java.util.ArrayList;

public class GetHelpAPITask extends AsyncTask<Integer, Void, ArrayList<Help>> {


    @Override
    protected ArrayList<Help> doInBackground(Integer... integers) {

        GetHelpAPI client = new GetHelpAPI();

        ArrayList<Help> w = client.getJson();

        return w;
    }
}
