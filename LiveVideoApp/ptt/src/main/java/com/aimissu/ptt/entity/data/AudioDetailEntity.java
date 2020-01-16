package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.AudioDetail;
import com.aimissu.ptt.entity.ui.Permission;

import java.util.List;

public class AudioDetailEntity extends BaseEntity {
    List<AudioDetail> Entity;

    public List<AudioDetail> getEntity() {
        return Entity;
    }

    public void setEntity(List<AudioDetail> entity) {
        Entity = entity;
    }

    @Override
    public String toString() {
        return "AudioDetailEntity{" +
                "Entity=" + Entity +
                '}';
    }
}
