package com.phonecompany.billing;

import java.util.Date;

public class User {
    public Long phonenumber;
    public Date startTime;
    public Date endTime;

    public User(Long phonenumber, Date startTime, Date endTime) {
        this.phonenumber = phonenumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
