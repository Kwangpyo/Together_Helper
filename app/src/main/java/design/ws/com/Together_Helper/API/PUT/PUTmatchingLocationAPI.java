package design.ws.com.Together_Helper.API.PUT;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPut;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

public class PUTmatchingLocationAPI extends AsyncTask<String,Void,String> {

    protected String doInBackground(String... unused) {


        String content = executeClient(unused[0],unused[1],unused[2]);
        return content;
    }

    protected void onPostExecute(String result) {
        // 모두 작업을 마치고 실행할 일 (메소드 등등)
    }

    // 실제 전송하는 부분
    public String executeClient(String userId,String latitude,String longitude) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("userId", userId));
        post.add(new BasicNameValuePair("latitude", latitude));
        post.add(new BasicNameValuePair("longitude", longitude));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPut httpPost = new HttpPut("http://210.89.191.125/helper/real-matching/location");

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}