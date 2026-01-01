package src.frontend;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import src.backend.AllocationLogic;

public class AdminPage {

    public void start(Stage stage) {

        Label title = new Label("Admin Panel");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label semLabel = new Label("Select Semester:");
        ComboBox<String> semesterBox = new ComboBox<>();
        semesterBox.getItems().addAll("3", "5", "7");

        Button allocateBtn = new Button("Generate Allocation");
        Button exitBtn = new Button("Exit");

        Label status = new Label("");

        allocateBtn.setOnAction(e -> {
            String semester = semesterBox.getValue();
            if (semester == null) {
                status.setText("Please select a semester.");
                return;
            }

            AllocationLogic logic = new AllocationLogic();
            logic.generateAllocation(semester);

            status.setText("Allocation completed for Semester " + semester);
        });

        exitBtn.setOnAction(e -> stage.close());

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(15);
        grid.setHgap(10);

        grid.add(title, 0, 0, 2, 1);
        grid.add(semLabel, 0, 1);
        grid.add(semesterBox, 1, 1);
        grid.add(allocateBtn, 0, 2);
        grid.add(exitBtn, 1, 2);
        grid.add(status, 0, 3, 2, 1);

        stage.setTitle("Admin Page");
        stage.setScene(new Scene(grid, 400, 250));
        stage.show();
    }
}
