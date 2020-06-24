package com.fashionapp.fashionapp;

public class DbCheck
{
    String id, emailid, password, name, height, weight, foot, complexion, gender, visited;
    public DbCheck()
    {

    }

    public DbCheck(String emailid) {
        this.emailid = emailid;
    }

    DbCheck(String id, String emailid, String password) {
        this.id = id;
        this.emailid = emailid;
        this.password = password;
    }

    DbCheck(String id, String emailid, String password, String name, String height, String weight, String foot, String complexion, String gender)
    {
        this.id = id;
        this.emailid = emailid;
        this.password = password;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.foot = foot;
        this.complexion = complexion;
        this.gender = gender;
    }

    DbCheck(String id, String emailid, String password, String name, String height, String weight, String foot, String complexion, String gender,String visited)
    {
        this.id = id;
        this.emailid = emailid;
        this.password = password;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.foot = foot;
        this.visited = visited;
        this.complexion = complexion;
        this.gender = gender;
    }


    public DbCheck(String id, String emailid)
    {
        this.id = id;
        this.emailid = emailid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFoot() {
        return foot;
    }

    public void setFoot(String foot) {
        this.foot = foot;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

