package src.models;

public class Faculty {
    private int facultyId;
    private String name;
    private String designation;
    private boolean isSenior;
    private boolean isAbsent;

    public Faculty(int facultyId, String name, String designation, boolean isSenior, boolean isAbsent) {
        this.facultyId = facultyId;
        this.name = name;
        this.designation = designation;
        this.isSenior = isSenior;
        this.isAbsent = isAbsent;
    }

    // CRITICAL: This fixes dropdown showing "Faculty@123abc"
    @Override
    public String toString() {
        return name + " (" + designation + ")";
    }

    // All required getters
    public int getFacultyId() {
        return facultyId;
    }

    public String getName() {
        return name;
    }

    public String getDesignation() {
        return designation;
    }

    public boolean isSenior() {
        return isSenior;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        this.isAbsent = absent;
    }
}
