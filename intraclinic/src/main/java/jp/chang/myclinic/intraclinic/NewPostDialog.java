package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDate;

class NewPostDialog extends JDialog {

    interface Callback {
        void onPost(int postId);
    }

    private Callback callback;
    private JTextArea textarea = new JTextArea(10, 30);

    NewPostDialog(Window owner, Callback callback){
        super(owner);
        setTitle("新規投稿");
        this.callback = callback;
        setLayout(new MigLayout("fill", "", ""));
        textarea.setLineWrap(true);
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
                        return;
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
