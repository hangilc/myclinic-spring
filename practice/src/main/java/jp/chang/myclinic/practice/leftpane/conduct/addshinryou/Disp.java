package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class Disp extends JPanel {


    Disp(int width, ConductShinryouFullDTO shinryouFull){
        setLayout(new MigLayout("insets 0, gapx 4", "", ""));
        JLabel nameLabel = new JLabel("名称：");
        WrappedText nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4);
        nameText.setText(shinryouFull.master.name);
        add(nameLabel);
        add(nameText);
    }
}
