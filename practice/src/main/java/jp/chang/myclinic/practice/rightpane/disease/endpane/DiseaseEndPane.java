package jp.chang.myclinic.practice.rightpane.disease.endpane;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DiseaseUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiseaseEndPane extends JPanel {
    private int width;

    public DiseaseEndPane(int width, List<DiseaseFullDTO> diseases){
        this.width = width;
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        add(makeListArea(diseases), "wrap");
    }

    private Component makeListArea(List<DiseaseFullDTO> diseases){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[]2[]", ""));
        System.out.println("width: " + width);
        diseases.forEach(disease -> {
            JCheckBox check = new JCheckBox("");
            check.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            int w = width - check.getPreferredSize().width - 2;
            System.out.println("w: " + w);
            String label = DiseaseUtil.getFullName(disease);
            WrappedText t = new WrappedText(w, label);
            panel.add(check);
            panel.add(t, "wrap");
        });
        return panel;
    }
}
