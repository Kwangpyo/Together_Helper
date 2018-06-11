package design.ws.com.Together_Helper.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.app.AlarmManager;
import android.location.Geocoder;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GETPhotoURLAPITask;
import design.ws.com.Together_Helper.API.PUT.PUTArriveAPI;
import design.ws.com.Together_Helper.API.PUT.PUTDepartAPI;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.activity.MainActivity;
import design.ws.com.Together_Helper.activity.MonitorActivity;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.popup.Help_cancel_popup;
import design.ws.com.Together_Helper.popup.Help_finish_popup;
import design.ws.com.Together_Helper.popup.Helpee_detail_popup;
import design.ws.com.Together_Helper.popup.Photo_popup;
import design.ws.com.Together_Helper.receiver.LocationReceiver;
import design.ws.com.Together_Helper.receiver.processTimerReceiver;
import design.ws.com.Together_Helper.util.Sub;

import static android.content.Context.ALARM_SERVICE;

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
        TextView Help_finish;
        TextView help_cancel;
        LinearLayout adpaterView;
        TextView help_arrive;
        TextView helpee_location;
        LinearLayout linearLayout;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.main_image);
            matching_status = (TextView) view.findViewById(R.id.help_status);
            Helpee_name = (TextView) view.findViewById(R.id.Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.Help_location);
            Helpee_detail = (TextView) view.findViewById(R.id.helpee_detail);
            Help_time = (TextView) view.findViewById(R.id.Help_time);
            Help_finish = (TextView) view.findViewById(R.id.Help_startfinish);
            help_cancel = (TextView)view.findViewById(R.id.Help_cancel);
            adpaterView = (LinearLayout)view.findViewById(R.id.main_adapter);
            help_arrive = (TextView)view.findViewById(R.id.Help_arrive);
            helpee_location = (TextView)view.findViewById(R.id.Help_arrive_helpee_location);
            linearLayout = (LinearLayout)view.findViewById(R.id.help_loc_linear);

            Helpee_detail.setOnClickListener(this);
            help_cancel.setOnClickListener(this);
            Help_finish.setOnClickListener(this);
            image.setOnClickListener(this);
            help_arrive.setOnClickListener(this);
            helpee_location.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (v.getId() == Helpee_detail.getId()) {
                help = helpList.get(getAdapterPosition());
                 Intent intent = new Intent(context,Helpee_detail_popup.class);
                 intent.putExtra("helpee",help.getHelpeeId());
                 intent.putExtra("help",help);
                 context.startActivity(intent);

            } else if(v.getId() == help_cancel.getId()){
                help = helpList.get(getAdapterPosition());

                Intent intent = new Intent(context,Help_cancel_popup.class);
                intent.putExtra("helperid",HELPER_ME.getId());
                intent.putExtra("helpid",help.getHelpId());
                intent.putExtra("helper",HELPER_ME);
                context.startActivity(intent);
            }
            else if(v.getId() == Help_finish.getId()) {
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
             //       Toast.makeText(context, "아직 약속 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                } else if (late_date.compareTo(now_date) > 0 && from_date.compareTo(now_date) < 0) {

                    Intent intent = new Intent(context, Help_finish_popup.class);
                    intent.putExtra("helperid", HELPER_ME.getId());
                    intent.putExtra("helpid", help.getHelpId());
                    intent.putExtra("helper", HELPER_ME);
                    context.startActivity(intent);
                } else if (late_date.compareTo(now_date) < 0 && from_date.compareTo(now_date) < 0) {
               //     Toast.makeText(context, "약속 시간이 지났습니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show();
                }


            }


            else
                {
                 //   Toast.makeText(context,"아직 Helpee가 봉사 승인을 하지 않았습니다.",Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(context,Help_finish_popup.class);
                Log.d("sibal", String.valueOf(help.getHelpId()));
                intent.putExtra("help",help);
                intent.putExtra("helper",HELPER_ME);
                context.startActivity(intent);

            }

            else if(v.getId() == image.getId())
            {
                help = helpList.get(getAdapterPosition());
                String photoURL1="";
                GETPhotoURLAPITask t = new GETPhotoURLAPITask();

                try
                {
                    photoURL1 = t.execute(help.getHelpeeId()).get();
                }

                catch (InterruptedException e) {
                    e.printStackTrace();

                }
                catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context,Photo_popup.class);
                intent.putExtra("url",photoURL1);
                context.startActivity(intent);
            }

            else if(v.getId()==help_arrive.getId())
            {
/*


*/
                help = helpList.get(getAdapterPosition());

                Log.d("arrivedepart", String.valueOf(help_arrive.getText()));

                if(help_arrive.getText().equals("출발"))
                {
                    PUTDepartAPI putDepartAPI = new PUTDepartAPI();
                    putDepartAPI.execute(help.getHelpId());
                    help_arrive.setText("도착");
       /*             int repeatTime = 1;  //Repeat alarm time in seconds
                    AlarmManager processTimer = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                    Intent intent1 = new Intent(context, LocationReceiver.class);
                    Log.d("pqpqpqp",HELPER_ME.getId());
                    intent1.putExtra("helper",HELPER_ME.getId());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,  intent1, PendingIntent.FLAG_UPDATE_CURRENT);
//Repeat alarm every second
                    processTimer.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),repeatTime*5000, pendingIntent);
*/
                    Intent intent2 = new Intent(context,MonitorActivity.class);
                    intent2.putExtra("help",help);
                    intent2.putExtra("helper",HELPER_ME);
                    context.startActivity(intent2);
                }

                else if(help_arrive.getText().equals("도착"))
                {
                    PUTArriveAPI putArriveAPI = new PUTArriveAPI();
                    putArriveAPI.execute(help.getHelpId());

  /*                  Intent intent2 = new Intent(context, LocationReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);*/

                    help_arrive.setText("");
                }
                else
                {
                    help_arrive.setText("");
                }

            }

            else if(v.getId() == helpee_location.getId())
            {
                help = helpList.get(getAdapterPosition());
                Intent intent1 = new Intent(context,MonitorActivity.class);
                intent1.putExtra("help",help);
                intent1.putExtra("helper",HELPER_ME);
                context.startActivity(intent1);


            }





        }

    }


    public HelpAdapter(MainActivity mainActivityContacts, List<Help> helpsList, Helper helper) {
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

        Log.d("help.get", String.valueOf(help.getMatch_status()));
        Log.d("help.get2", String.valueOf(help.getStart_status()));
        Log.d("help.get3",help.getHelperDepartStatus());

        if(help.getMatch_status() == 1)
        {
            holder.matching_status.setText("매칭 중");
            holder.linearLayout.setVisibility(View.GONE);
        }

        else if(help.getMatch_status() == 2)
        {
            holder.matching_status.setText("매칭 완료");
            holder.linearLayout.setVisibility(View.VISIBLE);

            if(help.getStart_status()==1)
            {
                holder.help_arrive.setText("도착");
/*                Intent intent2 = new Intent(context, LocationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);*/

                holder.linearLayout.setVisibility(View.GONE);
                holder.matching_status.setText("봉사 중");
                holder.help_cancel.setVisibility(View.GONE);
            }

        }

        if(help.getHelperDepartStatus().equals("standBy"))
        {
            holder.help_arrive.setText("출발");
        }

        else if(help.getHelperDepartStatus().equals("depart"))
        {
            holder.help_arrive.setText("도착");
          /*  Intent intent2 = new Intent(context, LocationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent2, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);*/
        }
        else
        {
            holder.help_arrive.setText("");
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

        if(help.getStart_status() ==1)
        {
            holder.Help_finish.setVisibility(View.VISIBLE);
        }


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


        Picasso.with(context)
                .load(photoURL)
                .into(holder.image);



    }


    @Override
    public int getItemCount() {
        if(helpList!=null)
        return helpList.size();
        else
            return 0;
    }





}


