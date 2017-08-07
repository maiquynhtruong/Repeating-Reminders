package com.example.maiquynhtruong.heathyreminders;

public class Reminder {
    private int id;
    String title;
    private int hour;
    private int minute;
    private boolean repeat;
    private int repeatNumber;
    private String date;

    public Reminder(String title) {
        this.title = title;
        hour = 12;
        minute = 0;
        repeat = false;
        repeatNumber = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public void setRepeatNumber(int repeatNumber) {
        this.repeatNumber = repeatNumber;
    }
    public void setDate(String date) { this.date = date; }

    public String getDate() {return this.date;}



}
