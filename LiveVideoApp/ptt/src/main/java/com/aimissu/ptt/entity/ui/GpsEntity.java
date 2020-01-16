package com.aimissu.ptt.entity.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.aimissu.ptt.R;
import com.aimissu.ptt.utils.BdMapUtils;
import com.aimissu.ptt.utils.CommonUtils;
import com.baidu.mapapi.clusterutil.clustering.ClusterItem;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;

/**

 */
public class GpsEntity implements ClusterItem, Cloneable {

    /**
     * CaseName : null
     * CaseCode : null
     * UserCode : admin
     * UserName : 陆警官
     * UserHeadPortrait : null
     * MinuteTime : 0
     * DayTime : null
     * Interval : 0
     * DevCode : null
     * DiscussionCode : null
     * DiscussionName : 10009
     * GpsTime : null
     * _CreatedTime : null
     * Longitude : 113.3
     * Latitdue : 23.16
     * ID : 1
     * GpsTargetType : APP
     * status : 1
     * dCode : null
     * dName : null
     */

    private String CaseName;
    private String CaseCode;
    private String UserCode;
    private String UserName;
    private String UserHeadPortrait;
    private int MinuteTime;
    private String DayTime;
    private int Interval;
    private String DevCode;
    private String DiscussionCode;
    private String DiscussionName;
    private String GpsTime;
    private String _CreatedTime;
    private String Longitude;
    private String Latitdue;
    private String ID;
    private String GpsTargetType;
    private String status;
    private String dCode;
    private String dName;
    private String uDepartment;
    private String purpose;

    public String getuDepartment() {
        return uDepartment;
    }

    public void setuDepartment(String uDepartment) {
        this.uDepartment = uDepartment;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getCaseName() {
        return CaseName;
    }

    public void setCaseName(String CaseName) {
        this.CaseName = CaseName;
    }

    public String getCaseCode() {
        return CaseCode;
    }

    public void setCaseCode(String CaseCode) {
        this.CaseCode = CaseCode;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String UserCode) {
        this.UserCode = UserCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserHeadPortrait() {
        return UserHeadPortrait;
    }

    public void setUserHeadPortrait(String UserHeadPortrait) {
        this.UserHeadPortrait = UserHeadPortrait;
    }

    public int getMinuteTime() {
        return MinuteTime;
    }

    public void setMinuteTime(int MinuteTime) {
        this.MinuteTime = MinuteTime;
    }

    public String getDayTime() {
        return DayTime;
    }

    public void setDayTime(String DayTime) {
        this.DayTime = DayTime;
    }

    public int getInterval() {
        return Interval;
    }

    public void setInterval(int Interval) {
        this.Interval = Interval;
    }

    public String getDevCode() {
        return DevCode;
    }

    public void setDevCode(String DevCode) {
        this.DevCode = DevCode;
    }

    public String getDiscussionCode() {
        return DiscussionCode;
    }

    public void setDiscussionCode(String DiscussionCode) {
        this.DiscussionCode = DiscussionCode;
    }

    public String getDiscussionName() {
        return DiscussionName;
    }

    public void setDiscussionName(String DiscussionName) {
        this.DiscussionName = DiscussionName;
    }

    public String getGpsTime() {
        return GpsTime;
    }

    public void setGpsTime(String GpsTime) {
        this.GpsTime = GpsTime;
    }

    public String get_CreatedTime() {
        return _CreatedTime;
    }

    public void set_CreatedTime(String _CreatedTime) {
        this._CreatedTime = _CreatedTime;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitdue() {
        return Latitdue;
    }

    public void setLatitdue(String Latitdue) {
        this.Latitdue = Latitdue;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGpsTargetType() {
        return GpsTargetType;
    }

    public void setGpsTargetType(String GpsTargetType) {
        this.GpsTargetType = GpsTargetType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDCode() {
        return dCode;
    }

    public void setDCode(String dCode) {
        this.dCode = dCode;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    @Override
    public LatLng getPosition() {
        if (!getGpsTargetType().equals("APP")) {
            return BdMapUtils.converGpsToBd(CommonUtils.toDouble(getLatitdue()), CommonUtils.toDouble(getLongitude()));
        }
        return new LatLng(CommonUtils.toDouble(getLatitdue()), CommonUtils.toDouble(getLongitude()));
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory
                .fromResource(R.drawable.marker);
    }

    @Override
    public boolean isCustomView() {
        return true;
    }

    @Override
    public View getCustomView(Context context) {
        View view= null;
        if (getGpsTargetType().equals("APP")) {
             view = LayoutInflater.from(context).inflate(R.layout.location_custom_app_user_view, null);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.location_custom_user_view, null);
        }

        TextView tvTitle = view.findViewById(R.id.tv_custom_title);

        String titleText = CommonUtils.emptyIfNull(getuDepartment() == null ? null : getuDepartment().toString())
                + " "
                + CommonUtils.emptyIfNull(getPurpose() == null ? null : getPurpose().toString())
                + " "
                + CommonUtils.emptyIfNull(getUserName());

        tvTitle.setText(titleText);

//        tvTitle.setText(CommonUtils.emptyIfNull(getDName() == null ? null : getDName().toString()) + " " + CommonUtils.emptyIfNull(getUserName()));
        return view;


    }


    @Override
    public String toString() {
        return "GpsEntity{" +
                "CaseName=" + CaseName +
                ", CaseCode=" + CaseCode +
                ", UserCode='" + UserCode + '\'' +
                ", UserName='" + UserName + '\'' +
                ", UserHeadPortrait=" + UserHeadPortrait +
                ", MinuteTime=" + MinuteTime +
                ", DayTime=" + DayTime +
                ", Interval=" + Interval +
                ", DevCode=" + DevCode +
                ", DiscussionCode=" + DiscussionCode +
                ", DiscussionName='" + DiscussionName + '\'' +
                ", GpsTime=" + GpsTime +
                ", _CreatedTime=" + _CreatedTime +
                ", Longitude='" + Longitude + '\'' +
                ", Latitdue='" + Latitdue + '\'' +
                ", ID='" + ID + '\'' +
                ", GpsTargetType='" + GpsTargetType + '\'' +
                ", status='" + status + '\'' +
                ", dCode=" + dCode +
                ", dName=" + dName +
                ", uDepartment=" + uDepartment +
                ", purpose=" + purpose +
                '}';
    }

    @Override
    public GpsEntity clone() throws CloneNotSupportedException {
        GpsEntity gpsEntity = new GpsEntity();

        gpsEntity.CaseName = this.CaseName;
        gpsEntity.CaseCode = this.CaseCode;
        gpsEntity.UserCode = this.UserCode;
        gpsEntity.UserName = this.UserName;
        gpsEntity.UserHeadPortrait = this.UserHeadPortrait;
        gpsEntity.MinuteTime = this.MinuteTime;
        gpsEntity.DayTime = this.DayTime;
        gpsEntity.Interval = this.Interval;
        gpsEntity.DevCode = this.DevCode;
        gpsEntity.DiscussionCode = this.DiscussionCode;
        gpsEntity.DiscussionName = this.DiscussionName;
        gpsEntity.GpsTime = this.GpsTime;

        gpsEntity.Longitude = this.Longitude;
        gpsEntity.Latitdue = this.Latitdue;
        gpsEntity.ID = ID;
        gpsEntity.GpsTargetType = this.GpsTargetType;
        gpsEntity.status = status;
        gpsEntity.dCode = dCode;
        gpsEntity.dName = dName;

        gpsEntity.uDepartment = uDepartment;
        gpsEntity.purpose = purpose;

        return gpsEntity;

    }
}
