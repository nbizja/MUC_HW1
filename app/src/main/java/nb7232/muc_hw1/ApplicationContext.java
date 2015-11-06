package nb7232.muc_hw1;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by nejc on 6.11.2015.
 */
public class ApplicationContext extends Application {

    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
    }
    public static Context getContext()
    {
        ApplicationContext applicationContext = new ApplicationContext();
        Log.e("ApllicationContext", "JOLO");
        return instance;
    }
}
