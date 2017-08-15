package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

class DrugMenu extends JPanel {

    interface Callback {
        default void onNewDrug(DrugDTO drug){ throw new RuntimeException("not implemented"); }
    }

    private JComponent subMenuPane;
    private JComponent workPane;
    private Callback callback = new Callback(){};

    DrugMenu(VisitDTO visit){
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        Link mainMenuLink = new Link("[処方]");
        mainMenuLink.setCallback(event -> doNewDrug(visit));
        Link subMenuLink = new Link("[+]");
        subMenuLink.setCallback(this::doSubMenuClick);
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

    private void doSubMenuClick(MouseEvent event){
        if( workPane != null ){
            return;
        }
        SubMenuPane submenu = new SubMenuPane();
        submenu.show(this, event.getX(), event.getY());
    }

    private void doEnterNewDrug(DrugDTO drug, DrugNew drugNewPane){
        Service.api.enterDrug(drug)
                .thenAccept(newDrug -> {
                    callback.onNewDrug(newDrug);
                    drugNewPane.clear();
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });

    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
