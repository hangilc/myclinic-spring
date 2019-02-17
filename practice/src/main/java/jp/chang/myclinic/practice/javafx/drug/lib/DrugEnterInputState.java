package jp.chang.myclinic.practice.javafx.drug.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

class DrugEnterInputState extends DrugInputState {

    private boolean isDaysFixed = true;

    DrugEnterInputState() {

    }

    DrugEnterInputState(DrugInputState src) {
        src.assignTo(this);
    }

    DrugEnterInputState(DrugInputBaseState src) {
        src.assignTo(this);
    }

    void assignTo(DrugEnterInputState dst){
        super.assignTo(dst);
        dst.isDaysFixed = isDaysFixed;
    }

    public DrugEnterInputState copy(){
        DrugEnterInputState dst = new DrugEnterInputState();
        assignTo(dst);
        return dst;
    }

    public boolean isDaysFixed() {
        return isDaysFixed;
    }

    public void setDaysFixed(boolean daysFixed) {
        isDaysFixed = daysFixed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugEnterInputState)) return false;
        if (!super.equals(o)) return false;
        DrugEnterInputState that = (DrugEnterInputState) o;
        return isDaysFixed == that.isDaysFixed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isDaysFixed);
    }

    @Override
    public String toString() {
        return "DrugEnterInputState{" +
                "isDaysFixed=" + isDaysFixed +
                "} " + super.toString();
    }
}
