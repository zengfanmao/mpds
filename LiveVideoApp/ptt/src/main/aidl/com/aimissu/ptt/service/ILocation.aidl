// ILocation.aidl
package com.aimissu.ptt.service;

// Declare any non-default types here with import statements
import com.aimissu.ptt.service.ILocationListener;

interface ILocation {
    void startLocation();
    void stopLocation();
    double getLongitude();
    double getLatitude();
    String getPositionName();
    void registListener(in ILocationListener loctionListener);
    void unregistListener(in ILocationListener loctionListener);
}
