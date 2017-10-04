package jp.chang.myclinic.practice.rightpane.disease;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.rightpane.disease.addpane.DiseaseAddPane;
import jp.chang.myclinic.practice.rightpane.disease.endpane.DiseaseEndPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class DiseaseBox extends JPanel {

    private int width;
    private int patientId;
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
        reset(0, Collections.emptyList());
    }

    public void reset(int patientId, List<DiseaseFullDTO> diseaseList){
        this.patientId = patientId;
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
        listLink.setCallback(evt -> {
            DiseaseListPane diseaseListPane = new DiseaseListPane(width, currentDiseases);
            switchPane(diseaseListPane);
        });
        Link addLink = new Link("追加");
        addLink.setCallback(evt -> {
            if( patientId > 0 ) {
                DiseaseAddPane diseaseAddPane = new DiseaseAddPane(width, patientId);
                diseaseAddPane.setCallback(new DiseaseAddPane.Callback() {
                    @Override
                    public void onEnter(DiseaseFullDTO entered) {
                        currentDiseases.add(entered);
                    }
                });
                switchPane(diseaseAddPane);
            }
        });
        Link endLink = new Link("転帰");
        endLink.setCallback(evt -> openEndPanel());
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

    private void openEndPanel(){
        DiseaseEndPane pane = new DiseaseEndPane(width, currentDiseases);
        pane.setCallback(new DiseaseEndPane.Callback(){
            @Override
            public void onModified(List<Integer> diseaseIds) {
                Service.api.listCurrentDiseaseFull(patientId, LocalDate.now().toString())
                        .thenAccept(currents -> EventQueue.invokeLater(() ->{
                            currentDiseases = currents;
                            openEndPanel();
                        }))
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }
        });
        switchPane(pane);
    }


    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
