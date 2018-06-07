package design.ws.com.Together_Helper.API.GET;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import design.ws.com.Together_Helper.params.ReserveParam;

public class GETReservationCheckAPI {

    final static String openURL = "http://210.89.191.125/helper/reservation/check/";

    ReserveParam resultStr;

    public ReserveParam getJson(String id) {

        try {
            String url1 = openURL + id;
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String result ="";
            result =getStringFromInputStream(in);
            Log.d("reserveresultTest",result);
            parsing(result);


        } catch (MalformedURLException e) {

            System.err.println("Malformed URL");

            e.printStackTrace();
            return null;

        } catch (IOException e) {

            System.err.println("URL Connection failed");

            e.printStackTrace();

            return null;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultStr;
    }

    private void parsing(String result) throws JSONException {

            JSONArray Jarray = new JSONArray(result);

            String count="0";
            double lon = 0;
            double lat = 0;

            JSONObject JObject = null;
            try {
                JObject = Jarray.getJSONObject(0);

                if(JObject !=null) {
                    count = JObject.getString("count");

                    if (count.equals("1")) {
                        lon = JObject.getDouble("longitude");
                        lat = JObject.getDouble("latitude");
                    }

                }

            }

            catch (Exception e)
            {

            }




        ReserveParam returnlatlon = new ReserveParam(lat,lon,count);

        resultStr = returnlatlon;

    }


    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}

