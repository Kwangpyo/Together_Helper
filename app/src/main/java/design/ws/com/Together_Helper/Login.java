package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    EditText idInput, passwordInput;
    TextView signup;
    Button login;
    SharedPreferences auto;
    SharedPreferences.Editor autoLogin;

    String id;
    String psw;

    String loginId;
    String loginPwd;

    Helper helper_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView Togethericon = (ImageView) findViewById(R.id.login_background);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(Togethericon);
        Glide.with(this).load(R.drawable.gif).into(gifImage);

        idInput = (EditText) findViewById(R.id.login_emailInput);
        passwordInput = (EditText) findViewById(R.id.login_passwordInput);
        signup = (TextView) findViewById(R.id.login_signupButton);
        login = (Button) findViewById(R.id.login_loginButton);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId",null);
        loginPwd = auto.getString("inputPwd",null);


        if (loginId != null && loginPwd != null) {

            String result="";
            POSTLoginAPI postloginAPI = new POSTLoginAPI();
            try {
                result = postloginAPI.execute(loginId,loginPwd).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Log.d("result",result);

            Toast.makeText(getApplicationContext(), loginId + "님 자동로그인 입니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        else if (loginId == null && loginPwd == null)
        {
            login.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View view) {

                    id = idInput.getText().toString();
                    psw = passwordInput.getText().toString();

                    Log.d("loginid",id);
                    Log.d("loginpwd",psw);

                    String result="";
                    POSTLoginAPI postloginAPI = new POSTLoginAPI();
                    try {
                        result = postloginAPI.execute(id,psw).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("result",result);

                    if(result.equals("success"))
                    {
                        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        autoLogin = auto.edit();
                        autoLogin.putString("inputId", id);
                        autoLogin.putString("inputPwd", psw);
                        autoLogin.commit();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }

                    else if(result.equals("fail"))
                    {
                        Toast.makeText(getApplicationContext(),"아이디나 비밀번호를 확인하세요",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }




        signup.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent1);

            }
        });

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}