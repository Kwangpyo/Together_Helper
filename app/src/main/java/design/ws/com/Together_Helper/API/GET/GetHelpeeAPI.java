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
import java.util.ArrayList;

import design.ws.com.Together_Helper.domain.Helpee;

public class GetHelpeeAPI {


    String userId;
    ArrayList<Helpee> helpees = new ArrayList<>();


    public ArrayList<Helpee> getJson(String id) {

        String urlLocation = "http://210.89.191.125/helper/user/";
        final String openURL = urlLocation + id;

        try {

            URL url = new URL(openURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

//            JSONObject json = new JSONObject(getStringFromInputStream(in));
//            parseJSON(json);
            String result ="";
            result =getStringFromInputStream(in);
            Log.d("resultTest",result);
            parsing(result);

        } catch (MalformedURLException e) {

            System.err.println("Malformed URL");

            e.printStackTrace();
            return null;

        } catch (JSONException e) {

            System.err.println("JSON parsing error");

            e.printStackTrace();

            return null;

        } catch (IOException e) {

            System.err.println("URL Connection failed");

            e.printStackTrace();

            return null;

        }

        return helpees;
    }


    private void parsing(String result) throws JSONException {

        JSONArray Jarray = new JSONArray(result);

            JSONObject JObject = null;
            JObject = Jarray.getJSONObject(0);

            String id = JObject.getString("userId");
            String phone = JObject.getString("userPhone");
            Integer feedback;
            try {
                feedback = JObject.getInt("userFeedbackScore");
            }
            catch(Exception e)
        {
            feedback =0;
        }
            String token = JObject.getString("deviceId");

        double lat;
        double lon;
            try {
                lat = JObject.getDouble("helpeeLatitude");
                lon = JObject.getDouble("helpeeLongitude");
            }
            catch (Exception e)
            {
                lat = 0;
                lon = 0;
            }

            String pauseStatus = JObject.getString("pauseStatus");

            Helpee helpee = new Helpee(token, lat, lon, feedback, id, phone,pauseStatus);

            helpees.add(helpee);

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
