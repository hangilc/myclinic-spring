package jp.chang.myclinic.practice.leftpane.drug;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DrugMenu extends JPanel {

    private JComponent subMenuPane;
    private JComponent workPane;

    DrugMenu(){
        setLayout(new MigLayout("insets 0", "[]", ""));
        Link mainMenuLink = new Link("[処方]");
        Link subMenuLink = new Link("[+]");
        subMenuLink.setCallback(this::doSubMenuClick);
        add(mainMenuLink);
        add(subMenuLink);
    }

    private void doSubMenuClick(){
        if( workPane != null ){
            return;
        }
        SubMenuPane submenu = new SubMenuPane();
        submenu.show(this, x, y);
    }

}
