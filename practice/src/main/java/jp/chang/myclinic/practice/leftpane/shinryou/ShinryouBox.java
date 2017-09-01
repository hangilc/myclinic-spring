package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ShinryouBox extends JPanel {

    private int width;
    private VisitDTO visit;
    private ShinryouMenu menu;
    private WorkArea workarea;

    public ShinryouBox(int width, List<ShinryouFullDTO> shinryouList,VisitDTO visit){
        this.width = width;
        this.visit = visit;
        setLayout(new FixedWidthLayout(width));
        this.menu = new ShinryouMenu();
        add(menu);
        shinryouList.forEach(this::append);
    }

    private ShinryouMenu makeMenu(){
        ShinryouMenu m = new ShinryouMenu();
        m.setCallback(new ShinryouMenu.Callback(){
            @Override
            public void onMainMenu() {
                if( workarea != null ){
                    return;
                }
                setWorkArea(makeAddRegularWorkArea());
            }

            @Override
            public void onSubMenu(MouseEvent event) {
                doSubMenu();
            }
        });
        return m;
    }

     private WorkArea makeAddRegularWorkArea(){
        WorkArea wa = new WorkArea(width, "診療行為入力");
        return wa;
    }

    private void doSubMenu(){

    }

    private void setWorkArea(WorkArea wa){
        this.workarea = wa;
        add(wa, new FixedWidthLayout.After(menu));
        revalidate();
        repaint();
    }

    private void append(ShinryouFullDTO shinryouFull){
        ShinryouElement element = new ShinryouElement(width, shinryouFull, visit);
        add(element.getComponent());
    }
}
