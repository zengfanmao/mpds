package com.aimissu.ptt.db;

import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.data.DataDepartment;
import com.aimissu.ptt.entity.ui.Permission;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**

 */
public class LocalCache {
    double longitude;
    double latitude;
    String positionName;
    String cityName;
    List<UserGroup> userGroups;
    DataDepartment dataDepartment;
    String conferenceCreator;

    public String getConferenceCreator() {
        return conferenceCreator;
    }

    public void setConferenceCreator(String conferenceCreator) {
        this.conferenceCreator = conferenceCreator;
    }

    /**

     * 用户权限
     */
    List<Permission> UserPermisson;

    public List<Permission> getUserPermisson() {
        return UserPermisson;
    }

    public void setUserPermisson(List<Permission> userPermisson) {
        UserPermisson = userPermisson;
    }

    public LocalCache() {

    }

    private static volatile LocalCache localCache;

    public static LocalCache getInstance() {
        if (localCache == null) {
            synchronized (LocalCache.class) {
                if (localCache == null) {
                    localCache = new LocalCache();
                }
            }
        }
        return localCache;
    }

    public LocalCache setData(double longitude, double latitude, String positionName, String cityName) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.positionName = positionName;
        this.cityName = cityName;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LatLng getPosition() {
        try {
            if (getLatitude() > 0 && getLongitude() > 0) {
                return new LatLng(getLatitude(), getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserGroup> getUserGroups() {
        if(userGroups==null){
            synchronized (this){
                if(userGroups==null){
                    userGroups=AppManager.getUserGroupList();
                }
            }
        }
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public DataDepartment getDataDepartment() {
        return dataDepartment;
    }

    public void setDataDepartment(DataDepartment dataDepartment) {
        this.dataDepartment = dataDepartment;
    }
}
