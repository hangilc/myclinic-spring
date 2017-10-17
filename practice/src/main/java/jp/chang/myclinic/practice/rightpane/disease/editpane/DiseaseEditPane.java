package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.practice.lib.dateinput.DateInput;
import jp.chang.myclinic.practice.lib.searcharea.SearchArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class DiseaseEditPane extends JPanel {

    private FormPart formPart;

    public DiseaseEditPane(int width, DiseaseFullDTO disease){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        LocalDate startDate = LocalDate.parse(disease.disease.startDate);
        formPart = new FormPart();
        CommandBox commandBox = new CommandBox();
        commandBox.setCallback(new CommandBox.Callback() {
            @Override
            public void onEnter() {

            }

            @Override
            public void onDelAdj() {

            }

            @Override
            public void onDelete() {

            }
        });
        SearchArea searchArea = new SearchArea(width, new DateInput(){
            @Override
            public Result<LocalDate, List<String>> getValue(){
                return new Result<>(startDate, null);
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

    public void setDisease(DiseaseFullDTO disease){
        formPart.setDisease(disease);
    }
}
