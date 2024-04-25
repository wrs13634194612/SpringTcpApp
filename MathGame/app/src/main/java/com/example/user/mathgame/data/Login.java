package com.example.user.mathgame.data;



public class Login {
    /**
     * userId : userId
     * password : password
     * task : login
     */

    private String userId;
    private String userName;
    private String password;
    private String task;

    public Login(String userId, String userName, String password, String task) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.task = task;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
