package src.backend;

import src.dao.FacultyDAO;
import src.dao.SlotDAO;
import src.dao.AllocationDAO;
import src.dao.SubjectDAO;
import src.models.Faculty;
import src.models.Slot;
import java.util.*;

public class AllocationLogic {
    private FacultyDAO facultyDAO = new FacultyDAO();
    private SlotDAO slotDAO = new SlotDAO();
    private AllocationDAO allocationDAO = new AllocationDAO();
    private SubjectDAO subjectDAO = new SubjectDAO();

    public void generateAllocations() {
        System.out.println("=== FACULTY ALLOCATION WITH YOUR RULES ===");
        System.out.println("RULES: Assistants(2 duties/day) > Associates(1 duty/day) > Professors/HOD(Emergency only)");
        
        allocationDAO.clearAllAllocations();
        List<Slot> slots = slotDAO.getAllSlots();
        List<Faculty> faculties = facultyDAO.getAllFaculties();
        
        // DUTY COUNTER: facultyName_date -> count
        Map<String, Integer> dutyCount = new HashMap<>();
        
        for (Slot slot : slots) {
            // 9 rooms per slot
            for (int room = 101; room <= 109; room++) {
                Faculty selectedFaculty = selectFacultyByPriority(slot, faculties, dutyCount);
                
                if (selectedFaculty != null) {
                    // Get subject name using YOUR SubjectDAO method
                    String subjectName = subjectDAO.getSubjectByCode(slot.getSubjectCode()).getSubjectName();
                    
                    // Save using YOUR AllocationDAO (String date)
                    allocationDAO.saveAllocation(
                        slot.getExamDate(),      // String from Slot
                        slot.getTime(),
                        room,
                        slot.getSemester(),
                        subjectName,
                        selectedFaculty.getName(),
                        selectedFaculty.getDesignation()
                    );
                    
                    // Print with priority indicator
                    String priority = getPriorityLabel(selectedFaculty);
                    System.out.println(selectedFaculty.getName() + " (" + priority + 
                                     ") → Room " + room + ", Sem " + slot.getSemester() + 
                                     ", " + slot.getTime());
                    
                    // Update duty count for this faculty_date
                    String dutyKey = selectedFaculty.getName() + "_" + slot.getExamDate();
                    dutyCount.put(dutyKey, dutyCount.getOrDefault(dutyKey, 0) + 1);
                } else {
                    System.out.println("❌ No faculty available for " + slot.getExamDate() + " " + slot.getTime());
                }
            }
        }
        System.out.println("\n✅ ALLOCATION COMPLETE - FacultyPage ready!");
        System.out.println("Total allocations: " + (slots.size() * 9));
    }
    
    private Faculty selectFacultyByPriority(Slot slot, List<Faculty> faculties, Map<String, Integer> dutyCount) {
        Random rand = new Random();
        String dateKey = slot.getExamDate();
        
        // CONDITION 1: Assistants first (MAX 2 duties per day)
        List<Faculty> availableAssistants = new ArrayList<>();
        for (Faculty f : faculties) {
            if (f.getDesignation().toLowerCase().contains("assistant")) {
                String dutyKey = f.getName() + "_" + dateKey;
                int currentDuties = dutyCount.getOrDefault(dutyKey, 0);
                if (currentDuties < 2) {
                    availableAssistants.add(f);
                }
            }
        }
        if (!availableAssistants.isEmpty()) {
            Faculty selected = availableAssistants.get(rand.nextInt(availableAssistants.size()));
            System.out.println("   → Selected Assistant: " + selected.getName() + " (duties today: " + dutyCount.getOrDefault(selected.getName() + "_" + dateKey, 0) + "/2)");
            return selected;
        }
        
        // CONDITION 2: Associates next (MAX 1 duty per day)
        List<Faculty> availableAssociates = new ArrayList<>();
        for (Faculty f : faculties) {
            if (f.getDesignation().toLowerCase().contains("associate")) {
                String dutyKey = f.getName() + "_" + dateKey;
                int currentDuties = dutyCount.getOrDefault(dutyKey, 0);
                if (currentDuties < 1) {
                    availableAssociates.add(f);
                }
            }
        }
        if (!availableAssociates.isEmpty()) {
            Faculty selected = availableAssociates.get(rand.nextInt(availableAssociates.size()));
            System.out.println("   → Selected Associate: " + selected.getName() + " (duties today: " + dutyCount.getOrDefault(selected.getName() + "_" + dateKey, 0) + "/1)");
            return selected;
        }
        
        // CONDITION 3: Professors/HOD - EMERGENCY ONLY (MAX 1 duty per day)
        List<Faculty> availableSeniors = new ArrayList<>();
        for (Faculty f : faculties) {
            if (f.isSenior()) {
                String dutyKey = f.getName() + "_" + dateKey;
                int currentDuties = dutyCount.getOrDefault(dutyKey, 0);
                if (currentDuties < 1) {
                    availableSeniors.add(f);
                }
            }
        }
        if (!availableSeniors.isEmpty()) {
            Faculty selected = availableSeniors.get(0); // HOD first
            System.out.println("   → EMERGENCY Senior: " + selected.getName() + " (duties today: " + dutyCount.getOrDefault(selected.getName() + "_" + dateKey, 0) + "/1)");
            return selected;
        }
        
        // FINAL FALLBACK: Any faculty (no duty limit)
        Collections.shuffle(faculties);
        Faculty fallback = faculties.get(0);
        System.out.println("   → FALLBACK: " + fallback.getName());
        return fallback;
    }
    
    private String getPriorityLabel(Faculty faculty) {
        if (faculty.getDesignation().toLowerCase().contains("assistant")) return "ASST(2max)";
        if (faculty.getDesignation().toLowerCase().contains("associate")) return "ASSOC(1max)";
        if (faculty.isSenior()) return "PROF(EMRG)";
        return "FALLBACK";
    }
}
