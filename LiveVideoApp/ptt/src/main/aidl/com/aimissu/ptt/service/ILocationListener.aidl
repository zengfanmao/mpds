// ILocationCallback.aidl
package com.aimissu.ptt.service;

// Declare any non-default types here with import statements

interface ILocationListener {
     void onLocationResultSuccessed(double longitude,double latitude,String positionName,String cityName);
     void onLocationResultFailed(String msg);
}
