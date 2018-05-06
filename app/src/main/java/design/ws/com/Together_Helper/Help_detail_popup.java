package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Help_detail_popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_detail_popup);

        //UI 객체생성
        TextView detail_helpee = (TextView)findViewById(R.id.help_detail_helpee);
        TextView detail_location = (TextView)findViewById(R.id.help_detail_location);
        TextView detail_time = (TextView)findViewById(R.id.help_detail_time);
        TextView detail_status = (TextView)findViewById(R.id.help_detail_status);
        TextView detail_content = (TextView)findViewById(R.id.help_detail_content);

        //데이터 가져오기
        Intent intent = getIntent();
        Help help = (Help)intent.getSerializableExtra("help");
        detail_helpee.setText("Helpee : " + help.getHelpee().getName());
     //   detail_location.setText("위치 : "+help.getLocation());
      //  detail_time.setText("시간 : " + help.getTime());
        detail_status.setText("매칭 상태 : " + help.getMatch_status());
        detail_content.setText("기타 : " + help.getContent());

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

