package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterHelp_popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register_help_popup);


        //UI 객체생성
        TextView helpee = (TextView)findViewById(R.id.registerHelp_helpee);

        //데이터 가져오기
        Intent intent = getIntent();
        String help = intent.getStringExtra("help");
        helpee.setText("Helpee : " + help);

    }

    //신청 버튼 클릭
    public void mOnRegister(View v){
        //데이터 전달하기
        Toast.makeText(getApplicationContext(),"미구현",Toast.LENGTH_SHORT);
        //액티비티(팝업) 닫기
        finish();
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

