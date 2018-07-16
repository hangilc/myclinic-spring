package jp.chang.myclinic.reception;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Scope {

    private BooleanProperty tracking = new SimpleBooleanProperty();

    public Scope() {

    }

    public boolean isTracking() {
        return tracking.get();
    }

    public BooleanProperty trackingProperty() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking.set(tracking);
    }

}
