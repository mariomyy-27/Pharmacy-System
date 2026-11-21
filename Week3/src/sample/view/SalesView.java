package sample.view;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import sample.model.Sale;

public class SalesView {

    private final Stage stage;
    private final TableView<Sale> salesTable;
    private final ComboBox<String> medicineBox;
    private final TextField quantityField;
    private final Button sellButton;
    private final Label totalLabel;

    public SalesView(ObservableList<Sale> sales) {

        stage = new Stage();
        stage.setTitle("Sales");

        Label title = new Label("Sales");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        medicineBox = new ComboBox<>();
        medicineBox.setPromptText("Select Medicine");

        quantityField = new TextField();
        quantityField.setPromptText("Amount");

        sellButton = new Button("Sell");

        HBox form = new HBox(10, medicineBox, quantityField, sellButton);

        salesTable = new TableView<>();
        salesTable.setItems(sales);

        TableColumn<Sale, String> nameColumn = new TableColumn<>("Medicine Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("medicineName"));

        TableColumn<Sale, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Sale, Integer> amountColumn = new TableColumn<>("Amount Sold");
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amountSold"));

        TableColumn<Sale, Double> totalColumn = new TableColumn<>("Total Price");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        salesTable.getColumns().addAll(
                nameColumn, priceColumn, amountColumn, totalColumn
        ); 

      nameColumn.setPrefWidth(250);
     priceColumn.setPrefWidth(150);
     amountColumn.setPrefWidth(180);
     totalColumn.setPrefWidth(200);

salesTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        totalLabel = new Label("Total Sales: 0.0");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox root = new VBox(20, title, form, salesTable, totalLabel);
        root.setPadding(new Insets(25));

        stage.setScene(new Scene(root, 850, 600));
    }

    public Stage getStage() {
        return stage;
    }

    public TableView<Sale> getSalesTable() {
        return salesTable;
    }

    public ComboBox<String> getMedicineBox() {
        return medicineBox;
    }

    public TextField getQuantityField() {
        return quantityField;
    }

    public Button getSellButton() {
        return sellButton;
    }

    public void updateTotal(double total) {
        totalLabel.setText("Total Sales: " + total);
    }

    public void show() {
        stage.show();
    }
}