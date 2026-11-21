

package sample.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsView {

    private Stage stage;

    private PasswordField oldPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;

    private String currentPassword = "1234";

    public SettingsView() {
        stage = new Stage();
    }

    public void show() {

        VBox layout = new VBox(15);

        layout.setPadding(new Insets(20));

        Label title = new Label("Settings");

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Enter Current Password");

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Button saveButton = new Button("Save Changes");
        Button clearButton = new Button("Clear Fields");
        Button aboutButton = new Button("About Application");
        Button closeButton = new Button("Close");

        saveButton.setOnAction(event -> changePassword());

        clearButton.setOnAction(event -> clearFields());

        aboutButton.setOnAction(event -> showAbout());

        closeButton.setOnAction(event -> stage.close());

        layout.getChildren().addAll(
                title,
                oldPasswordField,
                newPasswordField,
                confirmPasswordField,
                saveButton,
                clearButton,
                aboutButton,
                closeButton
        );

        Scene scene = new Scene(layout, 400, 430);

        stage.setTitle("Settings");
        stage.setScene(scene);
        stage.show();
    }

    private void changePassword() {

        if (!oldPasswordField.getText().equals(currentPassword)) {
            showAlert("Error", "Current password is incorrect!");
            return;
        }

        if (newPasswordField.getText().isEmpty()) {
            showAlert("Error", "New password cannot be empty!");
            return;
        }

        if (newPasswordField.getText().length() < 4) {
            showAlert("Error", "Password must be at least 4 characters!");
            return;
        }

        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "New passwords do not match!");
            return;
        }

        currentPassword = newPasswordField.getText();

        showAlert("Success", "Password changed successfully!");

        stage.close();
    }

    private void clearFields() {

        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showAbout() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("About Application");
        alert.setHeaderText("Pharmacy Management System");

        alert.setContentText(
                "This application helps manage pharmacy medicines.\n\n" +
                "Version: 1.0\n" +
                "Developed using Java and JavaFX."
        );

        alert.showAndWait();
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
