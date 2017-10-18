package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.lib.Result;
import jp.chang.myclinic.practice.lib.dateinput.DateInputForm;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FormPart extends JPanel {
    private NameLabel nameLabel = new NameLabel();
    private DateInputForm startDateInput = new DateInputForm(Gengou.Current);
    private ReasonSelector reasonSelector = new ReasonSelector();
    private DateInputForm endDateInput = new DateInputForm(Gengou.Current);
    {
        endDateInput.setAllowEmpty(true);
    }
    private DiseaseDTO diseaseDTO;
    private ByoumeiMasterDTO byoumeiMaster;
    private List<ShuushokugoMasterDTO> shuushokugoMasters;

    FormPart(){
        setLayout(new MigLayout("insets 0", "[] [grow]", ""));
        add(new JLabel("名称："));
        add(nameLabel, "growx, wrap");
        add(new JLabel("開始日："));
        add(startDateInput, "wrap");
        add(new JLabel("転帰："));
        add(reasonSelector, "w 50, wrap");
        add(new JLabel("終了日；"));
        add(endDateInput);
    }

    void setDisease(DiseaseFullDTO disease){
        diseaseDTO = DiseaseDTO.copy(disease.disease);
        byoumeiMaster = disease.master;
        shuushokugoMasters = disease.adjList.stream().map(adj -> adj.master).collect(Collectors.toList());
        nameLabel.setText(composeFullName());
        startDateInput.setValue(LocalDate.parse(disease.disease.startDate));
        reasonSelector.setSelectedItem(DiseaseEndReason.fromCode(disease.disease.endReason));
        String endDate = disease.disease.endDate;
        if( "0000-00-00".equals(endDate) || endDate == null ){
            endDateInput.clear();
        } else {
            endDateInput.setValue(LocalDate.parse(endDate));
        }
        revalidate();
        repaint();
    }

    private String composeFullName(){
        return DiseaseUtil.getFullName(byoumeiMaster, shuushokugoMasters);
    }

    void setByoumeiMaster(ByoumeiMasterDTO master){
        diseaseDTO.shoubyoumeicode = master.shoubyoumeicode;
        byoumeiMaster = master;
        nameLabel.setText(composeFullName());
        revalidate();
        repaint();
    }

    void addShuushokugoMaster(ShuushokugoMasterDTO master){
        shuushokugoMasters.add(master);
        nameLabel.setText(composeFullName());
        revalidate();
        repaint();
    }

    void deleteAdj(){
        shuushokugoMasters.clear();
        nameLabel.setText(composeFullName());
        revalidate();
        repaint();
    }

    Result<DiseaseModifyDTO, List<String>> getModifyDTO(){
        List<String> errs = new ArrayList<>();
        DiseaseDTO disease = DiseaseDTO.copy(this.diseaseDTO);
        if( byoumeiMaster == null ){
            errs.add("病名が設定されていません。");
        } else {
            disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
        }
        startDateInput.getValue().accept(
                startDate -> {
                    disease.startDate = startDate.toString();
                },
                startDateErrors -> {
                    errs.add("開始日が不適切です。（" + String.join("", startDateErrors) + "）");
                }
        );
        endDateInput.getValue().accept(
                endDate -> {
                    if (endDate == null) {
                        disease.endDate = "0000-00-00";
                    } else {
                        disease.endDate = endDate.toString();
                    }
                },
                endDateErrors -> {
                    errs.add("終了日が不適切です。（" + String.join("", endDateErrors) + "）");
                }
        );
        disease.endReason = reasonSelector.getReason().getCode();
        if( errs.size() > 0 ){
            return new Result<>(null, errs);
        } else {
            DiseaseModifyDTO modifyDTO = new DiseaseModifyDTO();
            modifyDTO.disease = disease;
            modifyDTO.shuushokugocodes = shuushokugoMasters.stream().map(m -> m.shuushokugocode).collect(Collectors.toList());
            return new Result<>(modifyDTO, null);
        }
    }

}
