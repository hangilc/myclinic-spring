package jp.chang.myclinic.drawer.receipt;

import java.text.NumberFormat;

/**
 * Created by hangil on 2017/05/21.
 */
public class ReceiptDrawerData {

    private String patientName = "";
    private String charge = "";
    private String visitDate = "";
    private String issueDate = "";
    private String patientId = "";
    private String hoken = "";
    private String futanWari = "";
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public void setChargeByInt(int charge){
        this.charge = numberFormat.format(charge);
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getHoken() {
        return hoken;
    }

    public void setHoken(String hoken) {
        this.hoken = hoken;
    }

    public String getFutanWari() {
        return futanWari;
    }

    public void setFutanWari(String futanWari) {
        this.futanWari = futanWari;
    }
}
