package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.practice.lib.dateinput.DateInput;
import jp.chang.myclinic.practice.rightpane.disease.searcharea.SearchArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class DiseaseEditPane extends JPanel {

    public interface Callback {
        default void onModified(int diseaseId){}
        default void onDeleted(int diseaseId){}
    }

    private FormPart formPart;

    private Callback callback = new Callback(){};

    public DiseaseEditPane(int width, DiseaseFullDTO disease){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        LocalDate startDate = LocalDate.parse(disease.disease.startDate);
        formPart = new FormPart();
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {
                formPart.getModifyDTO().accept(
                        modifyDTO -> {
                            Service.api.modifyDisease(modifyDTO)
                                    .thenAccept(ok -> EventQueue.invokeLater(() -> callback.onModified(disease.disease.diseaseId)))
                                    .exceptionally(t -> {
                                        t.printStackTrace();
                                        EventQueue.invokeLater(() -> {
                                            alert(t.toString());
                                        });
                                        return null;
                                    });
                        },
                        errs -> {
                            alert(String.join("\n", errs));
                        }
                );
            }

            @Override
            public void onDelAdj() {
                formPart.deleteAdj();
            }

            @Override
            public void onDelete() {
                int diseaseId = disease.disease.diseaseId;
                Service.api.deleteDisease(diseaseId)
                        .thenAccept(ok -> EventQueue.invokeLater(() -> callback.onDeleted(diseaseId)))
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }
        });
        SearchArea searchArea = new SearchArea(width, new DateInput(){
            @Override
            public Result<LocalDate, List<String>> getValue(){
                return Result.createValue(startDate);
            }

            @Override
            public void setValue(LocalDate date) {
                throw new RuntimeException("cannot setValue");
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
        searchArea.setCallback(new SearchArea.Callback(){
            @Override
            public void onByoumeiSelect(ByoumeiMasterDTO byoumeiMaster) {
                formPart.setByoumeiMaster(byoumeiMaster);
            }

            @Override
            public void onShuushokugoSelect(ShuushokugoMasterDTO shuushokugoMaster) {
                formPart.addShuushokugoMaster(shuushokugoMaster);
            }
        });
        add(formPart, "growx, wrap");
        add(commandBox, "wrap");
        add(searchArea);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    public void setDisease(DiseaseFullDTO disease){
        formPart.setDisease(disease);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
