package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class Help_start_popup extends Activity {

    private EditText certificate;
    String certificate_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_start_popup);

        certificate =(EditText)findViewById(R.id.helpstart_certificate_num);
        certificate_num = certificate.getText().toString();

    }

    //확인 버튼 클릭
    public void mOnClick_start(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result ", "Close Popup");
        setResult(RESULT_OK, intent);
        Toast.makeText(getApplicationContext(),certificate.getText().toString(),Toast.LENGTH_SHORT).show();
        //액티비티(팝업) 닫기
        //finish();
    }


    public void mOnClose_start(View v){
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

