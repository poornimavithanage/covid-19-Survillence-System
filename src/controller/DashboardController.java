package controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class DashboardController{
    public AnchorPane root;
    public ImageView btnLogOut;
    public Label lblUserName;
    public Button btnHospitalMgt;
    public Button btnQCMgt;
    public Button btnUserMgt;
    public Button btnGlobalUpdates;

    public void initialize(){
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        lblUserName.setText("Welcome "+LoginController.user_name);
    }


    public void btn_globalCovidUpdate_OnAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/GlobalCOVID19.fxml"));
            Scene globalCovidUpdate = new Scene(root);
            Stage stage = (Stage) (this.root.getScene().getWindow());
            stage.setScene(globalCovidUpdate);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btn_hospitalMgt_OnAction(ActionEvent event) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {
            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/HospitalMgtForm.fxml"));
                Scene hospitalMgt = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(hospitalMgt);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No access to view hospital management", ButtonType.OK).showAndWait();
        }
    }

    public void btn_QCMgt_OnAction(ActionEvent event) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {

            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/QuarantineCenterMgtForm.fxml"));
                Scene qcMgt = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(qcMgt);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No access to view Quarantine Center management", ButtonType.OK).showAndWait();
        }
    }

    public void btn_userMgt_OnAction(ActionEvent event) {
        if (LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")) {
            try {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/UserMgtForm.fxml"));
                Scene userMgt = new Scene(root);
                Stage stage = (Stage) (this.root.getScene().getWindow());
                stage.setScene(userMgt);
                stage.centerOnScreen();
                stage.sizeToScene();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "No Access to add users", ButtonType.OK).showAndWait();
        }

    }

    public void icon_logout_OnMouseClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
            Scene loginScene = new Scene(root);
            Stage stage = (Stage)(this.root.getScene().getWindow());
            stage.setScene(loginScene);
            stage.centerOnScreen();
            stage.sizeToScene();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

