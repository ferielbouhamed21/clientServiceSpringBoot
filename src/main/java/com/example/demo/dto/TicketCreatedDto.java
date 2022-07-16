package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

public class TicketCreatedDto {

    @JsonProperty("subject")
    @NotNull
    private String subject;
    @JsonProperty("department")
    @NotNull
    private String departmentId;
    @JsonProperty("userId")
    @NotNull
    private Integer userId;
    @JsonProperty("email")
    @NotNull
    private String email;
    @JsonProperty("phone")
    @NotNull
    private String phone;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
