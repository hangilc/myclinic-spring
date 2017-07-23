package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

class NewPostDialog extends JDialog {

    interface Callback {
        void onPost(int postId);
    }

    private Callback callback;
    private JTextArea textarea = new JTextArea(10, 20);

    NewPostDialog(Callback callback){
        setTitle("新規投稿");
        this.callback = callback;
        setLayout(new MigLayout("fill", "", ""));
        add(textarea, "grow, wrap");
        JButton enterButton = new JButton("投稿");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        add(enterButton, "split 2, sizegroup btn");
        add(cancelButton, "sizegroup btn");
        pack();
    }

    private void doEnter(){
        String content = textarea.getText();
        if( content.isEmpty() ){
            return;
        }
        IntraclinicPostDTO post = new IntraclinicPostDTO();
        post.content = content;
        post.createdAt = LocalDate.now().toString();
        Service.api.enterPost(post)
                .thenAccept(postId -> {
                    EventQueue.invokeLater(() -> {
                        dispose();
                        callback.onPost(postId);
                    });
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    alert(t.toString());
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
