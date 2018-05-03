package design.ws.com.Together_Helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class LocationSearchMap extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private TextView title;
    Geocoder geocoder;
    Location address_loc;
    String address;
    List<Address> address_list;

    private ImageView refresh;
    private ImageView home;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private String provider;

    Marker myMarker;

    LatLng place1 = new LatLng(37.271414,127.038926);
    LatLng place2 = new LatLng(37.289283,127.047137);
    LatLng place3 = new LatLng(37.275721,127.049908);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search_map);

        refresh = (ImageView)findViewById(R.id.toolbar_refresh);
        home = (ImageView)findViewById(R.id.home);

        provider = LocationManager.GPS_PROVIDER;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkMyPermissionLocation();
        } else {
            initGoogleMapLocation();
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("위치 검색");


        home.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                mMap.clear();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    checkMyPermissionLocation();
                } else {
                    initGoogleMapLocation();
                }

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getTitle().equals("보원이"))
        {
            Intent intent = new Intent(getApplicationContext(),RegisterHelp_popup.class);
            intent.putExtra("help","보원이");
            startActivity(intent);
        }

        else if(marker.getTitle().equals("소영이"))
        {
            Intent intent = new Intent(getApplicationContext(),RegisterHelp_popup.class);
            intent.putExtra("help","소영이");
            startActivity(intent);
        }

        else if(marker.getTitle().equals("준민이"))
        {
            Intent intent = new Intent(getApplicationContext(),RegisterHelp_popup.class);
            intent.putExtra("help","준민이");
            startActivity(intent);
        }

        return false;
    }


    private void checkMyPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //Permission Check
            PermissionSettingUtils.requestPermission(this);
        } else {
            //권한을 받았다면 위치찾기 세팅을 시작한다
            initGoogleMapLocation();
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
                mCurrentLocation = result.getLocations().get(0);

                Log.d("qwe","qwe");
                Log.d("loc", String.valueOf(mCurrentLocation.getAltitude()));

                MarkerOptions options = new MarkerOptions();
                LatLng myplace = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

                Marker marker1;
                marker1 =mMap.addMarker(new MarkerOptions()
                        .position(place1)
                        .title("보원이"));
                marker1.setTag(1);

                Marker marker2;
                marker2 =mMap.addMarker(new MarkerOptions()
                        .position(place2)
                        .title("준민이"));
                marker2.setTag(2);

                Marker marker3;
                marker3 =mMap.addMarker(new MarkerOptions()
                        .position(place3)
                        .title("소영이"));
                marker1.setTag(3);

                myMarker =mMap.addMarker(new MarkerOptions()
                        .position(myplace)
                        .title("내 위치"));
                myMarker.setTag(0);
                options.position(myplace);
                BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
                options.icon(icon);
                Marker marker = mMap.addMarker(options);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));

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
                if (ActivityCompat.checkSelfPermission(LocationSearchMap.this, Manifest.permission.ACCESS_FINE_LOCATION)
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //요청코드가 맞지 않는다면
        if (requestCode != PermissionSettingUtils.REQUEST_CODE) {
            return;
        }
        if (PermissionSettingUtils.isPermissionGranted(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, grantResults)) {
            //허락을 받았다면 위치값을 알아오는 코드를 진행
            initGoogleMapLocation();
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

            Intent intent = new Intent(this, GPS_popup.class);
            startActivityForResult(intent, 1);

            return false;

        } else {
            initGoogleMapLocation();
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



}



