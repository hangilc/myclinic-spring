package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ListPart extends JPanel {

    private static class Item {
        JCheckBox check;
        DiseaseFullDTO diseaseFull;

        Item(JCheckBox check, DiseaseFullDTO diseaseFull){
            this.check = check;
            this.diseaseFull = diseaseFull;
        }

        boolean isSelected(){
            return check.isSelected();
        }

        DiseaseFullDTO getDiseaseFull(){
            return diseaseFull;
        }
    }

    private List<Item> items = new ArrayList<>();

    ListPart(int width, List<DiseaseFullDTO> diseases){
        setLayout(new MigLayout("insets 0, gapy 0", "[]2[]", ""));
        diseases.forEach(disease -> {
            JCheckBox check = new JCheckBox("");
            check.addActionListener(evt -> {
                System.out.println("action");
            });
            check.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            int w = width - check.getPreferredSize().width - 2;
            WrappedText t = new WrappedText(w, makeLabel(disease));
            items.add(new Item(check, disease));
            add(check);
            add(t, "wrap");
        });
    }

    List<DiseaseFullDTO> getSelected(){
        return items.stream().filter(Item::isSelected).map(Item::getDiseaseFull).collect(Collectors.toList());
    }

    private String makeLabel(DiseaseFullDTO d){
        String name = DiseaseUtil.getFullName(d);
        String start = DateTimeUtil.sqlDateToKanji(d.disease.startDate, DateTimeUtil.kanjiFormatter5);
        return name + " (" + start + ")";
    }
}
