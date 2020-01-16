package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.PersonUserEntity;

import java.util.List;

public class DataPersonUserEntity extends BaseEntity {
    List<PersonUserEntity> Entity;

    public List<PersonUserEntity> getEntity() {
        return Entity;
    }

    @Override
    public String toString() {
        return "PersonUserEntity{" +
                "Entity=" + Entity +
                '}';
    }
}
