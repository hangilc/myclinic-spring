package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class ShinryouArea extends JPanel {

    public ShinryouArea(List<ShinryouFullDTO> shinryouList){
        super(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        for(ShinryouFullDTO shinryou: shinryouList){
            String label = shinryou.master.name;
            JEditorPane ep = new JEditorPane("text/plain", label);
            ep.setEditable(false);
            ep.setBackground(getBackground());
            ep.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            add(ep, "growx, wrap");
        }
    }
}
