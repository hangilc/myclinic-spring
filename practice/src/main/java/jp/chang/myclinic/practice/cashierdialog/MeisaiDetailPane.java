package jp.chang.myclinic.practice.cashierdialog;

import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.MeisaiSectionDTO;
import jp.chang.myclinic.dto.SectionItemDTO;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MeisaiDetailPane extends JPanel {
    MeisaiDetailPane(MeisaiDTO meisai) {
        super(new MigLayout("insets 0, gapy 1", "", ""));
        for(int i=0; i < meisai.sections.size(); i++){
            MeisaiSectionDTO section = meisai.sections.get(i);
            add(new JLabel("【" + section.label + "】"), "span 2, wrap");
            for(SectionItemDTO item: section.items){
                WrappedText wt = new WrappedText(200, item.label);
                add(wt, "");
                add(new JLabel(String.format("%dx%d=%d", item.tanka, item.count, item.tanka * item.count)), "right, wrap");
            }
        }
    }
}
