package myapp.myapps.co.benpro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DAL extends SQLiteOpenHelper {
    private SQLiteDatabase database;

    public DAL(Context context) {
        super(context, DB.DB_NAME, null, DB.VESRSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.FAVORITE_PLACES.CREATE_TABLE);
        db.execSQL(DB.LAST_SEARCH.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB.FAVORITE_PLACES.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DB.LAST_SEARCH.TABLE_NAME);
        onCreate(db);
    }

    public void open(){
        database = super.getWritableDatabase();
    }


    public long insert(String tableName,ContentValues values){
        return database.insert(tableName,null,values);
    }

    public long update(String tableName,ContentValues values,String where){
        return database.update(tableName,values,where,null);
    }


    public long delete(String tableName,String where){
        return database.delete(tableName,where,null);
    }

    public Cursor getTable(String tableName,String[]columns,String where){
        return database.query(tableName,columns,where,null,null,null,null,null);
    }

    public Cursor getTable(String tableName,String[]columns){
        return database.query(tableName,columns,null,null,null,null,null,null);
    }

    public void close(){
        database.close();
    }

}
