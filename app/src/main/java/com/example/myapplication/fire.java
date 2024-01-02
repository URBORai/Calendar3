package com.example.myapplication;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

public class fire {
    String message,date,time,key,TimeAndMessage;
    boolean check;
    long event;
    public fire(){}

    public fire(long event,String TimeAndMessage,String key,String message, String date, String time, boolean check) {
        this.event = event;
        this.TimeAndMessage = TimeAndMessage;
        this.key = key;
        this.message = message;
        this.date = date;
        this.time = time;
        this.check = check;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }

    public String getTimeAndMessage() {
        return TimeAndMessage;
    }

    public void setTimeAndMessage(String timeAndMessage) {
        TimeAndMessage = timeAndMessage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

