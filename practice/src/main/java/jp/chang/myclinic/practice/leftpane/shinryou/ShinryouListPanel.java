package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ShinryouListPanel extends JPanel {

    private Map<JCheckBox, ShinryouFullDTO> checkMap = new HashMap<>();

    ShinryouListPanel(int width, List<ShinryouFullDTO> shinryouList){
        setLayout(new MigLayout("insets 0, gapy 0, gapx 0", String.format("[%dpx!]", width), ""));
        shinryouList.forEach(shinryouFull -> {
            JCheckBox check = new JCheckBox("");
            int w = width - check.getPreferredSize().width;
            WrappedText text = new WrappedText(w, shinryouFull.master.name);
            checkMap.put(check, shinryouFull);
            add(check, "split 2");
            add(text, "wrap");
        });
    }

    List<ShinryouFullDTO> getChecked(){
        List<ShinryouFullDTO> checked = new ArrayList<>();
        for(JCheckBox check: checkMap.keySet()){
            if( check.isSelected() ){
                checked.add(checkMap.get(check));
            }
        }
        return checked;
    }
}
