package design.ws.com.Together_Helper.popup;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import design.ws.com.Together_Helper.API.POST.POSTReservationCancelAPI;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.params.ReserveParam;

public class ReserveState_popup extends Activity {

    private Helper HELPER_ME;
    private ReserveParam reserveParam;
    private TextView loc_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reserve_state_popup);

        Intent intent = getIntent();
        HELPER_ME = (Helper) intent.getSerializableExtra("helper");
        reserveParam = (ReserveParam) intent.getSerializableExtra("reserveparam");
        loc_txt = (TextView) findViewById(R.id.reserve_state_txt);

        final Geocoder geocoder = new Geocoder(this);


        List<Address> list = null;
        try {
            double d1 = reserveParam.getLat();
            double d2 = reserveParam.getLon();

            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size() == 0) {
                loc_txt.setText("해당되는 주소 정보는 없습니다");
            } else {

                String s = "예약하신 봉사 위치는 " + "<b>"+list.get(0).getAddressLine(0)+"</b>"+ " 입니다.";

                loc_txt.setText(Html.fromHtml(s));
            }
        }





    }

    public void mOnCancel(View v){

        POSTReservationCancelAPI postReservationCancelAPI = new POSTReservationCancelAPI();
        postReservationCancelAPI.execute(HELPER_ME.getId());

        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        Toast.makeText(getApplicationContext(),"봉사 예약이 취소되었습니다.",Toast.LENGTH_SHORT).show();
        //액티비티(팝업) 닫기
        finish();

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
