package com.example.chat.data;

public class Login {
    /**
     * userId : userId
     * password : password
     * task : login
     */

    private String userId;
    private String password;
    private String task;

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
