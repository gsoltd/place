package myapp.myapps.co.benpro;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import myapp.myapps.co.benpro.broadcast_receiver.GPSChangeReceiver;
import myapp.myapps.co.benpro.fragments.FavoriteFragment;
import myapp.myapps.co.benpro.fragments.SearchFragment;
import myapp.myapps.co.benpro.logic.FavoriteLogic;
import myapp.myapps.co.benpro.logic.LastSearchLogic;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final int GPS_PERMISSION_REQUEST = 3;
    private MapFragment mapFragment;
    private Location currentLocation;
    private GoogleApiClient mClient;
    private FavoriteLogic logic;
    private LastSearchLogic searchLogic;
    private GoogleMap googleMap;
    private GPSChangeReceiver gpsState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       // View searchHolder = findViewById(R.id.search_holder);
       // boolean isSideBySide = (searchHolder != null);
        initViews();


    }

    private void initViews() {
        if(isNetworkAvailable()) {
            mapFragment = MapFragment.newInstance();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.map_holder, mapFragment).commit();


            mapFragment.getMapAsync(this);

            mClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .build();
            mClient.connect();

            gpsState = new GPSChangeReceiver(MainActivity.this);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Network is not available");
            builder.setPositiveButton("OK",null);
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this,SettingActivity.class));
        }

        if(id == R.id.search_new_place){

                showSearchFragment();

        }

        if(id == R.id.action_favorite){
            getFragmentManager().beginTransaction().add(R.id.map_holder,new FavoriteFragment()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        transaction.add(R.id.map_holder, searchFragment);
        transaction.addToBackStack("searchFragment");
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
           Intent i = new Intent(this,RestartService.class);
           startService(i);
       }else {
           AlertDialog.Builder b = new AlertDialog.Builder(this);
           b.setTitle("Unable to work").setMessage("This app cant work without GPS permission").show();
       }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},GPS_PERMISSION_REQUEST);
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);

        if (currentLocation == null) {

            locationManager.requestLocationUpdates(provider, 3000, 100, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currentLocation = location;
                    addMarkerToLocation(currentLocation,true,getResources().getString(R.string.you_are_here));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            }, null);
        } else {
            addMarkerToLocation(currentLocation,true,getResources().getString(R.string.you_are_here));
        }
    }

    public void addMarkerToLocation(Location loc,boolean isCurrentLocation,String title) {

        LatLng ll = new LatLng(loc.getLatitude(), loc.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                CameraPosition.builder()
                        .target(ll)
                        .tilt(70)
                        .zoom(18)
                        .build()
        ), 2000, null);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ll);
        if(isCurrentLocation){
            markerOptions.title(getResources().getString(R.string.you_are_here));
        }else{
            markerOptions.title(title);
            markerOptions.alpha(8);
        }

        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constant.REQUEST_CODE_GOOGLE) {
            // This result is from the PlacePicker dialog.

            if (resultCode == Activity.RESULT_OK) {
       /* User has picked a place, extract data.
          Data is extracted from the returned intent by retrieving a Place
          object from the PlacePicker.
         */

                Place place = PlacePicker.getPlace(data, this);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.builder()
                                .target(place.getLatLng())
                                .zoom(16)
                                .build()
                ), 2000, null);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.alpha(9f);
                markerOptions.title(place.getName() + "");
                markerOptions.position(place.getLatLng());

                googleMap.addMarker(markerOptions);

                onBackPressed();
            } else if (resultCode == PlacePicker.RESULT_ERROR) {
                Toast.makeText(this, "Places API failure! Check that the API is enabled for your key",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logic = new FavoriteLogic(MainActivity.this);
        logic.open();

        searchLogic = new LastSearchLogic(MainActivity.this);
        searchLogic.open();
        try {
            gpsState = new GPSChangeReceiver(this);
            final String GPS_ACTION = "android.location.PROVIDERS_CHANGED";
            IntentFilter intentFilter = new IntentFilter(GPS_ACTION);
            registerReceiver(gpsState, intentFilter);
        }catch(Exception e){

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        logic.close();
        searchLogic.close();
        unregisterReceiver(gpsState);
    }


    public Location getCurrentLocation(){
        return currentLocation;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public FavoriteLogic getLogic(){
        return logic;
    }

    public LastSearchLogic getLastSearchLogic(){
        return searchLogic;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }


}
