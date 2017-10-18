package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseEvent;

class DrugMenu extends JPanel {

    interface Callback extends SubMenuPane.Callback {
        default void onNewDrug(){}
        default void onSubMenu(MouseEvent event, JComponent triggerComponent){}
    }

    private Callback callback = new Callback(){};

    DrugMenu(){
        setLayout(new MigLayout("insets 0", "", ""));
        Link mainMenuLink = new Link("[処方]");
        mainMenuLink.setCallback(event -> callback.onNewDrug());
        Link subMenuLink = new Link("[+]");
        subMenuLink.setCallback(event -> {
            callback.onSubMenu(event, subMenuLink);
        });
        add(mainMenuLink);
        add(subMenuLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

}
