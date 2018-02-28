package com.example.thomson.hci.Entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Thomson on 28/2/2018.
 */

@DatabaseTable(tableName = "SleepData")
public class SleepLog {

    @DatabaseField(id=true)
    private String id;
    @DatabaseField
    private String date;
    @DatabaseField
    private String time;
    @DatabaseField
    private String is_active;
    @DatabaseField
    private String wake_time;

    public SleepLog(){

    }

    public SleepLog(String id, String date, String time, String is_active, String wake_time) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.is_active = is_active;
        this.wake_time = wake_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getWake_time() {
        return wake_time;
    }

    public void setWake_time(String wake_time) {
        this.wake_time = wake_time;
    }
}
