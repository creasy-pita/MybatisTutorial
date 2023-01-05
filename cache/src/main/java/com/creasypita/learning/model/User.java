package com.creasypita.learning.model;

import java.io.Serializable;

/**
 * Created by lujq on 10/17/2021.
 */
public class User implements Serializable{
    private int id;
    private String name;
    private String pwd;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

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

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
