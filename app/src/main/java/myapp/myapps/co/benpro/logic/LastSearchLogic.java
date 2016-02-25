package myapp.myapps.co.benpro.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import myapp.myapps.co.benpro.database.DB;
import myapp.myapps.co.benpro.objects.FavoritePlace;



public class LastSearchLogic extends BaseLogic {

    public LastSearchLogic(Context context) {
        super(context);
    }

    public long addLastSearch(String name, String address, double distance, String image){
        ContentValues values = new ContentValues();
        values.put(DB.LAST_SEARCH.NAME,name);
        values.put(DB.LAST_SEARCH.ADDRESS,address);
        values.put(DB.LAST_SEARCH.DISTANCE,distance);
        values.put(DB.LAST_SEARCH.IMAGE, image);

        return dal.insert(DB.LAST_SEARCH.TABLE_NAME,values);
    }


    public ArrayList<FavoritePlace> getLastSearch(){
        Cursor cursor = dal.getTable(DB.LAST_SEARCH.TABLE_NAME,DB.LAST_SEARCH.allColumns);
        ArrayList<FavoritePlace> lastSearch = new ArrayList<FavoritePlace>();

        while(cursor.moveToNext()){
            FavoritePlace favoritePlace = new FavoritePlace();

            favoritePlace.setId(cursor.getInt(cursor.getColumnIndex(DB.LAST_SEARCH.ID)));
            favoritePlace.setName(cursor.getString(cursor.getColumnIndex(DB.LAST_SEARCH.NAME)));
            favoritePlace.setAddress(cursor.getString(cursor.getColumnIndex(DB.LAST_SEARCH.ADDRESS)));
            favoritePlace.setDistance(cursor.getDouble(cursor.getColumnIndex(DB.LAST_SEARCH.DISTANCE)));
            favoritePlace.setImage(cursor.getString(cursor.getColumnIndex(DB.LAST_SEARCH.IMAGE)));

            lastSearch.add(favoritePlace);
        }
        return lastSearch;
    }

    public long updateFavorite(int id,String name,String address,double distance,String image){
        ContentValues values = new ContentValues();
        values.put(DB.LAST_SEARCH.NAME,name);
        values.put(DB.LAST_SEARCH.ADDRESS,address);
        values.put(DB.LAST_SEARCH.DISTANCE,distance);
        values.put(DB.LAST_SEARCH.IMAGE,image);

        String where = DB.LAST_SEARCH.ID + "=" + id;

        return dal.update(DB.LAST_SEARCH.TABLE_NAME,values,where);
    }

    public long deleteById(int id){
        String where = DB.LAST_SEARCH.ID + "=" + id;
        return dal.delete(DB.LAST_SEARCH.TABLE_NAME, where);
    }

    public long deleteAll(){
        return dal.delete(DB.LAST_SEARCH.TABLE_NAME,null);
    }

    public long updateLastSearch(String name,String address,double distance,String image){
        ContentValues values = new ContentValues();
        values.put(DB.LAST_SEARCH.NAME,name);
        values.put(DB.LAST_SEARCH.ADDRESS,address);
        values.put(DB.LAST_SEARCH.DISTANCE,distance);
        values.put(DB.LAST_SEARCH.IMAGE,image);

        String where = DB.LAST_SEARCH.ID + "=" + 1;

        return dal.update(DB.LAST_SEARCH.TABLE_NAME,values,where);
    }

}
