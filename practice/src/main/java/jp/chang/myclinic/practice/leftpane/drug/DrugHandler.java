package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import java.awt.*;

public class DrugHandler {

    private int width;
    private Container wrapper;
    private WorkArea workarea;

    public DrugHandler(int width, Container wrapper){
        this.width = width;
        this.wrapper = wrapper;
    }

    public void setup(Container container){
        DrugMenu drugMenu = new DrugMenu();
        drugMenu.setCallback(new DrugMenu.Callback(){
            @Override
            public void onNewDrug() {
                if( workarea != null ){

                } else {
                    workarea = makeDrugNewWorkArea();
                    System.out.println(container.getWidth());
                    container.add(workarea, new FixedWidthLayout.After(drugMenu));
                    workarea.repaint();
                    workarea.revalidate();
                }
            }
        });
        container.add(drugMenu);
    }

    private WorkArea makeDrugNewWorkArea(){
        WorkArea wa = new WorkArea(width, "新規処方の入力");
        return wa;
    }
}
