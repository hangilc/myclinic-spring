package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.WrappedText;

import javax.swing.*;
import java.util.List;

class ShinryouListDisp extends JPanel {

    ShinryouListDisp(List<ShinryouFullDTO> shinryouList, int width, MainExecContext mainExecContext){
        //setLayout(new MigLayout("insets 0, gapy 0", String.format("[%dpx!]", width), ""));
        setLayout(new FixedWidthLayout(width));
        for(ShinryouFullDTO shinryou: shinryouList){
//            ShinryouDispWrapper shinryouDispWrapper = new ShinryouDispWrapper(shinryou, width, mainExecContext);
//            add(shinryouDispWrapper, "wrap");
            WrappedText disp = new WrappedText(width, shinryou.master.name);
            add(disp);
        }
    }
}
