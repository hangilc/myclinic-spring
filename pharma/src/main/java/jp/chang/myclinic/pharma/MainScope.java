package jp.chang.myclinic.pharma;

import javafx.beans.property.ObjectProperty;
import jp.chang.myclinic.pharma.tracking.ModelDispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MainScope {

    @Autowired
    @Qualifier("tracking-flag")
    private ObjectProperty<Boolean> tracking;

    @Autowired
    private ModelDispatchAction modelDispatchAction;

    public MainScope(){

    }

    public Boolean isTracking() {
        return tracking.get();
    }

    public ObjectProperty<Boolean> trackingProperty() {
        return tracking;
    }

    public void setTracking(Boolean tracking) {
        this.tracking.set(tracking);
    }

    public ModelDispatchAction getModelDispatchAction() {
        return modelDispatchAction;
    }
}
