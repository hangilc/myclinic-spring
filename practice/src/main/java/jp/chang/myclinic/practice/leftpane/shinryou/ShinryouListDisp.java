package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class ShinryouListDisp extends JPanel {

    ShinryouListDisp(List<ShinryouFullDTO> shinryouList){
        setLayout(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        for(ShinryouFullDTO shinryou: shinryouList){
            ShinryouDispWrapper shinryouDispWrapper = new ShinryouDispWrapper(shinryou);
            add(shinryouDispWrapper, "growx, wrap");
        }
    }
}
