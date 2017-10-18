package jp.chang.myclinic.practice.leftpane.hoken;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.util.KouhiUtil;
import jp.chang.myclinic.util.KoukikoureiUtil;
import jp.chang.myclinic.util.RoujinUtil;
import jp.chang.myclinic.util.ShahokokuhoUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class HokenChooser extends JPanel {

    public interface Callback {
        default void onEnter(HokenDTO hokenDTO){};
        default void onCancel(){};
    }

    private HokenDTO available;
    //private VisitDTO visit;
    private Callback callback = new Callback(){};
    private JCheckBox shahokokuhoCheck;
    private JCheckBox koukikoureiCheck;
    private JCheckBox roujinCheck;
    private JCheckBox kouhi1Check;
    private JCheckBox kouhi2Check;
    private JCheckBox kouhi3Check;

    public HokenChooser(HokenDTO available, HokenDTO current, int visitId, String atDate, int patientId){
        this.available = available;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        if( available.shahokokuho != null ){
            shahokokuhoCheck = new JCheckBox(ShahokokuhoUtil.rep(available.shahokokuho));
            boolean selected = current.shahokokuho != null &&
                    available.shahokokuho.shahokokuhoId == current.shahokokuho.shahokokuhoId;
            shahokokuhoCheck.setSelected (selected);
            add(shahokokuhoCheck, "wrap");
        }
        if( available.koukikourei != null ){
            koukikoureiCheck = new JCheckBox(KoukikoureiUtil.rep(available.koukikourei));
            boolean selected = current.koukikourei != null &&
                    available.koukikourei.koukikoureiId == current.koukikourei.koukikoureiId;
            koukikoureiCheck.setSelected (selected);
            add(koukikoureiCheck, "wrap");
        }
        if( available.roujin != null ){
            roujinCheck = new JCheckBox(RoujinUtil.rep(available.roujin));
            boolean selected = current.roujin != null &&
                    available.roujin.roujinId == current.roujin.roujinId;
            roujinCheck.setSelected (selected);
            add(roujinCheck, "wrap");
        }
        if( available.kouhi1 != null ){
            kouhi1Check = new JCheckBox(KouhiUtil.rep(available.kouhi1));
            boolean selected = current.kouhi1 != null &&
                    available.kouhi1.kouhiId == current.kouhi1.kouhiId;
            kouhi1Check.setSelected (selected);
            add(kouhi1Check, "wrap");
        }
        if( available.kouhi2 != null ){
            kouhi2Check = new JCheckBox(KouhiUtil.rep(available.kouhi2));
            boolean selected = current.kouhi2 != null &&
                    available.kouhi2.kouhiId == current.kouhi2.kouhiId;
            kouhi2Check.setSelected (selected);
            add(kouhi2Check, "wrap");
        }
        if( available.kouhi3 != null ){
            kouhi3Check = new JCheckBox(KouhiUtil.rep(available.kouhi3));
            boolean selected = current.kouhi3 != null &&
                    available.kouhi3.kouhiId == current.kouhi3.kouhiId;
            kouhi3Check.setSelected (selected);
            add(kouhi3Check, "wrap");
        }
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(event -> doEnter(visitId, atDate, patientId));
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        add(enterButton, "newline, span, split 2");
        add(cancelButton);
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doEnter(int visitId, String atDate, int patientId){
        VisitDTO newVisit = new VisitDTO();
        newVisit.visitId = visitId;
        if( shahokokuhoCheck != null && shahokokuhoCheck.isSelected() ){
            newVisit.shahokokuhoId = available.shahokokuho.shahokokuhoId;
        }
        if( koukikoureiCheck != null && koukikoureiCheck.isSelected() ){
            newVisit.koukikoureiId = available.koukikourei.koukikoureiId;
        }
        if( roujinCheck != null && roujinCheck.isSelected() ){
            newVisit.roujinId = available.roujin.roujinId;
        }
        if( kouhi1Check!= null && kouhi1Check.isSelected() ){
            newVisit.kouhi1Id = available.kouhi1.kouhiId;
        }
        if( kouhi2Check!= null && kouhi2Check.isSelected() ){
            newVisit.kouhi2Id = available.kouhi2.kouhiId;
        }
        if( kouhi3Check!= null && kouhi3Check.isSelected() ){
            newVisit.kouhi3Id = available.kouhi3.kouhiId;
        }
        Service.api.updateHoken(newVisit)
                .thenCompose(result -> Service.api.getHoken(visitId))
                .thenAccept(newHoken -> EventQueue.invokeLater(() -> callback.onEnter(newHoken)))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
