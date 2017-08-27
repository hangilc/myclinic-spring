package jp.chang.myclinic.practice.leftpane.hoken;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeUtil;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.HokenUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HokenArea extends JPanel {

    private int width;

    public HokenArea(HokenDTO hoken, VisitDTO visit, int width){
        this.width = width;
        setLayout(new MigLayout("insets 0, fill, debug", "[grow]", ""));
        add(makeDisp(hoken));
    }

    private JEditorPane makeDisp(HokenDTO hoken){
        WrappedText disp = new WrappedText(width, hokenRepText(hoken));
        disp.setCursor(PracticeUtil.handCursor);
        bindDisp(disp);
        return disp;
    }

    private String hokenRepText(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        if (rep.isEmpty()) {
            rep = "(保険なし)";
        }
        return rep;
    }

    private void bindDisp(WrappedText disp){
        disp.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }
}
