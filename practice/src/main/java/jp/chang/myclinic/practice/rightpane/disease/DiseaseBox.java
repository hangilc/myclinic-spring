package jp.chang.myclinic.practice.rightpane.disease;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.rightpane.disease.addpane.DiseaseAddPane;
import jp.chang.myclinic.practice.rightpane.disease.browsepane.DiseaseBrowsePane;
import jp.chang.myclinic.practice.rightpane.disease.editpane.DiseaseEditPane;
import jp.chang.myclinic.practice.rightpane.disease.endpane.DiseaseEndPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class DiseaseBox extends JPanel {

    private static final int itemsPerPage = 10;

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
        switchPane(pane, null);
    }

    private void switchPane(Component pane, Runnable uiCallback){
        add(pane, new FixedWidthLayout.Replace(this.workPane));
        this.workPane = pane;
        revalidate();
        repaint();
        if( uiCallback != null ){
            EventQueue.invokeLater(uiCallback::run);
        }
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
        endLink.setCallback(evt -> openEndPane());
        Link editLink = new Link("編集");
        editLink.setCallback(evt -> openBrowsePane());
        panel.add(listLink);
        panel.add(new JLabel("|"));
        panel.add(addLink);
        panel.add(new JLabel("|"));
        panel.add(endLink);
        panel.add(new JLabel("|"));
        panel.add(editLink);
        return panel;
    }

    private void openEndPane(){
        DiseaseEndPane pane = new DiseaseEndPane(width, currentDiseases);
        pane.setCallback(new DiseaseEndPane.Callback(){
            @Override
            public void onModified(List<Integer> diseaseIds) {
                Service.api.listCurrentDiseaseFull(patientId)
                        .thenAccept(currents -> EventQueue.invokeLater(() ->{
                            currentDiseases = currents;
                            openEndPane();
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

    private static class BrowseData {
        int count;
    }

    private void openBrowsePane(){
        BrowseData data = new BrowseData();
        Service.api.countPageOfDiseaseByPatient(patientId, itemsPerPage)
                .thenCompose(count -> {
                    data.count = count;
                    return Service.api.pageDiseaseFull(patientId, 0, itemsPerPage);
                })
                .thenAccept(diseases -> {
                    DiseaseBrowsePane pane = new DiseaseBrowsePane(width, diseases, patientId, itemsPerPage, data.count);
                    pane.setCallback(new DiseaseBrowsePane.Callback() {
                        @Override
                        public void onEdit(DiseaseFullDTO disease) {
                            openEditPane(disease);
                        }
                    });
                    switchPane(pane);
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void openEditPane(DiseaseFullDTO disease){
        DiseaseEditPane pane = new DiseaseEditPane(width, disease);
        switchPane(pane, () -> pane.setDisease(disease));
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
