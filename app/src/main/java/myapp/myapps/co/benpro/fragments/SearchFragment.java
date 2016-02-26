package myapp.myapps.co.benpro.fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import myapp.myapps.co.benpro.Constant;
import myapp.myapps.co.benpro.MainActivity;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.adapters.LastSearchAdapter;
import myapp.myapps.co.benpro.adapters.SearchAdapter;
import myapp.myapps.co.benpro.logic.LastSearchLogic;
import myapp.myapps.co.benpro.objects.FavoritePlace;
import myapp.myapps.co.benpro.objects.SearchObject;
import myapp.myapps.co.benpro.logic.FavoriteLogic;


public class SearchFragment extends Fragment {

    private View view;
    private ArrayList<SearchObject> arrayListSearchResult;
    private SearchAdapter searchAdapter;
    private LastSearchAdapter lastSearchAdapter;
    private SharedPreferences pref;
    private String findDistanceIn;
    private Location currentLocation;
    private String saveQuery;
    private SearchView searchView;
    private Button btnSearchByName, btnSearchNearbyPlace;
    private MainActivity mainActivity;
    private LastSearchLogic searchLogic;


    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_fragment, null);
        mainActivity = (MainActivity) getActivity();

        searchView = (SearchView) view.findViewById(R.id.searchViewPlace);
        final ListView listView = (ListView) view.findViewById(R.id.listViewSearchResult);
        arrayListSearchResult = new ArrayList<>();
        searchAdapter = new SearchAdapter(getActivity(), arrayListSearchResult);
        listView.setAdapter(searchAdapter);

        btnSearchByName = (Button) view.findViewById(R.id.btnSearchByNameSearchFragment);
        btnSearchNearbyPlace = (Button) view.findViewById(R.id.btnSearchNearbyPlaceSearchFragment);

        currentLocation = mainActivity.getCurrentLocation();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        searchLogic = mainActivity.getLastSearchLogic();


        btnSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkQuery = searchView.getQuery().toString();
                if (checkQuery.equals(null) || checkQuery.equals("")) {
                    Toast.makeText(getActivity(), R.string.enter_place, Toast.LENGTH_SHORT).show();
                } else {
                    SearchPlace(checkQuery);
                }
            }
        });

        btnSearchNearbyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.FindPlacesAroundYou();
            }
        });

        if (mainActivity.isNetworkAvailable()) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchObject searchObject = arrayListSearchResult.get(position);
                    Location location = new Location("");
                    location.setLatitude(searchObject.getLat());
                    location.setLongitude(searchObject.getLon());

                    String title = searchObject.getName();

                    mainActivity.addMarkerToLocation(location, false, title);

                    getActivity().getFragmentManager().beginTransaction().remove(SearchFragment.this).commit();
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getActivity().getResources().getString(R.string.menu));
                    builder.setPositiveButton(getActivity().getResources().getString(R.string.add_to_favorite), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FavoriteLogic logic = mainActivity.getLogic();
                            SearchObject searchObject = arrayListSearchResult.get(position);
                            logic.addFavoritePlace(searchObject.getName(), searchObject.getAddress(), searchObject.getDistance());
                            Toast.makeText(getActivity(), mainActivity.getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton(getActivity().getResources().getString(R.string.share_with_friends), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SearchObject searchObject = arrayListSearchResult.get(position);
                            Intent intent = new Intent();
                            intent.setAction(android.content.Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_SUBJECT, searchObject.getName());
                            intent.setType("text/plain");
                            startActivity(intent);
                        }
                    });

                    builder.show();

                    return false;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    saveQuery = query;
                    if (!query.equals(null) && !query.equals("")) {
                        if (mainActivity.isNetworkAvailable()) {
                            SearchPlace(query);
                        } else {
                            Toast.makeText(getActivity(), "Network Disabled", Toast.LENGTH_LONG).show();

                        }
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        } else {
            Toast.makeText(getActivity(), "Network Disabled", Toast.LENGTH_LONG).show();
            ArrayList<FavoritePlace> arrayList = searchLogic.getLastSearch();


            lastSearchAdapter = new LastSearchAdapter(getActivity(), arrayList);

            listView.setAdapter(lastSearchAdapter);
            lastSearchAdapter.notifyDataSetChanged();

        }
        return view;
    }

    private void saveLastSearch(ArrayList<SearchObject> arrayList) {
        if (pref.getBoolean(Constant.IS_FIRST_SEARCH, true)) {

        } else {

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        boolean refresh = pref.getBoolean(Constant.REFRESH_LIST, false);
        if (refresh) {
            if (mainActivity.isNetworkAvailable()) {
                SearchPlace(saveQuery);
            } else {
                Toast.makeText(getActivity(), "Network Disabled", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void SearchPlace(String query) {
        findDistanceIn = pref.getString(Constant.DISTANCE, Constant.DISTANCE_KM);
        String my_query = query.replace(" ", "%20");
        String urlAddress = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1k2m27_zXr0_bY3r6_HH5H098-xbd59o&sensor=false&location=" + currentLocation.getLatitude() + "," + currentLocation.getLongitude() + "&radius=100000&name=";
        Ion.with(getActivity()).load(urlAddress + my_query)
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.show();

                if (e == null) {
                    arrayListSearchResult.clear();
                    searchAdapter.notifyDataSetChanged();
                    try {
                        searchLogic.deleteAll();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    try {
                        // The response object:

                        JSONObject jsonResult = new JSONObject(result);

                        // Get the "results" array
                        JSONArray resultsArray = jsonResult.getJSONArray("results");

                        // Get the search status:
                        String searchStatus = jsonResult.getString("status");
                        //       searchStatus = getSearchStatus(searchStatus, query);
                        //      showStatusToast(searchStatus);


                        // Get all the data:
                        for (int i = 0; i < resultsArray.length(); i++) {

                            // Get the current item's object:
                            JSONObject place = resultsArray.getJSONObject(i);

                            // Get the name:
                            String icon = place.getString("icon");

                            String photoReference = null;
                            JSONArray photos = place.getJSONArray("photos");
                            if (photos != null) {
                                if (photos.length() > 0) {
                                    JSONObject photo = photos.getJSONObject(0);
                                    photoReference = photo.getString("photo_reference");
                                }
                            }
                            String photoAddress = null;
                            if (photoReference != null) {
                                photoAddress =
                                        "https://maps.googleapis.com/maps/api/place/photo?" +
                                                "key=AIzaSyC1k2m27_zXr0_bY3r6_HH5H098-xbd59o" +
                                                "&photoreference=" + photoReference +
                                                "&maxheight=400";
                            }


                            String name = place.getString("name");
                            Log.d("name", name + "");

                            // Get the address:
                            //  String address = getAddress(place);
                            String address = place.getString("vicinity");

                            // Get the location (LatLng):
                            JSONObject placeGeometry = place.getJSONObject("geometry");
                            JSONObject placeGeometryLocation = placeGeometry.getJSONObject("location");

                            String latitude = placeGeometryLocation.getString("lat");
                            String longitude = placeGeometryLocation.getString("lng");

                            Location placeLocation = new Location("nevermind");
                            placeLocation.setLatitude(Double.valueOf(latitude));
                            placeLocation.setLongitude(Double.valueOf(longitude));
                            double distanceInMeter = currentLocation.distanceTo(placeLocation);

                            double distance = 0;
                            if (findDistanceIn.equals(Constant.DISTANCE_KM)) {
                                double kmFromYou = distanceInMeter / 1000;
                                distance = kmFromYou;
                            } else if (findDistanceIn.equals(Constant.DISTANCE_MILES)) {
                                double milesFromYou = distanceInMeter / 1600;
                                distance = milesFromYou;
                            }


                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                            distance = Double.parseDouble(decimalFormat.format(distance));

                            SearchObject searchObject = new SearchObject(Double.parseDouble(latitude),
                                    Double.parseDouble(longitude),
                                    name,
                                    address,
                                    icon,
                                    distance,
                                    photoAddress);
                            arrayListSearchResult.add(searchObject);
                            searchAdapter.notifyDataSetChanged();

                            searchLogic.addLastSearch(name, address, distance, icon);
                            if (i == resultsArray.length() - 1) {
                                saveLastSearch(arrayListSearchResult);
                                progressDialog.cancel();
                            }

                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();

                    }
                }
            }
        });
    }
}
