package jp.chang.myclinic.practice.javafx;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainPaneServiceDelegate implements MainPaneService {

    private MainPaneService delegate;

    public MainPaneServiceDelegate(MainPaneService delegate) {
        this.delegate = delegate;
    }

    @Override
    public PatientDTO getCurrentPatient() {
        return delegate.getCurrentPatient();
    }

    @Override
    public int getCurrentVisitId() {
        return delegate.getCurrentVisitId();
    }

    @Override
    public int getTempVisitId() {
        return delegate.getTempVisitId();
    }

    @Override
    public int getCurrentOrTempVisitId() {
        return delegate.getCurrentOrTempVisitId();
    }

    @Override
    public void broadcastNewText(TextDTO newText) {
        delegate.broadcastNewText(newText);
    }

}
