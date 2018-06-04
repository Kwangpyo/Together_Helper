package design.ws.com.Together_Helper.API;

import android.os.AsyncTask;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.params.ParamsForCustom;

public class GETCustomHelpAPITask extends AsyncTask<ParamsForCustom, Void, ArrayList<Help>> {

    @Override
    protected ArrayList<Help> doInBackground(ParamsForCustom... params) {

        GetCustomHelpAPI client = new GetCustomHelpAPI();

        ArrayList<Help> w = client.getJson(params[0]);

        return w;
    }
}
