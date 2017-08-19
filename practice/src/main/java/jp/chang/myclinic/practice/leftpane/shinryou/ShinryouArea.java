package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class ShinryouArea extends JPanel {

    private ShinryouListDisp shinryouListDisp;

    public ShinryouArea(List<ShinryouFullDTO> shinryouList, VisitDTO visit, int currentVisitId, int tempVisitId){
        super(new MigLayout("insets 0, gapy 3", "[grow]", ""));
        shinryouListDisp = new ShinryouListDisp(shinryouList);
        add(new ShinryouMenu(visit.visitId, currentVisitId, tempVisitId), "growx, wrap");
        add(shinryouListDisp, "growx");
    }
}
