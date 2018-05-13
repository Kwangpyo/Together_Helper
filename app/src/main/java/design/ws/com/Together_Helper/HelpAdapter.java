package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rp on 6/14/2016.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder> {
Context context;

    boolean showingFirst = true;

    private List<Help> helpList;

    ImageView NormalImageView;
    Bitmap ImageBit;
    float ImageRadius = 40.0f;

    Geocoder geocoder;
    String address;

    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

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

            image = (ImageView) view.findViewById(R.id.image);
            matching_status = (TextView) view.findViewById(R.id.help_status);
            Helpee_name = (TextView) view.findViewById(R.id.Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.Help_location);
            Helpee_detail = (TextView) view.findViewById(R.id.helpee_detail);
            Help_time = (TextView) view.findViewById(R.id.Help_time);
            Help_start = (TextView) view.findViewById(R.id.Help_startfinish);
            help_cancel = (TextView)view.findViewById(R.id.Help_cancel);

        }
    }


    public HelpAdapter(MainActivity mainActivityContacts, List<Help> helpsList) {
        this.helpList = helpsList;
       this.context = mainActivityContacts;
        mContext = mainActivityContacts;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_recycler_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Help help = helpList.get(position);
        if(help.getMatch_status() == 1)
        {
            holder.matching_status.setText("매칭 중");
        }

        else if(help.getMatch_status() == 2)
        {
            holder.matching_status.setText("매칭 완료");
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
        //holder.image.setImageResource(movie.getImage());




        holder.Helpee_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Helpee_detail_popup.class);
                intent.putExtra("helpee",help.getHelpeeId());
                context.startActivity(intent);
            }
        });

        holder.Help_start.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                Toast.makeText(context,"미구현",Toast.LENGTH_SHORT).show();
            }
        });

        holder.help_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"미구현",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

}


