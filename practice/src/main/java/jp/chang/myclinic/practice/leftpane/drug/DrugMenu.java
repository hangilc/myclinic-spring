package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseEvent;

class DrugMenu extends JPanel {

    private int patientId;
    private String at;
    private JComponent subMenuPane;
    private JComponent workPane;

    DrugMenu(int patientId, String at){
        this.patientId = patientId;
        this.at = at;
        setLayout(new MigLayout("insets 0", "[grow]", ""));
        Link mainMenuLink = new Link("[処方]");
        mainMenuLink.setCallback(event -> doNewDrug());
        Link subMenuLink = new Link("[+]");
        subMenuLink.setCallback(this::doSubMenuClick);
        add(mainMenuLink, "span, split 2");
        add(subMenuLink);
    }

    private void doNewDrug(){
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
        DrugNew drugNew = new DrugNew(patientId, at);
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

}
