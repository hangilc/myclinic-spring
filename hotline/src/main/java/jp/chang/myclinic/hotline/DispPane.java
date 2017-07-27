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
    private int largestHotlineId;

    DispPane(){
        setLayout(new MigLayout("insets 0, fillx, top, gapy 2", "", ""));
        add(new Strut(w -> {
            width = w;
            batchAddhotlines(pendingHotlines);
            pendingHotlines = null;
        }), "growx");
    }

    void addHotlines(Collection<HotlineDTO> hotlines){
        if( width < 0 ){
            pendingHotlines.addAll(hotlines);
        } else {
            batchAddhotlines(hotlines);
        }
    }

    int getLargestHotlineId(){
        return largestHotlineId;
    }

    private void batchAddhotlines(Collection<HotlineDTO> hotlines){
        for(HotlineDTO hotline: hotlines){
            doAddHotline(hotline);
        }
        repaint();
        revalidate();
    }

    private void doAddHotline(HotlineDTO hotline){
        if( hotline.hotlineId > largestHotlineId ){
            String s = String.format("%s(%03d)> %s", roleLabel(hotline.sender), hotline.hotlineId % 1000, hotline.message);
            WrappedText text = new WrappedText(width, s);
            add(text, "wrap");
            largestHotlineId = hotline.hotlineId;
        }
    }

    private String roleLabel(String role){
        switch(role){
            case "Practice": return "診察";
            case "Pharmacy": return "薬局";
            case "Reception": return "受付";
            default: return "不明";
        }
    }
}
