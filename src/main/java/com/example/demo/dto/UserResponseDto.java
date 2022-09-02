package com.example.demo.dto;

import com.sun.istack.NotNull;

public class UserResponseDto {

    @NotNull
    private Integer id;
    @NotNull
    private String username;
    @NotNull
    private String phone;
    @NotNull
    private String email;

    @NotNull
    private String contactId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
