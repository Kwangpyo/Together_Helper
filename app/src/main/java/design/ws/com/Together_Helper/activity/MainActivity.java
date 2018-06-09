package design.ws.com.Together_Helper.activity;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GETCheckPauseUserAPITask;
import design.ws.com.Together_Helper.API.GET.GETHelperPhotoURLAPITask;
import design.ws.com.Together_Helper.API.GET.GETMyHelpAPITask;
import design.ws.com.Together_Helper.API.GET.GETReservationCheckAPITask;
import design.ws.com.Together_Helper.API.PUT.PUTUpdateLocation;
import design.ws.com.Together_Helper.params.ReserveParam;
import design.ws.com.Together_Helper.popup.RejectUser_popup;
import design.ws.com.Together_Helper.popup.ReserveState_popup;
import design.ws.com.Together_Helper.util.GPSInfo;
import design.ws.com.Together_Helper.adapter.HelpAdapter;
import design.ws.com.Together_Helper.recyclerview.History_RecyclerView;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.popup.Explain_popup;
import design.ws.com.Together_Helper.popup.Photo_popup;
import design.ws.com.Together_Helper.popup.Reserve_popup;
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

public class MainActivity extends AppCompatActivity {

    private ArrayList<Help> HelpList;

    private RecyclerView recyclerView;
    private HelpAdapter mAdapter;

    private Helper HELPER_ME;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Button location_search;
    private Button custom_search;
    private TextView registered_help;
    private Button register_btn;

    public static FloatingActionButton fab;

    private String pushdata;

    private GPSInfo gps;

    double longitude;
    double latitude;


    private TextView id_txt;
    private TextView name_txt;
    private ImageView image;
    TextView change_picture;
    Bitmap picture;
    private String photoURL="";
    int changeflag=0;
    private TextView helptime_txt;
    private TextView logout_txt;
    private Button help_history;
    private TextView feedback_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        location_search = (Button) findViewById(R.id.location_search);
        custom_search = (Button) findViewById(R.id.custom_search);
        registered_help = (TextView) findViewById(R.id.main_registered_help);
        fab = (FloatingActionButton) findViewById(R.id.main_fab);
        register_btn = (Button)findViewById(R.id.help_reserve);
        id_txt = (TextView)findViewById(R.id.profile_id);
        name_txt = (TextView)findViewById(R.id.profile_name);
        image = (ImageView) findViewById(R.id.profile_image);
        change_picture =(TextView)findViewById(R.id.profile_change_picture);
        helptime_txt = (TextView)findViewById(R.id.profile_helptime);
        logout_txt = (TextView)findViewById(R.id.profile_logout);
        help_history = (Button)findViewById(R.id.help_history);
        feedback_txt = (TextView)findViewById(R.id.profile_feedback);

        Intent intent = getIntent();
        Helper helper = (Helper) intent.getSerializableExtra("helper");
        HELPER_ME = helper;
        Log.d("mainhelper", helper.getId());
        Integer intentFlag = intent.getIntExtra("intentflag",-1);



        if(intentFlag==1)
        {
            Intent intent2 = new Intent(getApplicationContext(),LocationSearchMap.class);
            intent2.putExtra("helper",HELPER_ME);
            startActivity(intent2);
        }

/*
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        appNetwork receiver = new appNetwork(this);
        registerReceiver(receiver, filter);
*/


        String checkUserFlag="";
        GETCheckPauseUserAPITask getCheckPauseUserAPITask = new GETCheckPauseUserAPITask();
        try {
            checkUserFlag = getCheckPauseUserAPITask.execute(helper.getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(checkUserFlag.equals("reject"))
        {
            Intent intent1 = new Intent(getApplicationContext(),RejectUser_popup.class);
            startActivity(intent1);
        }


        id_txt.setText("아이디 : "+helper.getId());
        if(helper.getName()==null)
        {
            name_txt.setText("");
        }
        else {
            name_txt.setText("이름 : "+helper.getName());
        }

        helptime_txt.setText("총 봉사 시간 : "+HELPER_ME.getAdmitTime());

        feedback_txt.setText("평점 : "+HELPER_ME.getFeedback());

        GETHelperPhotoURLAPITask t2 = new GETHelperPhotoURLAPITask();

        try
        {
            photoURL = t2.execute(helper.getId()).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("photoURL",photoURL);

        Picasso.with(getApplicationContext())
                .load(photoURL)
                .into(image);

        gps = new GPSInfo(getApplicationContext());

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {
             latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.d("fdasds",String.valueOf(latitude));
            Log.d("fdasds",String.valueOf(longitude));

        } else {
            // GPS 를 사용할수 없으므로
           // gps.showSettingsAlert();
            Snackbar.make(fab,"GPS를 사용할 수 없습니다", Snackbar.LENGTH_LONG).show();

        }


        PUTUpdateLocation putUpdateLocation = new PUTUpdateLocation();
        putUpdateLocation.execute(String.valueOf(latitude),String.valueOf(longitude),HELPER_ME.getId());


        HelpList = new ArrayList<>();

        GETMyHelpAPITask t = new GETMyHelpAPITask();
        try
        {
            HelpList = t.execute(HELPER_ME.getId()).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(HelpList.size()==0)
        {
            registered_help.setText("신청한 봉사가 없습니다.");
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Simulates a long running task (updating data)
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("asd","asd");

                        GETMyHelpAPITask t = new GETMyHelpAPITask();
                        try
                        {
                            HelpList = t.execute(HELPER_ME.getId()).get();
                        }

                        catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        catch (ExecutionException e) {
                            e.printStackTrace();
                        }


                        if(HelpList.size()==0)
                        {
                            registered_help.setText("신청한 봉사가 없습니다.");
                            Snackbar.make(fab,"신청한 봉사가 없습니다", Snackbar.LENGTH_LONG).show();
                        }

                        mAdapter = new HelpAdapter(MainActivity.this,HelpList,HELPER_ME);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, 2000);
            }
        });



        Log.d("qwe","qwe");

        mAdapter = new HelpAdapter(MainActivity.this,HelpList,HELPER_ME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        custom_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CustomSearch.class);
                intent.putExtra("helper",HELPER_ME);
                startActivity(intent);
            }
        });

        location_search.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LocationSearchMap.class);
                intent.putExtra("helper",HELPER_ME);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view,"fab", Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),Explain_popup.class);
                intent.putExtra("helper",HELPER_ME);
                startActivity(intent);

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                ReserveParam reserve_test = null;
                GETReservationCheckAPITask getReservationCheckAPITask = new GETReservationCheckAPITask();
                try {
                    reserve_test = getReservationCheckAPITask.execute(HELPER_ME.getId()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }



                if(reserve_test.getCount().equals("1"))
                {
                    Intent intent = new Intent(getApplicationContext(),ReserveState_popup.class);
                    intent.putExtra("helper",HELPER_ME);
                    intent.putExtra("reserveparam",reserve_test);
                    startActivity(intent);
                }

                else if(reserve_test.getCount().equals("0"))
                {
                    Intent intent = new Intent(getApplicationContext(),Reserve_popup.class);
                    intent.putExtra("helper",HELPER_ME);
                    startActivity(intent);

                }

                else
                {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                }



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


        logout_txt.setOnClickListener(new View.OnClickListener()
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


        help_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),History_RecyclerView.class);
                intent.putExtra("helper",HELPER_ME);
                startActivity(intent);

            }
        });


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

        RequestBody user_phone = RequestBody.create(MediaType.parse("text"), HELPER_ME.getId());

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

