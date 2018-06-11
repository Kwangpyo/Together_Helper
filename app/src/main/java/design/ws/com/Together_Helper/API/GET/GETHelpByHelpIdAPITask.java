package design.ws.com.Together_Helper.API.GET;

import android.os.AsyncTask;

import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Help;

public class GETHelpByHelpIdAPITask extends AsyncTask<String, Void, Help> {


    @Override
    protected Help doInBackground(String... integers) {

        GETHelpByHelpIdAPI client = new GETHelpByHelpIdAPI();

        Help w = client.getJson(integers[0]);

        return w;
    }
}
