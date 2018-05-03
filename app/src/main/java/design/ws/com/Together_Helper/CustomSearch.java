package design.ws.com.Together_Helper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomSearch extends AppCompatActivity {

    private ImageView refresh;
    private ImageView home;
    private TextView title;
    private TextView mindate;
    private TextView maxdate;

    private Button mindate_btn;
    private Button maxdate_btn;
    private Button clock_btn;
    private TextView clock_txt;

    DatePickerDialog dialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);

        refresh = (ImageView)findViewById(R.id.toolbar_refresh);
        home = (ImageView)findViewById(R.id.home);

        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("맞춤검색");
        mindate_btn = (Button)findViewById(R.id.customsearch_mindate_btn);
        maxdate_btn = (Button)findViewById(R.id.customsearch_maxdate_btn);
        mindate = (TextView)findViewById(R.id.custom_search_mindate_txt);
        maxdate = (TextView)findViewById(R.id.custom_search_maxdate_txt);
        clock_btn = (Button)findViewById(R.id.customsearch_clock_btn);
        clock_txt = (TextView)findViewById(R.id.custom_search_clock_txt);

        Calendar cal = Calendar.getInstance();

        today_year = cal.get(Calendar.YEAR);
        today_month = cal.get(Calendar.MONTH);
        today_day = cal.get(Calendar.DATE);

        calendar_flag=0;




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
                                mindate.setText("select date : "+ min_year + "-"+min_month+"-"+min_day);
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
                                    maxdate.setText("select date : "+ max_year + "-"+max_month+"-"+max_day);
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

        clock_btn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

            }
        });


        

        home.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(),"미구현",Toast.LENGTH_SHORT).show();

            }
        });


    }



}
