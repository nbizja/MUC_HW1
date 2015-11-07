package nb7232.muc_hw1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import nb7232.muc_hw1.LocationService;

/**
 * Created by nejc on 7.11.2015.
 */
public class LocationDbHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "signal_mapper";
    final static int DATABASE_VERSION = 1;
    final public static String TABLE_NAME = "location";

    final public static String  ID = "id";
    final public static String LATITUDE = "latitude";
    final public static String LONGITUDE = "longitutde";
    final public static String TIMESTAMP = "timestamp";

    final private static String CMD = "CREATE TABLE " + TABLE_NAME + " (" + ID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            LATITUDE + " REAL NOT NULL, " + LONGITUDE + " REAL NOT NULL, " + TIMESTAMP + " TEXT NOT NULL)";

    public LocationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("LocationDbHelper", "Creating database: "+CMD);
        db.execSQL(CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
