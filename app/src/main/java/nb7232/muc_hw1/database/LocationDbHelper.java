package nb7232.muc_hw1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import nb7232.muc_hw1.R;

/**
 * Created by nejc on 7.11.2015.
 */
public class LocationDbHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "signal_mapper";
    final static int DATABASE_VERSION = 1;
    final public static String TABLE_NAME = "location";

    final public static String ID = "id";
    final public static String LATITUDE = "latitude";
    final public static String LONGITUDE = "longitude";
    final public static String LABEL = "label";
    final public static String TIMESTAMP = "timestamp";

    public String DB_PATH;
    private Context mContext;

    final private static String CMD = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LATITUDE + " REAL NOT NULL, " + LONGITUDE + " REAL NOT NULL, " +
            LABEL + " TEXT, " +
            TIMESTAMP + " TEXT NOT NULL)";

    public LocationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("LocationDbHelper", "Creating database: " + CMD);
        db.execSQL(CMD);
        try {
            populateDatabase(db, R.raw.home_samples);
            populateDatabase(db, R.raw.work_samples);
        } catch (IOException ioe) {
            Log.e("LocationDbHelper", "File does not exist!");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void populateDatabase(SQLiteDatabase db, int resourceId) throws IOException {
        //If database not exists copy it from the assets
        try {
            //Copy the database from assests
            db.execSQL(sqlFileToString(resourceId));
            Log.e("LocationDbHelper", "createDatabase database created");
        } catch (IOException mIOException) {
            throw new Error(mIOException.getMessage());
        }
    }

    //Copy the database from assets
    private String sqlFileToString(int resourceId) throws IOException {
        InputStream mInput = mContext.getResources().openRawResource(resourceId);
        String sqlString = new Scanner(mInput, "UTF-8").useDelimiter("\\A").next();
        return sqlString;
    }
}
