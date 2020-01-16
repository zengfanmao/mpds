package com.aimissu.ptt.entity.event;

import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.entity.ui.UserGroupRank;

import java.util.List;

public class GetGroupByRankEvent {

//    UserGroupRank userGroupRank;
    List<UserGroup> userGroupList;

    public GetGroupByRankEvent(List<UserGroup> GroupList){
        userGroupList = GroupList;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }
}
