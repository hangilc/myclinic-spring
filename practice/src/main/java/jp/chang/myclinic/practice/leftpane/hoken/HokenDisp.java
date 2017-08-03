package jp.chang.myclinic.practice.leftpane.hoken;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.PracticeUtil;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.util.HokenUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HokenDisp extends JPanel {

    private VisitDTO visit;
    private JComponent disp;
    private HokenDTO currentHoken;

    public HokenDisp(HokenDTO hoken, VisitDTO visit){
        currentHoken = hoken;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        this.visit = visit;
        String rep = HokenUtil.hokenRep(hoken);
        if( rep.isEmpty() ){
            rep = "(保険なし)";
        }
        disp = new JLabel(rep);
        disp.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                doEdit();
            }
        });
        setCursor(PracticeUtil.handCursor);
        add(disp);
    }

    private void doEdit() {
        Service.api.listAvailableHoken(visit.patientId, visit.visitedAt.substring(0, 10))
                .thenAccept(available -> {
                    HokenChooser chooser = new HokenChooser(available, currentHoken);
                    chooser.setCallback(new HokenChooser.Callback(){
                        @Override
                        public void onEnter(HokenDTO selectedHoken){
                            doEnter(selectedHoken);
                        }

                        @Override
                        public void onCancel(){
                            EventQueue.invokeLater(() -> {
                                remove(chooser);
                                add(disp);
                                repaint();
                                revalidate();
                            });
                        }
                    });
                    remove(disp);
                    add(chooser, "growx");
                    repaint();
                    revalidate();
                })
                .exceptionally(t -> {
                    EventQueue.invokeLater(() -> {
                        t.printStackTrace();
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doEnter(HokenDTO selectedHoken){
        visit.shahokokuhoId = selectedHoken.shahokokuho == null ? 0 :
                selectedHoken.shahokokuho.shahokokuhoId;
        visit.koukikoureiId = selectedHoken.koukikourei == null ? 0 :
                selectedHoken.koukikourei.koukikoureiId;
        visit.roujinId = selectedHoken.roujin == null ? 0 :
                selectedHoken.roujin.roujinId;
        visit.kouhi1Id = selectedHoken.kouhi1 == null ? 0 :
                selectedHoken.kouhi1.kouhiId;
        visit.kouhi2Id = selectedHoken.kouhi2 == null ? 0 :
                selectedHoken.kouhi2.kouhiId;
        visit.kouhi3Id = selectedHoken.kouhi3 == null ? 0 :
                selectedHoken.kouhi3.kouhiId;
        Service.api.updateHoken(visit)
                .thenAccept(result -> {
                    // TODO: update disp
                })
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
