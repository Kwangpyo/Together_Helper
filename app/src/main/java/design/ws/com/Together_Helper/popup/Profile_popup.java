package design.ws.com.Together_Helper.popup;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GETHelperPhotoURLAPITask;
import design.ws.com.Together_Helper.service.FileUploadService;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.recyclerview.History_RecyclerView;
import design.ws.com.Together_Helper.activity.Login;
import design.ws.com.Together_Helper.R;
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

public class Profile_popup extends Activity {

    TextView logout;
    TextView id;
    TextView name;
    TextView helphistory;
    Helper helper;
    ImageView image;
    TextView change_picture;

    String photoURL="";
    Bitmap bitmap;

    Bitmap picture;
    int changeflag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile_popup);

        logout = (TextView)findViewById(R.id.profile_logout);
        id = (TextView)findViewById(R.id.profile_id);
        name = (TextView)findViewById(R.id.profile_name);
        helphistory =(TextView)findViewById(R.id.profile_helphistory);
        image = (ImageView) findViewById(R.id.profile_image);
        change_picture =(TextView)findViewById(R.id.profile_change_picture);

        Intent intent = getIntent();
        helper = (Helper)intent.getSerializableExtra("helper");

        id.setText(helper.getId());
        if(helper.getName()==null)
        {
            name.setText("");
        }
        else {
            name.setText(helper.getName());
        }


        GETHelperPhotoURLAPITask t = new GETHelperPhotoURLAPITask();

        try
        {
            photoURL = t.execute(helper.getId()).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("photoURL",photoURL);



/*        Thread mThread = new Thread(){
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
            image.setImageBitmap(bitmap);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
*/

        Picasso.with(getApplicationContext())
                .load(photoURL)
                .into(image);




        logout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent1);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor  = auto.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getApplicationContext(), "로그아웃", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        helphistory.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),History_RecyclerView.class);
                intent.putExtra("helper",helper);
                startActivity(intent);
            }
        });



        change_picture.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                if(changeflag==0)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                    //change_picture.setText("확인");
                    //changeflag=1;
                }

                else if(changeflag==1)
                {
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
                    changeflag=0;
                    change_picture.setText("사진 변경하기");

                }



            }
        });

        image.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),Photo_popup.class);
                intent.putExtra("url",photoURL);
                startActivity(intent);

            }
        });



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

//                    배치해놓은 ImageView에 이미지를 넣어봅시다.

                    if(bitmap!=null) {
                        picture = bitmap;
                        image.setImageBitmap(picture);
                        change_picture.setText("확인");
                        changeflag = 1;
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"사진을 선택하지 않았습니다.",Toast.LENGTH_SHORT).show();
                    }
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

Log.d("profileuri", String.valueOf(fileUri));
        File file = new File(String.valueOf(fileUri));
        Log.d("adf",String.valueOf(file.length()));
        RequestBody reqFile = RequestBody.create(MediaType.parse("file"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", file.getName(), reqFile);

        RequestBody user_phone = RequestBody.create(MediaType.parse("text"), helper.getId());

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


}

