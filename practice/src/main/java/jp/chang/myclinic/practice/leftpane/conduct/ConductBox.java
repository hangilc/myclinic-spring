package jp.chang.myclinic.practice.leftpane.conduct;

import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.LeftPaneContext;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ConductBox extends JPanel implements ConductBoxContext {

    private int width;
    Link menuLink;
    private WorkArea menuWorkArea;
    private List<ConductElement> elements = new ArrayList<>();

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
        elements.add(element);
    }

    public void appendConduct(List<ConductFullDTO> entered) {
        entered.forEach(this::append);
        revalidate();
        repaint();
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
            item.addActionListener(ev -> {
                MainContext mainContext = MainContext.get(this);
                int targetVisitId = mainContext.getTargetVisitId();
                if( targetVisitId > 0 ){
                    doCopyAll(targetVisitId, visit.visitId);
                } else {
                    alert("コピー先が設定されていません。");
                }
            });
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
                int conductKindCode;
                if( "皮下筋注".equals(shinryouName) ){
                    conductKindCode = ConductKind.HikaChuusha.getCode();
                } else if( "静注".equals(shinryouName) ){
                    conductKindCode = ConductKind.JoumyakuChuusha.getCode();
                } else {
                    throw new RuntimeException("cannot find conduct kind for " + shinryouName);
                }
                Service.api.enterInject(visitId, conductKindCode, master.iyakuhincode, amount)
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

    private void doCopyAll(int targetVisitId, int sourceVisitId){
        Service.api.copyAllConducts(targetVisitId, sourceVisitId)
                .thenCompose(conductIds -> Service.api.listConductFullByIds(conductIds))
                .thenAccept(newConducts -> EventQueue.invokeLater(() ->{
                    LeftPaneContext.get(this).onConductEntered(targetVisitId, newConducts, () -> {});
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
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

    private ConductElement getElement(int conductId){
        for(ConductElement element: elements){
            if( element.getConductId() == conductId ){
                return element;
            }
        }
        throw new RuntimeException("cannot find element");
    }

    @Override
    public void onDelete(int conductId) {
        ConductElement element = getElement(conductId);
        remove(element.getComponent());
        revalidate();
        repaint();
        elements.remove(element);
    }
}
