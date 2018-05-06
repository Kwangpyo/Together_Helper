package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView matching_status;
        TextView Helpee_name;
        TextView Help_location;
        TextView Help_detail;
        TextView Helpee_detail;
        TextView Help_time;
        TextView Help_start;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            matching_status = (TextView) view.findViewById(R.id.help_status);
            Helpee_name = (TextView) view.findViewById(R.id.Helpee_name);
            Help_location = (TextView) view.findViewById(R.id.Help_location);
            Help_detail = (TextView) view.findViewById(R.id.help_detail);
            Helpee_detail = (TextView) view.findViewById(R.id.helpee_detail);
            Help_time = (TextView) view.findViewById(R.id.Help_time);
            Help_start = (TextView) view.findViewById(R.id.Help_startfinish);

        }
    }


    public HelpAdapter(MainActivity mainActivityContacts, List<Help> helpsList) {
        this.helpList = helpsList;
       this.context = mainActivityContacts;
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
     //   holder.matching_status.setText(help.getMatch_status());
        holder.Helpee_name.setText(help.getHelpee().getName());

      //  holder.Help_location.setText(help.getLocation());
      //  holder.Help_time.setText(help.getTime());
        //holder.image.setImageResource(movie.getImage());

        holder.Help_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,Help_detail_popup.class);
                intent.putExtra("help",help);
                context.startActivity(intent);

            }
        });

        holder.Helpee_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Helpee_detail_popup.class);
                intent.putExtra("helpee",help.getHelpee());
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

    }

    @Override
    public int getItemCount() {
        return helpList.size();
    }

}


