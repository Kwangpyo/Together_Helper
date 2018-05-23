package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Rp on 6/14/2016.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {
Context context;

    private List<Help> helpList;

    Geocoder geocoder;
    String address;

    private Context mContext;
    private Helper HELPER_ME;
    String photoURL="";
    Help help;
    Bitmap bitmap;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView matching_status;
        TextView Helpee_name;
        TextView Help_location;
        TextView Helpee_detail;
        TextView Help_time;
        TextView Help_start;
        TextView help_cancel;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.main_image);
            matching_status = (TextView) view.findViewById(R.id.help_status);
            Helpee_name = (TextView) view.findViewById(R.id.Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.Help_location);
            Helpee_detail = (TextView) view.findViewById(R.id.helpee_detail);
            Help_time = (TextView) view.findViewById(R.id.Help_time);
            Help_start = (TextView) view.findViewById(R.id.Help_startfinish);
            help_cancel = (TextView)view.findViewById(R.id.Help_cancel);

            Helpee_detail.setOnClickListener(this);
            help_cancel.setOnClickListener(this);
            Help_start.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == Helpee_detail.getId()) {
                help = helpList.get(getAdapterPosition());
                 Intent intent = new Intent(context,Helpee_detail_popup.class);
                 intent.putExtra("helpee",help.getHelpeeId());
                 context.startActivity(intent);

            } else if(v.getId() == help_cancel.getId()){
                help = helpList.get(getAdapterPosition());
                Intent intent = new Intent(context,Help_cancel_popup.class);
                intent.putExtra("helperid",HELPER_ME.getId());
                intent.putExtra("helpid",help.getHelpId());
                intent.putExtra("helper",HELPER_ME);
                context.startActivity(intent);
            }
            else if(v.getId() == Help_start.getId()) {
                help = helpList.get(getAdapterPosition());


                if(help.getMatch_status()==2){

                Date from_date = new Date();
                from_date.setYear(help.getYear());
                from_date.setMonth(help.getMonth());
                from_date.setDate(help.getDay());
                from_date.setHours(help.getHour());
                from_date.setMinutes(help.getMinute());

                Date late_date = new Date();
                late_date.setHours(help.getHour());
                late_date.setMinutes(help.getMinute() + 10);
                late_date.setYear(help.getYear());
                late_date.setMonth(help.getMonth());
                late_date.setDate(help.getDay());

                Calendar cals = Calendar.getInstance();
                int nowyear = cals.get(cals.YEAR);
                int nowmonth = cals.get(cals.MONTH) + 1;
                int nowdate = cals.get(cals.DATE);
                int nowhour = cals.get(cals.HOUR_OF_DAY);
                int nowmin = cals.get(cals.MINUTE);

                Date now_date = new Date();
                now_date.setYear(nowyear);
                now_date.setMonth(nowmonth);
                now_date.setDate(nowdate);
                now_date.setHours(nowhour);
                now_date.setMinutes(nowmin);

                Log.d("fromhour", from_date.toString());
                Log.d("latehour", late_date.toString());
                Log.d("nowdate", now_date.toString());

                if (from_date.compareTo(now_date) > 0 && late_date.compareTo(now_date) > 0) {
                    Toast.makeText(context, "아직 약속 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                } else if (late_date.compareTo(now_date) > 0 && from_date.compareTo(now_date) < 0) {

                    Intent intent = new Intent(context, Help_start_popup.class);
                    intent.putExtra("helperid", HELPER_ME.getId());
                    intent.putExtra("helpid", help.getHelpId());
                    intent.putExtra("helper", HELPER_ME);
                    context.startActivity(intent);
                } else if (late_date.compareTo(now_date) < 0 && from_date.compareTo(now_date) < 0) {
                    Toast.makeText(context, "약속 시간이 지났습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                }


            }

            else
                {
                    Toast.makeText(context,"아직 Helpee가 봉사 승인을 하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }





                Intent intent = new Intent(context,Help_start_popup.class);
                intent.putExtra("helperid",HELPER_ME.getId());
                intent.putExtra("helpid",help.getHelpId());
                intent.putExtra("helper",HELPER_ME);
                context.startActivity(intent);






            }

        }


    }


    public HelpAdapter(MainActivity mainActivityContacts, List<Help> helpsList,Helper helper) {
        this.helpList = helpsList;
       this.context = mainActivityContacts;
        mContext = mainActivityContacts;
        HELPER_ME = helper;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_recycler_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        help = helpList.get(position);
        if(help.getMatch_status() == 1)
        {
            holder.matching_status.setText("매칭 중");
        }

        else if(help.getMatch_status() == 2)
        {
            holder.matching_status.setText("매칭 완료");

            if(help.getStart_status()==1)
            {
                holder.matching_status.setText("봉사 중");
            }

        }

        holder.Helpee_name.setText(help.getHelpeeId());

        geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> list = null;
        try {
            double d1 = help.getLat();
            double d2 = help.getLon();

            list = geocoder.getFromLocation(
                    d1, // 위도
                    d2, // 경도
                    10); // 얻어올 값의 개수
        }
        catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size()==0) {
                Log.d("geocoder","no address");
            } else {
                address = list.get(0).getAddressLine(0);
            }
        }


      holder.Help_location.setText(address);
      holder.Help_time.setText(help.getMonth() +"월 "+help.getDay()+"일 " + help.getHour()+"시 " + help.getMinute()+"분");


        GETPhotoURLAPITask t = new GETPhotoURLAPITask();

        try
        {
            photoURL = t.execute(help.getHelpeeId()).get();
        }

        catch (InterruptedException e) {
            e.printStackTrace();

        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("photoURL",photoURL);



        Thread mThread = new Thread(){
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
            holder.image.setImageBitmap(bitmap);
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        if(helpList!=null)
        return helpList.size();
        else
            return 0;
    }

}


