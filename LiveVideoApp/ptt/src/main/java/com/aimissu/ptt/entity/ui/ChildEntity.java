package com.aimissu.ptt.entity.ui;

import java.util.List;

/**

 */
public class ChildEntity {
    private DepartmentEntity dept;
    private List<ChildEntity> children;
    private String olUser;
    private String allUser;

    public String getOlUser() {
        return olUser;
    }

    public void setOlUser(String olUser) {
        this.olUser = olUser;
    }

    public String getAllUser() {
        return allUser;
    }

    public void setAllUser(String allUser) {
        this.allUser = allUser;
    }

    public DepartmentEntity getDept() {
        return dept;
    }

    public void setDept(DepartmentEntity dept) {
        this.dept = dept;
    }

    public List<ChildEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ChildEntity> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ChildEntity{" +
                "dept=" + dept +
                ", children=" + children +
                ", olUser='" + olUser + '\'' +
                ", allUser='" + allUser + '\'' +
                '}';
    }
}
