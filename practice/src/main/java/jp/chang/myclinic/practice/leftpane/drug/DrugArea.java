package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class DrugArea extends JPanel {

    public DrugArea(List<DrugFullDTO> drugs, VisitDTO visit){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        add(new DrugMenu(visit), "growx, wrap");
        add(new DrugListDisp(drugs), "growx, wrap");
    }
}
