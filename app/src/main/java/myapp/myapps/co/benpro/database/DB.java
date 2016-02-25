package myapp.myapps.co.benpro.database;


public class DB {

    public static final String DB_NAME = "Places";
    public static final int VESRSION = 3;

    public static class FAVORITE_PLACES{
        public final static String TABLE_NAME = "Favorite";
        public final static String ID = "ID";
        public final static String NAME = "Name";
        public final static String ADDRESS = "Address";
        public final static String DISTANCE = "Distance";
        public final static String IMAGE = "Image";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                ADDRESS + " TEXT," +
                DISTANCE + " NUMERIC," +
                IMAGE + " TEXT" + ")";

        public static String[] allColumns = new String[]{ID,NAME,ADDRESS,DISTANCE,IMAGE};

    }

    public static class LAST_SEARCH{
        public final static String TABLE_NAME = "LastSearchTable";
        public final static String ID = "ID";
        public final static String NAME = "Name";
        public final static String ADDRESS = "Address";
        public final static String DISTANCE = "Distance";
        public final static String IMAGE = "Image";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                ADDRESS + " TEXT," +
                DISTANCE + " NUMERIC," +
                IMAGE + " TEXT" + ")";

        public static String[] allColumns = new String[]{ID,NAME,ADDRESS,DISTANCE,IMAGE};

    }
}
