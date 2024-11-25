package dev.mobile.ensapp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AbsenceItem {
    private final String teacherName;
    private final Absence absence;

    public AbsenceItem(String teacherName, Absence absence) {
        this.teacherName = teacherName;
        this.absence = absence;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getAbsenceDateRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(absence.getStartDate()) + " - " + sdf.format(absence.getEndDate());
    }
}
