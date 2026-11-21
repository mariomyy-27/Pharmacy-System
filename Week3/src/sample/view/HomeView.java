package sample.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;

public class HomeView {

    private final Image logo;
    private final ImageView logoView;

    private final TextField userNameField;
    private final PasswordField passwordField;

    private final Label name;
    private final Label password;

    private final Button loginButton;
    private final Button signUpButton;

    private final BorderPane bPane;

    private final HBox userNamePane;
    private final HBox passwordPane;
    private final VBox fields;

    private final StackPane root;

    public HomeView() {

        bPane = new BorderPane();

        userNamePane = new HBox(10);
        passwordPane = new HBox(18);
        userNamePane.setAlignment(Pos.CENTER_LEFT);
        passwordPane.setAlignment(Pos.CENTER_LEFT);

        fields = new VBox(18);
        fields.setAlignment(Pos.CENTER);

        root = new StackPane();

        logo = new Image(
                new File("Week3/Data/Logo.jpg").toURI().toString()
        );

        logoView = new ImageView(logo);
        logoView.setFitWidth(200);
        logoView.setFitHeight(200);
        logoView.setPreserveRatio(true);

        name = new Label("User Name:");
        password = new Label("Password:");

        userNameField = new TextField();
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        signUpButton = new Button("Sign Up");

        userNamePane.getChildren().addAll(name, userNameField);
        passwordPane.getChildren().addAll(password, passwordField);
        fields.getChildren().addAll(userNamePane, passwordPane);

        bPane.setTop(logoView);
        BorderPane.setAlignment(logoView, Pos.CENTER);
        BorderPane.setMargin(logoView, new Insets(20, 0, 25, 0));

        bPane.setCenter(fields);
        BorderPane.setAlignment(fields, Pos.CENTER);
        BorderPane.setMargin(fields, new Insets(0, 0, 22, 0));

        VBox buttons = new VBox(10);
        Label text = new Label("Don't have \nan account?");
        text.setStyle("-fx-font-size: 12px; -fx-text-fill: skyblue;");
        buttons.setAlignment(Pos.CENTER);

        buttons.getChildren().addAll(loginButton, text, signUpButton);

        bPane.setBottom(buttons);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        BorderPane.setMargin(buttons, new Insets(0, 0, 20, 0));

        bPane.setPadding(new Insets(20));

        bPane.setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE
        );

        root.getChildren().add(bPane);

        StackPane.setAlignment(bPane, Pos.TOP_LEFT);

        bPane.translateXProperty().bind(
                Bindings.subtract(
                        root.widthProperty(),
                        bPane.widthProperty()
                ).divide(2)
        );

        bPane.translateYProperty().bind(
                Bindings.subtract(
                        root.heightProperty(),
                        bPane.heightProperty()
                ).divide(2)
        );
    }

    public void clearFields() {
        userNameField.clear();
        passwordField.clear();
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getSignUpButton() {
        return signUpButton;
    }

    public String getUserName() {
        return userNameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}