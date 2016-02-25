package myapp.myapps.co.benpro.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import myapp.myapps.co.benpro.objects.FavoritePlace;
import myapp.myapps.co.benpro.database.DB;

public class FavoriteLogic extends BaseLogic {
    public FavoriteLogic(Context context) {
        super(context);
    }

    public long addFavoritePlace(String name, String address, double distance, String image){
        ContentValues values = new ContentValues();
        values.put(DB.FAVORITE_PLACES.NAME,name);
        values.put(DB.FAVORITE_PLACES.ADDRESS,address);
        values.put(DB.FAVORITE_PLACES.DISTANCE,distance);
        values.put(DB.FAVORITE_PLACES.IMAGE, image);

        return dal.insert(DB.FAVORITE_PLACES.TABLE_NAME,values);
    }

    public long addFavoritePlace(String name, String address, double distance){
        ContentValues values = new ContentValues();
        values.put(DB.FAVORITE_PLACES.NAME,name);
        values.put(DB.FAVORITE_PLACES.ADDRESS,address);
        values.put(DB.FAVORITE_PLACES.DISTANCE,distance);

        return dal.insert(DB.FAVORITE_PLACES.TABLE_NAME,values);
    }


    public ArrayList<FavoritePlace> getAllFavoritePlaces(){
        Cursor cursor = dal.getTable(DB.FAVORITE_PLACES.TABLE_NAME,DB.FAVORITE_PLACES.allColumns);
        ArrayList<FavoritePlace> allFavoritePlaces = new ArrayList<FavoritePlace>();

        while(cursor.moveToNext()){
            FavoritePlace favoritePlace = new FavoritePlace();

            favoritePlace.setId(cursor.getInt(cursor.getColumnIndex(DB.FAVORITE_PLACES.ID)));
            favoritePlace.setName(cursor.getString(cursor.getColumnIndex(DB.FAVORITE_PLACES.NAME)));
            favoritePlace.setAddress(cursor.getString(cursor.getColumnIndex(DB.FAVORITE_PLACES.ADDRESS)));
            favoritePlace.setDistance(cursor.getDouble(cursor.getColumnIndex(DB.FAVORITE_PLACES.DISTANCE)));
            favoritePlace.setImage(cursor.getString(cursor.getColumnIndex(DB.FAVORITE_PLACES.IMAGE)));

            allFavoritePlaces.add(favoritePlace);
        }
        return allFavoritePlaces;
    }

    public long updateFavorite(int id,String name,String address,double distance,String image){
        ContentValues values = new ContentValues();
        values.put(DB.FAVORITE_PLACES.NAME,name);
        values.put(DB.FAVORITE_PLACES.ADDRESS,address);
        values.put(DB.FAVORITE_PLACES.DISTANCE,distance);
        values.put(DB.FAVORITE_PLACES.IMAGE,image);

        String where = DB.FAVORITE_PLACES.ID + "=" + id;

        return dal.update(DB.FAVORITE_PLACES.TABLE_NAME,values,where);
    }

    public long deleteById(int id){
        String where = DB.FAVORITE_PLACES.ID + "=" + id;
        return dal.delete(DB.FAVORITE_PLACES.TABLE_NAME, where);
    }

    public long deleteAll(){
        return dal.delete(DB.FAVORITE_PLACES.TABLE_NAME,null);
    }

    public long addLastSearch(String name, String address, double distance, String image){
        ContentValues values = new ContentValues();
        values.put(DB.FAVORITE_PLACES.NAME,name);
        values.put(DB.FAVORITE_PLACES.ADDRESS,address);
        values.put(DB.FAVORITE_PLACES.DISTANCE,distance);
        values.put(DB.FAVORITE_PLACES.IMAGE, image);

        return dal.insert(DB.FAVORITE_PLACES.TABLE_NAME,values);
    }

    public long updateLastSearch(String name,String address,double distance,String image){
        ContentValues values = new ContentValues();
        values.put(DB.FAVORITE_PLACES.NAME,name);
        values.put(DB.FAVORITE_PLACES.ADDRESS,address);
        values.put(DB.FAVORITE_PLACES.DISTANCE,distance);
        values.put(DB.FAVORITE_PLACES.IMAGE,image);

        String where = DB.FAVORITE_PLACES.ID + "=" + 1;

        return dal.update(DB.FAVORITE_PLACES.TABLE_NAME,values,where);
    }

}
