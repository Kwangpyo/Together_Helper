package design.ws.com.Together_Helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CustomSearch extends AppCompatActivity {

    private ImageView refresh;
    private ImageView home;
    private TextView title;


    private ImageView mindate_btn;
    private ImageView maxdate_btn;
    private ImageView minclock_btn;
    private ImageView maxclock_btn;

    private TextView mindate;
    private TextView maxdate;
    private TextView minclock_txt;
    private TextView maxclock_txt;

    private EditText loc_txt;

    private TextView search_btn;

    private Spinner type_spinner;

    ArrayList<Help> ps = new ArrayList<>();

    Integer today_year;
    Integer today_month;
    Integer today_day;

    Integer min_year;
    Integer min_month;
    Integer min_day;
    Integer max_year;
    Integer max_month;
    Integer max_day;
    int calendar_flag;
    Geocoder geocoder;
    Integer min_hour;
    Integer min_minute;
    Integer max_hour;
    Integer max_minute;
    String help_type;

    double lon;
    double lat;

    Helper HELPER_ME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);


        Intent intent = getIntent();
        Helper helper = (Helper)intent.getSerializableExtra("helper");
        HELPER_ME = helper;

        refresh = (ImageView)findViewById(R.id.toolbar_refresh);
        home = (ImageView)findViewById(R.id.home);
        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("맞춤검색");

        mindate = (TextView)findViewById(R.id.custom_search_mindate_txt);
        maxdate = (TextView)findViewById(R.id.custom_search_maxdate_txt);
        minclock_txt = (TextView)findViewById(R.id.custom_search_minclock_txt);
        maxclock_txt = (TextView)findViewById(R.id.custom_search_maxclock_txt);

        mindate_btn = (ImageView)findViewById(R.id.customsearch_mindate_img);
        maxdate_btn = (ImageView)findViewById(R.id.customsearch_maxdate_img);

        minclock_btn = (ImageView)findViewById(R.id.customsearch_minclock_img);
        maxclock_btn = (ImageView)findViewById(R.id.customsearch_maxclock_img);


        loc_txt = (EditText)findViewById(R.id.custom_search_loc_edittxt);
        search_btn = (TextView) findViewById(R.id.custom_search_find_btn);

        type_spinner = (Spinner)findViewById(R.id.custom_search_spinner);

        Calendar cal = Calendar.getInstance();

        today_year = cal.get(Calendar.YEAR);
        today_month = cal.get(Calendar.MONTH);
        today_day = cal.get(Calendar.DATE);

        calendar_flag=0;

        geocoder = new Geocoder(this, Locale.getDefault());



        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                help_type = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });





        mindate_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Calendar pickedDate = Calendar.getInstance();
                Calendar minDate = Calendar.getInstance();

                pickedDate.set(today_year,today_month,today_day);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CustomSearch.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                min_year = year;
                                min_month = month+1;
                                min_day = dayOfMonth;
                                mindate.setText( min_year + "-"+min_month+"-"+min_day);
                                calendar_flag =1;
                            }
                        },
                        pickedDate.get(Calendar.YEAR),
                        pickedDate.get(Calendar.MONTH),
                        pickedDate.get(Calendar.DATE)
                );


                minDate.set(today_year,today_month,today_day);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());

                datePickerDialog.show();
            }
        });

        maxdate_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if(calendar_flag==0)
                {
                    Toast.makeText(getApplicationContext(),"MIN 날짜를 먼저 선택해 주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    Calendar pickedDate = Calendar.getInstance();
                    Calendar minDate = Calendar.getInstance();

                    pickedDate.set(min_year,min_month-1,min_day);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            CustomSearch.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                    max_year = year;
                                    max_month = month+1;
                                    max_day = dayOfMonth;
                                    maxdate.setText( max_year + "-"+max_month+"-"+max_day);
                                }
                            },
                            pickedDate.get(Calendar.YEAR),
                            pickedDate.get(Calendar.MONTH),
                            pickedDate.get(Calendar.DATE)
                    );


                    minDate.set(min_year,min_month-1,min_day);
                    datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());


                    datePickerDialog.show();

                }

            }
        });

        minclock_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = new TimePickerDialog(CustomSearch.this, listener_min, 15, 24, false);
                dialog.show();


            }
        });

        maxclock_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = new TimePickerDialog(CustomSearch.this, listener_max, 15, 24, false);
                dialog.show();


            }
        });


        search_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                String location_name =loc_txt.getText().toString();
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(
                            location_name, // 지역 이름
                            10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                  //  Toast.makeText(getApplicationContext(),"주소를 입력해주세요",Toast.LENGTH_SHORT).show();
                }

                if (list != null) {
                    if (list.size() == 0) {
                        Toast.makeText(getApplicationContext(),"해당되는 주소 정보는 없습니다",Toast.LENGTH_SHORT).show();
                    } else {
                        lat = list.get(0).getLatitude();
                        lon = list.get(0).getLongitude();

                        //          list.get(0).getCountryName();  // 국가명
                        //          list.get(0).getLatitude();        // 위도
                        //          list.get(0).getLongitude();    // 경도
                    }
                }



                GETCustomHelpAPITask t = new GETCustomHelpAPITask();

                try {
                    ParamsForCustom paramsForCustom = new ParamsForCustom(min_year, min_month, min_day, min_hour, min_minute, max_year, max_month, max_day, max_hour, max_minute, lat, lon, help_type);


                    try {
                        ps = t.execute(paramsForCustom).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    Log.d("customhelp_ps", ps.get(0).getHelpeeId());
                    Intent intent = new Intent(getApplicationContext(),Custom_RecyclerView.class);
                    intent.putExtra("helper",HELPER_ME);
                    intent.putExtra("help",ps);
                    startActivity(intent);

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"정보를 모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }



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

        refresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

            }
        });




    }


    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    private TimePickerDialog.OnTimeSetListener listener_min = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            minclock_txt.setText(hourOfDay + "시 " + minute + "분");
            min_hour = hourOfDay;
            min_minute = minute;
        }
    };

    private TimePickerDialog.OnTimeSetListener listener_max = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            if(min_hour==null)
            {
                Toast.makeText(getApplicationContext(),"MIN 시간을 먼저 선택해주세요",Toast.LENGTH_SHORT).show();
            }

            else{
/*
                if(min_hour <max_hour)
                {
                    if(min_minute < max_minute)
                    {


                    }
                }*/
                maxclock_txt.setText(hourOfDay + "시 " + minute + "분");
                max_hour = hourOfDay;
                max_minute = minute;

            }

            //maxclock_txt.setText(hourOfDay + "시 " + minute + "분");
            //max_hour = hourOfDay;
            //max_minute = minute;
        }
    };



}
