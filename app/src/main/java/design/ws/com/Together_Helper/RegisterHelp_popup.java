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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class RegisterHelp_popup extends Activity {

    Geocoder geocoder;
    String address;

    Help help;
    String helpeeid;

    Helper HELPER_ME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_help_popup);


        //UI 객체생성
        TextView helperid_txt = (TextView)findViewById(R.id.registerHelp_helperid);
        TextView helpeeid_txt = (TextView)findViewById(R.id.registerHelp_helpeeid);
        TextView helpeephone_txt = (TextView)findViewById(R.id.registerHelp_helpeephone);
        TextView helpeefeedback_txt = (TextView)findViewById(R.id.registerHelp_helpeefeedback);
        TextView helptype_txt = (TextView)findViewById(R.id.registerHelp_helptype);
        TextView helpdate_txt = (TextView)findViewById(R.id.registerHelp_helpdate);
        TextView helplocation_txt = (TextView)findViewById(R.id.registerHelp_helplocation);

        //데이터 가져오기
        Intent intent = getIntent();
        helpeeid = intent.getStringExtra("helpeeid");
        help = (Help)intent.getSerializableExtra("help");
        ArrayList<Helpee> ps = new ArrayList<>();
        Helper helper = (Helper)intent.getSerializableExtra("helper");
        HELPER_ME = helper;


        Log.d("registerhelptest1",HELPER_ME.getId());
        Log.d("registerhelptest2",helpeeid);

        GetHelpeeAPITask t = new GetHelpeeAPITask();
        try
        {
            ps = t.execute(helpeeid).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        Helpee helpee = ps.get(0);

        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list = null;
        try {
            double d1 = help.getLat();
            double d2 = help.getLon();

            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10); // 얻어올 값의 개수
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size()==0) {
                Log.d("geocoder","no address");
            } else {
                address = list.get(0).getAddressLine(0);
            }
        }


        helpeeid_txt.setText("Helpee : " + helpee.getId());
        helpeephone_txt.setText("Helpee 전화번호 : " + helpee.getPhonenumber());
        helpeefeedback_txt.setText("Helpee 피드백 : " + helpee.getFeedback());
        helptype_txt.setText("봉사 종류 : "+help.getType());
        String date = help.getYear() +"년 " + help.getMonth()+"월 " + help.getDay() +"일  " + help.getHour()+"시 " + help.getMinute()+"분";
        helpdate_txt.setText("봉사 시간 : "+date);
        helplocation_txt.setText("봉사 주소 :"+address);


        if(help.getMatch_status()==0)
        {
            helperid_txt.setText("신청 가능합니다" );
        }

        else{
            helperid_txt.setText("누가 이미 신청했습니다");
        }

    }

    //신청 버튼 클릭
    public void mOnRegister(View v){
        //데이터 전달하기


        if(help.getMatch_status()==0)
        {
            MyTaskParam param = new MyTaskParam(help.getHelpId(),HELPER_ME.getId());
            new PUTRegisterHelpAPI().execute(param);
            Toast.makeText(getApplicationContext(),"신청이 완료되었습니다.",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),LocationSearchMap.class);
            intent.putExtra("helper",HELPER_ME);
            startActivity(intent);

        }

        else
        {
            Log.d("registerbtn","asd");
            Toast.makeText(getApplicationContext(),"이미 신청된 봉사입니다.",Toast.LENGTH_SHORT).show();
            finish();

        }

        //액티비티(팝업) 닫기




    }

    //확인 버튼 클릭
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

