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

    DispPane(Runnable onComplete){
        setLayout(new MigLayout("insets 0, fillx, top, gapy 2", "", ""));
        add(new Strut(w -> {
            width = w;
            batchAddhotlines(pendingHotlines, onComplete);
            pendingHotlines = null;
        }), "growx");
    }

    void addHotlines(Collection<HotlineDTO> hotlines, Runnable onComplete){
        if( width < 0 ){
            pendingHotlines.addAll(hotlines);
        } else {
            batchAddhotlines(hotlines, onComplete);
        }
    }

    int getLargestHotlineId(){
        return largestHotlineId;
    }

    private void batchAddhotlines(Collection<HotlineDTO> hotlines, Runnable onComplete){
        for(HotlineDTO hotline: hotlines){
            doAddHotline(hotline);
        }
        repaint();
        revalidate();
        onComplete.run();
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
        for(User user: User.values()){
            if( user.getName().equals(role) ){
                return user.getDispName();
            }
        }
        return "不明";
    }
}
