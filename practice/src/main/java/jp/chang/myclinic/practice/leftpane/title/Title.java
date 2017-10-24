package jp.chang.myclinic.practice.leftpane.title;

import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.util.DateTimeUtil;

import javax.swing.*;
import java.awt.*;

// TODO: implement delete visit
// TODO: implement temp visit
public class Title extends JLabel {

    public Title(VisitDTO visit, int currentVisitId, int tempVisitId){
        String text = DateTimeUtil.sqlDateTimeToKanji(visit.visitedAt,
                DateTimeUtil.kanjiFormatter3, DateTimeUtil.kanjiFormatter4);
        setText(text);
        Font font = getFont().deriveFont(Font.BOLD);
        setFont(font);
        if( visit.visitId == currentVisitId ){
            setBackground(new Color(0xff, 0xff, 0x99));
        } else if( visit.visitId == tempVisitId ){
            setBackground(new Color(0x99, 0xff, 0xff));
        } else {
            setBackground(new Color(0xdd, 0xdd, 0xdd));
        }
        setOpaque(true);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

}
