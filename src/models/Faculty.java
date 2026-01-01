package src.models;

public class Faculty {
    private int facultyId;
    private String name;
    private String designation;
    private boolean absent;
    private boolean senior;

    public Faculty(int facultyId, String name, String designation,
                   boolean absent, boolean senior) {
        this.facultyId = facultyId;
        this.name = name;
        this.designation = designation;
        this.absent = absent;
        this.senior = senior;
    }

    public int getFacultyId() { return facultyId; }
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public boolean isAbsent() { return absent; }
    public boolean isSenior() { return senior; }

    @Override
    public String toString() {
        return name;
    }
}
