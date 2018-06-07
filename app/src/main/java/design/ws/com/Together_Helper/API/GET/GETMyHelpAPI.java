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

import design.ws.com.Together_Helper.domain.Help;

public class GETMyHelpAPI {

    ArrayList<Help> helps = new ArrayList<>();

    public ArrayList<Help> getJson(String id) {

        String urlLocation = "http://210.89.191.125/helper/volunteers/";
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

        return helps;
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

        int size = Jarray.length();

        for(int i=0;i<size;i++)
        {
            JSONObject JObject = null;
            JObject = Jarray.getJSONObject(i);

            Integer volunteerId= JObject.getInt("volunteerId");
            String type = JObject.getString("type");
            String HelpeeID = JObject.getString("helpeeId");
            double lon = JObject.getDouble("longitude");
            double lat = JObject.getDouble("latitude");
            Integer matching_status = JObject.getInt("matchingStatus");
            Integer start_status = JObject.getInt("startStatus");
            String content = JObject.getString("content");
         //   Integer hour = JObject.getInt("hour");
         //   Integer minute = JObject.getInt("minute");
            Integer duration = JObject.getInt("duration");
        //    Integer year = JObject.getInt("year");
        //    Integer month = JObject.getInt("month");
         //   Integer day = JObject.getInt("day");
            String helperid = JObject.getString("helperId");

            String date = JObject.getString("date");
            String[] date_word = date.split("T");
            String[] dates = date_word[0].split("-");
            Integer year = Integer.parseInt(dates[0]);
            Integer month = Integer.parseInt(dates[1]);
            Integer day = Integer.parseInt(dates[2]);


            String time = JObject.getString("time");
            String[] times = time.split(":");
            Integer hour = Integer.parseInt(times[0]);
            Integer minute = Integer.parseInt(times[1]);

            Log.d("testparsing",JObject.getString("type"));
            Help st = new Help(HelpeeID,lon,lat,hour,minute,duration,year,month,day,type,matching_status,start_status,content,volunteerId,helperid);


            //       public Help( Helpee helpee, double lon, double lat, int hour, int minute, int duration, int year, int month, int day, String type, int match_status, int start_status, String content)
            //       Help st = new Help(datas.getJSONObject(i).getString("title"),datas.getJSONObject(i).getString("password"),datas.getJSONObject(i).getString("content"));
            helps.add(st);
        }


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

