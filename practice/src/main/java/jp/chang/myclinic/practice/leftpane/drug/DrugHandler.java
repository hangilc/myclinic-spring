package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.leftpane.LeftPaneContext;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DrugHandler {

    private int width;
    private Container wrapper;
    private VisitDTO visit;
    private DrugMenu drugMenu;
    private List<DrugElement> elements = new ArrayList<>();
    private WorkArea workarea;

    public DrugHandler(int width, Container wrapper, VisitDTO visit){
        this.width = width;
        this.wrapper = wrapper;
        this.visit = visit;
    }

    public void setup(List<DrugFullDTO> drugs){
        wrapper.add(makeDrugMenu());
        int index = 1;
        for(DrugFullDTO drug: drugs){
            DrugElement element = new DrugElement(index, drug, width);
            wrapper.add(element.getDisp());
            elements.add(element);
            index += 1;
        }
    }

    private void appendDrug(DrugFullDTO drug){
        int index = elements.size() + 1;
        DrugElement element = new DrugElement(index, drug, width);
        if( elements.size() == 0 ){
            wrapper.add(element.getDisp(), new FixedWidthLayout.After(drugMenu));
        } else {
            Component anchor = elements.get(elements.size()-1).getComponent();
            wrapper.add(element.getDisp(), new FixedWidthLayout.After(anchor));
        }
        elements.add(element);
    }

    public void appendDrugs(List<DrugFullDTO> drugs){
        drugs.forEach(this::appendDrug);
        wrapper.revalidate();
        wrapper.repaint();
    }

    private DrugMenu makeDrugMenu(){
        drugMenu = new DrugMenu();
        drugMenu.setCallback(new DrugMenu.Callback(){
            @Override
            public void onNewDrug() {
                doNewDrug(drugMenu);
            }

            @Override
            public void onSubMenu(MouseEvent event, JComponent triggerComponent) {
                doSubMenu(event, triggerComponent);
            }
        });
        return drugMenu;
    }

    private void doNewDrug(DrugMenu drugMenu){
        if( workarea != null ){
            Component comp = workarea.getComponent();
            if( comp instanceof DrugNewPane ){
                closeWorkArea();
            }
        } else {
            workarea = makeDrugNewWorkArea();
            wrapper.add(workarea, new FixedWidthLayout.After(drugMenu));
            workarea.repaint();
            workarea.revalidate();
        }
    }

    private void doSubMenu(MouseEvent event, JComponent triggerComponent){
        if( workarea != null ){
            return;
        }
        MainContext mainContext = MainContext.get(wrapper);
        int currentVisitId = mainContext.getCurrentVisitId();
        int tempVisitId = mainContext.getTempVisitId();
        SubMenuPane submenu = new SubMenuPane(visit, currentVisitId, tempVisitId);
        submenu.setCallback(new SubMenuPane.Callback(){
            @Override
            public void onCopyAll(int targetVisitId, List<DrugFullDTO> enteredDrugs) {
                LeftPaneContext.get(wrapper).ifPresent(ctx -> ctx.onDrugsEntered(targetVisitId, enteredDrugs));
            }

            @Override
            public void onCopySome(int targetVisitId) {
                //handleCopySome(targetVisitId, visit);
            }

            @Override
            public void onModifyDays() {
                //handleModifyDays(visit.visitId);
            }

            @Override
            public void onDeleteSome() {
                //handleDeleteSelected(visit.visitId);
            }
        });
        submenu.show(triggerComponent, event.getX(), event.getY());

    }

    private WorkArea makeDrugNewWorkArea(){
        WorkArea wa = new WorkArea(width, "新規処方の入力");
        DrugNewPane drugNewPane = new DrugNewPane(visit);
        drugNewPane.setCallback(new DrugNewPane.Callback(){
            @Override
            public void onEnter(DrugFullDTO enteredDrug) {
                // TODO: add drug
            }

            @Override
            public void onClose() {
                closeWorkArea();
            }
        });
        wa.setComponent(drugNewPane);
        return wa;
    }

    private void closeWorkArea(){
        wrapper.remove(workarea);
        wrapper.revalidate();
        wrapper.repaint();
        workarea = null;
    }
}
