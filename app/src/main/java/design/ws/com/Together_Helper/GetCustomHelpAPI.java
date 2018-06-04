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

public class GetCustomHelpAPI{

        ArrayList<Help> helps = new ArrayList<>();


        public ArrayList<Help> getJson(ParamsForCustom params) {

            String volunteerType = params.getVolunteerType();

            if(volunteerType.equals("외출"))
            {
                volunteerType = "outside";
            }
            else if(volunteerType.equals("가사"))
            {
                volunteerType = "housework";
            }
            else if(volunteerType.equals("교육"))
            {
                volunteerType = "education";
            }
            else if(volunteerType.equals("말동무"))
            {
                volunteerType="talk";
            }


            String fromTime;
            String toTime;

            fromTime = String.format("%02d",params.getFromHour())+":"+String.format("%02d",params.getFromMin());
            toTime = String.format("%02d",params.getToHour())+":"+String.format("%02d",params.getToMin());

            String fromdate = String.format("%02d",params.getFromYear())+"-"+String.format("%02d",params.getFromMonth())+"-"+String.format("%02d",params.getFromDay());
            String todate = String.format("%02d",params.getToYear())+"-"+String.format("%02d",params.getToMonth())+"-"+String.format("%02d",params.getToDay());



            String URL = "http://210.89.191.125/helper/volunteer/search";
            String query = "?fromDate="+fromdate+"&&toDate="+todate+"&&fromTime="+fromTime+"&&toTime="+toTime+"&&latitude="+params.getLatitude()+"&&longitude="+params.getLongitude()+"&&volunteerType="+volunteerType;

            String openURL = URL+query;
            try {

                Log.d("getcustomhelpapi",openURL);
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

            }
            //catch (JSONException e) {

                //System.err.println("JSON parsing error");

               // e.printStackTrace();

             //   return null;

           // }
            catch (IOException e) {

                System.err.println("URL Connection failed");

                e.printStackTrace();

                return null;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return helps;
        }



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
                Integer duration = JObject.getInt("duration");
                String helperid = JObject.getString("helperId");

                Integer year;
                Integer month;
                Integer day ;

                String date = JObject.getString("date");
                if(date.contains("T"))
                {
                    String[] date_word = date.split("T");
                    String[] dates = date_word[0].split("-");
                    year = Integer.parseInt(dates[0]);
                    month = Integer.parseInt(dates[1]);
                    day = Integer.parseInt(dates[2]);
                }
                else
                {
                    String[] dates = date.split("-");
                    year = Integer.parseInt(dates[0]);
                    month = Integer.parseInt(dates[1]);
                    day = Integer.parseInt(dates[2]);
                }


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

