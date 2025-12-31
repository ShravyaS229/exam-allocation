package src.frontend;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.dao.FacultyDAO;
import src.dao.AllocationDAO;
import src.models.Faculty;
import src.models.AllocationResult;
import java.util.List;

public class FacultyPage extends Application {
    private TableView<AllocationResult> tableView = new TableView<>();
    private ComboBox<Faculty> facultyComboBox = new ComboBox<>();
    private FacultyDAO facultyDAO = new FacultyDAO();
    private AllocationDAO allocationDAO = new AllocationDAO();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Faculty Allocation Viewer");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox selectorBox = new HBox(15);
        selectorBox.getChildren().addAll(
            new Label("Select Faculty:"),
            facultyComboBox,
            createShowButton()
        );

        setupTable();
        loadFaculties();

        root.getChildren().addAll(titleLabel, selectorBox, tableView);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Faculty Allocation Viewer");
        primaryStage.show();
    }

    private Button createShowButton() {
        Button showButton = new Button("Show My Allocations");
        showButton.setOnAction(e -> showSelectedFacultyAllocations());
        return showButton;
    }

    private void setupTable() {
        TableColumn<AllocationResult, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("examDate"));
        dateCol.setPrefWidth(120);

        TableColumn<AllocationResult, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setPrefWidth(120);

        TableColumn<AllocationResult, String> semCol = new TableColumn<>("Sem");
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        semCol.setPrefWidth(60);

        TableColumn<AllocationResult, String> subjectCol = new TableColumn<>("Subject");
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        subjectCol.setPrefWidth(350);

        TableColumn<AllocationResult, Integer> roomCol = new TableColumn<>("Room");
        roomCol.setCellValueFactory(new PropertyValueFactory<>("roomNo"));
        roomCol.setPrefWidth(80);

        tableView.getColumns().addAll(dateCol, timeCol, semCol, subjectCol, roomCol);
    }

    private void loadFaculties() {
        List<Faculty> faculties = facultyDAO.getAllFaculties();
        facultyComboBox.setItems(FXCollections.observableArrayList(faculties));
    }

    private void showSelectedFacultyAllocations() {
        Faculty selectedFaculty = facultyComboBox.getValue();
        if (selectedFaculty == null) {
            showAlert("Please select a faculty first!");
            return;
        }

        List<AllocationResult> allocations = allocationDAO.getAllocationsForFaculty(selectedFaculty.getName());
        tableView.setItems(FXCollections.observableArrayList(allocations));

        if (allocations.isEmpty()) {
            showAlert("No allocations found for " + selectedFaculty.getName());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
