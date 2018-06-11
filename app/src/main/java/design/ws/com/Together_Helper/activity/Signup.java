package design.ws.com.Together_Helper.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.POST.POSTSignupAPI;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.service.FileUploadService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Signup extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private final int REQUEST_READ_PHONE_STATE =1;

    Button signupcomplete;
    private TextView newid;
    EditText newpwd, newpwdcheck, name_text;
    String new_id;
    String new_pwd1;
    String new_pwd2;
    String name;
    ImageView gallery;
    Bitmap picture;

    ByteArrayOutputStream output;
    byte[] imageBytes;

    private int gallery_flag=0;

    private String PhoneNum="";

    @SuppressLint({"HardwareIds", "MissingPermission"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ImageView Togethericon = (ImageView) findViewById(R.id.signup_background);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(Togethericon);
        Glide.with(this).load(R.drawable.signupgif).into(gifImage);

        signupcomplete = (Button)findViewById(R.id.login_loginButton);
        newid = (TextView)findViewById(R.id.signup_id);
        newpwd = (EditText)findViewById(R.id.signup_pwd1);
        newpwdcheck = (EditText)findViewById(R.id.signup_pwd2);
        name_text = (EditText)findViewById(R.id.signup_name);
        gallery = (ImageView)findViewById(R.id.signup_picture);


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);


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

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }


        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        PhoneNum = telManager.getLine1Number();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }
        newid.setText(PhoneNum);




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

                //new_id = newid.getText().toString();
                new_id = PhoneNum;
              //  new_id = "01079825334";
                new_pwd1 = newpwd.getText().toString();
                new_pwd2 = newpwdcheck.getText().toString();
                name = name_text.getText().toString();

                if(new_id.length()==0||new_pwd1.length()==0||new_pwd2.length()==0||name.length()==0){
                    Toast.makeText(getApplicationContext(), "회원가입을 완료해주세요.", Toast.LENGTH_SHORT).show();

                }


                if(new_pwd1.equals(new_pwd2)==true &&gallery_flag==1) {

                    String result="";
                    String uniqueID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    POSTSignupAPI postSignupAPI = new POSTSignupAPI();
                    Log.d("signupuniqueID",uniqueID);
                    try {

                        result = postSignupAPI.execute(new_id,new_pwd1,name,uniqueID).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("signupresult",result);

                    if(result.equals("success"))
                    {

                        output = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 10, output);
                        imageBytes = output.toByteArray();


                        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
                        File file = wrapper.getDir("Images", MODE_PRIVATE);
                        file = new File(file, "UniqueFileName" + ".jpg");
                        try {
                            OutputStream stream = null;
                            stream = new FileOutputStream(file);
                            picture.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                            stream.flush();
                            stream.close();
                        } catch (IOException e) // Catch the exception
                        {
                            e.printStackTrace();
                        }

                        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

                        uploadFile(savedImageURI);

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
                    gallery_flag=1;
//                    Glide.with(mContext).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView); // OOM 없애기위해 그레들사용



                } catch (Exception e) {
                    Log.e("test", e.getMessage());
                }
            }
        }
    }






    private void uploadFile(Uri fileUri) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

// Change base URL to your upload server URL.
        FileUploadService service = new Retrofit.Builder().baseUrl("http://210.89.191.125").client(client).build().create(FileUploadService.class);


        File file = new File(String.valueOf(fileUri));
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", file.getName(), reqFile);


        RequestBody user_phone = RequestBody.create(MediaType.parse("text"), new_id);

        Call<ResponseBody> req = service.upload(user_phone,body);

        req.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                Log.d("fadsfsads", "Success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("fadsfsads", "afsdsdf");
            }

        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO


                }
                break;

            default:
                break;
        }
    }



}

