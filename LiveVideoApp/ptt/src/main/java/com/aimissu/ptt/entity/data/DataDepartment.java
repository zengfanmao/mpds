package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.ChildEntity;
import com.aimissu.ptt.entity.ui.DepartmentEntity;

/**

 */
public class DataDepartment extends BaseEntity {
    ChildEntity Entity;

    public ChildEntity getEntity() {
        return Entity;
    }

    public void setEntity(ChildEntity entity) {
        Entity = entity;
    }
}
