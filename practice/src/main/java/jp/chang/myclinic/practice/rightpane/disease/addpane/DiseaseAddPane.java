package jp.chang.myclinic.practice.rightpane.disease.addpane;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class DiseaseAddPane extends JPanel {

    public interface Callback {
        default void onEnter(DiseaseFullDTO entered){}
    }

    private int patientId;
    private Disp disp;
    private DateInputForm startDateInput;
    private Callback callback = new Callback(){};

    public DiseaseAddPane(int width, int patientId) {
        this.patientId = patientId;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        disp = new Disp(width);
        startDateInput = new DateInputForm(Gengou.Current);
        startDateInput.setValue(LocalDate.now());
        startDateInput.setValue(LocalDate.now());
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {
                doEnter();
            }

            @Override
            public void onAddSusp() {
                doAddSusp();
            }

            @Override
            public void onDeleteAdj() {
                disp.delAdj();
            }
        });
        SearchArea searchArea = new SearchArea(width, startDateInput);
        searchArea.setCallback(new SearchArea.Callback() {
            @Override
            public void onByoumeiSelect(ByoumeiMasterDTO byoumeiMaster) {
                disp.setByoumeiMaster(byoumeiMaster);
            }

            @Override
            public void onShuushokugoSelect(ShuushokugoMasterDTO shuushokugoMaster) {
                disp.addShuushokugoMaster(shuushokugoMaster);
            }
        });
        add(disp, "wrap");
        add(startDateInput, "wrap");
        add(commandBox, "wrap");
        add(searchArea);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doEnter() {
        ByoumeiMasterDTO byoumeiMaster = disp.getByoumeiMaster();
        if (byoumeiMaster == null) {
            alert("疾患名が入力されていません。");
            return;
        }
        startDateInput.getValue().ifPresent(startDate -> {
            DiseaseDTO disease = new DiseaseDTO();
            disease.patientId = patientId;
            disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
            disease.startDate = startDate.toString();
            disease.endDate = "0000-00-00";
            disease.endReason = 'N';
            DiseaseNewDTO diseaseNew = new DiseaseNewDTO();
            diseaseNew.disease = disease;
            diseaseNew.adjList = disp.getAdjList().stream()
                    .map(m -> {
                        DiseaseAdjDTO adjDTO = new DiseaseAdjDTO();
                        adjDTO.shuushokugocode = m.shuushokugocode;
                        return adjDTO;
                    })
                    .collect(Collectors.toList());
            Service.api.enterDisease(diseaseNew)
                    .thenCompose(diseaseId -> Service.api.getDiseaseFull(diseaseId))
                    .thenAccept(diseaseFull -> EventQueue.invokeLater(() ->{
                        callback.onEnter(diseaseFull);
                    }))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        });
    }

    private void doAddSusp(){
        Service.api.findShuushokugoMasterByName("の疑い")
                .thenAccept(master -> EventQueue.invokeLater(() -> {
                    disp.addShuushokugoMaster(master);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
