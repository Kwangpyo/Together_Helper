package design.ws.com.Together_Helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    protected static final String PREFS_FILE = "device_id.xml";
    protected static final String PREFS_DEVICE_ID = "device_id";
    protected volatile static UUID uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = FirebaseInstanceId.getInstance().getToken();
       // Log.d("splashtoken", token);

        String uniqueID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

       // Log.d("splashid",uniqueID);

        String deviceresult = "";
        GETDeviceKeyAPITask getDeviceKeyAPITask = new GETDeviceKeyAPITask();
        try {
            deviceresult = getDeviceKeyAPITask.execute(uniqueID).get();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (deviceresult.equals("true"))
        {
            String result = null;
            PUTUpdateToken putUpdateToken = new PUTUpdateToken();
            try {
                result = putUpdateToken.execute(token, uniqueID).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else if(deviceresult.equals("false"))
        {
            String result = null;
            POSTSaveToken postSaveTokenAPI = new POSTSaveToken();
            try {
                result = postSaveTokenAPI.execute(token, uniqueID).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }







        Intent intent2 = new Intent(this, Login.class);
        startActivity(intent2);

        finish();
    }
}
