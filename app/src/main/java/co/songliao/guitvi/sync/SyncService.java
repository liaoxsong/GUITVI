package co.songliao.guitvi.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Song on 1/19/15.
 */

//this takes the Binder object from the SyncAdapter class and put them on the framework
public class SyncService extends Service {

    private SyncAdapter sSyncAdapter = null;

    private static final Object sSyncAdatperLock = new Object();

    @Override
    public void onCreate() {
        //create sync adater as a singleton,
        // set the sync adatper as syncable

        //disallow parallel syncs
        synchronized (sSyncAdatperLock){
            if(sSyncAdapter == null){
                sSyncAdapter = new SyncAdapter(getApplicationContext(),true);
            }
        }

    }

    //return an object that allow the system the invoke on sync adapter
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
