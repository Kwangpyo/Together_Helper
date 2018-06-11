package design.ws.com.Together_Helper.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
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
import design.ws.com.Together_Helper.API.GET.GETHelpByHelpIdAPITask;
import design.ws.com.Together_Helper.API.GET.GetHelpAPITask;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.HelpMarker;
import design.ws.com.Together_Helper.popup.Help_finish_popup;
import design.ws.com.Together_Helper.receiver.processTimerReceiver;
import design.ws.com.Together_Helper.util.GPSInfo;
import design.ws.com.Together_Helper.util.PermissionSettingUtils;

public class DuringHelpActivity extends AppCompatActivity {

    private LinearLayout finish_btn;
    private Timer timer;

    private GPSInfo gps;

    double longitude;
    double latitude;
    int volunteerId;

    POSTLocation postLocation;

    Help help;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_help);

        finish_btn = (LinearLayout)findViewById(R.id.during_finish_btn);

        Intent intent = getIntent();

        volunteerId = Integer.parseInt(intent.getStringExtra("volunteerId"));

        GETHelpByHelpIdAPITask getHelpByHelpIdAPITask = new GETHelpByHelpIdAPITask();
        try {
            help = getHelpByHelpIdAPITask.execute(String.valueOf(volunteerId)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        provider = LocationManager.GPS_PROVIDER;


        TimerTask timerTask;

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                /**
                 * Location Setting API를
                 */
                SettingsClient mSettingsClient = LocationServices.getSettingsClient(getApplicationContext());
                /*
                 * 위치정보 결과를 리턴하는 Callback
                 */
                mLocationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult result) {
                        super.onLocationResult(result);
                        //mCurrentLocation = locationResult.getLastLocation();
                        mCurrentLocation = result.getLocations().get(0);

                        latitude = mCurrentLocation.getLatitude();
                        longitude = mCurrentLocation.getLongitude();

                        Log.d("jaebal", String.valueOf(latitude));

                        String postresult = null;
                        postLocation = new POSTLocation();
                        try {
                            postresult = postLocation.execute(latitude, longitude).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }



                        /**
                         * 지속적으로 위치정보를 받으려면
                         * mLocationRequest.setNumUpdates(1) 주석처리하고
                         * 밑에 코드를 주석을 푼다
                         */
                        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                    }

                    //Location관련정보를 모두 사용할 수 있음을 의미
                    @Override
                    public void onLocationAvailability(LocationAvailability availability) {
                        //boolean isLocation = availability.isLocationAvailable();
                    }
                };
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                //여기선 한번만 위치정보를 가져오기 위함
               // mLocationRequest.setNumUpdates(1);
                if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
                    //배터리소모에 상관없이 정확도를 최우선으로 고려
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                }else{
                    //배터리와 정확도의 밸런스를 고려하여 위치정보를 획득(정확도 다소 높음)
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
                builder.addLocationRequest(mLocationRequest);
                /**
                 * 클라이언트가 사용하고자하는 위치 서비스 유형을 저장합니다. 위치 설정에도 사용됩니다.
                 */
                LocationSettingsRequest mLocationSettingsRequest = builder.build();

                Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSettingsRequest);
                locationResponse.addOnSuccessListener(DuringHelpActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.e("Response", "Successful acquisition of location information!!");
                        //
                        if (ActivityCompat.checkSelfPermission(DuringHelpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    }
                });
                //위치 정보를 설정 및 획득하지 못했을때 callback
                locationResponse.addOnFailureListener(DuringHelpActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.e("onFailure", "위치환경체크");
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "위치설정체크";
                                Log.e("onFailure", errorMessage);
                        }
                    }
                });

            }
            @Override
            public boolean cancel() {
                Log.v("monitortimer","타이머 종료");
                return super.cancel();
            }
        };

        timer.schedule(timerTask, 0, 5000);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();

                Intent intent = new Intent(getApplicationContext(),Help_finish_popup.class);
                intent.putExtra("helpid",volunteerId);
                intent.putExtra("help",help);
                startActivity(intent);
            }
        });



    }


    private void checkMyPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission Check
            PermissionSettingUtils.requestPermission(this);
        } else {
            //권한을 받았다면 위치찾기 세팅을 시작한다

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //요청코드가 맞지 않는다면
        if (requestCode != PermissionSettingUtils.REQUEST_CODE) {
            return;
        }
        if (PermissionSettingUtils.isPermissionGranted(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, grantResults)) {
            //허락을 받았다면 위치값을 알아오는 코드를 진행

        } else {
            Toast.makeText(this, "위치정보사용 허락을 하지않아 앱을 중지합니다", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }

    /**
     * 위치정보 제거
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }


    public boolean checkGPSService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            //    Intent intent = new Intent(this, GPS_popup.class);
            //    startActivityForResult(intent, 1);

            return false;

        } else {

            return true;
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            checkGPSService();
        }

    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;

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
        public String executeClient(double lat,double lon) {
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
