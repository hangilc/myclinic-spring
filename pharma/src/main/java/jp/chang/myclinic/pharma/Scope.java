package jp.chang.myclinic.pharma;

import jp.chang.myclinic.pharma.tracker.Tracker;

public class Scope {

    private Tracker tracker;

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public void reloadPatientList(){
        tracker.reload();
    }

}
