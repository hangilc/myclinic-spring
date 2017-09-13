package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static jp.chang.myclinic.practice.leftpane.shinryou.ShinryouElement.Mode.DISP;

class ShinryouElement {

    enum Mode { DISP, EDIT };

    private int width;
    private ShinryouFullDTO shinryouFull;
    private VisitDTO visit;
    private Mode mode;
    private Component disp;
    private WorkArea editorWorkArea;

    ShinryouElement(int width, ShinryouFullDTO shinryouFull, VisitDTO visit){
        this.width = width;
        this.shinryouFull = shinryouFull;
        this.visit = visit;
        this.mode = DISP;
        disp = makeDisp();
    }

    Component getComponent(){
        switch(mode){
            case DISP: return disp;
            case EDIT: return editorWorkArea;
            default: throw new RuntimeException("invalid mode");
        }
    }

    int getShinryoucode(){
        return shinryouFull.shinryou.shinryoucode;
    }

    ShinryouFullDTO getShinryouFull(){
        return shinryouFull;
    }

    ShinryouDTO getShinryou(){
        return shinryouFull.shinryou;
    }

    int getShinryouId(){
        return shinryouFull.shinryou.shinryouId;
    }

    private void reset(ShinryouFullDTO newShinryouFull){
        Component curr = getComponent();
        Container parent = curr.getParent();
        this.shinryouFull = newShinryouFull;
        mode = Mode.DISP;
        disp = makeDisp();
        editorWorkArea = null;
        parent.add(disp, new FixedWidthLayout.Replace(curr));
        parent.revalidate();
        parent.repaint();
    }

    private Component makeDisp(){
        WrappedText wt = new WrappedText(width, shinryouFull.master.name);
        wt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                WorkArea wa = new WorkArea(width, "診療行為の編集");
                ShinryouEditor editor = new ShinryouEditor(wa.getInnerColumnWidth(), shinryouFull, visit);
                editor.setCallback(new ShinryouEditor.Callback() {
                    @Override
                    public void onEnter(ShinryouMasterDTO selectedMaster) {
                        doEnter(selectedMaster);
                    }

                    @Override
                    public void onDelete() {

                    }

                    @Override
                    public void onCancel() {
                        closeEdit();
                    }
                });
                wa.setComponent(editor);
                Container parent = wt.getParent();
                parent.add(wa, new FixedWidthLayout.Replace(wt));
                editorWorkArea = wa;
                mode = Mode.EDIT;
                parent.revalidate();
                parent.repaint();
            }
        });
        return wt;
    }

    private void doEnter(ShinryouMasterDTO newMaster){
        ShinryouDTO shinryou = ShinryouDTO.copy(shinryouFull.shinryou);
        shinryou.shinryoucode = newMaster.shinryoucode;
        Service.api.updateShinryou(shinryou)
                .thenCompose(ok -> Service.api.getShinryouFull(shinryou.shinryouId))
                .thenAccept(newShinryouFull -> EventQueue.invokeLater(() ->{
                    reset(newShinryouFull);
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void closeEdit(){
        Container parent = editorWorkArea.getParent();
        parent.add(disp, new FixedWidthLayout.Replace(editorWorkArea));
        this.mode = Mode.DISP;
        this.editorWorkArea = null;
        parent.revalidate();
        parent.repaint();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(null, message);
    }

}
