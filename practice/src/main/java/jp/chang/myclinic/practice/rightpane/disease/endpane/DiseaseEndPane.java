package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class DiseaseEndPane extends JPanel {
    private int width;

    public DiseaseEndPane(int width, List<DiseaseFullDTO> diseases){
        this.width = width;
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
                    List<Integer> diseaseIds = listPart.getSelected().stream().map(d -> d.disease.diseaseId).collect(Collectors.toList());
                    System.out.println("disease IDs: " + diseaseIds);
                    System.out.println("end date: " + endDate.toString());
                });
            }
        });
        add(listPart, "wrap");
        add(endDateInput, "wrap");
        add(dateManipPart, "wrap");
        add(reasonPart, "wrap");
        add(commandPart);
    }

}
