package src.models;

public class Slot {
    private int slotId;
    private String examDate;
    private String semester;
    private String subjectCode;
    private String time;

    // FIXED: 5-parameter constructor
    public Slot(int slotId, String examDate, String semester, String subjectCode, String time) {
        this.slotId = slotId;
        this.examDate = examDate;
        this.semester = semester;
        this.subjectCode = subjectCode;
        this.time = time;
    }

    // GETTERS
    public int getSlotId() { return slotId; }
    public String getExamDate() { return examDate; }
    public String getSemester() { return semester; }
    public String getSubjectCode() { return subjectCode; }  // FIXED
    public String getTime() { return time; }
}
