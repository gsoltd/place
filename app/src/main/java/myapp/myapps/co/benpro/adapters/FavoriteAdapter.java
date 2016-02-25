package myapp.myapps.co.benpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import myapp.myapps.co.benpro.R;
import myapp.myapps.co.benpro.objects.FavoritePlace;



public class FavoriteAdapter extends ArrayAdapter<FavoritePlace> {

    private ArrayList<FavoritePlace>arrayList;

    private LayoutInflater inflater;

    public FavoriteAdapter(Context context,ArrayList<FavoritePlace>arrayList) {
        super(context,android.R.layout.simple_list_item_1,arrayList);

        this.arrayList = arrayList;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder2 holder = new ViewHolder2();

        if(row == null)
        {
            row = inflater.inflate(R.layout.favorite_row,null);
            // initialize the elements
            holder.tvName = (TextView) row.findViewById(R.id.tvFavoriteRowName);
            holder.tvAddress = (TextView) row.findViewById(R.id.tvFavoriteRowAddress);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder2)row.getTag();
        }

        holder.tvName.setText(arrayList.get(position).getName());
        holder.tvAddress.setText(arrayList.get(position).getAddress());

        return row;
    }
}

class ViewHolder2 {
    TextView tvName;
    TextView tvAddress;
}

