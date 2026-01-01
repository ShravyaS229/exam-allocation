package src.models;

public class Slot {
    private String examDate;
    private String semester;
    private String subjectCode;
    private String time;

    public Slot(String examDate, String semester,
                String subjectCode, String time) {
        this.examDate = examDate;
        this.semester = semester;
        this.subjectCode = subjectCode;
        this.time = time;
    }

    public String getExamDate() { return examDate; }
    public String getSemester() { return semester; }
    public String getSubjectCode() { return subjectCode; }
    public String getTime() { return time; }
}
