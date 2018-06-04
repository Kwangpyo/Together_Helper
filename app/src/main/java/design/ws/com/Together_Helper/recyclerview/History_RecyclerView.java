package design.ws.com.Together_Helper.recyclerview;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

import design.ws.com.Together_Helper.API.GETHistoryAPITask;
import design.ws.com.Together_Helper.adapter.HistoryAdapter;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;

public class History_RecyclerView extends Activity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private HistoryAdapter mAdapter;
    private Helper HELPER_ME;
    private ArrayList<Help> HelpList;

    private TextView title;
    private ImageView home;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history__recycler_view);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.historyview_swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.historyview_recycleview);
        home = (ImageView)findViewById(R.id.back_home);
        back = (ImageView)findViewById(R.id.back_back);
        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("봉사 내역 보기");

        home.setVisibility(View.GONE);

        Intent intent = getIntent();
        Helper helper = (Helper)intent.getSerializableExtra("helper");
        HELPER_ME = helper;

        GETHistoryAPITask t = new GETHistoryAPITask();
        try {
            HelpList = t.execute(HELPER_ME.getId()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(HelpList.size()==0)
        {
            Toast.makeText(getApplicationContext(),"검색된 결과가 없습니다.",Toast.LENGTH_SHORT).show();
        }



        mAdapter = new HistoryAdapter(History_RecyclerView.this,HelpList,HELPER_ME);




        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(History_RecyclerView.this);
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

                        GETHistoryAPITask t = new GETHistoryAPITask();
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
                            Toast.makeText(getApplicationContext(),"검색된 결과가 없습니다.",Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new HistoryAdapter(History_RecyclerView.this,HelpList,HELPER_ME);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, 2000);
            }
        });


        back.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

               finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        return;
    }
}
