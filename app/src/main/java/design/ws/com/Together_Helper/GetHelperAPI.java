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

public class GetHelperAPI {

    String userId;
    ArrayList<Helper> helpers = new ArrayList<>();


    public ArrayList<Helper> getJson(String id) {

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

        Log.d("sisi",helpers.get(0).getId()+"******");

        return helpers;
    }


    private void parsing(String result) throws JSONException {

        JSONArray Jarray = new JSONArray(result);

        JSONObject JObject = null;
        JObject = Jarray.getJSONObject(0);

        String id = JObject.getString("userId");
        String psw = JObject.getString("helperPwd");
        String name = JObject.getString("name");
        String phone = JObject.getString("userPhone");
        Integer feedback;
        if(JObject.getString("userFeedbackScore").equals("null"))
        {
            feedback = 0;
        }
        else
        {
            feedback = JObject.getInt("userFeedbackScore");
        }

        String token = JObject.getString("deviceId");
        String admitTime = JObject.getString("admitTime");
        Helper helper = new Helper(name,feedback,id,psw,phone,token,admitTime);
        Log.d("qweqeqew",helper.getId());
        helpers.add(helper);

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

