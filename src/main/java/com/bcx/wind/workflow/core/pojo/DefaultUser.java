package com.bcx.wind.workflow.core.pojo;

import java.io.Serializable;

/**
 * @author zhanglei
 */
public class DefaultUser implements User,Serializable {

    private String userId;

    private String userName;

    private String nickName;

    private String status;

    private String group;

    private String idCard;

    private String mobilePhone;

    private String officePhone;

    private String email;

    private String gender;

    private String job;

    private String hireDate;

    private String age;

    private String height;

    private String qq;

    private String boss;

    private String company;

    private String school;

    private String local;

    private String homeTown;

    private String birthday;

    private String remark;

    private String description;

    private String weight;

    public DefaultUser(){}

    public DefaultUser(String userId,String userName){
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public DefaultUser setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public DefaultUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String userId() {
        return this.userId;
    }

    @Override
    public String userName() {
        return this.userName;
    }

    public String getNickName() {
        return nickName;
    }

    public DefaultUser setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public DefaultUser setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getGroup() {
        return group;
    }

    public DefaultUser setGroup(String group) {
        this.group = group;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }

    public DefaultUser setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public DefaultUser setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public DefaultUser setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public DefaultUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public DefaultUser setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getJob() {
        return job;
    }

    public DefaultUser setJob(String job) {
        this.job = job;
        return this;
    }

    public String getHireDate() {
        return hireDate;
    }

    public DefaultUser setHireDate(String hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public String getAge() {
        return age;
    }

    public DefaultUser setAge(String age) {
        this.age = age;
        return this;
    }

    public String getHeight() {
        return height;
    }

    public DefaultUser setHeight(String height) {
        this.height = height;
        return this;
    }

    public String getQq() {
        return qq;
    }

    public DefaultUser setQq(String qq) {
        this.qq = qq;
        return this;
    }

    public String getBoss() {
        return boss;
    }

    public DefaultUser setBoss(String boss) {
        this.boss = boss;
        return this;
    }

    public String getCompany() {
        return company;
    }

    public DefaultUser setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getSchool() {
        return school;
    }

    public DefaultUser setSchool(String school) {
        this.school = school;
        return this;
    }

    public String getLocal() {
        return local;
    }

    public DefaultUser setLocal(String local) {
        this.local = local;
        return this;
    }

    public String getHomeTown() {
        return homeTown;
    }

    public DefaultUser setHomeTown(String homeTown) {
        this.homeTown = homeTown;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public DefaultUser setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public DefaultUser setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DefaultUser setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getWeight() {
        return weight;
    }

    public DefaultUser setWeight(String weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DefaultUser){
            return ((DefaultUser) obj).getUserId().equals(this.userId);
        }
        return super.equals(obj);
    }
}
