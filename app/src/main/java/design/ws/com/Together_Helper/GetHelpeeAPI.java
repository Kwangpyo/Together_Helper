package design.ws.com.Together_Helper;

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

public class GetHelpeeAPI {


    String userId;
    ArrayList<Helpee> helpees = new ArrayList<>();


    public ArrayList<Helpee> getJson(String id) {

        String urlLocation = "http://192.168.17.15:9001/helper/getHelpeeInfo/";
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


/*
    private void parseJSON(JSONObject json) throws JSONException {

        JSONArray datas = json.getJSONArray(result);

        int size = datas.length();

        for(int i=0;i<size;i++)
        {
            Log.d("testparsing",result);
            Help st = new Help(datas.getJSONObject(i).getString("type"));


         //       public Help( Helpee helpee, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, int match_status, int start_status, String content)
         //       Help st = new Help(datas.getJSONObject(i).getString("title"),datas.getJSONObject(i).getString("password"),datas.getJSONObject(i).getString("content"));
                helps.add(st);
        }


    }  */


    private void parsing(String result) throws JSONException {

        JSONArray Jarray = new JSONArray(result);

            JSONObject JObject = null;
            JObject = Jarray.getJSONObject(0);

            String id = JObject.getString("userID");
            String phone = JObject.getString("user_phone");
            Integer feedback = JObject.getInt("userFeedbackScore");
            String token = JObject.getString("token");
            double lat = JObject.getDouble("helpee_latitude");
            double lon = JObject.getDouble("helpee_longitude");
            Helpee helpee = new Helpee(token, lat, lon, feedback, id, phone);

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
