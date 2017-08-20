package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.MainExecContext;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class ShinryouDispWrapper extends JPanel {

    ShinryouDispWrapper(ShinryouFullDTO shinryouFull, int width, MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        ShinryouDisp shinryouDisp = new ShinryouDisp(shinryouFull, getBackground(), width, mainExecContext);
        add(shinryouDisp, "");
    }
}
