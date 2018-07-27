package jp.chang.myclinic.pharma;

import javafx.beans.property.SimpleBooleanProperty;
import jp.chang.myclinic.pharma.tracking.ModelDispatchAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MainScope {

    @Autowired
    @Qualifier("tracking-flag")
    private SimpleBooleanProperty tracking;

    @Autowired
    private ModelDispatchAction modelDispatchAction;

    public MainScope(){

    }

    public SimpleBooleanProperty trackingProperty() {
        return tracking;
    }

    public ModelDispatchAction getModelDispatchAction() {
        return modelDispatchAction;
    }
}
