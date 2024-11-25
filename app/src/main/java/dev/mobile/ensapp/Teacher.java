package dev.mobile.ensapp;

import java.util.List;

public class Teacher {
    private String id;
    private String name;
    private List<Absence> absences;

    public Teacher() {
        // Default constructor required for Firestore
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }
}
