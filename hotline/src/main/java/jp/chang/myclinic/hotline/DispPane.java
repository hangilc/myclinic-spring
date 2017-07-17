package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.wrappedtext.Strut;
import jp.chang.myclinic.hotline.wrappedtext.WrappedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class DispPane extends JPanel {

    private int width = -1;
    private List<HotlineDTO> pendingHotlines = new ArrayList<>();

    DispPane(){
        setLayout(new MigLayout("insets 0, fill", "", ""));
        add(new Strut(w -> {
            width = w;
            batchAddhotlines(pendingHotlines);
            pendingHotlines = null;
        }), "grow");
    }

    void addHotlines(Collection<HotlineDTO> hotlines){
        if( width < 0 ){
            pendingHotlines.addAll(hotlines);
        } else {
            batchAddhotlines(hotlines);
        }
    }

    private void batchAddhotlines(Collection<HotlineDTO> hotlines){
        for(HotlineDTO hotline: hotlines){
            doAddHotline(hotline);
        }
        repaint();
        revalidate();
    }

    private void doAddHotline(HotlineDTO hotline){
        WrappedText text = new WrappedText(hotline.message, width);
        add(text, "wrap");
    }
}
