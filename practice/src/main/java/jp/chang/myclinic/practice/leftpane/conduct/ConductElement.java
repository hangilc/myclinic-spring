package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.WrappedText;
import jp.chang.myclinic.practice.leftpane.WorkArea;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class ConductElement {

    private enum Mode { DISP, EDIT };

    private int width;
    private Mode mode;
    private ConductFullDTO conductFull;
    private VisitDTO visit;
    private Component disp;
    private WorkArea editor;

    ConductElement(int width, ConductFullDTO conductFull, VisitDTO visit){
        this.width = width;
        this.visit = visit;
        this.mode = Mode.DISP;
        this.conductFull = conductFull;
        this.disp = addClickListener(makeDisp());
    }

    Component getComponent(){
        switch(mode){
            case DISP: return this.disp;
            case EDIT: return this.editor;
            default: throw new RuntimeException("invalid mode");
        }
    }

    int getConductId(){
        return conductFull.conduct.conductId;
    }

    private Component makeDisp(){
        JPanel panel = new JPanel(new FixedWidthLayout(width));
        {
            String kindRep;
            ConductKind conductKind = ConductKind.fromCode(conductFull.conduct.kind);
            if( conductKind != null ){
                kindRep = "<" + conductKind.getKanjiRep() + ">";
            } else {
                kindRep = "<" + "不明" + ">";
            }
            panel.add(new JLabel(kindRep));
        }
        if( conductFull.gazouLabel != null ){
            panel.add(addClickListener(new WrappedText(width, conductFull.gazouLabel.label)));
        }
        for(ConductShinryouFullDTO shinryou: conductFull.conductShinryouList){
            String label = shinryou.master.name;
            panel.add(addClickListener(new WrappedText(width, label)));
        }
        for(ConductDrugFullDTO drug: conductFull.conductDrugs){
            String label = DrugUtil.conductDrugRep(drug);
            panel.add(addClickListener(new WrappedText(width, label)));
        }
        for(ConductKizaiFullDTO kizai: conductFull.conductKizaiList){
            String label = KizaiUtil.kizaiRep(kizai);
            panel.add(addClickListener(new WrappedText(width, label)));
        }
        return panel;
    }

    private Component addClickListener(Component comp){
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openEditor();
            }
        });
        return comp;
    }

    private WorkArea makeEditor(){
        WorkArea wa = new WorkArea(width, "処置の編集");
        ConductEditor conductEditor = new ConductEditor(wa.getInnerColumnWidth(), conductFull, visit);
        conductEditor.setCallback(new ConductEditor.Callback(){
            @Override
            public void onModified(ConductFullDTO modified) {
                conductFull = modified;
                openEditor();
            }

            @Override
            public void onDelete() {
                ConductBoxContext.get(wa).onDelete(conductFull.conduct.conductId);
            }

            @Override
            public void onClose(ConductFullDTO current) {
                conductFull = current;
                closeEditor();
            }
        });
        wa.setComponent(conductEditor);
        return wa;
    }

    private void openEditor(){
        WorkArea wa = makeEditor();
        Component curr = getComponent();
        Container parent = curr.getParent();
        parent.add(wa, new FixedWidthLayout.Replace(curr));
        editor = wa;
        mode = Mode.EDIT;
        parent.revalidate();
        parent.repaint();
    }

    private void closeEditor(){
        Component curr = getComponent();
        Container parent = curr.getParent();
        this.disp = makeDisp();
        parent.add(this.disp, new FixedWidthLayout.Replace(curr));
        mode = Mode.DISP;
        parent.revalidate();
        parent.repaint();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(getComponent(), message);
    }

}
