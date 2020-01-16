package com.aimissu.ptt.entity.ui;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class UserGroupRank extends AbstractExpandableItem<UserGroup> implements MultiItemEntity {

    public Long rank;
    public String rankName;
    public List<UserGroup> groupList;

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String randName) {
        this.rankName = randName;
    }

    public List<UserGroup> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<UserGroup> groupList) {
        this.groupList = groupList;
    }

    @Override
    public String toString() {
        return "UserGroupRank{" +
                "rankId=" + rank +
                ", rankName='" + rankName + '\'' +
                ", UserGroupList='" + groupList + '\'' +
                '}';
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }


}
