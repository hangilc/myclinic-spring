package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class ConductBox extends JPanel {

    private int width;
    Link menuLink;
    private WorkArea menuWorkArea;

    public ConductBox(int width, List<ConductFullDTO> conducts, VisitDTO visit){
        this.width = width;
        setLayout(new FixedWidthLayout(width));
        menuLink = new Link("[処置]");
        menuLink.setCallback(event -> doMenu(menuLink, event));
        add(menuLink);
        conducts.forEach(this::append);
    }

    public void append(ConductFullDTO conductFull){
        ConductElement element = new ConductElement(width, conductFull);
        add(element.getComponent());
    }

    private void doMenu(Component invoker, MouseEvent event){
        if( menuWorkArea != null ){
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        {
            JMenuItem item = new JMenuItem("Ｘ線検査追加");
            item.addActionListener(ev -> doEnterXp());
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("注射追加");
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("全部コピー");
            popup.add(item);
        }
        popup.show(invoker, event.getX(), event.getY());
    }

    private void doEnterXp(){
        WorkArea wa = new WorkArea(width, "Ｘ線入力");
        EnterXpForm form = new EnterXpForm();
        form.setCallback(new EnterXpForm.Callback() {
            @Override
            public void onEnter() {
                String label = form.getLabel();
                String film = form.getFilm();
                System.out.println(label + " " + film);
            }

            @Override
            public void onCancel() {
                closeWorkArea();
            }
        });
        wa.setComponent(form);
        openWorkArea(wa);
    }

    private void openWorkArea(WorkArea wa){
        add(wa, new FixedWidthLayout.After(menuLink));
        menuWorkArea = wa;
        revalidate();
        repaint();
    }

    private void closeWorkArea(){
        remove(menuWorkArea);
        menuWorkArea = null;
        revalidate();
        repaint();
    }
}
