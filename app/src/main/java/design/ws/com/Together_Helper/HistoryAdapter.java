package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        ImageView image;
        TextView Helpee_name;
        TextView Help_location;
        TextView help_accept;
        TextView help_duration;
        TextView help_type;
        TextView help_question;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.history_image);
            Helpee_name = (TextView) view.findViewById(R.id.history_Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.history_Help_location);
            help_accept = (TextView)view.findViewById(R.id.history_Help_accept);
            help_duration = (TextView)view.findViewById(R.id.history_help_duration);
            help_type = (TextView)view.findViewById(R.id.history_help_type);
            help_question = (TextView)view.findViewById(R.id.history_question);

            help_question.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            if (view.getId() == help_question.getId())
            {
                help = helpList.get(getAdapterPosition());
                Intent intent = new Intent(context,Question_popup.class);
                intent.putExtra("helper",HELPER_ME);
                intent.putExtra("help",help);
                context.startActivity(intent);
            }


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
        String type="";
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
        holder.help_type.setText("종류: " +type);
        holder.help_duration.setText("시간: "+help.getDuration()+"시간");

        if(help.getAccept_status().equals("wait"))
        {
            holder.help_accept.setText("봉사 시간 승인 대기 중 입니다");
        }

        else if(help.getAccept_status().equals("accept"))
        {
            holder.help_accept.setText("봉사 시간 승인 되었습니다");
        }

        else if(help.getAccept_status().equals("reject"))
        {
            holder.help_accept.setText("봉사 시간 승인 거부 되었습니다");
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


/*
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

*/

        Picasso.with(context)
                .load(photoURL)
                .into(holder.image);





    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }
}
