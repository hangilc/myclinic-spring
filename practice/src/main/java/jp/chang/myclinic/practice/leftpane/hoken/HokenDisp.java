package jp.chang.myclinic.practice.leftpane.hoken;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.practice.PracticeUtil;
import jp.chang.myclinic.util.HokenUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HokenDisp extends JLabel {

    public HokenDisp(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        if( rep.isEmpty() ){
            rep = "(保険なし)";
        }
        setText(rep);
        setCursor(PracticeUtil.handCursor);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("hoken clicked");
            }
        });
    }

}
