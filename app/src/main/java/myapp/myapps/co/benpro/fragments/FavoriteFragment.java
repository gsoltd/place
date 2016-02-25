package myapp.myapps.co.benpro.fragments;


import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import myapp.myapps.co.benpro.adapters.FavoriteAdapter;
import myapp.myapps.co.benpro.objects.FavoritePlace;
import myapp.myapps.co.benpro.MainActivity;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.logic.FavoriteLogic;


public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final MainActivity mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.favorite_fragment,null);

        ListView listView = (ListView) view.findViewById(R.id.lvFavoritePlacesFragment);

        final ArrayList<FavoritePlace> arrayList = new ArrayList<>();

        FavoriteLogic logic = mainActivity.getLogic();

        for(int i=0; i<logic.getAllFavoritePlaces().size(); i++){
            arrayList.add(logic.getAllFavoritePlaces().get(i));
        }

        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(getActivity(),arrayList);
        listView.setAdapter(favoriteAdapter);

        if(mainActivity.isNetworkAvailable()) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    FavoritePlace favoritePlace = arrayList.get(position);
                    Location location = new Location("");
                    location.setLongitude(location.getLongitude());
                    location.setLatitude(location.getLatitude());
                    mainActivity.addMarkerToLocation(location, false, favoritePlace.getName());
                    getActivity().getFragmentManager().beginTransaction().remove(FavoriteFragment.this).commit();

                }
            });
        }

        return view;
    }

}
