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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.HospitalTM;

import java.awt.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManageHospitalController {

    public AnchorPane root;
    public TextField txtSearchHospital;
    public javafx.scene.control.TextField txtID;
    public javafx.scene.control.TextField txtHospitalName;
    public javafx.scene.control.TextField txtCity;
    public javafx.scene.control.TextField txtCapacity;
    public javafx.scene.control.TextField txtDirectorName;
    public ComboBox<String> cmbDistrict;
    public javafx.scene.control.TextField txtDirectorContactNo;
    public javafx.scene.control.TextField txtHospitalContactNo1;
    public javafx.scene.control.TextField txtHospitalContactNo2;
    public javafx.scene.control.TextField txtHospitalFax;
    public javafx.scene.control.TextField txtHospitalEmail;
    public ListView<HospitalTM> hospitalDetails;
    public javafx.scene.control.Label lblEmailError;
    public javafx.scene.control.Label lblFaxError;
    public javafx.scene.control.Label lblHospitalContactError2;
    public javafx.scene.control.Label lblHospitalContactError1;
    public javafx.scene.control.Label lblDirectorContactError;
    public javafx.scene.control.Label lblDirectorNameError;
    public javafx.scene.control.Label lblCapacityError;
    public javafx.scene.control.Label lblDistrictError;
    public javafx.scene.control.Label lblCityError;
    public javafx.scene.control.Label lblHospitalNameError;
    public javafx.scene.control.Label lblIDError;
    public javafx.scene.control.TextField txtSearchHospital1;
    public javafx.scene.control.Button btnSave1;
    public javafx.scene.control.Button btnDelete1;
    public javafx.scene.control.Button btnAddNewHospital1;
    private ArrayList<HospitalTM> hospitalTMArrayList = new ArrayList<>();



    public void initialize(){
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
                {
                    btnAddNewHospital1.requestFocus();
                }
        );

        txtID.setDisable(true);

        String districtsText = " Colombo\n" +
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

        //load all hospitals
        loadAllHospitals();

        //selection of the listView
        hospitalDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HospitalTM>() {
            @Override
            public void changed(ObservableValue<? extends HospitalTM> observable, HospitalTM oldValue, HospitalTM newValue) {
                if(newValue == null){
                    hospitalDetails.getSelectionModel().clearSelection();

                }else{
                    txtID.setText(newValue.getId());
                    txtHospitalName.setText((newValue.getHospitalName()));
                    txtCity.setText(newValue.getCity());
                    cmbDistrict.getSelectionModel().select(newValue.getDistrict());
                    txtCapacity.setText(newValue.getCapacity());
                    txtDirectorName.setText(newValue.getDirectorName());
                    txtDirectorContactNo.setText(newValue.getDirectorContactNo());
                    txtHospitalContactNo1.setText(newValue.getHospitalContact1());
                    txtHospitalContactNo2.setText(newValue.getHospitalContact2());
                    txtHospitalFax.setText(newValue.getHospitalFax());
                    txtHospitalEmail.setText(newValue.getHospitalEmail());
                    btnSave1.setText("Update");
                    btnSave1.setDisable(false);
                    btnDelete1.setDisable(false);
                }
            }
        });

        //search hospitals
        txtSearchHospital1.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<HospitalTM> searchHospitals = hospitalDetails.getItems();
                hospitalDetails.getItems().clear();
                for(HospitalTM hospitals : hospitalTMArrayList){
                    if(hospitals.getHospitalName().contains(newValue)){
                        searchHospitals.add(hospitals);
                    }
                }
            }
        });

    }

    private void loadAllHospitals() {
        try {
            PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT hospitalId,hospitalName,city,district,capacity,directorName,directorContactNo,hospitalContactNo1,hospitalContactNo2,hospitalFaxNo,hospitalEmailAddress FROM Hospital");
            ResultSet rst = preparedStatement.executeQuery();
            hospitalDetails.getItems().clear();
            while (rst.next()){
            String hospitalId = rst.getString(1);
            String hospitalName = rst.getString(2);
            String city = rst.getString(3);
            String district = rst.getString(4);
            String capacity = rst.getString(5);
            String directorName = rst.getString(6);
            String directorContactNo = rst.getString(7);
            String hospitalContact1 = rst.getString(8);
            String hospitalContact2 = rst.getString(9);
            String hospitalFax = rst.getString(10);
            String hospitalEmail = rst.getString(11);

            HospitalTM hospital_details = new HospitalTM(hospitalId,hospitalName,city,district,capacity,directorName,
                    directorContactNo,hospitalContact1,hospitalContact2,hospitalFax,hospitalEmail);
            hospitalDetails.getItems().add(hospital_details);
            hospitalTMArrayList.add(hospital_details);
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
        HospitalTM selectedHospital = hospitalDetails.getSelectionModel().getSelectedItem();


        String id = txtID.getText();
        String hospitalName = txtHospitalName.getText();
        String city = txtCity.getText();
        String district = cmbDistrict.getSelectionModel().getSelectedItem();
        String capacity = txtCapacity.getText();
        String directorName = txtDirectorName.getText();
        String directorContactNo = txtDirectorContactNo.getText();
        String hospitalContactNo1 = txtHospitalContactNo1.getText();
        String hospitalContactNo2 = txtHospitalContactNo2.getText();
        String hospitalFax = txtHospitalFax.getText();
        String hospitalEmail = txtHospitalEmail.getText();

       //Form Validation
        boolean userIDValidation = FormValidation.userIDValidation(txtID,lblIDError,"* Press add new hospital button");
        boolean nameValidation = FormValidation.nameValidation(txtHospitalName,lblHospitalNameError,"*Insert a valid name,only letters");
        boolean cityValidation = FormValidation.cityValidation(txtCity,lblCityError,"* Insert a valid city name");
        boolean districtValidation = FormValidation.comBoxValidation(cmbDistrict,lblDistrictError,"* Select a district");
        boolean capacityValidation = FormValidation.capacity(txtCapacity,lblCapacityError,"* Insert a valid capacity");
        boolean directorNameValidation = FormValidation.directorName(txtDirectorName,lblDirectorNameError,"* Insert a valid name");
        boolean directorContactValidation = FormValidation.contactNumber(txtDirectorContactNo,lblDirectorContactError,"* xxx-xxxxxxx");
        boolean hospitalContact1Validation = FormValidation.contactNumber(txtHospitalContactNo1,lblHospitalContactError1,"xxx-xxxxxxx");
        boolean hospitalContact2Validation = FormValidation.contactNumber(txtHospitalContactNo2,lblHospitalContactError2,"xxx-xxxxxxx");
        boolean hospitalFaxValidation = FormValidation.contactNumber(txtHospitalFax,lblFaxError,"xxx-xxxxxxx");
        boolean hospitalEmailValidation = FormValidation.emailValidation(txtHospitalEmail,lblEmailError,"* name@example.com");

        if(userIDValidation && nameValidation && cityValidation && districtValidation && capacityValidation &&
        directorNameValidation && directorContactValidation && hospitalContact1Validation && hospitalContact2Validation
        && hospitalFaxValidation && hospitalEmailValidation){

            if(btnSave1.getText().equals("Save")){
                String sql = "INSERT INTO Hospital(hospitalId, hospitalName, city, district, capacity, directorName, directorContactNo, hospitalContactNo1, hospitalContactNo2, hospitalFaxNo, hospitalEmailAddress) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                            (sql);
                    preparedStatement.setObject(1,id);
                    preparedStatement.setObject(2,hospitalName);
                    preparedStatement.setObject(3,city);
                    preparedStatement.setObject(4,district);
                    preparedStatement.setObject(5,capacity);
                    preparedStatement.setObject(6,directorName);
                    preparedStatement.setObject(7,directorContactNo);
                    preparedStatement.setObject(8,hospitalContactNo1);
                    preparedStatement.setObject(9,hospitalContactNo2);
                    preparedStatement.setObject(10,hospitalFax);
                    preparedStatement.setObject(11,hospitalEmail);

                    int affectedRows = preparedStatement.executeUpdate();

                    if(affectedRows > 0) {
                        new Alert(Alert.AlertType.INFORMATION, "Hospital added successfully", ButtonType.OK).showAndWait();
                        loadAllHospitals();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Can not add Hospitals,Check with admin", ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }else{
                String sql = "UPDATE Hospital SET hospitalId=?,hospitalName=?,city=?,district=?," +
                        "capacity=?,directorName=?,directorContactNo=?,hospitalContactNo1=?," +
                        "hospitalContactNo2=?,hospitalFaxNo=?,hospitalEmailAddress=? WHERE hospitalId=?";

                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement(sql);
                    pstm.setObject(1,id);
                    pstm.setObject(2,hospitalName);
                    pstm.setObject(3,city);
                    pstm.setObject(4,district);
                    pstm.setObject(5,capacity);
                    pstm.setObject(6,directorName);
                    pstm.setObject(7,directorContactNo);
                    pstm.setObject(8,hospitalContactNo1);
                    pstm.setObject(9,hospitalContactNo2);
                    pstm.setObject(10,hospitalFax);
                    pstm.setObject(11,hospitalEmail);
                    pstm.setObject(12,selectedHospital.getId());
                    int affectedRows = pstm.executeUpdate();
                    if(affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Updated Successfully",ButtonType.OK).showAndWait();
                        hospitalDetails.getSelectionModel().clearSelection();
                        btnSave1.setText("Save");
                        hospitalDetails.refresh();
                        loadAllHospitals();
                    }else{

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            clearForm();
        }

    }



    public void btnDelete_onAction(ActionEvent event) {
        HospitalTM selectedHospital = hospitalDetails.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                    ("DELETE FROM Hospital WHERE hospitalId=?");
            preparedStatement.setObject(1,selectedHospital.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                hospitalDetails.refresh();
                loadAllHospitals();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnAddNew_onAction(ActionEvent event) {

        try {
            PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT hospitalId FROM Hospital ORDER BY hospitalId DESC LIMIT 1");
            ResultSet rst = preparedStatement.executeQuery();
            if(!rst.next()){
            txtID.setText("H001");
            }else{
            String lastRowId = rst.getString(1);
            String subString = lastRowId.substring(1,4);
            int hospitalId = Integer.parseInt(subString)+1;
            if(hospitalId < 10){
                txtID.setText("H00" + hospitalId);
            }else if(hospitalId < 100 ){
                txtID.setText("H0" + hospitalId);
            }else{
                txtID.setText("H" + hospitalId);
            }
            }
            txtHospitalName.requestFocus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtID.setText("HXXX");
        txtHospitalName.clear();
        txtCity.clear();
        cmbDistrict.getSelectionModel().clearSelection();
        txtCapacity.clear();
        txtDirectorName.clear();
        txtDirectorContactNo.clear();
        txtHospitalContactNo1.clear();
        txtHospitalContactNo2.clear();
        txtHospitalFax.clear();
        txtHospitalEmail.clear();

    }


}
