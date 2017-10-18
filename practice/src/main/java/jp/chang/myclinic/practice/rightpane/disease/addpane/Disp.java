package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class Disp extends JPanel {

    private WrappedText nameText;
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> adjList = new ArrayList<>();

    Disp(int width){
        setLayout(new MigLayout("insets 0", "[]4[]", ""));
        JLabel nameLabel = new JLabel("名称：");
        nameText = new WrappedText(width - nameLabel.getPreferredSize().width - 4, "");
        add(nameLabel);
        add(nameText);
    }

    void setByoumeiMaster(ByoumeiMasterDTO byoumeiMaster){
        this.byoumeiMaster = byoumeiMaster;
        updateName();
    }

    void addShuushokugoMaster(ShuushokugoMasterDTO shuushokugoMaster){
        adjList.add(shuushokugoMaster);
        updateName();
    }

    void delAdj(){
        adjList.clear();
        updateName();
    }

    public ByoumeiMasterDTO getByoumeiMaster() {
        return byoumeiMaster;
    }

    public List<ShuushokugoMasterDTO> getAdjList() {
        return adjList;
    }

    private void updateName(){
        nameText.setText(DiseaseUtil.getFullName(byoumeiMaster, adjList));
    }

}
