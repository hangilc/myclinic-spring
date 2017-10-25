package jp.chang.myclinic.drawer.preview;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class CommandBox extends JPanel {

    interface Callback {
        default void onPrint(){}
    }

    private Callback callback = new Callback(){};

    CommandBox(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton printButton = new JButton("印刷");
        printButton.addActionListener(evt -> callback.onPrint());
        add(printButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

}
