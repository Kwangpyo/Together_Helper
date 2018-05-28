package design.ws.com.Together_Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

public class processTimerReceiver extends BroadcastReceiver {

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private GPSInfo gps;

    double longitude;
    double latitude;
    int volunteerId;

    POSTLocation postLocation;
    @Override
    public void onReceive(Context context, Intent intent) {
        gps = new GPSInfo(context);
        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            volunteerId = Integer.parseInt(intent.getStringExtra("volunteerId"));

            Log.d("volid", String.valueOf(volunteerId));

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.d("fdasds",String.valueOf(latitude));
            Log.d("fdasds",String.valueOf(longitude));


            String result = null;
            postLocation = new POSTLocation();
            try {
                result = postLocation.execute(latitude, longitude).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.d("postlocation",result);

        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }



    class POSTLocation extends AsyncTask<Double,Void,String> {

        protected String doInBackground(Double... unused) {
            String content = executeClient(unused[0],unused[1]);
            return content;
        }

        protected void onPostExecute(String result) {
            // 모두 작업을 마치고 실행할 일 (메소드 등등)
        }

        // 실제 전송하는 부분
        public String executeClient(double lon,double lat) {
            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
            post.add(new BasicNameValuePair("helperLongitude", String.valueOf(lon)));
            post.add(new BasicNameValuePair("helperLatitude", String.valueOf(lat)));
            post.add(new BasicNameValuePair("volunteerId", String.valueOf(volunteerId)));

            // 연결 HttpClient 객체 생성
            HttpClient client = new DefaultHttpClient();

            // 객체 연결 설정 부분, 연결 최대시간 등등
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 5000);
            HttpConnectionParams.setSoTimeout(params, 5000);

            // Post객체 생
            HttpPost httpPost = new HttpPost("http://210.89.191.125/helper/location");
            Log.d("zxc","asdasd");

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
                httpPost.setEntity(entity);
                HttpResponse response = client.execute(httpPost);
                HttpEntity hentity = response.getEntity();
                String result = EntityUtils.toString(hentity);
                return result;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }



    }


}
