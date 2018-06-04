package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Register_popup extends Activity {

    private EditText loc_txt;
    private TextView latlon_txt;
    private TextView change_txt;
    private Geocoder geocoder;
    private double lat;
    private double lon;
    private Helper HELPER_ME;

    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_popup);

        loc_txt = (EditText)findViewById(R.id.register_loc_edit);
        latlon_txt = (TextView)findViewById(R.id.register_latlon_txt);
        change_txt = (TextView)findViewById(R.id.register_change);

        HELPER_ME = (Helper) getIntent().getSerializableExtra("helper");
        geocoder = new Geocoder(this, Locale.getDefault());

        flag =0;

        change_txt.setOnClickListener(new View.OnClickListener() {
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
                        latlon_txt.setText("");
                    } else {
                        lat = list.get(0).getLatitude();
                        lon = list.get(0).getLongitude();
                        flag=1;
                        //          list.get(0).getCountryName();  // 국가명
                        //          list.get(0).getLatitude();        // 위도
                        //          list.get(0).getLongitude();    // 경도

                        double lat_6 = Double.parseDouble(String.format("%.6f",lat));
                        double lon_6 = Double.parseDouble(String.format("%.6f",lon));
                        latlon_txt.setText("위도 : "+lat_6 +" 경도 : "+lon_6);
                    }
                }


            }
        });
    }


    public void mOnRegister(View v){

        if(flag==1)
        {
            POSTReserveAPI postReserveAPI = new POSTReserveAPI();
            postReserveAPI.execute(HELPER_ME.getId(),String.valueOf(lat),String.valueOf(lon));

            Intent intent = new Intent();
            intent.putExtra("result", "Close Popup");
            setResult(RESULT_OK, intent);
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


}
