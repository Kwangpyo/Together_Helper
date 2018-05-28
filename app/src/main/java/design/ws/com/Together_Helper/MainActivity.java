package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Help> HelpList;

    private RecyclerView recyclerView;
    private HelpAdapter mAdapter;

    private Helper HELPER_ME;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView location_search;
    private TextView custom_search;
    private TextView registered_help;

    private ImageView profile;
    public static FloatingActionButton fab;

    private String pushdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        location_search = (TextView) findViewById(R.id.location_search);
        custom_search = (TextView) findViewById(R.id.custom_search);
        profile = (ImageView) findViewById(R.id.profile);
        registered_help = (TextView) findViewById(R.id.main_registered_help);
        fab = (FloatingActionButton) findViewById(R.id.main_fab);

        Intent intent = getIntent();
        Helper helper = (Helper) intent.getSerializableExtra("helper");
        HELPER_ME = helper;
        Log.d("mainhelper", helper.getId());



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

        profile.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Profile_popup.class);
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

    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


    }

