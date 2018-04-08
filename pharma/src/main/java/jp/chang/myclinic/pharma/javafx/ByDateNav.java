package jp.chang.myclinic.pharma.javafx;

import jp.chang.myclinic.dto.VisitFullDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class ByDateNav implements Nav {

    private static Logger logger = LoggerFactory.getLogger(ByDateNav.class);
    private int patientId;

    ByDateNav(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public void onPage(List<VisitFullDTO> visits, String hilight) {

    }

    @Override
    public void trigger() {

    }
}
