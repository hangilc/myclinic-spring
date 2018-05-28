package jp.chang.myclinic.rcptdrawer;

class StoringDispatchHook implements DispatchHook {

    private int patientId;

    public int getPatientId() {
        return patientId;
    }

    public void onPatientId(int patientId) {
        this.patientId = patientId;
    }
}
