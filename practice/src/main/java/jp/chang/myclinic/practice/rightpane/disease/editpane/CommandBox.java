package jp.chang.myclinic.practice.rightpane.disease.editpane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class CommandBox extends JPanel {

    interface Callback {
        default void onEnter(){}
        default void onDelAdj(){}
        default void onDelete(){}
    }

    private Callback callback = new Callback(){};

    CommandBox(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton enterButton = new JButton("入力");
        enterButton.addActionListener(evt -> callback.onEnter());
        Link delAdjLink = new Link("修飾語削除");
        delAdjLink.setCallback(evt -> callback.onDelAdj());
        Link delLink = new Link("削除");
        delLink.setCallback(evt -> {
            if( confirm("この病名を削除していいですか？") ){
                callback.onDelete();
            }
        });
        add(enterButton);
        add(delAdjLink);
        add(new JLabel("|"));
        add(delLink);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

}
