package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.UserGroupRank;

import java.util.List;

public class DataUserGroupRank extends BaseEntity {

    List<UserGroupRank> Entity;

    public List<UserGroupRank> getEntity() {
        return Entity;
    }

    @Override
    public String toString() {
        return "UserGroupRank{" +
                "Entity=" + Entity +
                '}';
    }

}
