package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.GpsEntity;

import java.util.List;

/**

 */
public class DataGps extends BaseEntity {

    List<GpsEntity> Entity;

    public List<GpsEntity> getEntity() {
        return Entity;
    }

    public void setEntity(List<GpsEntity> entity) {
        Entity = entity;
    }
}
