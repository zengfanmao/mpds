package com.aimissu.ptt.entity.ui;

public class Permission {
    public String roleId;
    public String navName;
    public String actionType;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getNavName() {
        return navName;
    }

    public void setNavName(String navName) {
        this.navName = navName;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "roleId='" + roleId + '\'' +
                ", navName='" + navName + '\'' +
                ", actionType='" + actionType + '\'' +
                '}';
    }
}
