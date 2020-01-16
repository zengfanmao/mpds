package com.aimissu.ptt.utils;

import android.view.View;

import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.entity.ui.Permission;

import java.util.List;

/**
 * 用户权限管理
 */
public class PermissionUtils {
    public static List<Permission> UserPermissons= LocalCache.getInstance().getUserPermisson();

    public static boolean hasGroupCallPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){

            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_group_call")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasMainPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_main")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGroupPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_group")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGpsPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_gps")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasRecordingPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_recording")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGroupCutPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_group_cut")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonStopPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_stop")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonKillPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_kill")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonDisablePermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_disable")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonEnablePermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_enable")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGpsSearchPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_gps_search")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasRecordingSearchPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_recording_search")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonSearchPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_search")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonCallPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_call")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPersonCutPermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_person_cut")){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasGroupChargePermisson(){
        if (UserPermissons!=null&&UserPermissons.size()>0){
            for (Permission permission :UserPermissons){
                if (permission.getNavName().equals("app_group_charge")){
                    return true;
                }
            }
        }
        return false;
    }
}
