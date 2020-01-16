package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.Permission;

import java.util.List;

public class DataPermission extends BaseEntity {
    List<Permission> Entity;

    public List<Permission> getEntity() {
        return Entity;
    }

    public void setEntity(List<Permission> entity) {
        Entity = entity;
    }

    @Override
    public String toString() {
        return "DataPermission{" +
                "Entity=" + Entity +
                '}';
    }
}
