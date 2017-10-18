package jp.chang.myclinic.practice.leftpane.hoken;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.util.HokenUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HokenDisp extends WrappedText {

    private HokenDTO hoken;

    public HokenDisp(int width, HokenDTO hoken){
        super(width);
        this.hoken = hoken;
        setText(makeText(hoken));
    }

    public void setOnClick(Runnable callback){
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                callback.run();
            }
        });
    }

    public HokenDTO getHoken(){
        return hoken;
    }

    private String makeText(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        if (rep.isEmpty()) {
            rep = "(保険なし)";
        }
        return rep;
    }

}
