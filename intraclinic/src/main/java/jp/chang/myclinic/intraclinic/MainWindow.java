package jp.chang.myclinic.intraclinic;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MainWindow extends JFrame {

    MainWindow(boolean isAdmin, String name){
        super("院内ミーティング");
        setLayout(new MigLayout("", "", ""));
        pack();
    }
}
