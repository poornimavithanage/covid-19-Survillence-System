package controller;

import db.DB_connection;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.FormValidation;
import util.UserTM;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

public class ManageUsersController {
    public AnchorPane root;
    public Button btnSave;
    public Button btnAddNewUser;
    public TextField txtContactNo;
    public TextField txtEmail;
    public TextField txtUsername;
    public ComboBox<String> cmbUserRole;
    public ComboBox<String> cmbUserLocation;
    public TextField txtSearch;
    public TableView<UserTM> tblUsers;
    public PasswordField txtPasswordHide;
    public TextField txtPasswordShow;

    public TextField txtUserId;
    private final ArrayList<UserTM> userTMArrayList = new ArrayList<>();
    public TextField txtFirst_name;
    public Label lbl_userLocation;
    public Label lbl_passwordError;
    public Label lbl_uerNameError;
    public Label lbl_emailError;
    public Label lbl_ContactNoError;
    public Label lbl_firstNameError;
    public Button btnDelete;
    public ImageView imgPasswordHide;
    public ImageView imgPasswordShow;
    public Label lbl_roleError;


    public void initialize(){
        //Transition.....
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.play();

        Platform.runLater(() ->
        {
            btnAddNewUser.requestFocus();
        });

        cmbUserLocation.setVisible(false);
        txtPasswordShow.setVisible(false);
        imgPasswordShow.setVisible(false);


        //values added to comboBox
        cmbUserRole.getItems().add("Admin");
        cmbUserRole.getItems().add("P.S.T.F");
        cmbUserRole.getItems().add("Hospital IT");
        cmbUserRole.getItems().add("Quarantine IT");

        //comboBox selection(user role)
        cmbUserRole.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue == null){
                    return;
                }
                if(newValue.intValue()==2){
                    cmbUserLocation.setVisible(true);
                    cmbUserLocation.setPromptText("Select Hospital");
                    cmbUserLocation.getSelectionModel().clearSelection();
                    cmbUserLocation.getSelectionModel().select(null);
                    cmbUserLocation.getItems().clear();
                    loadAllHospital();
                }else if(newValue.intValue()==3){
                    cmbUserLocation.setVisible(true);
                    cmbUserLocation.setPromptText("Select Quarantine Center");
                    cmbUserLocation.getSelectionModel().clearSelection();
                    cmbUserLocation.getSelectionModel().select(null);
                    cmbUserLocation.getItems().clear();
                    loadAllQuarantineCenters();
                }else{
                    cmbUserLocation.setVisible(false);

                }
            }
        });

        //load userdata to table
        tblUsers.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblUsers.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("username"));
        tblUsers.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("role"));
        //tblUsers.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("delete"));
        tblUsers.getColumns().get(0).setStyle("-fx-alignment: center");
        tblUsers.getColumns().get(1).setStyle("-fx-alignment: center");
        tblUsers.getColumns().get(2).setStyle("-fx-alignment: center");
        loadAllUsers();

        //userTable selection
        tblUsers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {
            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {
                UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
                if(selectedUser == null){
                    return;
                }

                if(newValue.getRole().equals("Hospital IT") || newValue.getRole().equals("Quarantine IT")){
                    txtUserId.setText(selectedUser.getUserId());
                    txtFirst_name.setText(selectedUser.getName());
                    txtContactNo.setText(selectedUser.getContactNumber());
                    txtEmail.setText(selectedUser.getEmail());
                    txtUsername.setText(selectedUser.getUsername());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(selectedUser.getRole());
                    cmbUserLocation.getSelectionModel().select(selectedUser.getLocation());

                    cmbUserLocation.setVisible(true);

                }else{
                    txtUserId.setText(selectedUser.getUserId());
                    txtFirst_name.setText(selectedUser.getName());
                    txtContactNo.setText(selectedUser.getContactNumber());
                    txtEmail.setText(selectedUser.getEmail());
                    txtUsername.setText(selectedUser.getUsername());
                    txtPasswordHide.setText(selectedUser.getPassword());
                    cmbUserRole.getSelectionModel().select(newValue.getRole());
                }
                btnSave.setText("Update");
            }
        });

        txtUserId.setDisable(true);

        txtPasswordHide.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtPasswordShow.setText(newValue);
            }
        });

        txtPasswordShow.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                txtPasswordHide.setText(newValue);
            }
        });

        txtUsername.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                txtPasswordHide.setText(generatePassword(8));
            }
        });


        //search
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<UserTM>selectedUser = tblUsers.getItems();
                selectedUser.clear();
                for(UserTM users : userTMArrayList){
                    if(users.getName().contains(newValue) || users.getUsername().contains(newValue)){
                        tblUsers.getItems().add(users);
                    }
                }
            }
        });
    }
//generate password
    private static String generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return String.valueOf(password);
    }

    //load all users
    private void loadAllUsers() {
        try {
            Statement stm = DB_connection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT userId,firstName,contactNumber,email,username,password,userRole,location FROM User");
            while (rst.next()){
            String id = rst.getString(1);
            String name = rst.getString(2);
            String contact = rst.getString(3);
            String email = rst.getString(4);
            String username = rst.getString(5);
            String password = rst.getString(6);
            String role = rst.getString(7);
            String location = rst.getString(8);

            UserTM user = new UserTM(id,name,contact,email,username,password,role,location);
            tblUsers.getItems().add(user);
            userTMArrayList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //load qc to comboBox
    private void loadAllQuarantineCenters() {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement("SELECT * FROM QuarantineCenter WHERE status=?");
            pstm.setObject(1,"not reserved");
            ResultSet rst = pstm.executeQuery();
            cmbUserLocation.getItems().clear();
            while (rst.next()){
                String qc_id = rst.getString(1);
                String qc_name = rst.getString(2);

                cmbUserLocation.getItems().add(qc_id+ "-" +qc_name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//load hospitals to comboBox
    private void loadAllHospital() {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT * FROM Hospital WHERE status=?");
            pstm.setObject(1,"not reserved");
            ResultSet rst = pstm.executeQuery();
            cmbUserLocation.getItems().clear();
            while (rst.next()){
                String hospitalId = rst.getString(1);
                String hospitalName = rst.getString(2);

                cmbUserLocation.getItems().add(hospitalId+  "-" +hospitalName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void btnHome_OnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/Dashboard.fxml");
        Parent root = FXMLLoader.load(resource);
        Scene scene = new Scene(root);
        Stage primaryStage = (Stage) (this.root.getScene().getWindow());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public void btnSave_onAction(ActionEvent event) {
//Form Validation
        boolean nameValidation = FormValidation.nameValidation(txtFirst_name,lbl_firstNameError,"* insert a valid name(only letters)");
        boolean contactNoValidation = FormValidation.contactNumber(txtContactNo,lbl_ContactNoError,"* XXX-XXXXXXX");
        boolean emailValidation = FormValidation.emailValidation(txtEmail,lbl_emailError,"* name@example.com");
        boolean userNameValidation = FormValidation.userNameValidation(txtUsername, lbl_uerNameError, "* insert a valid user name(only a-z,A-Z,0-9,@,.)");
        boolean passwordValidation = FormValidation.passwordValidation(txtPasswordHide,lbl_passwordError,"Password length should be 8");
        boolean userRoleValidation = FormValidation.comBoxValidation(cmbUserRole, lbl_roleError, "* select a role");

        if(nameValidation && contactNoValidation && emailValidation && userNameValidation && passwordValidation && userRoleValidation){
            
            if(btnSave.getText().equals("Save")){
                //check username is taken or not
                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                            ("SELECT username FROM User WHERE username=?");
                    pstm.setObject(1,txtUsername.getText());
                    ResultSet rst = pstm.executeQuery();
                    if(rst.next()){
                        new Alert(Alert.AlertType.ERROR,"Username has already used.Try another one",ButtonType.OK).showAndWait();
                        txtUsername.selectAll();
                        txtUsername.requestFocus();
                    }else{
                        //send data to database
                        if(cmbUserRole.getSelectionModel().getSelectedIndex()==2){
                            boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* select a location");
                            if(locationValidation){
                                try {
                                    PreparedStatement pstm1 = DB_connection.getInstance().getConnection().prepareStatement
                                            ("INSERT INTO User (userId,covid19.User.firstName,contactNumber,email,username,password,userRole,covid19.User.location) VALUES (?,?,?,?,?,?,?,?)");
                                    pstm1.setObject(1,txtUserId.getText());
                                    pstm1.setObject(2,txtFirst_name.getText());
                                    pstm1.setObject(3,txtContactNo.getText());
                                    pstm1.setObject(4,txtEmail.getText());
                                    pstm1.setObject(5,txtUsername.getText());
                                    pstm1.setObject(6,txtPasswordHide.getText());
                                    pstm1.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                                    pstm1.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selectedHospitalId = selectedItem.substring(0,4);

                                    int affectedRows = pstm1.executeUpdate();
                                    if(affectedRows > 0){
                                        //update hospital status as reserved
                                        PreparedStatement pstm2 = DB_connection.getInstance().getConnection().prepareStatement
                                                ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                                        pstm2.setObject(1,"reserved");
                                        pstm2.setObject(2,selectedHospitalId);
                                        pstm2.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION,"User added successfully",ButtonType.OK).showAndWait();
                                    }else{
                                        new Alert(Alert.AlertType.INFORMATION,"user failed to add",ButtonType.OK).showAndWait();
                                    }
                                }catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                tblUsers.getItems().clear();
                                loadAllUsers();
                                tblUsers.refresh();
                                clearForm();
                                btnAddNewUser.requestFocus();
                            }
                        }
                        else if(cmbUserRole.getSelectionModel().getSelectedIndex()==3){
                            boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* select a location");
                            if(locationValidation){
                                try {
                                    PreparedStatement pstm1 = DB_connection.getInstance().getConnection().prepareStatement
                                            ("INSERT INTO User (userId,covid19.User.firstName,contactNumber,email,username,password,userRole,covid19.User.location) VALUES (?,?,?,?,?,?,?,?)");
                                    pstm1.setObject(1,txtUserId.getText());
                                    pstm1.setObject(2,txtFirst_name.getText());
                                    pstm1.setObject(3,txtContactNo.getText());
                                    pstm1.setObject(4,txtEmail.getText());
                                    pstm1.setObject(5,txtUsername.getText());
                                    pstm1.setObject(6,txtPasswordHide.getText());
                                    pstm1.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                                    pstm1.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());

                                    String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
                                    String selectedQuarantineId = selectedItem.substring(0,4);

                                    int affectedRows = pstm1.executeUpdate();
                                    if(affectedRows>0){
                                        //update quarantine center status as reserved
                                        PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                                                ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                                        preparedStatement.setObject(1,"reserved");
                                        preparedStatement.setObject(2,selectedQuarantineId);
                                        preparedStatement.executeUpdate();
                                        new Alert(Alert.AlertType.INFORMATION,"User Added successfully",ButtonType.OK).showAndWait();
                                    }else{
                                        new Alert(Alert.AlertType.ERROR,"Something wrong in user adding",ButtonType.OK).showAndWait();
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                tblUsers.getItems().clear();
                                loadAllUsers();
                                tblUsers.refresh();
                                clearForm();
                                btnAddNewUser.requestFocus();
                            }
                        }else{
                            try {
                                PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                                        ("INSERT INTO User (userId,covid19.User.firstName,contactNumber,email,username,password,userRole) VALUES (?,?,?,?,?,?,?)");
                                preparedStatement.setObject(1,txtUserId.getText());
                                preparedStatement.setObject(2,txtFirst_name.getText());
                                preparedStatement.setObject(3,txtContactNo.getText());
                                preparedStatement.setObject(4,txtEmail.getText());
                                preparedStatement.setObject(5,txtUsername.getText());
                                preparedStatement.setObject(6,txtPasswordHide.getText());
                                preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
                                int affectedRows = preparedStatement.executeUpdate();
                                if(affectedRows > 0){
                                    new Alert(Alert.AlertType.INFORMATION,"User Added successfully",ButtonType.OK).showAndWait();
                                }else{
                                    new Alert(Alert.AlertType.ERROR,"Something wrong in user adding",ButtonType.OK).showAndWait();
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            tblUsers.getItems().clear();
                            loadAllUsers();
                            tblUsers.refresh();
                            clearForm();
                            btnAddNewUser.requestFocus();

                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                //update
                if(txtUsername.getText().equals(tblUsers.getSelectionModel().getSelectedItem().getUsername())) {
                    if (tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Hospital IT")) {
                        hospitalITUpdate();
                    } else if (tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Quarantine IT")) {
                        quarantineCenterITUpdate();
                    } else if (tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Admin") ||
                            tblUsers.getSelectionModel().getSelectedItem().getRole().equals("P.S.T.F")) {
                        adminAndPSTFUpdate();
                    }
                }
                else{
                    try {
                        PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                                ("SELECT username FROM User WHERE username=?");
                        pstm.setObject(1,txtUsername.getText());
                        ResultSet rst = pstm.executeQuery();
                        if(rst.next()){
                            new Alert(Alert.AlertType.ERROR,"Username has already taken.Try another name",ButtonType.OK).showAndWait();
                            txtUsername.selectAll();
                            txtUsername.requestFocus();
                        }else{
                            if (tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Hospital IT")){
                                hospitalITUpdate();
                            }else if(tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Quarantine IT")){
                                quarantineCenterITUpdate();
                            }else if(tblUsers.getSelectionModel().getSelectedItem().getRole().equals("Admin")||
                                    tblUsers.getSelectionModel().getSelectedItem().getRole().equals("P.S.T.F")){
                                adminAndPSTFUpdate();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    }
                }

            }
        }


//update admin and PSTF
    private void adminAndPSTFUpdate() {
       if(cmbUserRole.getSelectionModel().getSelectedIndex()==0 || cmbUserRole.getSelectionModel().getSelectedIndex()==1){

           try {
               PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                       ("UPDATE User SET userId=?,covid19.User.firstName=?,contactNumber=?,email=?,username=?,password=?,userRole=? WHERE userId=?");
               pstm.setObject(1,txtUserId.getText());
               pstm.setObject(2,txtFirst_name.getText());
               pstm.setObject(3,txtContactNo.getText());
               pstm.setObject(4,txtEmail.getText());
               pstm.setObject(5,txtUsername.getText());
               pstm.setObject(6,txtPasswordHide.getText());
               pstm.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
               pstm.setObject(8,tblUsers.getSelectionModel().getSelectedItem().getUserId());
               int affectedRows = pstm.executeUpdate();
               if(affectedRows > 0){
                   System.out.println("update success");
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           tblRefresh();
       }else if(cmbUserRole.getSelectionModel().getSelectedIndex()==2){
           boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* Select a location");
           if(locationValidation){
               String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
               String selectHospitalId = selectedItem.substring(0,4);

               try {
                   PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                           ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                   pstm.setObject(1,"reserved");
                   pstm.setObject(2,selectHospitalId);
                   int i = pstm.executeUpdate();
                   if(i>0){
                       System.out.println("OK1");
                   }
                   updateSQLOfLocation();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
               tblRefresh();
           }
       }else{
        boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* Select a location");

        if(locationValidation){
            String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
            String selectedQuarantineId = selectedItem.substring(0,4);

            try {
                PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                        ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                pstm.setObject(1,"reserved");
                pstm.setObject(2,selectedQuarantineId);
                int i = pstm.executeUpdate();
                if(i>0){
                    System.out.println("OK3");
                }
                updateSQLOfLocation();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblRefresh();
        }
       }

    }

//Quarantine center update method
    private void quarantineCenterITUpdate() {
       if(cmbUserRole.getSelectionModel().getSelectedIndex() == 0 || cmbUserRole.getSelectionModel().getSelectedIndex() ==1){
           String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
           String subStringLocationId = selectedLocation.substring(0,4);

           try {
               PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                       ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
               pstm.setObject(1,"not reserved");
               pstm.setObject(2,subStringLocationId);
               int affectedRows = pstm.executeUpdate();
               if(affectedRows >0){
                   System.out.println("OK");
               }
               updateSQLOfAdminAndPSTF();

           } catch (SQLException e) {
               e.printStackTrace();
           }
           tblRefresh();
       }else if(cmbUserRole.getSelectionModel().getSelectedIndex()==2){
        boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* select a location");
        if(locationValidation){
            String selectedQCLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
            String selectedQCLocationId = selectedQCLocation.substring(0,4);
            try {
                PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                        ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                preparedStatement.setObject(1,"not reserved");
                preparedStatement.setObject(2,selectedQCLocationId);
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows > 0){
                    System.out.println("OK2");
                }

                String selectedHospital = cmbUserLocation.getSelectionModel().getSelectedItem();
                String selectedHospitalId = selectedHospital.substring(0,4);

                PreparedStatement preparedStatement1 = DB_connection.getInstance().getConnection().prepareStatement
                        ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                preparedStatement1.setObject(1,"reserved");
                preparedStatement1.setObject(2,selectedHospitalId);
                int i = preparedStatement1.executeUpdate();
                if(i>0){
                    System.out.println("Ok3");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateSQLOfLocation();
            tblRefresh();
        }
       }else{
           if(!cmbUserLocation.getSelectionModel().getSelectedItem().equals
                   (tblUsers.getSelectionModel().getSelectedItem().getLocation())){
               String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
               String selectedQuarantineId = selectedItem.substring(0,4);

               try {
                   PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                           ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                   pstm.setObject(1,"reserved");
                   pstm.setObject(2,selectedQuarantineId);
                   int i = pstm.executeUpdate();
                   if(i > 0){
                       System.out.println("OK3");
                   }

                   String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
                   String subStringLocationId = selectedLocation.substring(0,4);

                   PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                           ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                   preparedStatement.setObject(1,"not reserved");
                   preparedStatement.setObject(2,subStringLocationId);
                   int affectedRows = preparedStatement.executeUpdate();
                   if(affectedRows > 0){
                       System.out.println("OK4");
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
           updateSQLOfLocation();
           tblRefresh();
       }

    }

    private void updateSQLOfAdminAndPSTF() {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("UPDATE User SET userId=?,covid19.User.firstName=?,contactNumber=?,email=?,username=?,password=?,userRole=? WHERE userId=?");
            pstm.setObject(1,txtUserId.getText());
            pstm.setObject(2,txtFirst_name.getText());
            pstm.setObject(3,txtContactNo.getText());
            pstm.setObject(4,txtEmail.getText());
            pstm.setObject(5,txtUsername.getText());
            pstm.setObject(6,txtPasswordHide.getText());
            pstm.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
            pstm.setObject(8,tblUsers.getSelectionModel().getSelectedItem().getUserId());
            int affectedRows = pstm.executeUpdate();
            if(affectedRows > 0 ){
                System.out.println("Update Success");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void hospitalITUpdate() {
        if(cmbUserRole.getSelectionModel().getSelectedIndex()==0 || cmbUserRole.getSelectionModel().getSelectedIndex()==1){
            String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
            String subStringLocationId = selectedLocation.substring(0,4);

            try {
                PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                        ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                preparedStatement.setObject(1,"not reserved");
                preparedStatement.setObject(2,subStringLocationId);
                int affectedRows = preparedStatement.executeUpdate();
                if(affectedRows>0){
                    System.out.println("ok");
                }
                updateSQLOfAdminAndPSTF();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tblRefresh();
        }else if(cmbUserRole.getSelectionModel().getSelectedIndex()==2){
            if(!cmbUserLocation.getSelectionModel().getSelectedItem().equals
                    (tblUsers.getSelectionModel().getSelectedItem().getLocation())){
                String selectedItem = cmbUserLocation.getSelectionModel().getSelectedItem();
                String selectedHospitalId = selectedItem.substring(0,4);

                try {
                    PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                            ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                    preparedStatement.setObject(1,"reserved");
                    preparedStatement.setObject(2,selectedHospitalId);
                    int i = preparedStatement.executeUpdate();
                    if(i>0){
                        System.out.println("ok1");
                    }
                    String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
                    String subString_locationId = selectedLocation.substring(0,4);

                    PreparedStatement preparedStatement1= DB_connection.getInstance().getConnection().prepareStatement("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                    preparedStatement1.setObject(1,"not reserved");
                    preparedStatement1.setObject(2,subString_locationId);
                    int affectedRows = preparedStatement1.executeUpdate();
                    if(affectedRows >0){
                        System.out.println("ok2");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            updateSQLOfLocation();
            tblRefresh();
        }else{
            boolean locationValidation = FormValidation.comBoxValidation(cmbUserLocation,lbl_userLocation,"* Select a location");
            if(locationValidation){
                String selectedHospital_location = tblUsers.getSelectionModel().getSelectedItem().getLocation();
                String selectedHospital_locationId = selectedHospital_location.substring(0,4);

                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                            ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                    pstm.setObject(1,"not reserved");
                    pstm.setObject(2,selectedHospital_locationId);
                    int affectedRows = pstm.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("ok3");
                    }
                    String selected_quarantineCenter = cmbUserLocation.getSelectionModel().getSelectedItem();
                    String selected_quarantineCenterId = selected_quarantineCenter.substring(0,4);

                    PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                            ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                    preparedStatement.setObject(1,"reserved");
                    preparedStatement.setObject(2,selected_quarantineCenterId);
                    int i = preparedStatement.executeUpdate();
                    if(i>0){
                        System.out.println("ok");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateSQLOfLocation();
                tblRefresh();

            }
        }
    }

    private void updateSQLOfLocation() {
        try {
            PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                    ("UPDATE User SET userId=?,covid19.User.firstName=?,contactNumber=?,email=?,username=?,password=?,userRole=?,location=? WHERE userId=?");
            preparedStatement.setObject(1,txtUserId.getText());
            preparedStatement.setObject(2,txtFirst_name.getText());
            preparedStatement.setObject(3,txtContactNo.getText());
            preparedStatement.setObject(4,txtEmail.getText());
            preparedStatement.setObject(5,txtUsername.getText());
            preparedStatement.setObject(6,txtPasswordHide.getText());
            preparedStatement.setObject(7,cmbUserRole.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(8,cmbUserLocation.getSelectionModel().getSelectedItem());
            preparedStatement.setObject(9,tblUsers.getSelectionModel().getSelectedItem().getUserId());
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                System.out.println("update success");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void tblRefresh() {
        tblUsers.getItems().clear();
        loadAllUsers();
        tblUsers.refresh();
        clearForm();
        btnSave.setText("Save");
        btnAddNewUser.requestFocus();
    }

    private void clearForm() {
        cmbUserRole.getSelectionModel().clearSelection();
        cmbUserLocation.getSelectionModel().clearSelection();
        tblUsers.getSelectionModel().clearSelection();
        txtFirst_name.clear();
        txtContactNo.clear();
        txtEmail.clear();
        txtUsername.clear();
        txtPasswordHide.clear();
        txtPasswordShow.clear();
        txtSearch.clear();
    }

    public void btnAddNew_onAction(ActionEvent event) {
        try {
            PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                    ("SELECT userId FROM User ORDER BY userId DESC LIMIT 1");
            ResultSet rst = pstm.executeQuery();
            if(!rst.next()){
                txtUserId.setText("M001");
            }else{
                String lastRowId = rst.getString(1);
                String subString = lastRowId.substring(1,4);
                int userId = Integer.parseInt(subString)+1;
                if(userId < 10){
                    txtUserId.setText("M00" + userId);
                }else if(userId < 100){
                    txtUserId.setText("M0" + userId);
                }else{
                    txtUserId.setText("M" + userId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
       txtFirst_name.requestFocus();
    }
//hide the password
    public void imgPasswordHide_OnMouseClicked(MouseEvent event) {

      txtPasswordHide.setVisible(false);
      imgPasswordHide.setVisible(false);
      imgPasswordShow.setVisible(true);
      txtPasswordShow.setVisible(true);

      txtPasswordShow.requestFocus();
      txtPasswordShow.deselect();
      txtPasswordShow.positionCaret(txtPasswordShow.getLength());
     
    }

    //show the password
    public void imgPasswordShow_OnMouseClicked(MouseEvent event) {
        imgPasswordShow.setVisible(false);
        txtPasswordShow.setVisible(false);
        txtPasswordHide.setVisible(true);
        imgPasswordHide.setVisible(true);

        txtPasswordHide.requestFocus();
        txtPasswordHide.deselect();
        txtPasswordHide.positionCaret(txtPasswordHide.getLength());
    }

    public void btnDelete_onAction(ActionEvent event) {
        UserTM selectedUser = tblUsers.getSelectionModel().getSelectedItem();
        if(selectedUser == null){
            new Alert(Alert.AlertType.WARNING,"Please select a user to delete",ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Are you sure that you want to delete the selected user",ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){
            //HospitalIT delete
            if(cmbUserRole.getSelectionModel().getSelectedIndex()==2){
                String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
                String subsStringLocationId = selectedLocation.substring(0,4);

                try {
                    PreparedStatement pstm = DB_connection.getInstance().getConnection().prepareStatement
                            ("UPDATE Hospital SET covid19.hospital.status=? WHERE hospitalId=?");
                  pstm.setObject(1,"not reserved");
                  pstm.setObject(2,subsStringLocationId);
                  int ars = pstm.executeUpdate();

                  PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                          ("DELETE FROM User WHERE userId=?");
                  preparedStatement.setObject(1,selectedUser.getUserId());
                  int affectedRows = preparedStatement.executeUpdate();
                  if(affectedRows > 0){
                      new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                  }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //QuarantineIT delete....
            }else if(cmbUserRole.getSelectionModel().getSelectedIndex()==3){
                try {
                    String selectedLocation = tblUsers.getSelectionModel().getSelectedItem().getLocation();
                    String subStringLocationId = selectedLocation.substring(0,4);

                    PreparedStatement pstm2 = DB_connection.getInstance().getConnection().prepareStatement
                            ("UPDATE QuarantineCenter SET covid19.QuarantineCenter.status=? WHERE quarantineCenterId=?");
                    pstm2.setObject(1,"not reserved");
                    pstm2.setObject(2,subStringLocationId);
                    int i = pstm2.executeUpdate();

                    PreparedStatement pstm3 = DB_connection.getInstance().getConnection().prepareStatement
                            ("DELETE FROM User WHERE userId=?");
                    pstm3.setObject(1,selectedUser.getUserId());
                    int affectedRows = pstm3.executeUpdate();
                    if(affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                //admin and PSTF delete...........
                try {
                    PreparedStatement preparedStatement = DB_connection.getInstance().getConnection().prepareStatement
                            ("DELETE FROM User WHERE userId=?");
                    preparedStatement.setObject(1,selectedUser.getUserId());
                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows > 0){
                        new Alert(Alert.AlertType.INFORMATION,"Deleted Successfully",ButtonType.OK).showAndWait();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            tblUsers.getItems().clear();
            tblUsers.refresh();
            loadAllUsers();
            clearForm();
            btnAddNewUser.requestFocus();
        }
    }

}
