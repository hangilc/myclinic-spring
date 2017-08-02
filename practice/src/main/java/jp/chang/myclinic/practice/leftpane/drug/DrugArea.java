package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class DrugArea extends JPanel {

    public DrugArea(List<DrugFullDTO> drugs){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        add(new DrugMenu(), "growx, wrap");
        add(new DrugListDisp(drugs), "growx, wrap");
    }
}
