package controller;

import db.DB_connection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
    public AnchorPane root;
    public TextField txtUsername;
    public Label lblUsernameError;
    public PasswordField txtPassword;
    public Label lblPasswordError;
    public ComboBox cmbUserRole;
    public Label lblUserRoleError;
    public Button btnLogin;
    public ImageView icon_loading;
    public static String user_id;
    public static String user_name;
    public static String user_role;


    public void initialize(){
        //Transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2),root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        cmbUserRole.getItems().addAll("Admin","P.S.T.F","Hospital IT","Quarantine IT");
        icon_loading.setVisible(false);
    }

    public void btnLogin_OnAction(ActionEvent event) {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM User WHERE username=? AND password=? AND userRole=? ");
            pstm.setObject(1,txtUsername.getText());
            pstm.setObject(2,txtPassword.getText());
            pstm.setObject(3,cmbUserRole.getSelectionModel().getSelectedItem());

            ResultSet rst = pstm.executeQuery();
            if(rst.next()){
                 user_id = rst.getString(1);
                 user_role=rst.getString(2);
                 user_name=rst.getString(3);

                try {
                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml"));
                    Scene dashboardScene = new Scene(root);
                    Stage stage = (Stage)(this.root.getScene().getWindow());
                    stage.setScene(dashboardScene);
                    stage.centerOnScreen();
                    stage.sizeToScene();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                //form Validation
                //boolean userNameValidation = FormValidation.userNameValidation(txtUsername,lblUsernameError,"* Wrong user name");
               // boolean passwordValidation = FormValidation.passwordValidation(txtPassword,lblPasswordError,"* Wrong Password");
                //boolean userRoleValidation = FormValidation.comBoxValidation(cmbUserRole,lblUserRoleError,"* Wrong user role");

                lblUsernameError.setStyle("-fx-border-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");
                lblPasswordError.setStyle("-fx-border-color: red;-fx-background-radius: 50px;-fx-border-radius: 50px");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
