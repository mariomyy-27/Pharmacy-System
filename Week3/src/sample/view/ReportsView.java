package sample.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Medicine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportsView {

    public static void show(List<Medicine> medicines) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pharmacy Reports");

        long totalCount = medicines.size();
        long availableCount = medicines.stream()
                .filter(m -> m.getQuantity() > 10)
                .count();
        long lowStockCount = medicines.stream()
                .filter(m -> m.getQuantity() > 0 && m.getQuantity() <= 10)
                .count();
        long outOfStockCount = medicines.stream()
                .filter(m -> m.getQuantity() <= 0)
                .count();

        Label titleLabel = new Label("Inventory & Stock Reports");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox summaryBox = new HBox(15);
        summaryBox.setAlignment(Pos.CENTER_LEFT);
        summaryBox.getChildren().addAll(
                createStatCard("Total Items", String.valueOf(totalCount)),
                createStatCard("Available", String.valueOf(availableCount)),
                createStatCard("Low Stock", String.valueOf(lowStockCount)),
                createStatCard("Out of Stock", String.valueOf(outOfStockCount))
        );

        TableView<Medicine> table = new TableView<>();
        ObservableList<Medicine> data = FXCollections.observableArrayList(medicines);
        table.setItems(data);

        TableColumn<Medicine, String> nameCol = new TableColumn<>("Medicine");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<Medicine, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);

        TableColumn<Medicine, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(100);

        TableColumn<Medicine, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(getStatus(cellData.getValue().getQuantity())));
        statusCol.setPrefWidth(140);

        table.getColumns().addAll(nameCol, priceCol, quantityCol, statusCol);

        Label statusMessage = new Label();
        Button exportBtn = new Button("Export Report (.txt)");
        exportBtn.setOnAction(e -> exportTxtReport(medicines, statusMessage));

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());

        HBox bottomBar = new HBox(10, exportBtn, closeBtn, statusMessage);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(15, titleLabel, summaryBox, table, bottomBar);
        root.setPadding(new Insets(18));

        stage.setScene(new Scene(root, 620, 440));
        stage.showAndWait();
    }

    private static String getStatus(int quantity) {
        if (quantity <= 0) {
            return "Out of Stock";
        } else if (quantity <= 10) {
            return "Low Stock";
        } else {
            return "Available";
        }
    }

    private static VBox createStatCard(String labelText, String valueText) {
        Label lbl = new Label(labelText);
        lbl.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        Label val = new Label(valueText);
        val.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox box = new VBox(4, lbl, val);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 6; -fx-background-radius: 6;");
        return box;
    }

    private static void exportTxtReport(List<Medicine> medicines, Label statusMessage) {
        File folder = new File("Week3/Data");
        if (!folder.exists()) {
            folder = new File("Data");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }

        long totalCount = medicines.size();
        long availableCount = medicines.stream()
                .filter(m -> m.getQuantity() > 10)
                .count();
        long lowStockCount = medicines.stream()
                .filter(m -> m.getQuantity() > 0 && m.getQuantity() <= 10)
                .count();
        long outOfStockCount = medicines.stream()
                .filter(m -> m.getQuantity() <= 0)
                .count();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File outFile = new File(folder, "pharmacy_report_" + timestamp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(outFile, StandardCharsets.UTF_8))) {
            writer.write("=== PHARMACY INVENTORY REPORT ===");
            writer.newLine();
            writer.write("Generated at: " + LocalDateTime.now());
            writer.newLine();
            writer.newLine();
            writer.write("Total Items : " + totalCount);
            writer.newLine();
            writer.write("Available   : " + availableCount);
            writer.newLine();
            writer.write("Low Stock   : " + lowStockCount);
            writer.newLine();
            writer.write("Out of Stock: " + outOfStockCount);
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            for (Medicine m : medicines) {
                String status = getStatus(m.getQuantity());
                writer.write(String.format("Name: %s | Price: %.2f | Quantity: %d | Status: %s",
                        m.getName(), m.getPrice(), m.getQuantity(), status));
                writer.newLine();
            }

            statusMessage.setText("Saved to: " + outFile.getPath());
        } catch (IOException ex) {
            statusMessage.setText("Export failed: " + ex.getMessage());
        }
    }
}