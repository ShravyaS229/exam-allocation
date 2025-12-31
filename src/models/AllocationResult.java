package src.models;

public class AllocationResult {
    private String examDate;
    private String time;
    private int roomNo;
    private String semester;
    private String subject;
    private String facultyName;

    // FIXED: 6-parameter constructor EXACTLY matching AllocationDAO
    public AllocationResult(String examDate, String time, int roomNo, String semester, String subject, String facultyName) {
        this.examDate = examDate;
        this.time = time;
        this.roomNo = roomNo;
        this.semester = semester;
        this.subject = subject;
        this.facultyName = facultyName;
    }

    // GETTERS (FacultyPage needs these)
    public String getExamDate() { return examDate; }
    public String getTime() { return time; }
    public int getRoomNo() { return roomNo; }
    public String getSemester() { return semester; }
    public String getSubject() { return subject; }
    public String getFacultyName() { return facultyName; }
}
