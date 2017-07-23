package jp.chang.myclinic.intraclinic;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NewPostDialog extends JDialog {

    interface Callback {
        void onPost(int postId);
    }

    private Callback callback;

    NewPostDialog(Callback callback){
        setTitle("新規投稿");
        this.callback = callback;
        setLayout(new MigLayout("fill", "", ""));
        JTextArea textarea = new JTextArea(10, 20);
        add(textarea, "grow, wrap");
        JButton enterButton = new JButton("投稿");
        JButton cancelButton = new JButton("キャンセル");
        add(enterButton, "split 2, sizegroup btn");
        add(cancelButton, "sizegroup btn");
        pack();
    }
}
