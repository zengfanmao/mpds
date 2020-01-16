package com.aimissu.ptt.entity.data;

import com.aimissu.ptt.entity.BaseEntity;
import com.aimissu.ptt.entity.ui.UserGroup;

import java.util.List;

public class DataUserGroup extends BaseEntity {

     List<UserGroup> Entity;

     public List<UserGroup> getEntity() {
          return Entity;
     }

     @Override
     public String toString() {
          return "UserGroup{" +
                  "Entity=" + Entity +
                  '}';
     }
}
