package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CustomHelpAdapter extends RecyclerView.Adapter<CustomHelpAdapter.MyViewHolder> {


    private List<Help> helpList;
    private Context mContext;
    private Helper HELPER_ME;
    private Help help;

    Context context;
    Geocoder geocoder;
    String address;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView Helpee_name;
        TextView Help_location;
        TextView Helpee_detail;
        TextView Help_time;
        TextView Help_detail;
        TextView help_register;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.custom_image);
            Helpee_name = (TextView) view.findViewById(R.id.custom_Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.custom_Help_location);
            Helpee_detail = (TextView) view.findViewById(R.id.custom_helpee_detail);
            Help_time = (TextView) view.findViewById(R.id.custom_Help_time);
            Help_detail = (TextView) view.findViewById(R.id.custom_help_detail);
            help_register = (TextView)view.findViewById(R.id.custom_Help_register);

            help_register.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (view.getId() == help_register.getId()) {
                help = helpList.get(getAdapterPosition());
            }


        }
    }

    public CustomHelpAdapter(Custom_RecyclerView mainActivityContacts, List<Help> helpsList, Helper helper) {
        this.helpList = helpsList;
        this.context = mainActivityContacts;
        mContext = mainActivityContacts;
        HELPER_ME = helper;
    }



    @Override
    public CustomHelpAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recycler_view, parent, false);

        return new CustomHelpAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomHelpAdapter.MyViewHolder holder, final int position) {

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
        holder.Help_time.setText(help.getMonth() +"월 "+help.getDay()+"일 " + help.getHour()+"시 " + help.getMinute()+"분");
        holder.Help_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {



            }
        });

        holder.help_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if(itemClick != null)
                {
                    itemClick.onClick(view,position,help);
                }
/*
                Log.d("custom_helpeeid2",help.getHelpeeId());
                Intent intent = new Intent(context,RegisterHelp_popup.class);
                intent.putExtra("helpeeid",help.getHelpeeId());
                intent.putExtra("help",help);
                intent.putExtra("helper",HELPER_ME);
                context.startActivity(intent);*/

            }
        });

        holder.Helpee_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {



            }
        });


    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }


    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position,Help help);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }




}
