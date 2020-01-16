package com.aimissu.ptt.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

/**

 */
public class LocationManger {

    private volatile static ILocation iLocation;
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            synchronized (this) {
                iLocation = ILocation.Stub.asInterface(service);
            }
            if (conLisntener != null) {
                conLisntener.onServiceConnected();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iLocation = null;
        }
    };

    public static class Single {
        public static LocationManger locationManger = new LocationManger();
    }

    public static LocationManger getInstance() {
        return Single.locationManger;
    }

    public void bindService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbindService(Context context) {
        context.unbindService(serviceConnection);
    }

    public void registListener(ILocationListener iLocationListener) {
        if (iLocation != null) {
            try {
                iLocation.registListener(iLocationListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unregistListener(ILocationListener iLocationListener) {
        if (iLocation != null) {
            try {
                iLocation.unregistListener(iLocationListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void startLocation() {
        if (iLocation != null) {
            try {
                iLocation.startLocation();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopLocation() {
        if (iLocation != null) {
            try {
                iLocation.stopLocation();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private ConLisntener conLisntener;

    public ConLisntener getConLisntener() {
        return conLisntener;
    }

    public void setConLisntener(ConLisntener conLisntener) {
        this.conLisntener = conLisntener;
    }

    public interface ConLisntener {
        void onServiceConnected();
    }

}
