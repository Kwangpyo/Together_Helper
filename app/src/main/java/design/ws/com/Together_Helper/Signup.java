package design.ws.com.Together_Helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.concurrent.ExecutionException;

public class Signup extends AppCompatActivity {

    Button signupcomplete;
    EditText newid, newpwd, newpwdcheck, name_text;
    String new_id;
    String new_pwd1;
    String new_pwd2;
    String name;
    ImageView gallery;
    Bitmap picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView Togethericon = (ImageView) findViewById(R.id.signup_background);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(Togethericon);
        Glide.with(this).load(R.drawable.gif).into(gifImage);

        signupcomplete = (Button)findViewById(R.id.login_loginButton);
        newid = (EditText)findViewById(R.id.signup_id);
        newpwd = (EditText)findViewById(R.id.signup_pwd1);
        newpwdcheck = (EditText)findViewById(R.id.signup_pwd2);
        name_text = (EditText)findViewById(R.id.signup_name);
        gallery = (ImageView)findViewById(R.id.signup_picture);

        gallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);

            }
        });

        signupcomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_id = newid.getText().toString();
                new_pwd1 = newpwd.getText().toString();
                new_pwd2 = newpwdcheck.getText().toString();
                name = name_text.getText().toString();

                if(new_id.length()==0||new_pwd1.length()==0||new_pwd2.length()==0||name.length()==0){
                    Toast.makeText(getApplicationContext(), "회원가입을 완료해주세요.", Toast.LENGTH_SHORT).show();

                }


                if(new_pwd1.equals(new_pwd2)==true) {

                    String result="";
                    POSTSignupAPI postSignupAPI = new POSTSignupAPI();
                    try {
                        result = postSignupAPI.execute(new_id,new_pwd1,name).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("result",result);

                    if(result.equals("success"))
                    {
                        Toast.makeText(getApplicationContext(), "회원 가입 완료", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent2);
                    }
                    else if(result.equals("duplication"))
                    {
                        Toast.makeText(getApplicationContext(),"중복된 아이디입니다",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    }

                        }

                        else if (new_pwd1.equals(new_pwd2) == false) {
                        Toast.makeText(getApplicationContext(), "비밀번호를 다르게 입력했습니다", Toast.LENGTH_SHORT).show();
                    }


                }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

//                    배치해놓은 ImageView에 이미지를 넣어봅시다.
                    picture = bitmap;
                    gallery.setImageBitmap(picture);
//                    Glide.with(mContext).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView); // OOM 없애기위해 그레들사용



                } catch (Exception e) {
                    Log.e("test", e.getMessage());
                }
            }
        }
    }
}

