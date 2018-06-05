package design.ws.com.Together_Helper.popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import design.ws.com.Together_Helper.API.PUT.PUTCancelHelpAPI;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.activity.MainActivity;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.params.MyTaskParam;

public class Help_cancel_popup extends Activity {

    String helperid;
    Helper HELPER_ME;
    int volunteerid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_cancel_popup);



        //데이터 가져오기
        Intent intent = getIntent();
        helperid = intent.getStringExtra("helperid");
        volunteerid = intent.getIntExtra("helpid",0);
        HELPER_ME = (Helper)intent.getSerializableExtra("helper");

  //      Log.d("helpcancel_helperid",helperid);
//        Log.d("helpcancel_volunteerid", String.valueOf(volunteerid));

    }
    //확인 버튼 클릭
    public void mOnClick(View v){
        //데이터 전달하기

        MyTaskParam param = new MyTaskParam(volunteerid,helperid);
        new PUTCancelHelpAPI().execute(param);

        Toast.makeText(getApplicationContext(),"취소가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        //액티비티(팝업) 닫기
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("helper",HELPER_ME);
        startActivity(intent);
    //    finish();
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

