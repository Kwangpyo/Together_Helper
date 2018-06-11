package design.ws.com.Together_Helper.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GETHelpeeLocationAPITask;
import design.ws.com.Together_Helper.API.PUT.PUTArriveAPI;
import design.ws.com.Together_Helper.API.PUT.PUTmatchingLocationAPI;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.popup.GPS_popup;
import design.ws.com.Together_Helper.popup.Reserve_popup;
import design.ws.com.Together_Helper.receiver.LocationReceiver;
import design.ws.com.Together_Helper.util.GPSInfo;
import design.ws.com.Together_Helper.util.PermissionSettingUtils;

public class MonitorActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private EditText loc_txt;
    private Geocoder geocoder;
    private double lat;
    private double lon;
    private Helper HELPER_ME;

    private GPSInfo gps;

    private int flag;
    private GoogleMap mMap;

    private GoogleMap googlemap;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private String provider;

    private Help help;

    private TextView title;
    private ImageView home;
    private ImageView back;

    private LinearLayout arrive_btn;

    private Integer volunteerId;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        volunteerId = getIntent().getIntExtra("helpid",-1);
        HELPER_ME = (Helper) getIntent().getSerializableExtra("helper");
        help = (Help)getIntent().getSerializableExtra("help");

        Log.d("sisiba", String.valueOf(volunteerId));
        Log.d("sibsbs",HELPER_ME.getId());
        Log.d("sibaba", String.valueOf(help.getHelpId()));

        home = (ImageView)findViewById(R.id.back_home);
        back = (ImageView)findViewById(R.id.back_back);
        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("어르신 위치");

        arrive_btn = (LinearLayout) findViewById(R.id.monitor_arrive_btn);

        TimerTask timerTask;

        timer = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {

                initGoogleMapLocation();

            }
            @Override
            public boolean cancel() {
                Log.v("monitortimer","타이머 종료");
                return super.cancel();
            }
        };
        timer.schedule(timerTask, 0, 5000);


        provider = LocationManager.GPS_PROVIDER;


        arrive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PUTArriveAPI putArriveAPI = new PUTArriveAPI();
                putArriveAPI.execute(volunteerId);
                timer.cancel();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("helper", HELPER_ME);
                startActivity(intent);

            }
        });

        home.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                timer.cancel();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("helper", HELPER_ME);
                startActivity(intent);

            }
        });


        geocoder = new Geocoder(this, Locale.getDefault());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        gps = new GPSInfo(getApplicationContext());
        double latitude=0;
        double longitude=0;

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.d("fdasds",String.valueOf(latitude));
            Log.d("fdasds",String.valueOf(longitude));

        } else {
            // GPS 를 사용할수 없으므로
            // gps.showSettingsAlert();

        }
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        LatLng startplace = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startplace, 30));
        mMap.setOnMarkerClickListener(this);



        GETHelpeeLocationAPITask getHelpeeLocationAPITask = new GETHelpeeLocationAPITask();
        LatLng location = new LatLng(0,0);
        try {
            location = getHelpeeLocationAPITask.execute(String.valueOf(help.getHelpId())).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("gethelpeeloc", String.valueOf(location));

        Marker marker;
        marker= mMap.addMarker(new MarkerOptions()
                .position(location)
                .title("어르신 위치"));
        marker.setTag(1001);
        marker.showInfoWindow();


        LatLng myplace2 = new LatLng(latitude, longitude);

        PUTmatchingLocationAPI puTmatchingLocationAPI = new PUTmatchingLocationAPI();
        puTmatchingLocationAPI.execute(HELPER_ME.getId(),String.valueOf(latitude),String.valueOf(longitude));

        Marker myMarker;
        myMarker =mMap.addMarker(new MarkerOptions()
                .position(myplace2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title("내 위치"));
        myMarker.setTag(1000);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myplace2, 30));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            checkGPSService();
        }

    }


    public boolean checkGPSService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Intent intent = new Intent(this, GPS_popup.class);
            startActivityForResult(intent, 1);

            return false;

        } else {
            //  initGoogleMapLocation();
            return true;
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
            //  initGoogleMapLocation();
        } else {
            Toast.makeText(this, "위치정보사용 허락을 하지않아 앱을 중지합니다", Toast.LENGTH_SHORT).show();
            //finish();
        }
    }




    private void initGoogleMapLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        /**
         * Location Setting API를
         */
        SettingsClient mSettingsClient = LocationServices.getSettingsClient(this);
        /*
         * 위치정보 결과를 리턴하는 Callback
         */
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult result) {
                super.onLocationResult(result);
                //mCurrentLocation = locationResult.getLastLocation();

                mMap.clear();

                GETHelpeeLocationAPITask getHelpeeLocationAPITask = new GETHelpeeLocationAPITask();
                LatLng location = new LatLng(0,0);
                try {
                    location = getHelpeeLocationAPITask.execute(String.valueOf(help.getHelpId())).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Log.d("gethelpeeloc", String.valueOf(location));

                try
                {
                    Marker marker;
                    marker= mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title("어르신 위치"));
                    marker.setTag(1001);
                    marker.showInfoWindow();
                }
                catch(Exception e)
                {

                }
                mCurrentLocation = result.getLocations().get(0);

                gps = new GPSInfo(getApplicationContext());
                double latitude=0;
                double longitude=0;

                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    Log.d("fdasds",String.valueOf(latitude));
                    Log.d("fdasds",String.valueOf(longitude));

                } else {
                    // GPS 를 사용할수 없으므로
                    // gps.showSettingsAlert();

                }

                LatLng myplace2 = new LatLng(latitude, longitude);

                PUTmatchingLocationAPI puTmatchingLocationAPI = new PUTmatchingLocationAPI();
                puTmatchingLocationAPI.execute(HELPER_ME.getId(),String.valueOf(latitude),String.valueOf(longitude));

                Marker myMarker;
                myMarker =mMap.addMarker(new MarkerOptions()
                        .position(myplace2)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        .title("내 위치"));
                myMarker.setTag(1000);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myplace2, 30));

                /**
                 * 지속적으로 위치정보를 받으려면
                 * mLocationRequest.setNumUpdates(1) 주석처리하고
                 * 밑에 코드를 주석을 푼다
                 */
                //mFusedLocationClient.removeLocationUpdates(mLocationCallback);
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
        mLocationRequest.setNumUpdates(1);
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
        locationResponse.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("Response", "Successful acquisition of location information!!");
                //
                if (ActivityCompat.checkSelfPermission(MonitorActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        });
        //위치 정보를 설정 및 획득하지 못했을때 callback
        locationResponse.addOnFailureListener(this, new OnFailureListener() {
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
    public void onBackPressed() {
        finish();
    }
}

