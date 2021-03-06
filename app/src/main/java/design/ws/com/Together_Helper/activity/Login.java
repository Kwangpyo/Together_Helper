package design.ws.com.Together_Helper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GetHelperAPITask;
import design.ws.com.Together_Helper.API.POST.POSTLoginAPI;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Helper;

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

    private String PhoneNum = "";

    private String datas = "no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ImageView Togethericon = (ImageView) findViewById(R.id.login_background);

        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(Togethericon);
        Glide.with(this).load(R.drawable.signupgif).into(gifImage);

        idInput = (EditText) findViewById(R.id.login_emailInput);
        passwordInput = (EditText) findViewById(R.id.login_passwordInput);
        signup = (TextView) findViewById(R.id.login_signupButton);
        login = (Button) findViewById(R.id.login_loginButton);

        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);




        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, "android.permission.READ_PHONE_NUMBERS") != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                }


            };

            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setRationaleMessage("로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                    .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                    .setPermissions(Manifest.permission.READ_PHONE_STATE)
                    .check();

            return;
        }
      /*  PhoneNum = telManager.getLine1Number();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }

        Log.d("phonenumber11",PhoneNum);

        idInput.setText(PhoneNum);
*/
        if (loginId != null && loginPwd != null) {

            Log.d("login1", "1");

            String result = null;
            POSTLoginAPI postloginAPI = new POSTLoginAPI();
            try {
                result = postloginAPI.execute(loginId, loginPwd).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            if (result == null) {
                Log.d("login2", "2");
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                loginId = null;
                loginPwd = null;
                Snackbar.make(login,"자동로그인에 실패했습니다. 네트워크 상태를 확인해주세요", Snackbar.LENGTH_LONG).show();
            } else if (result != null)
            {
                if (result.equals("success")) {
                    Log.d("login3", "3");
                    Log.d("2", "2");
                    Log.d("result", result);
                    ArrayList<Helper> ps = new ArrayList<>();

                    GetHelperAPITask t = new GetHelperAPITask();
                    try {
                        ps = t.execute(loginId).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Helper helper = ps.get(0);

                    Intent getIntent = getIntent();
                    Integer integer = getIntent.getIntExtra("intentflag",-1);

                    Snackbar.make(login,loginId + "님 자동로그인 입니다.", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("helper", helper);
                    intent.putExtra("pushdata",datas);
                    intent.putExtra("intentflag",integer);
                    startActivity(intent);
                }

                else {
                    Log.d("3", "3");
                    Log.d("login4", "4");
                    Log.d("result", result);
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = auto.edit();
                    editor.clear();
                    editor.commit();
                    loginId = null;
                    loginPwd = null;
                    Snackbar.make(login,"다시 시도해주세요", Snackbar.LENGTH_LONG).show();

                }

        }

        }

        //else if (loginId == null && loginPwd == null)
      //  {
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


                    if(result == null)
                    {
                        Log.d("login10","10");
                        Snackbar.make(login,"네트워크 상태를 확인해주세요", Snackbar.LENGTH_LONG).show();

                    }

                    else if(result != null)
                    {

                    if(result.equals("success"))
                    {
                        auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                        autoLogin = auto.edit();
                        autoLogin.putString("inputId", id);
                        autoLogin.putString("inputPwd", psw);
                        autoLogin.commit();

                        ArrayList<Helper> ps = new ArrayList<>();

                        GetHelperAPITask t = new GetHelperAPITask();
                        try
                        {
                            ps = t.execute(id).get();
                        }

                        catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        Helper helper = ps.get(0);

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("helper",helper);
                        startActivity(intent);
                    }

                    else if(result.equals("fail"))
                    {
                        Snackbar.make(login,"아이디나 비밀번호를 확인하세요", Snackbar.LENGTH_LONG).show();
                    }

                    else
                        Snackbar.make(login,"네트워크 상태를 확인해주세요", Snackbar.LENGTH_LONG).show();
                    }

                }
            });
   //     }




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


    @Override
    public void onResume(){
        super.onResume();




    }


}
