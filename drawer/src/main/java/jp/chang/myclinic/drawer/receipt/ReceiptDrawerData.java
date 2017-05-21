package jp.chang.myclinic.drawer.receipt;

/**
 * Created by hangil on 2017/05/21.
 */
public class ReceiptDrawerData {

    private String patientName;
    private String charge;

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
}
