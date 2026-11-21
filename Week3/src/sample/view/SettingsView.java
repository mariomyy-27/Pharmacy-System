

package sample.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsView {

    private Stage stage;

    private PasswordField oldPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmPasswordField;

    private RadioButton lightTheme;
    private RadioButton darkTheme;

    private String currentPassword = "1234";

    public SettingsView() {
        stage = new Stage();
    }

    public void show() {

        VBox layout = new VBox(15);

        Label title = new Label("Settings");

        oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Enter Current Password");

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Button saveButton = new Button("Save Changes");

        Label themeLabel = new Label("Choose Theme:");

        lightTheme = new RadioButton("Light");
        darkTheme = new RadioButton("Dark");

        ToggleGroup themeGroup = new ToggleGroup();

        lightTheme.setToggleGroup(themeGroup);
        darkTheme.setToggleGroup(themeGroup);

        lightTheme.setSelected(true);

        layout.setStyle("-fx-background-color: #f2f2f2;");

        lightTheme.setOnAction(event -> {
            layout.setStyle("-fx-background-color: #f2f2f2;");
        });

        darkTheme.setOnAction(event -> {
            layout.setStyle("-fx-background-color: #3a3a3a;");
        });

        saveButton.setOnAction(event -> changePassword());

        layout.setPadding(new Insets(20));

        layout.getChildren().addAll(
                title,
                oldPasswordField,
                newPasswordField,
                confirmPasswordField,
                saveButton,
                themeLabel,
                lightTheme,
                darkTheme
        );

        Scene scene = new Scene(layout, 400, 500);

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

        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "New passwords do not match!");
            return;
        }

        currentPassword = newPasswordField.getText();

        showAlert("Success", "Password changed successfully!");

        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
