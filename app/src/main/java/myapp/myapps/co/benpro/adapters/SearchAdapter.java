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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import myapp.myapps.co.benpro.Constant;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.objects.SearchObject;

public class SearchAdapter extends ArrayAdapter<SearchObject> {

    private ArrayList<SearchObject> arrayList;

    private Context context;
    private LayoutInflater inflater;
    private SharedPreferences pref;

    public SearchAdapter(Context context, ArrayList<SearchObject> arrayList) {
        super(context, android.R.layout.simple_list_item_1, arrayList);

        this.arrayList = arrayList;
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            row = inflater.inflate(R.layout.search_row, null);
            // initialize the elements
            holder.textViewName = (TextView) row.findViewById(R.id.tvSearchRowName);
            holder.textViewAddress = (TextView) row.findViewById(R.id.tvSearchRowAddress);
            holder.icon = (ImageView) row.findViewById(R.id.imgViewSearchRow);
            holder.textViewDistance = (TextView) row.findViewById(R.id.tvDistanceSearchRow);
            holder.imageViewPhoto = (ImageView) row.findViewById(R.id.imgViewSearchRow2);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.textViewName.setText(arrayList.get(position).getName());
        holder.textViewAddress.setText(arrayList.get(position).getAddress());
        Picasso.with(context).load(arrayList.get(position).getIcon()).into(holder.icon);
        String photo = arrayList.get(position).getPhoto();
        if (photo != null) {
            Picasso.with(context).load(photo).into(holder.imageViewPhoto);
        }
        String distanceIn = pref.getString(Constant.DISTANCE, Constant.DISTANCE_KM);
        if (distanceIn.equals(Constant.DISTANCE_KM)) {
            holder.textViewDistance.setText("KM from you: " + arrayList.get(position).getDistance());
        } else if (distanceIn.equals(Constant.DISTANCE_MILES)) {
            holder.textViewDistance.setText("Miles from you: " + arrayList.get(position).getDistance());
        }

        return row;
    }


}

class ViewHolder {
    TextView textViewName;
    TextView textViewAddress;
    ImageView icon;
    TextView textViewDistance;
    ImageView imageViewPhoto;
}


