package design.ws.com.Together_Helper.recyclerview;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GETCustomHelpAPITask;
import design.ws.com.Together_Helper.adapter.CustomHelpAdapter;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.activity.CustomSearch;
import design.ws.com.Together_Helper.activity.MainActivity;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.params.ParamsForCustom;

public class Custom_RecyclerView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomHelpAdapter mAdapter;

    private TextView title;
    private ImageView home;
    private ImageView back;

    private Helper HELPER_ME;
    private ArrayList<Help> HelpList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom__recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.customview_swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.customview_recycleview);

        home = (ImageView)findViewById(R.id.back_home);
        back = (ImageView)findViewById(R.id.back_back);
        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("맞춤 검색");



        Intent intent = getIntent();
        Helper helper = (Helper)intent.getSerializableExtra("helper");
        HELPER_ME = helper;
        ParamsForCustom params = (ParamsForCustom)intent.getSerializableExtra("params");
        GETCustomHelpAPITask t = new GETCustomHelpAPITask();
        try {
          HelpList = t.execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(HelpList.size()==0)
        {
            Toast.makeText(getApplicationContext(),"검색된 결과가 없습니다.",Toast.LENGTH_SHORT).show();
        }

        mAdapter = new CustomHelpAdapter(Custom_RecyclerView.this,HelpList,HELPER_ME);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Custom_RecyclerView.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

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

                        if(HelpList.size()==0)
                        {
                            Toast.makeText(getApplicationContext(),"검색된 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                        mAdapter = new CustomHelpAdapter(Custom_RecyclerView.this,HelpList,HELPER_ME);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, 2000);
            }
        });

        home.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("helper",HELPER_ME);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),CustomSearch.class);
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
