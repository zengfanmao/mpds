package com.aimissu.ptt.entity.ui;

/**

 */
public class DepartmentEntity {


    /**
     * isDel : false
     * recSN :
     * dCode : 10003
     * dName : 孙公司
     * dFather : 10002
     * dDesc : 0
     * ID : 3
     * sort_id : 1
     * createtime : 2018-09-02T07:02:11
     * updatetime : 2018-09-02T07:02:15
     * farther : 10002
     */

    private String isDel;
    private String recSN;
    private String dCode;
    private String dName;
    private String dFather;
    private String dDesc;
    private String ID;
    private String sort_id;
    private String createtime;
    private String updatetime;
    private String farther;

    public String isIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getRecSN() {
        return recSN;
    }

    public void setRecSN(String recSN) {
        this.recSN = recSN;
    }

    public String getDCode() {
        return dCode;
    }

    public void setDCode(String dCode) {
        this.dCode = dCode;
    }

    public String getDName() {
        return dName;
    }

    public void setDName(String dName) {
        this.dName = dName;
    }

    public String getDFather() {
        return dFather;
    }

    public void setDFather(String dFather) {
        this.dFather = dFather;
    }

    public String getDDesc() {
        return dDesc;
    }

    public void setDDesc(String dDesc) {
        this.dDesc = dDesc;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSort_id() {
        return sort_id;
    }

    public void setSort_id(String sort_id) {
        this.sort_id = sort_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getFarther() {
        return farther;
    }

    public void setFarther(String farther) {
        this.farther = farther;
    }

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "isDel='" + isDel + '\'' +
                ", recSN='" + recSN + '\'' +
                ", dCode='" + dCode + '\'' +
                ", dName='" + dName + '\'' +
                ", dFather='" + dFather + '\'' +
                ", dDesc='" + dDesc + '\'' +
                ", ID='" + ID + '\'' +
                ", sort_id='" + sort_id + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", farther='" + farther + '\'' +
                '}';
    }
}
