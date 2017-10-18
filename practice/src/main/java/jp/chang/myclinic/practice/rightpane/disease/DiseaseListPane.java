package jp.chang.myclinic.practice.rightpane.disease;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.DiseaseUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

class DiseaseListPane extends JPanel {

    interface Callback {
        default void onClick(DiseaseFullDTO disease){}
    }

    private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private Callback callback = new Callback(){};

    DiseaseListPane(int width, List<DiseaseFullDTO> diseases){
        setLayout(new FixedWidthLayout(width));
        diseases.forEach(d -> {
            WrappedText text = new WrappedText(width, makeLabel(d));
            text.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    callback.onClick(d);
                }
            });
            text.setCursor(handCursor);
            add(text);
        });
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private String makeLabel(DiseaseFullDTO diseaseFull){
        return DiseaseUtil.getFullName(diseaseFull) + " (" +
                DateTimeUtil.sqlDateToKanji(diseaseFull.disease.startDate, DateTimeUtil.kanjiFormatter5) + ")";
    }
}
