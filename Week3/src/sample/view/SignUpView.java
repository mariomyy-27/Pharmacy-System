
package sample.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class SignUpView {

    private final StackPane root;

    private final TextField userNameField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;

    private final Button createAccountButton;


    public SignUpView() {

        root = new StackPane();

        VBox fields = new VBox(15);
        fields.setAlignment(Pos.CENTER);
        fields.setPadding(new Insets(30));

        Label title = new Label("Create New Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label userNameLabel = new Label("User Name:");
        userNameField = new TextField();
        userNameField.setPromptText("Enter username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");

        createAccountButton = new Button("Create Account");

        HBox userNamePane = new HBox(48);
        HBox passwordPane = new HBox(56);
        HBox confirmPane = new HBox(10);
        HBox buttonPane = new HBox(8);

        userNamePane.getChildren().addAll(
                userNameLabel,
                userNameField
        );

        passwordPane.getChildren().addAll(
                passwordLabel,
                passwordField
        );

        confirmPane.getChildren().addAll(
                confirmPasswordLabel,
                confirmPasswordField
        );

        buttonPane.getChildren().add(
                createAccountButton
        );

        buttonPane.setAlignment(Pos.CENTER);

        fields.getChildren().addAll(
                title,
                userNamePane,
                passwordPane,
                confirmPane,
                buttonPane
        );

        root.getChildren().add(fields);

        fields.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );

        StackPane.setAlignment(fields, Pos.TOP_LEFT);

        fields.translateXProperty().bind(
                Bindings.subtract(
                        root.widthProperty(),
                        fields.widthProperty()
                ).divide(2)
        );

        fields.translateYProperty().bind(
                Bindings.subtract(
                        root.heightProperty(),
                        fields.heightProperty()
                ).divide(2)
        );
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getCreateAccountButton() {
        return createAccountButton;
    }

    public String getUserName() {
        return userNameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getConfirmPassword() {
        return confirmPasswordField.getText();
    }

    public void clearFields() {
        userNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
