package sample.controller;

import javafx.scene.control.Alert;

import sample.service.UserService;
import sample.view.HomeView;
import sample.view.SignUpView;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class HomeController {

    private final HomeView view;
    private final UserService userService;
    private final MedicineController medicineController;

    public HomeController() {

        view = new HomeView();

        userService = new UserService();

        medicineController = new MedicineController();

        medicineController.getView()
                .setCenterContent(view.getRoot());

        setupLoginButton();
        setupHomeButton();
        setupSignUpButton();
    }

    private void setupLoginButton() {

        view.getLoginButton().setOnAction(e -> login());
    }

    private void login() {

        String userName = view.getUserName();
        String password = view.getPassword();

        if (userName.isEmpty() || password.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Data",
                    "Please enter username and password."
            );

            return;
        }

        if (userService.isAuthorized(userName, password)) {

            medicineController.getView().setUserName(userName);

            medicineController.getView().showMedicineContent();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Login Failed",
                    "Invalid username or password."
            );
        }
    }

    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    private void setupHomeButton() {

        medicineController.getView()
                .getHomeButton()
                .setOnAction(e -> {

                    view.clearFields();

                    medicineController.getView().setUserName("");

                    medicineController.getView()
                            .setCenterContent(view.getRoot());

                });
    }


    private void setupSignUpButton() {

        view.getSignUpButton().setOnAction(e -> {

            SignUpView signUpView = new SignUpView();

            Stage signUpStage = new Stage();

            Scene scene = new Scene(
                    signUpView.getRoot(),
                    400,
                    350
            );

            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(scene);

            signUpView.getCreateAccountButton().setOnAction(event -> {

                String userName = signUpView.getUserName()
                        .trim().toLowerCase();

                String password = signUpView.getPassword();
                String confirmPassword = signUpView.getConfirmPassword();

                if (userName.isEmpty() || password.isEmpty()
                        || confirmPassword.isEmpty()) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Missing Data",
                            "Please fill in all fields."
                    );
                    return;
                }

                if (!password.equals(confirmPassword)) {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Password Error",
                            "Passwords do not match."
                    );
                    return;
                }

                if (userService.userNameExists(userName)) {

                    showAlert(
                            Alert.AlertType.WARNING,
                            "Username Exists",
                            "This username is already taken."
                    );
                    return;
                }

                if (userService.registerNewUser(userName, password)) {

                    showAlert(
                            Alert.AlertType.INFORMATION,
                            "Success",
                            "Account created successfully!"
                    );

                    signUpStage.close();

                } else {

                    showAlert(
                            Alert.AlertType.ERROR,
                            "Registration Failed",
                            "Could not create account."
                    );
                }
            });

            signUpStage.show();
        });
    }


    public MedicineController getMedicineController() {

        return medicineController;
    }
}