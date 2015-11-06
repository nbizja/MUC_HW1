package nb7232.muc_hw1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import nb7232.muc_hw1.service.locationUpdater;


public class locationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //context.bindService(new Intent(locationUpdater.class))
    }
}
