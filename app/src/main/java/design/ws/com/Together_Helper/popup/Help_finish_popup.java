package design.ws.com.Together_Helper.popup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import design.ws.com.Together_Helper.API.PUT.PUTHelpFinish;
import design.ws.com.Together_Helper.activity.Login;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.activity.MainActivity;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.params.ParamsForHistory;
import design.ws.com.Together_Helper.receiver.processTimerReceiver;

public class Help_finish_popup extends Activity {

    private Help help;
    private int volunteerId;
    Helper HELPER_ME;
    private RadioGroup rg;

    private String helpid;

    private EditText feedback_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_help_finish_popup);

        Intent intent = getIntent();
        help = (Help)intent.getSerializableExtra("help");
        //volunteerId = help.getHelpId();
//        HELPER_ME = (Helper)intent.getSerializableExtra("helper");

        volunteerId = intent.getIntExtra("helpid",-1);

        rg = (RadioGroup)findViewById(R.id.feedback_radiogroup);
        feedback_txt = (EditText)findViewById(R.id.feedback_txt);




    }

    //확인 버튼 클릭
    public void mOnClick_start(View v){

        int id = rg.getCheckedRadioButtonId();
        //getCheckedRadioButtonId() 의 리턴값은 선택된 RadioButton 의 id 값.
        RadioButton rb = (RadioButton) findViewById(id);

        if(rb == null)
        {
            Toast.makeText(getApplicationContext(),"피드백을 작성해 주세요",Toast.LENGTH_SHORT).show();
        }

        else {

            if (feedback_txt.getText() == null || feedback_txt.getText().length() < 10)
            {
                Toast.makeText(getApplicationContext(),"10글자 이상 작성해주세요",Toast.LENGTH_SHORT).show();
            }

            else if (feedback_txt.getText().length()>=10) {
                String feedback = rb.getText().toString();
                String[] data = feedback.split("점 ");
                String feedback_score_str = data[0];
                Log.d("feedback_score", feedback_score_str);
                int feedback_score = Integer.parseInt(feedback_score_str);
                String feedback_content = feedback_txt.getText().toString();

                Log.d("feedback_content", feedback_content);

                ParamsForHistory params = new ParamsForHistory(volunteerId, feedback_score, feedback_content);

                new PUTHelpFinish().execute(params);
                Intent intent = new Intent(getApplicationContext(),Login.class);
                //          intent.putExtra("helper",HELPER_ME);
                startActivity(intent);
            }

/*            else
            {
                String feedback = rb.getText().toString();
                String[] data = feedback.split("점 ");
                String feedback_score_str = data[0];
                Log.d("feedback_score",feedback_score_str);
                int feedback_score = Integer.parseInt(feedback_score_str);
                String feedback_content = data[1];

                Log.d("feedback_content",feedback_content);

                ParamsForHistory params = new ParamsForHistory(volunteerId,feedback_score,feedback_content);

                new PUTHelpFinish().execute(params);
            }*/



/*            Intent intent2 = new Intent(getApplicationContext(), processTimerReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);*/


//            Toast.makeText(getApplicationContext(),"종료가 완료되었습니다.",Toast.LENGTH_SHORT).show();
            //액티비티(팝업) 닫기

        }


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

