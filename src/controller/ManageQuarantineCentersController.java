package controller;

import db.DB_connection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.QuarantineCenterTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManageQuarantineCentersController {
    public AnchorPane root;
    public TextField txtSearchQuarantineCenter;
    public TextField txtQuarantineCenterID;
    public TextField txtQuarantineCenterName;
    public TextField txtQuarantineCenterCity;
    public TextField txtQuarantineCenterCapacity;
    public TextField txtHeadName;
    public ComboBox<String> cmbDistrict;
    public TextField txtHeadContactNo;
    public TextField txtQuarantineCenterContactNo1;
    public TextField txtQuarantineCenterContactNo2;
    public Button btnSave;
    public Button btnDelete;
    public Button btnAddNewQuarantineCenter;
    public ListView<QuarantineCenterTM>centerDetails;
    public Label lblQC_Contact2Error;
    public Label lblQC_Contact1Error;
    public Label lblHeadContactError;
    public Label lblHeadNameError;
    public Label lblCapacityError;
    public Label lblDistrictError;
    public Label lblCityError;
    public Label lblQC_NameError;
    public Label lblQC_IDError;
    private ArrayList<QuarantineCenterTM> centerTMArrayList = new ArrayList<>();

    public void initialize(){

        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
                {
                    btnAddNewQuarantineCenter.requestFocus();
                }
        );

        txtQuarantineCenterID.setDisable(true);

        String districtsText=" Colombo\n" +
                " Gampaha\n" +
                " Kalutara\n" +
                " Kandy\n" +
                " Matale\n" +
                " Nuwara Eliya\n" +
                " Galle\n" +
                " Matara\n" +
                " Hambantota\n" +
                " Jaffna\n" +
                " Mannar\n" +
                " Vauniya\n" +
                " Mullativue\n" +
                " Ampara\n" +
                " Trincomalee\n" +
                " Batticaloa\n" +
                " Kilinochchi\n" +
                " Kurunegala\n" +
                " Puttalam\n" +
                " Anuradhapura\n" +
                " Polonnaruwa\n" +
                " Badulla\n" +
                " Moneragala\n" +
                " Ratnapura\n" +
                " Kegalle";
        String[] districts = districtsText.split("\n");
        ObservableList<String> olDistricts = FXCollections.observableArrayList(Arrays.asList(districts));
        cmbDistrict.setItems(olDistricts);

        //load all centers
        loadAllCenters();

        //selection of the listView
        centerDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QuarantineCenterTM>() {
            @Override
            public void changed(ObservableValue<? extends QuarantineCenterTM> observable, QuarantineCenterTM oldValue, QuarantineCenterTM newValue) {
                if(newValue == null){
                    centerDetails.getSelectionModel().clearSelection();
                }else{
                    txtQuarantineCenterID.setText(newValue.getId());
                    txtQuarantineCenterName.setText(newValue.getQuarantineCenterName());
                    txtQuarantineCenterCity.setText(newValue.getCity());
                    cmbDistrict.getSelectionModel().select(newValue.getDistrict());
                    txtQuarantineCenterCapacity.setText(newValue.getCapacity());
                    txtHeadName.setText(newValue.getHeadName());
                    txtHeadContactNo.setText(newValue.getHeadContact());
                    txtQuarantineCenterContactNo1.setText(newValue.getCenterContact1());
                    txtQuarantineCenterContactNo2.setText(newValue.getCenterContact2());
                    btnSave.setText("Update");
                    btnSave.setDisable(false);
                    btnDelete.setDisable(false);
                }
            }
        });

        //search quarantine centers
        txtSearchQuarantineCenter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<QuarantineCenterTM>searchCenters = centerDetails.getItems();
                centerDetails.getItems().clear();
                for(QuarantineCenterTM qc_centers : centerTMArrayList){
                    if(qc_centers.getQuarantineCenterName().contains(newValue)){
                        searchCenters.add(qc_centers);
                    }

                }
            }
        });

    }

    private void loadAllCenters() {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT quarantineCenterId,quarantineCenterName,city,district,headName,headContactNo,centerContactNo1,centerContactNo2,capacity FROM QuarantineCenter");
            ResultSet rst = pstm.executeQuery();
            centerDetails.getItems().clear();
            while (rst.next()){
                String centerId = rst.getString(1);
                String centerName = rst.getString(2);
                String city = rst.getString(3);
                String district = rst.getString(4);
                String headName = rst.getString(5);
                String headContactNo = rst.getString(6);
                String centerContact1 = rst.getString(7);
                String centerContact2 = rst.getString(8);
                String capacity = rst.getString(9);

                QuarantineCenterTM center_details = new QuarantineCenterTM(centerId,centerName,city,district,headName,headContactNo,
                        centerContact1,centerContact2,capacity);
                centerDetails.getItems().add(center_details);
                centerTMArrayList.add(center_details);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void btnHome_OnAction(ActionEvent event) throws IOException {
        Scene mainScene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/Dashboard.fxml")));
        Stage primaryStage = (Stage) (cmbDistrict.getScene().getWindow());
        primaryStage.setScene(mainScene);
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
    }

    public void btnSave_onAction(ActionEvent event) {
        QuarantineCenterTM selectedCenter = centerDetails.getSelectionModel().getSelectedItem();

        String id = txtQuarantineCenterID.getText();
        String centerName = txtQuarantineCenterName.getText();
        String city = txtQuarantineCenterCity.getText();
        String district = cmbDistrict.getSelectionModel().getSelectedItem();
        String capacity = txtQuarantineCenterCapacity.getText();
        String headName = txtHeadName.getText();
        String headContactNo = txtHeadContactNo.getText();
        String centerContact1 = txtQuarantineCenterContactNo1.getText();
        String centerContact2 = txtQuarantineCenterContactNo2.getText();

        //Form Validation
        boolean userIDValidation = FormValidation.userIDValidation(txtQuarantineCenterID,lblQC_IDError,"* Press Add New QC Button");
        boolean nameValidation = FormValidation.nameValidation(txtQuarantineCenterName,lblQC_NameError,"* Insert a valid QC name");
        boolean cityValidation = FormValidation.cityValidation(txtQuarantineCenterCity,lblCityError,"* Insert a valid city name");
        boolean districtValidation = FormValidation.comBoxValidation(cmbDistrict,lblDistrictError,"* Select a valid district ");
        boolean capacityValidation = FormValidation.capacity(txtQuarantineCenterCapacity,lblCapacityError,"* Insert a valid capacity");
        boolean headNameValidation = FormValidation.directorName(txtHeadName,lblHeadNameError,"* Insert a valid name");
        boolean headContactNoValidation = FormValidation.contactNumber(txtHeadContactNo,lblHeadContactError,"* xxx-xxxxxxx");
        boolean qcContactNo1Validation = FormValidation.contactNumber(txtQuarantineCenterContactNo1,lblQC_Contact1Error,"xxx-xxxxxxx");
        boolean qcContactNo2Validation = FormValidation.contactNumber(txtQuarantineCenterContactNo2,lblQC_Contact2Error,"* xxx-xxxxxxx");

        if(userIDValidation && nameValidation && cityValidation && districtValidation &&
        capacityValidation && headNameValidation && headContactNoValidation && qcContactNo1Validation && qcContactNo2Validation){
            if(btnSave.getText().equals("Save")) {
                String sql = "INSERT INTO QuarantineCenter(quarantineCenterId, quarantineCenterName, city, district, capacity,headName, headContactNo, centerContactNo1, centerContactNo2 ) VALUES (?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement(sql);
                    pstm.setObject(1, id);
                    pstm.setObject(2, centerName);
                    pstm.setObject(3, city);
                    pstm.setObject(4, district);
                    pstm.setObject(5, capacity);
                    pstm.setObject(6, headName);
                    pstm.setObject(7, headContactNo);
                    pstm.setObject(8, centerContact1);
                    pstm.setObject(9, centerContact2);

                    int affectedRows = pstm.executeUpdate();
                    if (affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Center added successfully", ButtonType.OK).showAndWait();
                        loadAllCenters();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can not add Center.Please contact admin", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                String sql = "UPDATE QuarantineCenter SET quarantineCenterId=?,quarantineCenterName=?,city=?,district=?," +
                        "headName=?,headContactNo=?,centerContactNo1=?,centerContactNo2=?,capacity=? WHERE quarantineCenterId=?";
                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement(sql);
                    pstm.setObject(1, id);
                    pstm.setObject(2, centerName);
                    pstm.setObject(3, city);
                    pstm.setObject(4, district);
                    pstm.setObject(5, capacity);
                    pstm.setObject(6, headName);
                    pstm.setObject(7, headContactNo);
                    pstm.setObject(8, centerContact1);
                    pstm.setObject(9, centerContact2);
                    pstm.setObject(10,selectedCenter.getId());
                    int affectedRows = pstm.executeUpdate();
                    if(affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Updated successfully",ButtonType.OK).showAndWait();
                        clearForm();
                        centerDetails.refresh();
                        btnSave.setText("Save");
                        loadAllCenters();
                    }else{

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void btnDelete_onAction(ActionEvent event) {
    QuarantineCenterTM selectedCenter = centerDetails.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("DELETE FROM QuarantineCenter WHERE quarantineCenterId=?");
            pstm.setObject(1,selectedCenter.getId());
            int affectedRows = pstm.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                centerDetails.refresh();
                centerDetails.getSelectionModel().clearSelection();
                loadAllCenters();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddNew_onAction(ActionEvent event) {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT quarantineCenterId FROM QuarantineCenter ORDER BY quarantineCenterId DESC LIMIT 1");
            ResultSet rst = pstm.executeQuery();
            if(!rst.next()){
                txtQuarantineCenterID.setText("C001");
            }else{
                String lastRowId = rst.getString(1);
                String subString = lastRowId.substring(1,4);
                int quarantineCenterId = Integer.parseInt(subString)+1;
                if(quarantineCenterId <10){
                    txtQuarantineCenterID.setText("C00" + quarantineCenterId);
                }else if(quarantineCenterId < 100){
                    txtQuarantineCenterID.setText("C0" + quarantineCenterId);
                }else{
                    txtQuarantineCenterID.setText("C" + quarantineCenterId);
                }
            }
            txtQuarantineCenterName.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtQuarantineCenterID.setText("CXXX");
        txtQuarantineCenterName.clear();
        txtQuarantineCenterCity.clear();
        cmbDistrict.getSelectionModel().clearSelection();
        txtQuarantineCenterCapacity.clear();
        txtHeadName.clear();
        txtHeadContactNo.clear();
        txtQuarantineCenterContactNo1.clear();
        txtQuarantineCenterContactNo2.clear();
    }
}
