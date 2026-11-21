package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import sample.model.Medicine;
import sample.model.Sale;
import sample.service.MedicineService;
import sample.service.SalesService;
import sample.view.SalesView;

import java.util.List;

public class SalesController {

    private final ObservableList<Medicine> medicines;
    private final ObservableList<Sale> sales;
    private final MedicineService medicineService;
    private final SalesService salesService;
    private final SalesView view;

    public SalesController(ObservableList<Medicine> medicines) {

        this.medicines = medicines;

        medicineService = new MedicineService();
        salesService = new SalesService();

        sales = FXCollections.observableArrayList(
                salesService.loadSales()
        );

        view = new SalesView(sales);

        refreshMedicineBox();
        updateTotal();

        view.getSellButton().setOnAction(event -> sellMedicine());

        view.show();
    }

    private void refreshMedicineBox() {

        view.getMedicineBox().getItems().clear();

        for (Medicine medicine : medicines) {
            view.getMedicineBox().getItems().add(
                    medicine.getName()
            );
        }
    }

    private void sellMedicine() {

        String name = view.getMedicineBox().getValue();

        if (name == null) {
            showAlert("Please select a medicine.");
            return;
        }

        try {

            int amount = Integer.parseInt(
                    view.getQuantityField().getText()
            );

            if (amount <= 0) {
                showAlert("Amount must be greater than zero.");
                return;
            }

            Medicine selected = null;

            for (Medicine medicine : medicines) {
                if (medicine.getName().equals(name)) {
                    selected = medicine;
                    break;
                }
            }

            if (selected == null || amount > selected.getQuantity()) {
                showAlert("Not enough stock available.");
                return;
            }

            Medicine updated = new Medicine(
                    selected.getName(),
                    selected.getPrice(),
                    selected.getQuantity() - amount
            );

            medicineService.updateMedicine(selected, updated);

            int index = medicines.indexOf(selected);
            medicines.set(index, updated);

            Sale sale = new Sale(
                    selected.getName(),
                    selected.getPrice(),
                    amount
            );

            salesService.addSale(sale);
            sales.add(sale);

            view.getSalesTable().refresh();
            updateTotal();

            view.getQuantityField().clear();

            showAlert("Sale completed successfully.");

        } catch (NumberFormatException e) {
            showAlert("Enter a valid quantity.");
        }
    }

    private void updateTotal() {

        double total = 0;

        for (Sale sale : sales) {
            total += sale.getTotalPrice();
        }

        view.updateTotal(total);
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}