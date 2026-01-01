package src.backend;

import src.dao.*;
import src.models.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AllocationLogic {

    private final SlotDAO slotDAO = new SlotDAO();
    private final SubjectDAO subjectDAO = new SubjectDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final FacultyDAO facultyDAO = new FacultyDAO();
    private final AllocationDAO allocationDAO = new AllocationDAO();

    // Tracks faculty assignments per day: Date -> (FacultyId -> count)
    private final Map<String, Map<Integer, Integer>> facultyDailyCount = new HashMap<>();

    public void generateAllocation(String semester) {

        List<Slot> slots = slotDAO.getSlotsBySemester(semester);
        List<Subject> subjects = subjectDAO.getSubjectsBySemester(semester);
        List<Room> rooms = roomDAO.getAllRooms();
        List<Faculty> allFaculty = facultyDAO.getAllFaculties();

        if (slots.isEmpty() || subjects.isEmpty() || rooms.isEmpty()) {
            System.out.println("❌ Missing data for semester " + semester);
            return;
        }

        // Filter by designation
        List<Faculty> assistants = new ArrayList<>();
        List<Faculty> associates = new ArrayList<>();

        for (Faculty f : allFaculty) {
            String desig = f.getDesignation().toLowerCase();
            if (desig.contains("assistant")) assistants.add(f);
            else if (desig.contains("associate")) associates.add(f);
            // Professors/HODs are fallback only
        }

        // Sort slots by date + time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h.mm a");
        slots.sort(
            Comparator.comparing(Slot::getExamDate)
                      .thenComparing(s -> LocalTime.parse(s.getTime().split(" - ")[0], formatter))
        );

        int assistantIndex = 0;
        int associateIndex = 0;
        int subjectIndex = 0;

        System.out.println("Sem | Room | Date | Time | Subject | Faculty");
        System.out.println("---------------------------------------------------");

        for (Slot slot : slots) {

            String date = slot.getExamDate();
            facultyDailyCount.putIfAbsent(date, new HashMap<>());

            Subject subject = subjects.get(subjectIndex % subjects.size());
            subjectIndex++;

            for (Room room : rooms) {

                Faculty assigned = null;

                // Assign assistant (max 4 duties per day, not continuous)
                for (int i = 0; i < assistants.size(); i++) {
                    Faculty f = assistants.get(assistantIndex % assistants.size());
                    assistantIndex++;
                    int count = facultyDailyCount.get(date).getOrDefault(f.getFacultyId(), 0);
                    if (count < 4) {
                        assigned = f;
                        facultyDailyCount.get(date).put(f.getFacultyId(), count + 1);
                        break;
                    }
                }

                // Assign associate if no assistant is available (max 2 duties per day, not continuous)
                if (assigned == null) {
                    for (int i = 0; i < associates.size(); i++) {
                        Faculty f = associates.get(associateIndex % associates.size());
                        associateIndex++;
                        int count = facultyDailyCount.get(date).getOrDefault(f.getFacultyId(), 0);
                        if (count < 2) {
                            assigned = f;
                            facultyDailyCount.get(date).put(f.getFacultyId(), count + 1);
                            break;
                        }
                    }
                }

                // Fallback: Professors/HODs (rarely needed)
                if (assigned == null && !allFaculty.isEmpty()) {
                    assigned = allFaculty.get(0);
                }

                // Save allocation to DB
                AllocationResult ar = new AllocationResult(
                        slot.getExamDate(),
                        slot.getTime(),
                        room.getRoomNo(),
                        slot.getSemester(),
                        subject.getName(),
                        assigned.getName(),
                        assigned.getDesignation()
                );
                allocationDAO.saveAllocation(ar);

                // Print allocation
                System.out.printf("%s | %s | %s | %s | %s | %s%n",
                        slot.getSemester(),
                        room.getRoomNo(),
                        slot.getExamDate(),
                        slot.getTime(),
                        subject.getName(),
                        assigned.getName());
            }
        }

        System.out.println("---------------------------------------------------");
        System.out.println("✅ Allocation completed for semester " + semester);
    }
}
