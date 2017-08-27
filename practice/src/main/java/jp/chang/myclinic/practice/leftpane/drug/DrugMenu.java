package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugMenu extends JPanel {

    interface Callback {
        void onNewDrug();
    }

    DrugMenu(Callback callback){
        setLayout(new MigLayout("insets 0", "", ""));
        Link mainMenuLink = new Link("[処方]");
        mainMenuLink.setCallback(event -> callback.onNewDrug());
        Link subMenuLink = new Link("[+]");
        //subMenuLink.setCallback(event -> doSubMenuClick(event, visit, currentVisitId, tempVisitId));
        add(mainMenuLink);
        add(subMenuLink);
    }
}
