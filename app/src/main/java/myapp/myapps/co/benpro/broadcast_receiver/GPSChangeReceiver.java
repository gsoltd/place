package myapp.myapps.co.benpro.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;
import myapp.myapps.co.benpro.MainActivity;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.RestartService;


public class GPSChangeReceiver extends BroadcastReceiver {

    private MainActivity activity;

    public GPSChangeReceiver(){}

    public GPSChangeReceiver(MainActivity activity){
        this.activity = activity;
    }
    @Override
    public void onReceive(Context context, Intent intent) {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, activity.getResources().getString(R.string.gps_enabled), Toast.LENGTH_SHORT).show();
            Intent intentService = new Intent(context, RestartService.class);
            context.startService(intentService);
        } else {
            //do something else
            Toast.makeText(context, "GPS Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}