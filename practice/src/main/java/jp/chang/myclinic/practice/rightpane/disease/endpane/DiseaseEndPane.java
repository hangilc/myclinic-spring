package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.consts.Shuushokugo;
import jp.chang.myclinic.dto.DiseaseAdjFullDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseModifyEndReasonDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiseaseEndPane extends JPanel {

    public interface Callback {
        default void onModified(List<Integer> diseaseIds){}
    }

    private Callback callback = new Callback(){};

    public DiseaseEndPane(int width, List<DiseaseFullDTO> diseases){
        setLayout(new MigLayout("insets 0, gapy 2", String.format("[%dpx!]", width), ""));
        DateInputForm endDateInput = new DateInputForm(Gengou.Current);
        endDateInput.setValue(LocalDate.now());
        DateManipPart dateManipPart = new DateManipPart();
        ListPart listPart = new ListPart(width, diseases);
        listPart.setCallback(new ListPart.Callback(){
            @Override
            public void onChange() {
                doListSelectionChange(listPart, endDateInput);
            }
        });
        ReasonPart reasonPart = new ReasonPart();
        CommandPart commandPart = new CommandPart();
        commandPart.setCallback(new CommandPart.Callback() {
            @Override
            public void onEnter() {
                endDateInput.getValue()
                        .ifPresent(endDate -> {
                            char reason = reasonPart.getReason();
                            String endDateStr = endDate.toString();
                            List<DiseaseFullDTO> selected = listPart.getSelected();
                            List<DiseaseModifyEndReasonDTO> args = selected.stream()
                                    .map(d -> {
                                        DiseaseModifyEndReasonDTO arg = new DiseaseModifyEndReasonDTO();
                                        arg.diseaseId = d.disease.diseaseId;
                                        arg.endDate = endDateStr;
                                        arg.endReason = adaptReason(d, reason);
                                        return arg;
                                    })
                                    .collect(Collectors.toList());
                            Service.api.batchUpdateDiseaseEndReason(args)
                                    .thenAccept(ret -> EventQueue.invokeLater(() ->{
                                        List<Integer> diseaseIds = diseases.stream()
                                                .map(d -> d.disease.diseaseId).collect(Collectors.toList());
                                        callback.onModified(diseaseIds);
                                    }))
                                    .exceptionally(t -> {
                                        t.printStackTrace();
                                        EventQueue.invokeLater(() -> {
                                            alert(t.toString());
                                        });
                                        return null;
                                    });
                       })
                        .ifError(errs -> {
                            alert("終了日：\n" + String.join("\n", errs));
                        });
            }
        });
        add(listPart, "wrap");
        add(endDateInput, "wrap");
        add(dateManipPart, "wrap");
        add(reasonPart, "wrap");
        add(commandPart);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doListSelectionChange(ListPart listPart, DateInputForm endDateInput){
        Optional<String> lastDate = listPart.getSelected().stream().map(d -> d.disease.startDate)
                .sorted((a, b) -> b.compareTo(a)).findFirst();
        if( lastDate.isPresent() ){
            endDateInput.setValue(LocalDate.parse(lastDate.get()));
        } else {
            endDateInput.setValue(LocalDate.now());
        }
    }

    private char adaptReason(DiseaseFullDTO disease, char origReason){
        if( origReason == 'C' && hasSusp(disease) ){
            return 'S';
        } else {
            return origReason;
        }
    }

    private boolean hasSusp(DiseaseFullDTO disease){
        List<DiseaseAdjFullDTO> adjList = disease.adjList;
        int susp = Shuushokugo.Susp;
        if( adjList == null ){
            return false;
        } else {
            for(DiseaseAdjFullDTO adj: adjList){
                if( adj.diseaseAdj.shuushokugocode == susp ){
                    return true;
                }
            }
            return false;
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
