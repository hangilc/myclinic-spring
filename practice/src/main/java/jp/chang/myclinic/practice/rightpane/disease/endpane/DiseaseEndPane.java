package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.DiseaseModifyEndReasonDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseEndPane extends JPanel {

    public interface Callback {
        default void onModified(List<Integer> diseaseIds){}
    }

    private Callback callback = new Callback(){};

    public DiseaseEndPane(int width, List<DiseaseFullDTO> diseases){
        setLayout(new MigLayout("insets 0, gapy 2", String.format("[%dpx!]", width), ""));
        ListPart listPart = new ListPart(width, diseases);
        DateInputForm endDateInput = new DateInputForm(Gengou.Current);
        DateManipPart dateManipPart = new DateManipPart();
        ReasonPart reasonPart = new ReasonPart();
        CommandPart commandPart = new CommandPart();
        commandPart.setCallback(new CommandPart.Callback() {
            @Override
            public void onEnter() {
                endDateInput.getValue().ifPresent(endDate -> {
                    char reason = reasonPart.getReason();
                    String endDateStr = endDate.toString();
                    List<Integer> diseaseIds = listPart.getSelected().stream().map(d -> d.disease.diseaseId).collect(Collectors.toList());
                    List<DiseaseModifyEndReasonDTO> args = diseaseIds.stream()
                            .map(diseaseId -> {
                                DiseaseModifyEndReasonDTO arg = new DiseaseModifyEndReasonDTO();
                                arg.diseaseId = diseaseId;
                                arg.endDate = endDateStr;
                                arg.endReason = reason;
                                return arg;
                            })
                            .collect(Collectors.toList());
                    Service.api.batchUpdateDiseaseEndReason(args)
                            .thenAccept(ret -> EventQueue.invokeLater(() ->{
                                callback.onModified(diseaseIds);
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

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
