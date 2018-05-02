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
import java.util.Date;
import java.util.Locale;

public class CustomSearch extends AppCompatActivity {

    private ImageView refresh;
    private ImageView home;
    private TextView title;

    private Button date_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_search);

        refresh = (ImageView)findViewById(R.id.toolbar_refresh);
        home = (ImageView)findViewById(R.id.home);

        title = (TextView)findViewById(R.id.refreshtoolbar_text);
        title.setText("맞춤검색");
        date_btn = (Button)findViewById(R.id.customsearch_date_btn);

        final DatePickerDialog dialog = new DatePickerDialog(this, listener, 2013, 10, 22);





        date_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dialog.show();
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

        Toast.makeText(getApplicationContext(),"미구현",Toast.LENGTH_SHORT).show();


    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();

        }

    };

    public String getDateString()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }






}
