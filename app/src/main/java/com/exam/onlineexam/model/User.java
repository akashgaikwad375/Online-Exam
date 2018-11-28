package com.exam.onlineexam.model;

public class User {

    public String name = "";
    public String email = "";
    public String password = "";
    public String testResult = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public static User createUser(String name, String email, String password){
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        return user;
    }
}
