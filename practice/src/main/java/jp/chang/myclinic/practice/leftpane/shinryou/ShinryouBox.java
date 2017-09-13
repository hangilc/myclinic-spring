package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.FixedWidthLayout;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.LeftPaneContext;
import jp.chang.myclinic.practice.leftpane.RecordContext;
import jp.chang.myclinic.practice.leftpane.WorkArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ShinryouBox extends JPanel {

    private int width;
    private VisitDTO visit;
    private ShinryouMenu menu;
    private List<ShinryouElement> elements = new ArrayList<>();
    private WorkArea workarea;

    public ShinryouBox(int width, List<ShinryouFullDTO> shinryouList, VisitDTO visit) {
        this.width = width;
        this.visit = visit;
        setLayout(new FixedWidthLayout(width));
        this.menu = makeMenu();
        add(menu);
        shinryouList.forEach(this::append);
    }

    private ShinryouMenu makeMenu() {
        ShinryouMenu m = new ShinryouMenu();
        m.setCallback(new ShinryouMenu.Callback() {
            @Override
            public void onMainMenu() {
                if (workarea != null) {
                    if (workarea.getComponent() instanceof AddRegularPane) {
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

    private WorkArea makeAddRegularWorkArea() {
        JPanel self = this;
        WorkArea wa = new WorkArea(width, "診療行為入力");
        AddRegularPane pane = new AddRegularPane();
        pane.setCallback(new AddRegularPane.Callback() {
            @Override
            public void onEnter(List<String> names) {
                batchEnterShinryouByNames(names, visit.visitId, () -> closeWorkArea());
            }

            @Override
            public void onCancel() {
                closeWorkArea();
            }
        });
        wa.setComponent(pane);
        return wa;
    }

    private void batchEnterShinryouByNames(List<String> names, int visitId, Runnable uiCallback){
        ShinryouLib.batchEnterShinryouByNames(names, visitId)
                .thenAccept(result -> EventQueue.invokeLater(() ->{
                    result.shinryouList.forEach(this::append);
                    if (result.conducts.size() != 0 ){
                        RecordContext.get(this).ifPresent(ctx -> ctx.onConductsEntered(result.conducts));
                    }
                    reorder();
                    revalidate();
                    repaint();
                    uiCallback.run();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
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
                setWorkArea(makeSearchAndAddWorkArea());
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("全部コピー");
            item.addActionListener(event -> doCopyAll());
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("選択コピー");
            item.addActionListener(event -> {
                int targetVisitId = MainContext.get(this).getTargetVisitId();
                if( targetVisitId == 0 ){
                    alert("コピー先診察がありません。");
                    return;
                }
                if( targetVisitId == visit.visitId ){
                    alert("同じ診察にはコピーできません。");
                    return;
                }
                setWorkArea(makeCopySomeWorkArea(targetVisitId));
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("複数削除");
            item.addActionListener(event -> {
                setWorkArea(makeDeleteSomeWorkArea());
            });
            popup.add(item);
        }
        {
            JMenuItem item = new JMenuItem("重複削除");
            item.addActionListener(event -> {
                Service.api.deleteDuplicateShinryou(visit.visitId)
                        .thenAccept(shinryouIds -> {
                            removeShinryou(shinryouIds);
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
            });
            popup.add(item);
        }
        popup.show(invoker, mouseEvent.getX(), mouseEvent.getY());
    }

    private WorkArea makeCopySomeWorkArea(int targetVisitId) {
        WorkArea wa = new WorkArea(width, "診療行為の複数コピー");
        List<ShinryouFullDTO> shinryouList = elements.stream().map(ShinryouElement::getShinryouFull).collect(Collectors.toList());
        CopySomePane pane = new CopySomePane(wa.getInnerColumnWidth(), shinryouList);
        ShinryouBox self = this;
        pane.setCallback(new CopySomePane.Callback() {
            @Override
            public void onCopy(List<ShinryouFullDTO> selected) {
                List<ShinryouDTO> srcList = selected.stream().map(s -> s.shinryou).collect(Collectors.toList());
                Service.api.batchCopyShinryou(targetVisitId, srcList)
                        .thenCompose(shinryouIds -> Service.api.listShinryouFullByIds(shinryouIds))
                        .thenAccept(copiedList -> EventQueue.invokeLater(() -> {
                            LeftPaneContext.get(self).onShinryouEntered(targetVisitId, copiedList, () -> closeWorkArea());
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

    private void doCopyAll() {
        int targetVisitId = MainContext.get(this).getTargetVisitId();
        if( targetVisitId == 0 ){
            alert("コピー先診察がありません。");
            return;
        }
        if( targetVisitId == visit.visitId ){
            alert("同じ診察にはコピーできません。");
            return;
        }
        List<ShinryouDTO> srcList = elements.stream().map(s -> s.getShinryou()).collect(Collectors.toList());
        Service.api.batchCopyShinryou(targetVisitId, srcList)
                .thenCompose(shinryouIds -> Service.api.listShinryouFullByIds(shinryouIds))
                .thenAccept(copiedList -> EventQueue.invokeLater(() -> {
                    LeftPaneContext.get(this).onShinryouEntered(targetVisitId, copiedList, () -> {});
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private WorkArea makeDeleteSomeWorkArea() {
        WorkArea wa = new WorkArea(width, "複数診療削除");
        List<ShinryouFullDTO> shinryouList = elements.stream().map(ShinryouElement::getShinryouFull).collect(Collectors.toList());
        DeleteSomePane pane = new DeleteSomePane(wa.getInnerColumnWidth(), shinryouList);
        pane.setCallback(new DeleteSomePane.Callback() {
            @Override
            public void onDelete(List<ShinryouFullDTO> shinryouList) {
                List<Integer> shinryouIds = shinryouList.stream().map(s -> s.shinryou.shinryouId)
                        .collect(Collectors.toList());
                Service.api.batchDeleteShinryou(shinryouIds)
                        .thenAccept(res -> EventQueue.invokeLater(() -> {
                            removeShinryou(shinryouIds);
                            closeWorkArea();
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

    private WorkArea makeSearchAndAddWorkArea() {
        WorkArea wa = new WorkArea(width, "診療行為検索");
        SearchAndAddPane pane = new SearchAndAddPane(wa.getInnerColumnWidth(), visit.visitedAt.substring(0, 10));
        pane.setCallback(new SearchAndAddPane.Callback() {
            @Override
            public void onEnter(ShinryouMasterDTO master) {
                ShinryouDTO shinryouDTO = new ShinryouDTO();
                shinryouDTO.visitId = visit.visitId;
                shinryouDTO.shinryoucode = master.shinryoucode;
                Service.api.enterShinryou(shinryouDTO)
                        .thenCompose(shinryouId -> Service.api.getShinryouFull(shinryouId))
                        .thenAccept(shinryouFull -> EventQueue.invokeLater(() -> {
                            append(shinryouFull);
                            closeWorkArea();
                            reorder();
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

    private WorkArea makeKensaWorkArea() {
        WorkArea wa = new WorkArea(width, "検査の入力");
        KensaPane kensaPane = new KensaPane();
        kensaPane.setSize(wa.getInnerColumnWidth(), Integer.MAX_VALUE);
        kensaPane.setCallback(new KensaPane.Callback() {
            @Override
            public void onEnter(List<String> names) {
                batchEnterShinryouByNames(names, visit.visitId, () -> closeWorkArea());
            }

            @Override
            public void onCancel() {
                closeWorkArea();
            }
        });
        wa.setComponent(kensaPane);
        return wa;
    }

    private void setWorkArea(WorkArea wa) {
        this.workarea = wa;
        add(wa, new FixedWidthLayout.After(menu));
        revalidate();
        repaint();
    }

    private void closeWorkArea() {
        remove(workarea);
        this.workarea = null;
        revalidate();
        repaint();
    }

    private void append(ShinryouFullDTO shinryouFull) {
        ShinryouElement element = new ShinryouElement(width, shinryouFull, visit);
        element.setCallback(new ShinryouElement.Callback(){
            @Override
            public void onDelete() {
                remove(element.getComponent());
                elements.remove(element);
                revalidate();
                repaint();
            }
        });
        add(element.getComponent());
        elements.add(element);
    }

    private void reorder() {
        elements.forEach(element -> remove(element.getComponent()));
        elements.sort(Comparator.comparingInt(ShinryouElement::getShinryoucode));
        elements.forEach(element -> add(element.getComponent()));
    }

    private void alert(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void appendShinryou(List<ShinryouFullDTO> entered) {
        entered.forEach(this::append);
        reorder();
        revalidate();
        repaint();
    }

    public void removeShinryou(List<Integer> shinryouIds){
        elements.removeIf(element -> {
            if( shinryouIds.contains(element.getShinryouId()) ){
                remove(element.getComponent());
                return true;
            } else {
                return false;
            }
        });
        revalidate();
        repaint();
    }
}
