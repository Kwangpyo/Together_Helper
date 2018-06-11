package design.ws.com.Together_Helper.popup;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import design.ws.com.Together_Helper.API.POST.POSTReserveAPI;
import design.ws.com.Together_Helper.activity.CustomSearch;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.params.ReserveParam;
import design.ws.com.Together_Helper.util.PermissionSettingUtils;

public class Reserve_popup extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private EditText loc_txt;
    private Geocoder geocoder;
    private double lat;
    private double lon;
    private Helper HELPER_ME;

    private int flag;
    private GoogleMap mMap;

    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private String provider;

    private TextView map_txt;
    private TextView time_txt;
    private String getTime;

    private TextView registered_date_txt;

    Integer today_year;
    Integer today_month;
    Integer today_day;
    Integer min_year;
    Integer min_month;
    Integer min_day;
    Integer max_year;
    Integer max_month;
    Integer max_day;
    Integer min_hour;
    Integer min_minute;
    Integer max_hour;
    Integer max_minute;
    Integer date_flag;

    Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_popup);

        loc_txt = (EditText)findViewById(R.id.register_loc_edit);
        map_txt = (TextView)findViewById(R.id.register_map);
        time_txt = (TextView)findViewById(R.id.register_time);
        registered_date_txt = (TextView)findViewById(R.id.registered_time_txt);

        HELPER_ME = (Helper) getIntent().getSerializableExtra("helper");
        geocoder = new Geocoder(this, Locale.getDefault());

        provider = LocationManager.GPS_PROVIDER;
        flag =0;

        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        getTime = sdf.format(date);

        Calendar cal = Calendar.getInstance();

        today_year = cal.get(Calendar.YEAR);
        today_month = cal.get(Calendar.MONTH);
        today_day = cal.get(Calendar.DATE);

        date_flag=0;

        time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar pickedDate = Calendar.getInstance();
                Calendar minDate = Calendar.getInstance();

                pickedDate.set(today_year,today_month,today_day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Reserve_popup.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                min_year = year;
                                min_month = month+1;
                                min_day = dayOfMonth;
                                registered_date_txt.setText(min_year+"년 "+min_month+"월 "+min_day+"일");
                                time_txt.setText("예약 시간 재설정");
                                date_flag=1;
                            }
                        },
                        pickedDate.get(Calendar.YEAR),
                        pickedDate.get(Calendar.MONTH),
                        pickedDate.get(Calendar.DATE)

                );


                minDate.set(today_year,today_month,today_day);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());

                datePickerDialog.show();




            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String location_name =loc_txt.getText().toString();

                Log.d("reserve",location_name);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(
                            location_name, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("reservetest","입출력 오류 - 서버에서 주소변환시 에러발생");
                    Toast.makeText(getApplicationContext(),"주소를 입력해주세요",Toast.LENGTH_SHORT).show();
                }

                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getApplicationContext(),"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
                    } else {
                        lat = list.get(0).getLatitude();
                        lon = list.get(0).getLongitude();
                        flag=1;
                        initGoogleMapLocation();
                    }
                }


            }
        });



    }


    public void mOnRegister(View v){

        String location_name =loc_txt.getText().toString();
        String FromDate = min_year+"-"+min_month+"-"+min_day+" "+"00:00";
        String ToDate = min_year+"-"+min_month+"-"+min_day+" "+"23:59";

        Log.d("reserve",location_name);
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(
                    location_name, // 지역 이름
                    10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("reservetest","입출력 오류 - 서버에서 주소변환시 에러발생");
            Toast.makeText(getApplicationContext(),"주소를 입력해주세요",Toast.LENGTH_SHORT).show();
        }

        if (list != null) {
            if (list.size() == 0) {
                Toast.makeText(getApplicationContext(),"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
          //      latlon_txt.setText("");
            } else {
                lat = list.get(0).getLatitude();
                lon = list.get(0).getLongitude();
                flag=1;
                //          list.get(0).getCountryName();  // 국가명
                //          list.get(0).getLatitude();        // 위도
                //          list.get(0).getLongitude();    // 경도

                double lat_6 = Double.parseDouble(String.format("%.6f",lat));
                double lon_6 = Double.parseDouble(String.format("%.6f",lon));
             //   latlon_txt.setText("위도 : "+lat_6 +" 경도 : "+lon_6);
            }
        }

        if(flag==1)
        {
            POSTReserveAPI postReserveAPI = new POSTReserveAPI();
            postReserveAPI.execute(HELPER_ME.getId(),String.valueOf(lat),String.valueOf(lon),FromDate,ToDate);

            Intent intent = new Intent();
            intent.putExtra("result", "Close Popup");
            setResult(RESULT_OK, intent);
            finish();
            Toast.makeText(getApplicationContext(),"예약 완료되었습니다.",Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(getApplicationContext(),"주소를 입력해주세요.",Toast.LENGTH_SHORT).show();
        }

    }

    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng startplace = new LatLng(37.2635730, 127.0286010);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startplace, 14));
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setOnMarkerClickListener(this);

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
                mCurrentLocation = result.getLocations().get(0);

                Log.d("qwe","qwe");
                Log.d("loc", String.valueOf(mCurrentLocation.getAltitude()));

                LatLng myplace = new LatLng(lat, lon);


                  myMarker =mMap.addMarker(new MarkerOptions()
                          .position(myplace)
                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                          .title("예약할 봉사 위치"));
                  myMarker.setTag(1000);
                  myMarker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myplace, 14));

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
                if (ActivityCompat.checkSelfPermission(Reserve_popup.this, Manifest.permission.ACCESS_FINE_LOCATION)
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







}
