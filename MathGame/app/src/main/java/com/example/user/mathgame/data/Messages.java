package com.example.user.mathgame.data;


public class Messages {
    /**
     * msg : message
     * task : sendRoom
     */

    private String msg;
    private String task;

    public Messages(String msg, String task) {
        this.msg = msg;
        this.task = task;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
