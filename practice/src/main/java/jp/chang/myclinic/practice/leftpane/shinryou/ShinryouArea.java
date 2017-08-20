package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class ShinryouArea extends JPanel {

    private ShinryouListDisp shinryouListDisp;

    public ShinryouArea(List<ShinryouFullDTO> shinryouList, VisitDTO visit, int currentVisitId, int tempVisitId){
//        super(new MigLayout("insets 0, gapy 3", "[grow]", ""));
//        shinryouListDisp = new ShinryouListDisp(shinryouList);
//        add(new ShinryouMenu(visit.visitId, currentVisitId, tempVisitId), "growx, wrap");
//        add(shinryouListDisp, "growx");
    }

    public ShinryouArea(List<ShinryouFullDTO> shinryouList, VisitDTO visit, int width, MainExecContext mainExecContext){
        super(new MigLayout("insets 0, gapy 3", String.format("[%dpx!]", width), ""));
        int currentVisitId = mainExecContext.getCurrentVisitId();
        int tempVisitId = mainExecContext.getTempVisitId();
        shinryouListDisp = new ShinryouListDisp(shinryouList, width, mainExecContext);
        add(new ShinryouMenu(visit.visitId, width, mainExecContext), "wrap");
        add(shinryouListDisp, "growx");
    }
}
