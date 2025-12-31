package src.frontend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.dao.AllocationDAO;
import src.backend.AllocationLogic;

public class AdminPage extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Admin - Exam Allocation System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button generateButton = new Button("Generate Allocations");
        generateButton.setOnAction(e -> generateAllocations());

        Button viewFacultyButton = new Button("View Faculty Page");
        viewFacultyButton.setOnAction(e -> new FacultyPage().start(new Stage()));

        root.getChildren().addAll(titleLabel, generateButton, viewFacultyButton);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Page");
        primaryStage.show();
    }

    private void generateAllocations() {
        AllocationLogic logic = new AllocationLogic();
        logic.generateAllocations();
        showAlert("Allocations generated successfully!");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
