package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.leftpane.RecordContext;
import jp.chang.myclinic.practice.WorkArea;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.List;

class DrugElement {

    enum Kind { DISP, EDIT }

    private Kind kind;
    private int index;
    private DrugFullDTO drugFull;
    private VisitDTO visit;
    private int width;
    private DrugDisp disp;
    private WorkArea editorArea;

    DrugElement(int index, DrugFullDTO drugFull, VisitDTO visit, int width){
        this.index = index;
        this.drugFull = drugFull;
        this.visit = visit;
        this.width = width;
        this.kind = Kind.DISP;
        this.disp = new DrugDisp(index, drugFull, width);
        bindDisp(this.disp);
    }

    DrugDisp getDisp(){
        return disp;
    }

    Component getComponent(){
        switch(this.kind){
            case DISP: return disp;
            case EDIT: return editorArea;
            default: throw new RuntimeException("cannot find component");
        }
    }

    int getDrugId(){
        return drugFull.drug.drugId;
    }

    void setIndex(int index){
        this.index = index;
        this.disp.update(index, drugFull);
    }

    void onDrugModified(DrugFullDTO modifiedDrug){
        this.drugFull = modifiedDrug;
        this.disp.update(index, modifiedDrug);
    }

    private void bindDisp(DrugDisp disp){
        disp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openEditorWorkArea();
            }
        });
    }

    private DrugEditPane makeEditorPane(){
        DrugEditPane editor = new DrugEditPane(drugFull, visit);
        editor.setCallback(new DrugEditPane.Callback(){
            @Override
            public void onUpdated(DrugFullDTO drugFull) {
                disp.update(index, drugFull);
                closeEditorWorkArea();
            }

            @Override
            public void onDeleted() {
                List<Integer> drugIds = Collections.singletonList(drugFull.drug.drugId);
                RecordContext.get(getComponent()).ifPresent(ctx -> ctx.onDrugsDeleted(drugIds));
                closeEditorWorkArea();
            }

            @Override
            public void onClose() {
                closeEditorWorkArea();
            }
        });
        return editor;
    }

    private void openEditorWorkArea(){
        DrugEditPane editor = makeEditorPane();
        WorkArea wa = new WorkArea(width, "薬剤の編集");
        wa.setComponent(editor);
        Container wrapper = disp.getParent();
        wrapper.add(wa, new FixedWidthLayout.Replace(disp));
        kind = Kind.EDIT;
        editorArea = wa;
        wrapper.revalidate();
        wrapper.repaint();
    }

    private void closeEditorWorkArea(){
        Container wrapper = editorArea.getParent();
        if( wrapper != null ) {
            wrapper.add(this.disp, new FixedWidthLayout.Replace(editorArea));
            kind = Kind.DISP;
            editorArea = null;
            wrapper.revalidate();
            wrapper.repaint();
        }
    }
}
