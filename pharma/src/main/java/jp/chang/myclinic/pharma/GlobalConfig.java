package jp.chang.myclinic.pharma;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jp.chang.myclinic.pharma.javafx.PatientList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    //private static Logger logger = LoggerFactory.getLogger(GlobalConfig.class);
    private static Callback<PatientList.Model, Observable[]> modelExtractor = model -> new Observable[]{
            model.nameProperty(),
            model.waitStateProperty()
    };


    @Bean(name="tracking-visit-list")
    public ObservableList<PatientList.Model> trackingVisitList(){
        return FXCollections.observableArrayList(modelExtractor);
    }

    @Bean(name="tracking-pharma-list")
    public ObservableList<PatientList.Model> trackingPharmaList(){
        return FXCollections.observableArrayList(modelExtractor);
    }

    @Bean(name="tracking-flag")
    public SimpleBooleanProperty trackingFlag(){
        return new SimpleBooleanProperty(true);
    }
}
