package jp.chang.myclinic.pharma.javafx;

import jp.chang.myclinic.dto.VisitTextDrugDTO;

import java.util.List;

public interface Nav {
    void onPage(List<VisitTextDrugDTO> visits, String hilight);
    void trigger();
}
