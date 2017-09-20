package jp.chang.myclinic.practice.rightpane.disease;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.rightpane.disease.addpane.DiseaseAddPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class DiseaseBox extends JPanel {

    private int width;
    private Component workPane;
    private List<DiseaseFullDTO> currentDiseases;

    public DiseaseBox(int width){
        setLayout(new FixedWidthLayout(width));
        this.width = width;
        this.workPane = new JPanel();
        add(workPane);
        add(makeCommandBox());
    }

    public void clear(){
        reset(Collections.emptyList());
    }

    public void reset(List<DiseaseFullDTO> diseaseList){
        this.currentDiseases = diseaseList;
        gotoListMode();
    }

    private void gotoListMode(){
        switchPane(new DiseaseListPane(width, currentDiseases));
    }

    private void switchPane(Component pane){
        add(pane, new FixedWidthLayout.Replace(this.workPane));
        this.workPane = pane;
        revalidate();
        repaint();
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Link listLink = new Link("現行");
        Link addLink = new Link("追加");
        addLink.setCallback(evt -> switchPane(new DiseaseAddPane(width)));
        Link endLink = new Link("転帰");
        Link editLink = new Link("編集");
        panel.add(listLink);
        panel.add(new JLabel("|"));
        panel.add(addLink);
        panel.add(new JLabel("|"));
        panel.add(endLink);
        panel.add(new JLabel("|"));
        panel.add(editLink);
        return panel;
    }
}
