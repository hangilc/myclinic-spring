package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Charge {

    private int visitId;
    private StringProperty rep = new SimpleStringProperty("(未請求)");

    public Charge() {

    }

    public int getVisitId() {
        return visitId;
    }

    public String getRep() {
        return rep.get();
    }

    public StringProperty repProperty() {
        return rep;
    }

    public void setValue(int charge){
        rep.setValue(String.format("請求額：%,d円", charge));
    }

    public void setPayment(int payment){
        rep.setValue(String.format("領収額：%,d円", payment));
    }
}
