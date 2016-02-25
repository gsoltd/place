package myapp.myapps.co.benpro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import myapp.myapps.co.benpro.logic.FavoriteLogic;

public class SettingActivity extends AppCompatActivity {

    private RadioButton rbKM,rbMiles;
    private SharedPreferences pref;
    private String lastDistance;
    private FavoriteLogic logic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initalViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logic = new FavoriteLogic(this);
        logic.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        logic.close();
    }

    private void initalViews() {
        pref = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
        rbKM = (RadioButton) findViewById(R.id.rbKM);
        rbMiles = (RadioButton) findViewById(R.id.rbMiles);

        lastDistance = pref.getString(Constant.DISTANCE,Constant.DISTANCE_KM);

        if(lastDistance.equals(Constant.DISTANCE_MILES)){
            rbMiles.setChecked(true);
        }
    }

    public void saveSetting_onClick(View view){
        SharedPreferences.Editor editor = pref.edit();
        if(rbKM.isChecked()){
            editor.putString(Constant.DISTANCE, Constant.DISTANCE_KM);
        }else if(rbMiles.isChecked()){
            editor.putString(Constant.DISTANCE,Constant.DISTANCE_MILES);
        }

        editor.commit();

        boolean needRefresh =  !(lastDistance.equals(pref.getString(Constant.DISTANCE,Constant.DISTANCE_KM)));
        Log.d("distance from pref", pref.getString(Constant.DISTANCE,Constant.DISTANCE_KM));
        if(needRefresh){
            editor.putBoolean(Constant.REFRESH_LIST,true);
        }else{
            editor.putBoolean(Constant.REFRESH_LIST,false);
        }
        editor.commit();

        Log.d(pref.getString(Constant.DISTANCE, "---"), "--");

        finish();
    }

    public void deleteAllFromFav_onClick(View view) {
        logic.deleteAll();
    }
}
