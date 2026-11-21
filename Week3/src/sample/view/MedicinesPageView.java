package sample.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sample.model.Medicine;

public class MedicinesPageView {

    private final Stage stage;
    private final TextField searchField;
    private final Button searchButton;
    private final Label totalValueLabel;
    private final Label availableValueLabel;
    private final Label outOfStockValueLabel;
    private final TableView<Medicine> medicineTable;

    public MedicinesPageView(ObservableList<Medicine> medicines) {
        stage = new Stage();
        stage.setTitle("Medicines");

        Label title = new Label("Medicine Search");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        Label description = new Label("Search and view medicines");
        VBox titleSection = new VBox(5, title, description);

        searchField = new TextField();
        searchField.setPromptText("Search medicine name...");
        searchField.setPrefHeight(42);

        searchButton = new Button("Search");
        searchButton.setPrefHeight(42);
        searchButton.setPrefWidth(90);

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        totalValueLabel = createValueLabel("0");
        availableValueLabel = createValueLabel("0");
        outOfStockValueLabel = createValueLabel("0");

        VBox totalCard = createCard("Total Medicines", totalValueLabel);
        VBox availableCard = createCard("Available", availableValueLabel);
        VBox outOfStockCard = createCard("Out of Stock", outOfStockValueLabel);

        HBox statsBox = new HBox(20, totalCard, availableCard, outOfStockCard);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setFillHeight(true);
        HBox.setHgrow(totalCard, Priority.ALWAYS);
        HBox.setHgrow(availableCard, Priority.ALWAYS);
        HBox.setHgrow(outOfStockCard, Priority.ALWAYS);

        Label tableTitle = new Label("Available Medicines");
        tableTitle.setFont(Font.font("System", FontWeight.BOLD, 15));

        medicineTable = new TableView<>();
        medicineTable.setItems(medicines);
        setupColumns();

        VBox root = new VBox(20, titleSection, searchBox, statsBox, tableTitle, medicineTable);
        root.setPadding(new Insets(25));
        VBox.setVgrow(medicineTable, Priority.ALWAYS);

        Scene scene = new Scene(root, 950, 650);
        stage.setScene(scene);
    }

    private Label createValueLabel(String initialValue) {
        Label label = new Label(initialValue);
        label.setFont(Font.font("System", FontWeight.BOLD, 22));
        label.setAlignment(Pos.CENTER);
        return label;
    }

    private VBox createCard(String title, Label valueLabel) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        titleLabel.setAlignment(Pos.CENTER);

        VBox card = new VBox(8, titleLabel, valueLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(18));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setStyle(
                "-fx-background-color: #fafafa;" +
                "-fx-border-color: #e0e0e0;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );
        return card;
    }

    private void setupColumns() {
        TableColumn<Medicine, String> nameColumn = new TableColumn<>("Medicine");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Medicine, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Medicine, Integer> quantityColumn = new TableColumn<>("Stock");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Medicine, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setText(null);
                } else {
                    int quantity = getTableView().getItems().get(getIndex()).getQuantity();
                    if (quantity == 0) {
                        setText("● Out of Stock");
                    } else if (quantity <= 10) {
                        setText("● Low Stock");
                    } else {
                        setText("● Available");
                    }
                }
            }
        });

        medicineTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn, statusColumn);
        nameColumn.setPrefWidth(250);
        priceColumn.setPrefWidth(150);
        quantityColumn.setPrefWidth(150);
        statusColumn.setPrefWidth(180);
        medicineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void updateStatistics(int total, int available, int outOfStock) {
        totalValueLabel.setText(String.valueOf(total));
        availableValueLabel.setText(String.valueOf(available));
        outOfStockValueLabel.setText(String.valueOf(outOfStock));
    }

    public void refreshStatistics(ObservableList<Medicine> medicines) {
        int total = medicines.size();
        int available = 0;
        int outOfStock = 0;

        for (Medicine m : medicines) {
            if (m.getQuantity() > 0) {
                available++;
            } else {
                outOfStock++;
            }
        }

        updateStatistics(total, available, outOfStock);
    }

    public Stage getStage() {
        return stage;
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public TableView<Medicine> getMedicineTable() {
        return medicineTable;
    }

    public void show() {
        stage.show();
    }
}
