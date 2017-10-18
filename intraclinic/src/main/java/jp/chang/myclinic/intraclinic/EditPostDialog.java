package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

class EditPostDialog extends JDialog {

    interface Callback {
        void onUpdate();
    }

    private JTextArea textarea = new JTextArea(10, 30);
    private IntraclinicPostDTO post;
    private Callback callback;

    EditPostDialog(Window owner, IntraclinicPostDTO post, Callback callback){
        super(owner, "投稿の編集", ModalityType.MODELESS);
        this.post = post;
        this.callback = callback;
        setLayout(new MigLayout("fill", "", ""));
        textarea.setLineWrap(true);
        textarea.setText(post.content);
        {
            JPopupMenu popup = new JPopupMenu();
            {
                JMenuItem item = new JMenuItem("コピー");
                item.addActionListener(event -> {
                    String sel = textarea.getSelectedText();
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(sel), null);
                });
                popup.add(item);
            }
            {
                JMenuItem item = new JMenuItem("貼付け");
                item.addActionListener(event -> {
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    try {
                        String text = (String) clipboard.getData(DataFlavor.stringFlavor);
                        textarea.insert(text, textarea.getCaretPosition());
                    } catch(Exception ex){
                        ex.printStackTrace();
                        alert(ex.toString());
                    }
                });
                popup.add(item);
            }
            textarea.setComponentPopupMenu(popup);
        }
        JScrollPane sp = new JScrollPane(textarea);
        add(sp, "grow, wrap");
        JButton enterButton = new JButton("投稿");
        enterButton.addActionListener(event -> doEnter());
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> doCancel());
        add(enterButton, "split 2, sizegroup btn");
        add(cancelButton, "sizegroup btn");
        pack();
    }

    private void doEnter(){
        IntraclinicPostDTO newPost = new IntraclinicPostDTO();
        newPost.id = post.id;
        newPost.content = textarea.getText();
        newPost.createdAt = post.createdAt;
        Service.api.updatePost(newPost)
                .thenAccept(result -> EventQueue.invokeLater(() -> {
                    dispose();
                    callback.onUpdate();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void doCancel(){
        dispose();
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
