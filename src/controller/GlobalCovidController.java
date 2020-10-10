package controller;

import db.DB_connection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GlobalCovidController {
    public AnchorPane root;
    public DatePicker txtDate;
    public Pane Pane1;
    public TextField txtConfirmedCases;
    public TextField txtRecoveredCases;
    public TextField txtDeathCases;
    public Button btnUpdate;
    public Pane Pane2;
    public Label lblUpdatedDate;
    public Label lblConfirmedCases;
    public Label lblRecoveredCases;
    public Label lblDeathCases;
    public Label lbl_error_deathCases;
    public Label lbl_error_recoveredCases;
    public Label lbl_error_confirmedCases;
    public Label lbl_error_datePicker;

    public void initialize(){
        if(LoginController.user_role.equals("Hospital IT") || LoginController.user_role.equals("Quarantine IT")){
            Pane1.setDisable(true);
        }
        //Transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        //date limitation
        LocalDate minDate = LocalDate.of(2019,11,1);
        LocalDate maxDate = LocalDate.now();
        txtDate.setDayCellFactory(d->
                new DateCell(){
                    public void updateItem(LocalDate item, boolean empty){
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });

        loadAllEnteredRecords();
    }

    private void loadAllEnteredRecords() {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT date FROM GlobalCovidData ORDER BY date DESC LIMIT 1");
            ResultSet rst = pstm.executeQuery();
            if(rst.next()){
                lblUpdatedDate.setText(rst.getDate(1)+"");

                //load count cases
                sumOfCases();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sumOfCases() {
        try {
            PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT SUM(confirmedCases),SUM(recoveredCases),SUM(deadCases) FROM GlobalCovidData");
            ResultSet rst = preparedStatement.executeQuery();
            if(rst.next()){
                lblConfirmedCases.setText(rst.getInt(1)+"");
                lblRecoveredCases.setText(rst.getInt(2)+"");
                lblDeathCases.setText(rst.getInt(3)+"");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (txtDate.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnUpdate_OnAction(ActionEvent event) {
        if(LoginController.user_role.equals("Admin") || LoginController.user_role.equals("P.S.T.F")){

            boolean dateValidation = FormValidation.dateValidation(txtDate,lbl_error_datePicker,"* required date");
            boolean numberValidation = FormValidation.numberValidation(txtConfirmedCases,lbl_error_confirmedCases,"* input a number");
            boolean numberValidation1 = FormValidation.numberValidation(txtRecoveredCases,lbl_error_recoveredCases,"* Input a number");
            boolean numberValidation2 = FormValidation.numberValidation(txtDeathCases,lbl_error_deathCases,"* Input a number");

            if(dateValidation && numberValidation && numberValidation1 && numberValidation2){
                if(btnUpdate.getText().equals("Add New Record")) {
                    try {
                        PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                                ("INSERT INTO GlobalCovidData (admin_id, date, confirmedCases, recoveredCases, deadCases) VALUES (?,?,?,?,?)");
                        pstm.setObject(1,LoginController.user_id);
                        pstm.setObject(2,txtDate.getValue());
                        pstm.setObject(3,txtConfirmedCases.getText());
                        pstm.setObject(4,txtRecoveredCases.getText());
                        pstm.setObject(5,txtDeathCases.getText());
                        int affectedRows = pstm.executeUpdate();
                        if(affectedRows > 0){
                            new Alert(Alert.AlertType.INFORMATION,"Record inserted successfully").showAndWait();
                        }
                        loadAllEnteredRecords();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        PreparedStatement preparedStatement1 = DB_connection.getInstance().getConnection().prepareStatement
                                ("UPDATE GlobalCovidData SET admin_id=?,confirmedCases=?,recoveredCases=?,deadCases=? WHERE date=?");
                       preparedStatement1.setObject(1,LoginController.user_id);
                       preparedStatement1.setObject(2,txtDate.getValue());
                       preparedStatement1.setObject(3,txtConfirmedCases.getText());
                       preparedStatement1.setObject(4,txtRecoveredCases.getText());
                       preparedStatement1.setObject(5,txtDeathCases.getText());
                       int affectedRows = preparedStatement1.executeUpdate();

                       if(affectedRows > 0){
                           new Alert(Alert.AlertType.INFORMATION,"Record updated successfully").showAndWait();
                       }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    btnUpdate.setText("Add New Record");
                    clearTextField();
                    txtDate.getEditor().clear();
                    txtDate.requestFocus();
                    loadAllEnteredRecords();
                }
            }
        }else{
            Pane1.setVisible(false);

        }

    }

    public void date_picker_OnAction(ActionEvent event) {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM GlobalCovidData WHERE date=?");
            pstm.setObject(1,txtDate.getValue());
            ResultSet rst = pstm.executeQuery();
            if(rst.next()){
                btnUpdate.setText("Update");
                btnUpdate.setStyle("-fx-background-color: #f44336");

                txtConfirmedCases.setText(rst.getString(5));
                txtRecoveredCases.setText(rst.getString(6));
                txtDeathCases.setText(rst.getString(7));
            }else{
                clearTextField();
                btnUpdate.setText("Add New Record");
                btnUpdate.setStyle("-fx-background-color: #4c54af");
                txtConfirmedCases.requestFocus();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTextField() {
        txtConfirmedCases.clear();
        txtDeathCases.clear();
        txtRecoveredCases.clear();
    }
}
