package dev.mobile.ensapp;

import java.util.Date;

public class Absence {
    private Date startDate;
    private Date endDate;

    public Absence() {
        // Default constructor required for Firestore
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
