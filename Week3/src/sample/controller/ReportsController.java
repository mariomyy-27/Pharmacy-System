package sample.controller;

import sample.model.Medicine;
import sample.view.ReportsView;

import java.util.List;

public class ReportsController {

    private final List<Medicine> medicines;

    public ReportsController(List<Medicine> medicines) {
        this.medicines = medicines;
    }

    public void openReports() {
        ReportsView.show(medicines);
    }
}
