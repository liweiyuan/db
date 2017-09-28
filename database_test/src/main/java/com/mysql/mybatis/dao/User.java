package com.mysql.mybatis.dao;

/**
 * Created by tingyun on 2017/4/6.
 */
public class User {

    private int id;
    private String name;
    private String birthday;
    private String longcontent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLongcontent() {
        return longcontent;
    }

    public void setLongcontent(String longcontent) {
        this.longcontent = longcontent;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", longcontent='" + longcontent + '\'' +
                '}';
    }
}
