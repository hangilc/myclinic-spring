package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class ShinryouDispWrapper extends JPanel {

    ShinryouDispWrapper(ShinryouFullDTO shinryouFull){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        ShinryouDisp shinryouDisp = new ShinryouDisp(shinryouFull, getBackground());
        add(shinryouDisp, "growx");
    }
}
