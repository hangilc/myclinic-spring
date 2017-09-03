package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;

import javax.swing.*;
import java.util.List;

public class ConductBox extends JPanel {

    private int width;

    public ConductBox(int width, List<ConductFullDTO> conducts, VisitDTO visit){
        this.width = width;
        setLayout(new FixedWidthLayout(width));
        conducts.forEach(this::append);
    }

    private void append(ConductFullDTO conductFull){
        ConductElement element = new ConductElement(width, conductFull);
        add(element.getComponent());
    }
}
