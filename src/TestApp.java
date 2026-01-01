package src;

import src.backend.AllocationLogic;

public class TestApp {

    public static void main(String[] args) {

        System.out.println("=== Faculty Allocation Started ===");

        AllocationLogic logic = new AllocationLogic();

        // run semester-wise
        logic.generateAllocation("3");
        logic.generateAllocation("5");
        logic.generateAllocation("7");

        System.out.println("=== Faculty Allocation Completed ===");
    }
}
