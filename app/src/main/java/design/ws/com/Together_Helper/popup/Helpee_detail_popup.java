package design.ws.com.Together_Helper.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GetHelpeeAPITask;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helpee;
import design.ws.com.Together_Helper.R;

public class Helpee_detail_popup extends Activity {

    private TextView helpee_name;
    private TextView helpee_feedback;
    private ImageView helpee_image;
    private TextView help_duration;
    private TextView help_type;
    Bitmap bitmap;
    String photoURL="";

    private Help help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_helpee_detail_popup);

        helpee_name = (TextView)findViewById(R.id.helpee_detail_name);
        helpee_feedback =(TextView)findViewById(R.id.helpee_detail_feedback);
        helpee_image = (ImageView)findViewById(R.id.helpee_detail_image);
        help_duration = (TextView)findViewById(R.id.helpee_detail_duration);
        help_type = (TextView)findViewById(R.id.helpee_detail_type);

        Intent intent = getIntent();
        String helpeeid = (String)intent.getStringExtra("helpee");
        help = (Help)intent.getSerializableExtra("help");

        ArrayList<Helpee> ps = new ArrayList<>();
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

        helpee_name.setText("Helpee 번호 : " + helpee.getId());
        helpee_feedback.setText("Helpee 피드백 : "+ helpee.getFeedback());
        help_duration.setText("봉사 시간 : "+help.getDuration()+"시간");
        String type="";
        if(help.getType().equals("housework"))
        {
            type = "가사";
        }
        else if(help.getType().equals("outside"))
        {
            type = "외출";
        }
        else if(help.getType().equals("education"))
        {
            type = "교육";
        }
        else if(help.getType().equals("talk"))
        {
            type = "말동무";
        }

        help_type.setText("봉사 종류 : " +type);

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


