package com.aimissu.ptt.entity.event;

public class SetDefaultGroupEvent {

    private int mPosition = -1;


    private String isSetDefaultGroup = null;
    public SetDefaultGroupEvent(int position,String isDefault){
        mPosition=position;
        isSetDefaultGroup=isDefault;
    }

    public int getPosition(){
        return mPosition;
    }

    public String getIsDefaultGroup() {
        return isSetDefaultGroup;
    }

}
