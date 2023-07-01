package com.mock.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class users {

    @Id
    private int id;

    private String name;

    private Long number;

    private String email;

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "users [id=" + id + ", name=" + name + ", number=" + number + ", email=" + email + ", password="
                + password + "]";
    }

}