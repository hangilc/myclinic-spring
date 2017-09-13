package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
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
        menuLink.setCallback(event -> doMenu(menuLink, event, visit));
        add(menuLink);
        conducts.forEach(this::append);
    }

    public void append(ConductFullDTO conductFull){
        ConductElement element = new ConductElement(width, conductFull);
        add(element.getComponent());
    }

    private void doMenu(Component invoker, MouseEvent event, VisitDTO visit){
        if( menuWorkArea != null ){
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        {
            JMenuItem item = new JMenuItem("Ｘ線検査追加");
            item.addActionListener(ev -> doEnterXp(visit.visitId));
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("注射追加");
            item.addActionListener(ev -> doEnterInject(visit.visitId, visit.visitedAt));
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("全部コピー");
            popup.add(item);
        }
        popup.show(invoker, event.getX(), event.getY());
    }

    private void doEnterXp(int visitId){
        WorkArea wa = new WorkArea(width, "Ｘ線入力");
        EnterXpForm form = new EnterXpForm();
        form.setCallback(new EnterXpForm.Callback() {
            @Override
            public void onEnter() {
                String label = form.getLabel();
                String film = form.getFilm();
                Service.api.enterXp(visitId, label, film)
                        .thenCompose(conductId -> Service.api.getConductFull(conductId))
                        .thenAccept(conductFull -> {
                            append(conductFull);
                            closeWorkArea();
                            revalidate();
                            repaint();
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            }

            @Override
            public void onCancel() {
                closeWorkArea();
            }
        });
        wa.setComponent(form);
        openWorkArea(wa);
    }

    private void doEnterInject(int visitId, String visitedAt){
        WorkArea wa = new WorkArea(width, "処置注射入力");
        EnterInjectForm form = new EnterInjectForm(wa.getInnerColumnWidth(), visitedAt.substring(0, 10));
        form.setCallback(new EnterInjectForm.Callback() {
            @Override
            public void onEnter(IyakuhinMasterDTO master, double amount, String shinryouName) {
                Service.api.enterInject(visitId, master.iyakuhincode, amount, shinryouName)
                        .thenCompose(conductId -> Service.api.getConductFull(conductId))
                        .thenAccept(conductFull -> {
                            append(conductFull);
                            closeWorkArea();
                            revalidate();
                            repaint();
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
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

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
