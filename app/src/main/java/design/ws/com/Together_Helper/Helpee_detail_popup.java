package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Helpee_detail_popup extends Activity {

    private TextView helpee_name;
    private TextView helpee_feedback;
    private ImageView helpee_image;
    Bitmap bitmap;
    String photoURL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_helpee_detail_popup);

        helpee_name = (TextView)findViewById(R.id.helpee_detail_name);
        helpee_feedback =(TextView)findViewById(R.id.helpee_detail_feedback);
        helpee_image = (ImageView)findViewById(R.id.helpee_detail_image);


        Intent intent = getIntent();
        String helpeeid = (String)intent.getStringExtra("helpee");

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

        helpee_name.setText("Helpee ID : " + helpee.getId());
        helpee_feedback.setText("Helpee feedback : "+ helpee.getFeedback());



        GETPhotoURLAPITask t2 = new GETPhotoURLAPITask();

        try
        {
            photoURL = t2.execute(helpee.getId()).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("photoURL",photoURL);



        Thread mThread = new Thread(){
            @Override
            public void run() {
                try
                {
                    URL url =new URL(photoURL);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

            }
        };
        mThread.start();

        try {
            mThread.join();
            helpee_image.setImageBitmap(bitmap);
        }catch (InterruptedException e)
        {
            helpee_image.setImageResource(R.drawable.noimage);
            e.printStackTrace();
        }

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


