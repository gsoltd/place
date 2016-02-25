package myapp.myapps.co.benpro.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import myapp.myapps.co.benpro.Constant;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.objects.FavoritePlace;



public class LastSearchAdapter extends ArrayAdapter<FavoritePlace> {

    private ArrayList<FavoritePlace> arrayList;

    private LayoutInflater inflater;
    private SharedPreferences pref;

    public LastSearchAdapter(Context context,ArrayList<FavoritePlace>arrayList) {
        super(context,android.R.layout.simple_list_item_1,arrayList);

        this.arrayList = arrayList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null)
        {
            row = inflater.inflate(R.layout.search_row,null);
            // initialize the elements
            holder.tvNameHolder = (TextView) row.findViewById(R.id.tvSearchRowName);
            holder.tvAddressHolder = (TextView) row.findViewById(R.id.tvSearchRowAddress);
            holder.imageViewHolder = (ImageView) row.findViewById(R.id.imgViewSearchRow);
            holder.tvDistance = (TextView) row.findViewById(R.id.tvDistanceSearchRow);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.tvNameHolder.setText(arrayList.get(position).getName());
        holder.tvAddressHolder.setText(arrayList.get(position).getAddress());
        String distanceIn = pref.getString(Constant.DISTANCE,Constant.DISTANCE_KM);
        if(distanceIn.equals(Constant.DISTANCE_KM)){
            holder.tvDistance.setText("KM from you: " + arrayList.get(position).getDistance());
        }else if(distanceIn.equals(Constant.DISTANCE_MILES)){
            holder.tvDistance.setText("Miles from you: " + arrayList.get(position).getDistance());
        }

        return row;
    }
}
