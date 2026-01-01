package src.frontend;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import src.dao.AllocationDAO;
import src.dao.FacultyDAO;
import src.models.AllocationResult;
import src.models.Faculty;

import java.util.*;

public class FacultyPage extends Application {

    private TableView<AllocationResult> table = new TableView<>();
    private FacultyDAO facultyDAO = new FacultyDAO();
    private AllocationDAO allocationDAO = new AllocationDAO();

    @Override
    public void start(Stage stage) {
        Label label = new Label("Faculty Allocation Page");
        label.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Dropdown to select faculty
        ComboBox<Faculty> facultyCombo = new ComboBox<>();
        List<Faculty> faculties = facultyDAO.getAllFaculties();
        facultyCombo.setItems(FXCollections.observableArrayList(faculties));

        // ===== Table Columns =====
        TableColumn<AllocationResult, Integer> roomCol = new TableColumn<>("Room No");
        roomCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyObjectWrapper<>(f.getValue().getRoomNo()));
        roomCol.setPrefWidth(80);

        TableColumn<AllocationResult, String> dateCol = new TableColumn<>("Exam Date");
        dateCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyStringWrapper(f.getValue().getExamDate()));
        dateCol.setPrefWidth(120);

        TableColumn<AllocationResult, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyStringWrapper(f.getValue().getTime()));
        timeCol.setPrefWidth(120);

        TableColumn<AllocationResult, String> semCol = new TableColumn<>("Semester");
        semCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyStringWrapper(f.getValue().getSemester()));
        semCol.setPrefWidth(80);

        TableColumn<AllocationResult, String> subCol = new TableColumn<>("Subject");
        subCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyStringWrapper(f.getValue().getSubject()));
        subCol.setPrefWidth(250);

        TableColumn<AllocationResult, String> facCol = new TableColumn<>("Faculty");
        facCol.setCellValueFactory(f -> new javafx.beans.property.ReadOnlyStringWrapper(f.getValue().getFacultyName()));
        facCol.setPrefWidth(200);

        table.getColumns().addAll(roomCol, dateCol, timeCol, semCol, subCol, facCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ===== Button =====
        Button showBtn = new Button("Show Allocation");
        showBtn.setStyle("-fx-font-size: 14px; -fx-padding: 5px 15px;");
        showBtn.setOnAction(e -> {
            Faculty selected = facultyCombo.getSelectionModel().getSelectedItem();
            if (selected != null) {
                List<AllocationResult> allocations = allocationDAO.getAllocationsForFaculty(selected.getName());

                // Deduplicate based on room+date+time+subject
                Set<String> seen = new HashSet<>();
                List<AllocationResult> uniqueList = new ArrayList<>();
                for (AllocationResult ar : allocations) {
                    String key = ar.getRoomNo() + "|" + ar.getExamDate() + "|" + ar.getTime() + "|" + ar.getSubject();
                    if (!seen.contains(key)) {
                        uniqueList.add(ar);
                        seen.add(key);
                    }
                }

                // Find conflicts: same time but different subjects
                Set<String> conflictKeys = new HashSet<>();
                Map<String, List<AllocationResult>> timeMap = new HashMap<>();
                for (AllocationResult ar : uniqueList) {
                    String key = ar.getExamDate() + "|" + ar.getTime();
                    timeMap.computeIfAbsent(key, k -> new ArrayList<>()).add(ar);
                }
                for (Map.Entry<String, List<AllocationResult>> entry : timeMap.entrySet()) {
                    if (entry.getValue().size() > 1) {
                        conflictKeys.add(entry.getKey());
                    }
                }

                // Row factory for conflict highlighting
                table.setRowFactory(tv -> new TableRow<>() {
                    @Override
                    protected void updateItem(AllocationResult item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setStyle("");
                        } else {
                            String key = item.getExamDate() + "|" + item.getTime();
                            if (conflictKeys.contains(key)) {
                                setStyle("-fx-background-color: #ffcccc;"); // red highlight
                            } else {
                                setStyle("");
                            }
                        }
                    }
                });

                table.setItems(FXCollections.observableArrayList(uniqueList));
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a faculty first.");
                alert.showAndWait();
            }
        });

        VBox root = new VBox(15, label, facultyCombo, showBtn, table);
        root.setPadding(new javafx.geometry.Insets(15));
        root.setStyle("-fx-background-color: #f0f8ff;");

        Scene scene = new Scene(root, 950, 550);
        stage.setScene(scene);
        stage.setTitle("Faculty Allocation Page");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
