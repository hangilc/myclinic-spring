package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class ConductArea extends JPanel {

    public ConductArea(List<ConductFullDTO> conducts){
//        super(new MigLayout("insets 0", "[grow]", ""));
//        for(ConductFullDTO conduct: conducts){
//            add(new ConductDispWrapper(conduct), "growx, wrap");
//        }
    }

    public ConductArea(List<ConductFullDTO> conducts, int width, MainExecContext mainExecContext){
        super(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        for(ConductFullDTO conduct: conducts){
            add(new ConductDispWrapper(conduct, width, mainExecContext), "wrap");
        }
    }
}
