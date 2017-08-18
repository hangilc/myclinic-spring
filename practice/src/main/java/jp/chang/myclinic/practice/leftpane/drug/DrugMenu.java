package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.leftpane.WorkArea;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

class DrugMenu extends JPanel {

    interface Callback {
        default void onNewDrug(DrugDTO drug){ throw new RuntimeException("not implemented"); }
        default void onDrugsCopied(int targetVisitId, List<Integer> drugIds){ throw new RuntimeException("not implemented"); };
    }

    private JComponent subMenuPane;
    private JComponent workPane;
    private Callback callback = new Callback(){};

    DrugMenu(VisitDTO visit, int currentVisitId, int tempVisitId){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        Link mainMenuLink = new Link("[処方]");
        mainMenuLink.setCallback(event -> doNewDrug(visit));
        Link subMenuLink = new Link("[+]");
        subMenuLink.setCallback(event -> doSubMenuClick(event, visit, currentVisitId, tempVisitId));
        add(mainMenuLink, "span, split 2");
        add(subMenuLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doNewDrug(VisitDTO visit){
        if( subMenuPane != null ){
            return;
        }
        if( workPane != null ){
            if( workPane instanceof DrugNew ){
                remove(workPane);
                workPane = null;
                repaint();
                revalidate();
            }
            return;
        }
        DrugNew drugNew = new DrugNew(visit);
        drugNew.setCallback(new DrugNew.Callback(){
            @Override
            public void onEnter(DrugDTO drug) {
                doEnterNewDrug(drug, drugNew);
            }

            @Override
            public void onClose() {
                remove(workPane);
                workPane = null;
                repaint();
                revalidate();
            }
        });
        workPane = drugNew;
        add(drugNew, "newline, growx");
        repaint();
        revalidate();
    }

    private void doSubMenuClick(MouseEvent event, VisitDTO visit, int currentVisitId, int tempVisitId){
        if( workPane != null ){
            return;
        }
        SubMenuPane submenu = new SubMenuPane(visit, currentVisitId, tempVisitId, new SubMenuPane.Callback(){
            @Override
            public void onCopyAll(int targetVisitId, List<Integer> enteredDrugIds) {
                callback.onDrugsCopied(targetVisitId, enteredDrugIds);
            }

            @Override
            public void onCopySome(int targetVisitId) {
                handleCopySome(targetVisitId, visit);
            }
        });
        submenu.show(this, event.getX(), event.getY());
    }

    private void doEnterNewDrug(DrugDTO drug, DrugNew drugNewPane){
        Service.api.enterDrug(drug)
                .thenAccept(newDrug -> EventQueue.invokeLater(() ->{
                    callback.onNewDrug(newDrug);
                    drugNewPane.clear();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void handleCopySome(int targetVisitId, VisitDTO visit){
        if( workPane != null ){
            throw new RuntimeException("cannot happen");
        }
        Service.api.listDrugFull(visit.visitId)
                .thenAccept(candidateDrugs -> EventQueue.invokeLater(() -> {
                    CopySomePane copySomePane = new CopySomePane(candidateDrugs);
                    copySomePane.setCallback(new CopySomePane.Callback() {
                        @Override
                        public void onEnter(List<DrugFullDTO> selected) {
                            DrugLib.copyDrugs(targetVisitId, selected)
                                    .thenAccept(drugIds -> EventQueue.invokeLater(() -> {
                                        closeWorkArea();
                                        callback.onDrugsCopied(targetVisitId, drugIds);
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
                    workPane = new WorkArea("薬剤の選択コピー", copySomePane);
                    add(workPane, "newline, growx");
                    repaint();
                    revalidate();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void closeWorkArea(){
        remove(workPane);
        repaint();
        revalidate();
        workPane = null;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
