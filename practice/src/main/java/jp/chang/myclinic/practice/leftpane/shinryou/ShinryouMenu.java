package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

class ShinryouMenu extends JPanel {

    interface Callback {
        default void onMainMenu(){}
        default void onSubMenu(Component invoker, MouseEvent event){}
    }

    private Callback callback = new Callback(){};

    ShinryouMenu(){
        setLayout(new MigLayout("insets 0", "", ""));
        Link shohouLink = new Link("[診療行為]");
        shohouLink.setCallback(event -> callback.onMainMenu());
        Link auxLink = new Link("[+]");
        auxLink.setCallback(event -> callback.onSubMenu(auxLink, event));
        add(shohouLink);
        add(auxLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }
}
