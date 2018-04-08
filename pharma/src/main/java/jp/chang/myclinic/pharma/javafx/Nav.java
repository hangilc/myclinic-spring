package jp.chang.myclinic.pharma.javafx;

import jp.chang.myclinic.dto.VisitFullDTO;

import java.util.List;

public interface Nav {
    void onPage(List<VisitFullDTO> visits, String hilight);
    void trigger();
}
