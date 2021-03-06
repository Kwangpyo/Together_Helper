package design.ws.com.Together_Helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import design.ws.com.Together_Helper.API.GET.GETPhotoURLAPITask;
import design.ws.com.Together_Helper.R;
import design.ws.com.Together_Helper.domain.Help;
import design.ws.com.Together_Helper.domain.Helper;
import design.ws.com.Together_Helper.popup.Question_popup;
import design.ws.com.Together_Helper.recyclerview.History_RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {


    private List<Help> helpList;
    private Context mContext;
    private Helper HELPER_ME;
    private Help help;

    Context context;
    Geocoder geocoder;
    String address;

    String photoURL="";
    Bitmap bitmap;

    public class MyViewHolder extends RecyclerView.ViewHolder{


        ImageView image;
        TextView Helpee_name;
        TextView Help_location;
        TextView help_accept;
        TextView help_duration;
        TextView help_type;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.history_image);
            Helpee_name = (TextView) view.findViewById(R.id.history_Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.history_Help_location);
            help_accept = (TextView)view.findViewById(R.id.history_Help_accept);
            help_duration = (TextView)view.findViewById(R.id.history_help_duration);
            help_type = (TextView)view.findViewById(R.id.history_help_type);


        }

    }


    public HistoryAdapter(History_RecyclerView mainActivityContacts, List<Help> helpsList, Helper helper) {
        this.helpList = helpsList;
        this.context = mainActivityContacts;
        mContext = mainActivityContacts;
        HELPER_ME = helper;
    }


    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_recycle_view, parent, false);

        return new HistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        help = helpList.get(position);

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

        Log.d("custom_helpeeid1",help.getHelpeeId());
        holder.Helpee_name.setText(help.getHelpeeId());
        holder.Help_location.setText(address);
        /*String type="";
        if(help.getType().equals("housework"))
        {
            type = "가사";
        }
        else if(help.getType().equals("outside"))
        {
            type = "외출";
        }
        else if(help.getType().equals("education"))
        {
            type = "교육";
        }
        else if(help.getType().equals("talk"))
        {
            type = "말동무";
        }
        holder.help_type.setText("종류: " +type);*/
        holder.help_type.setText("봉사 시간: " +help.getYear()+"년 "+help.getMonth()+"월 "+help.getDay()+"일");
        holder.help_duration.setText("승인 시간: "+help.getDuration()+"시간");

        if(help.getAccept_status().equals("wait"))
        {
            holder.help_accept.setText("승인 대기 중");
            holder.help_accept.setTextColor(Color.parseColor("#228b22"));
        }

        else if(help.getAccept_status().equals("accept"))
        {
            holder.help_accept.setText("승인 완료");
            holder.help_accept.setTextColor(Color.parseColor("#0000ff"));
        }

        else if(help.getAccept_status().equals("reject"))
        {
            holder.help_accept.setText("승인 거부");
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
        return helpList.size();
    }
}
