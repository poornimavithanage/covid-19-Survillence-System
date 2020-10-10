package model;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class FormValidation {
    public static boolean textFieldIsNull(TextField t1, Label l1, String validationText) {
        boolean b = true;
        String s = null;

        if (t1.getText().trim().isEmpty()) {
            b = false;
            s = validationText;
        }
        l1.setText(s);
        return b;
    }

    public static boolean nameValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\sa-zA-Z])*")) {
            b = false;
            s = validationText;
            t.requestFocus();
            t.deselect();
        }
        l.setText(s);
        return b;
    }

    public static boolean cityValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\sa-zA-Z0-9])*")) {
            b = false;
            s = validationText;
            t.deselect();
        }
        l.setText(s);
        return b;
    }

    public static boolean comBoxValidation(ComboBox t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getSelectionModel().isEmpty()) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }

    public static boolean directorName(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([\\s.a-zA-Z])*")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean capacity(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[0-9]+")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;

    }

    public static boolean contactNumber(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[0-9]{3}[-][0-9]{7}")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean emailValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s = null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-z]+([.0-9a-z])*[@]([a-z])+[.]([a-z.])*")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
        }

    public static boolean userNameValidation(TextField t, Label l, String validationText) {
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !t.getText().matches("^[a-zA-Z]+([@.a-zA-Z0-9])*")) {
            b=false;
            s=validationText;
//            t.requestFocus();
        }
        l.setText(s);
        return b;
    }

        public static boolean passwordValidation(TextField t, Label l, String validationText){
            boolean b = true;
            String s=null;

            if (t.getText().trim().isEmpty() || !(t.getText().length()>=8)) {
                b=false;
                s=validationText;
            }
            l.setText(s);
            return b;
        }

    public static boolean numberValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getText().trim().isEmpty() || !(t.getText().matches("^[0-9]+"))) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean dateValidation(DatePicker t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getValue()==null){
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }

    public static boolean userIDValidation(TextField t, Label l, String validationText){
        boolean b = true;
        String s=null;

        if (t.getText().equals("MXXX") || t.getText().equals("HXXX") || t.getText().equals("CXXX")) {
            b=false;
            s=validationText;
        }
        l.setText(s);
        return b;
    }
    }



