package sample.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportsView {

    public static void show(List<Medicine> medicines) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Pharmacy Analytical Report");

        long totalItems = medicines.size();

        long availableCount = medicines.stream()
                .filter(m -> m.getQuantity() > 10)
                .count();

        long needsRestockCount = medicines.stream()
                .filter(m -> m.getQuantity() <= 10)
                .count();

        long outOfStockCount = medicines.stream()
                .filter(m -> m.getQuantity() <= 0)
                .count();

        Optional<Medicine> mostExpensiveOpt = medicines.stream()
                .max(Comparator.comparingDouble(Medicine::getPrice));

        Optional<Medicine> cheapestOpt = medicines.stream()
                .min(Comparator.comparingDouble(Medicine::getPrice));

        String mostExpensiveText = mostExpensiveOpt.isPresent()
                ? String.format("%s (%.2f)", mostExpensiveOpt.get().getName(), mostExpensiveOpt.get().getPrice())
                : "-";

        String cheapestText = cheapestOpt.isPresent()
                ? String.format("%s (%.2f)", cheapestOpt.get().getName(), cheapestOpt.get().getPrice())
                : "-";

        Label titleLabel = new Label("Inventory Insights & Restock Report");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox topCards = new HBox(12);
        topCards.getChildren().addAll(
                createMetricCard("Needs Restock", String.valueOf(needsRestockCount)),
                createMetricCard("Most Expensive", mostExpensiveText),
                createMetricCard("Cheapest", cheapestText)
        );

        VBox distributionBox = new VBox(8);
        distributionBox.setPadding(new Insets(10));
        distributionBox.setStyle("-fx-border-color: #dddddd; -fx-border-radius: 6;");
        distributionBox.getChildren().addAll(
                new Label("Stock Status Ratio"),
                createRatioRow("Available (> 10)", availableCount, totalItems),
                createRatioRow("Needs Restock (<= 10)", needsRestockCount, totalItems),
                createRatioRow("Out of Stock (0)", outOfStockCount, totalItems)
        );

        // افتراضياً نعرض الأصناف اللي محتاجة انتباه (<= 10) مرتبة حسب الكمية
        List<Medicine> urgentList = medicines.stream()
                .filter(m -> m.getQuantity() <= 10)
                .sorted(Comparator.comparingInt(Medicine::getQuantity))
                .collect(Collectors.toList());

        ObservableList<Medicine> tableItems = FXCollections.observableArrayList(urgentList);
        TableView<Medicine> table = new TableView<>(tableItems);

        TableColumn<Medicine, String> nameCol = new TableColumn<>("Medicine");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(180);

        TableColumn<Medicine, Integer> qtyCol = new TableColumn<>("Qty");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(70);

        TableColumn<Medicine, Double> unitPriceCol = new TableColumn<>("Unit Price");
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        unitPriceCol.setPrefWidth(95);

        TableColumn<Medicine, Double> lineValueCol = new TableColumn<>("Stock Value");
        lineValueCol.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(
                        Math.max(0, cellData.getValue().getQuantity()) * cellData.getValue().getPrice()
                ).asObject());
        lineValueCol.setPrefWidth(105);

        TableColumn<Medicine, String> statusCol = new TableColumn<>("Action / Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(getStatus(cellData.getValue().getQuantity())));
        statusCol.setPrefWidth(130);

        table.getColumns().addAll(nameCol, qtyCol, unitPriceCol, lineValueCol, statusCol);

        ComboBox<String> modeBox = new ComboBox<>();
        modeBox.getItems().addAll(
                "Action Required (Low & Out)",
                "All Medicines (By Value Desc)"
        );
        modeBox.setValue("Action Required (Low & Out)");
        modeBox.setOnAction(e -> {
            if ("All Medicines (By Value Desc)".equals(modeBox.getValue())) {
                List<Medicine> byValue = medicines.stream()
                        .sorted((a, b) -> Double.compare(
                                Math.max(0, b.getQuantity()) * b.getPrice(),
                                Math.max(0, a.getQuantity()) * a.getPrice()
                        ))
                        .collect(Collectors.toList());
                table.setItems(FXCollections.observableArrayList(byValue));
            } else {
                table.setItems(FXCollections.observableArrayList(urgentList));
            }
        });

        Label statusMessage = new Label();
        Button exportBtn = new Button("Export Insights (.txt)");
        exportBtn.setOnAction(e -> exportInsightsTxt(
                medicines, needsRestockCount, outOfStockCount,
                mostExpensiveText, cheapestText, statusMessage
        ));

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());

        HBox filterBar = new HBox(10, new Label("Report View:"), modeBox);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        HBox bottomBar = new HBox(10, exportBtn, closeBtn, statusMessage);
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(12, titleLabel, topCards, distributionBox, filterBar, table, bottomBar);
        root.setPadding(new Insets(16));

        stage.setScene(new Scene(root, 700, 550));
        stage.showAndWait();
    }

    private static HBox createRatioRow(String label, long count, long total) {
        double ratio = total == 0 ? 0 : (double) count / total;
        Label nameLbl = new Label(label);
        nameLbl.setPrefWidth(165);

        ProgressBar bar = new ProgressBar(ratio);
        bar.setPrefWidth(220);

        Label pctLbl = new Label(String.format("%d (%.0f%%)", count, ratio * 100));
        HBox row = new HBox(10, nameLbl, bar, pctLbl);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private static VBox createMetricCard(String title, String value) {
        Label t = new Label(title);
        t.setStyle("-fx-font-size: 11px; -fx-text-fill: #555555;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        VBox box = new VBox(4, t, v);
        box.setPadding(new Insets(10));
        box.setPrefWidth(205);
        box.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 6;");
        return box;
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

    private static void exportInsightsTxt(
            List<Medicine> medicines,
            long needsRestockCount,
            long outOfStockCount,
            String mostExpensiveText,
            String cheapestText,
            Label statusMessage
    ) {
        File folder = new File("Week3/Data");
        if (!folder.exists()) {
            folder = new File("Data");
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File outFile = new File(folder, "pharmacy_insights_" + timestamp + ".txt");

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(outFile, StandardCharsets.UTF_8))) {
            writer.write("=== PHARMACY INVENTORY INSIGHTS REPORT ===");
            writer.newLine();
            writer.write("Generated at: " + LocalDateTime.now());
            writer.newLine();
            writer.newLine();
            writer.write(String.format("Needs Restock (<=10): %d%n", needsRestockCount));
            writer.write(String.format("Out of Stock (0)    : %d%n", outOfStockCount));
            writer.write(String.format("Most Expensive      : %s%n", mostExpensiveText));
            writer.write(String.format("Cheapest            : %s%n", cheapestText));
            writer.write(String.format("Total SKUs          : %d%n", medicines.size()));
            writer.newLine();
            writer.write("--- URGENT RESTOCK LIST (Qty <= 10) ---");
            writer.newLine();

            medicines.stream()
                    .filter(m -> m.getQuantity() <= 10)
                    .sorted(Comparator.comparingInt(Medicine::getQuantity))
                    .forEach(m -> {
                        try {
                            double lineVal = Math.max(0, m.getQuantity()) * m.getPrice();
                            writer.write(String.format("- %s | Qty: %d | Unit: %.2f | Value: %.2f | %s%n",
                                    m.getName(), m.getQuantity(), m.getPrice(), lineVal,
                                    getStatus(m.getQuantity())));
                        } catch (IOException ignored) {
                        }
                    });

            statusMessage.setText("Saved to: " + outFile.getPath());
        } catch (IOException ex) {
            statusMessage.setText("Export failed: " + ex.getMessage());
        }
    }
}