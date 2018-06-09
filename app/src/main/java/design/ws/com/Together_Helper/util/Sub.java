package design.ws.com.Together_Helper.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import design.ws.com.Together_Helper.R;

public class Sub extends LinearLayout implements View.OnClickListener {

    public Sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_arrival_layout,this,true);
    }

    @Override
    public void onClick(View view) {



    }
}
