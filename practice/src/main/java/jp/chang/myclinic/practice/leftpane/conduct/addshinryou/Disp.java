package jp.chang.myclinic.practice.leftpane.conduct.addshinryou;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class Disp extends JPanel {

    private WrappedText nameText;
    private ShinryouMasterDTO master;

    Disp(int width){
        setLayout(new MigLayout("insets 0, gapx 4", "", ""));
        JLabel nameLabel = new JLabel("名称：");
        nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4);
        add(nameLabel);
        add(nameText);
    }

    Disp(int width, ShinryouMasterDTO master) {
        this(width);
        setMaster(master);
    }

    void setMaster(ShinryouMasterDTO master){
        this.master = master;
        nameText.setText(master.name);
    }

    ShinryouMasterDTO getMaster(){
        return master;
    }

}
