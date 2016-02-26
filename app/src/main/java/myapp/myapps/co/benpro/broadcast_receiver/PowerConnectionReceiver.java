package myapp.myapps.co.benpro.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import myapp.myapps.co.benpro.R;


public class PowerConnectionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() == Intent.ACTION_POWER_CONNECTED) {
            Toast.makeText(context, R.string.phone_was_connected_to_power, Toast.LENGTH_SHORT).show();
        }else if(intent.getAction() == Intent.ACTION_POWER_DISCONNECTED){
            Toast.makeText(context, R.string.phone_was_disconnected_from_power, Toast.LENGTH_SHORT).show();
        }
    }
}