package jp.chang.myclinic.shujii;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ShujiiForm extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(ShujiiForm.class);
    private TextArea textArea = new TextArea();
    private TextField doctorNameInput = new TextField();
    private TextField clinicNameInput = new TextField();
    private TextField clinicAddrInput = new TextField();
    private TextField clinicPhoneInput = new TextField();
    private TextField clinicFaxInput = new TextField();

    ShujiiForm() {
        rightAlignFirstColumn();
        textArea.setWrapText(true);
        addRow("詳記", textArea);
        addRow("医師氏名", doctorNameInput);
        addRow("医院名", clinicNameInput);
        addRow("医院住所", clinicAddrInput);
        addRow("医院電話", clinicPhoneInput);
        addRow("医院Fax", clinicFaxInput);
    }

    void setDoctorName(String value){
        doctorNameInput.setText(value);
    }

    String getDoctorName(){
        return doctorNameInput.getText();
    }

    void setClinicName(String value){
        clinicNameInput.setText(value);
    }

    String getClinicName(){
        return clinicNameInput.getText();
    }

    void setClinicAddr(String value){
        clinicAddrInput.setText(value);
    }

    String getClinicAddr(){
        return clinicAddrInput.getText();
    }

    void setClinicPhone(String value){
        clinicPhoneInput.setText(value);
    }

    String getClinicPhone(){
        return clinicPhoneInput.getText();
    }

    void setClinicFax(String value){
        clinicFaxInput.setText(value);
    }

    String getClinicFax(){
        return clinicFaxInput.getText();
    }

    String getDetail(){
        return textArea.getText();
    }

}
