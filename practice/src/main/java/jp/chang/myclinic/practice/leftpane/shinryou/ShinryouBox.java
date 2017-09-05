package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.RecordContext;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ShinryouBox extends JPanel {

    private int width;
    private VisitDTO visit;
    private ShinryouMenu menu;
    private List<ShinryouElement> elements = new ArrayList<>();
    private WorkArea workarea;

    public ShinryouBox(int width, List<ShinryouFullDTO> shinryouList,VisitDTO visit){
        this.width = width;
        this.visit = visit;
        setLayout(new FixedWidthLayout(width));
        this.menu = makeMenu();
        add(menu);
        shinryouList.forEach(this::append);
    }

    private ShinryouMenu makeMenu(){
        ShinryouMenu m = new ShinryouMenu();
        m.setCallback(new ShinryouMenu.Callback(){
            @Override
            public void onMainMenu() {
                if( workarea != null ){
                    if( workarea.getComponent() instanceof AddRegularPane ){
                        closeWorkArea();
                    }
                    return;
                }
                setWorkArea(makeAddRegularWorkArea());
            }

            @Override
            public void onSubMenu(Component invoker, MouseEvent event) {
                doSubMenu(invoker, event);
            }
        });
        return m;
    }

    private static class BatchEnterStore {
        List<Integer> conductIds;
        List<ShinryouFullDTO> shinryouList;
    }

     private WorkArea makeAddRegularWorkArea(){
        JPanel self = this;
        WorkArea wa = new WorkArea(width, "診療行為入力");
        AddRegularPane pane = new AddRegularPane();
        pane.setCallback(new AddRegularPane.Callback() {
            @Override
            public void onEnter(List<String> names) {
                BatchEnterStore store = new BatchEnterStore();
                Service.api.batchEnterShinryouByName(names, visit.visitId)
                        .thenCompose(result -> {
                            store.conductIds = result.conductIds;
                            return Service.api.listShinryouFullByIds(result.shinryouIds);
                        })
                        .thenCompose(shinryouList -> {
                            store.shinryouList = shinryouList;
                            if( store.conductIds == null || store.conductIds.size() == 0 ){
                                return CompletableFuture.completedFuture(null);
                            } else {
                                return Service.api.listConductFullByIds(store.conductIds);
                            }
                        })
                        .thenAccept(conducts -> EventQueue.invokeLater(() -> {
                            store.shinryouList.forEach(shinryouFull -> append(shinryouFull));
                            if( conducts != null ){
                                RecordContext.get(self).ifPresent(ctx -> ctx.onConductsEntered(conducts));
                            }
                            reorder();
                            closeWorkArea();
                            revalidate();
                            repaint();
                        }))
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
        wa.setComponent(pane);
        return wa;
    }

    private void doSubMenu(Component invoker, MouseEvent mouseEvent) {
        if (workarea != null) {
            return;
        }
        JPopupMenu popup = new JPopupMenu();
        {
            JMenuItem item = new JMenuItem("検査");
            item.addActionListener(event -> {
                setWorkArea(makeKensaWorkArea());
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("診療行為検索");
            item.addActionListener(event -> {
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("全部コピー");
            item.addActionListener(event -> {
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("選択コピー");
            item.addActionListener(event -> {
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("複数削除");
            item.addActionListener(event -> {
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("重複削除");
            item.addActionListener(event -> {
            });
            popup.add(item);
        }
        popup.show(invoker, mouseEvent.getX(), mouseEvent.getY());
    }

    private WorkArea makeKensaWorkArea(){
        WorkArea wa = new WorkArea(width, "検査の入力");
        KensaPane kensaPane = new KensaPane();
        kensaPane.setSize(wa.getInnerColumnWidth(), Integer.MAX_VALUE);
        kensaPane.setCallback(new KensaPane.Callback(){
            @Override
            public void onCancel() {
                closeWorkArea();
            }
        });
        wa.setComponent(kensaPane);
        return wa;
    }

    private void setWorkArea(WorkArea wa){
        this.workarea = wa;
        add(wa, new FixedWidthLayout.After(menu));
        revalidate();
        repaint();
    }

    private void closeWorkArea(){
        remove(workarea);
        this.workarea = null;
        revalidate();
        repaint();
    }

    private void append(ShinryouFullDTO shinryouFull){
        ShinryouElement element = new ShinryouElement(width, shinryouFull, visit);
        add(element.getComponent());
        elements.add(element);
    }

    private void reorder(){
        elements.forEach(element -> remove(element.getComponent()));
        elements.sort(Comparator.comparingInt(ShinryouElement::getShinryoucode));
        elements.forEach(element -> add(element.getComponent()));
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
