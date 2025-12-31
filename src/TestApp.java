package src;

import src.backend.AllocationLogic;
import src.dao.AllocationDAO;

public class TestApp {
    public static void main(String[] args) {
        try {
            System.out.println("🔥 STARTING FACULTY ALLOCATION WITH PRIORITY RULES");
            System.out.println("Rules: Assistants(2/day) > Associates(1/day) > Professors(emergency)");
            
            // Clear old data
            AllocationDAO allocationDAO = new AllocationDAO();
            allocationDAO.clearAllAllocations();
            
            // Generate with NEW priority rules
            AllocationLogic logic = new AllocationLogic();
            logic.generateAllocations();
            
            System.out.println("\n✅ ALLOCATION COMPLETE! Check FacultyPage now.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
