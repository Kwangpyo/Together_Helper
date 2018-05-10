package design.ws.com.Together_Helper;

import android.content.Intent;
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
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {


    private ArrayList<Help> HelpList;

    private RecyclerView recyclerView;
    private HelpAdapter mAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView location_search;
    private TextView custom_search;
    private TextView current_help;
 //   private TextView help_detail;
 //   private TextView helpee_detail;
    private ImageView profile;

//    Helpee helpee1 = new Helpee("준민이","june");
//    Help help1 = new Help(helpee1, 125, 123, 10, 24, 1, 2018, 10, 3, "외출", 1, 1, "남자만");

//    Helpee helpee2 = new Helpee("보원이","bowon");
//    Help help2 = new Help(helpee2, 124, 123, 10, 24, 1, 2018, 10, 3, "외출", 1, 1, "남자만");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        location_search = (TextView)findViewById(R.id.location_search);
        custom_search = (TextView)findViewById(R.id.custom_search);
        current_help = (TextView)findViewById(R.id.current_help);
       // help_detail = (TextView)findViewById(R.id.help_detail);
       // helpee_detail = (TextView)findViewById(R.id.helpee_detail);
        profile = (ImageView)findViewById(R.id.profile);

        HelpList = new ArrayList<>();

  //      HelpList.add(help1);
  //      HelpList.add(help2);


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
                        ArrayList<Help> HelpList1 = new ArrayList<>();
   //                     HelpList1.add(help2);
   //                     HelpList1.add(help1);
                        mAdapter = new HelpAdapter(MainActivity.this,HelpList1);
                        recyclerView.setAdapter(mAdapter);
                    }
                }, 2000);
            }
        });



        Log.d("qwe","qwe");

        mAdapter = new HelpAdapter(MainActivity.this,HelpList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        custom_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CustomSearch.class);
                startActivity(intent);
            }
        });

        location_search.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LocationSearchMap.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Profile_popup.class);
                startActivity(intent);

            }
        });

        current_help.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"미구현",Toast.LENGTH_SHORT).show();

            }
        });

    }
    }

