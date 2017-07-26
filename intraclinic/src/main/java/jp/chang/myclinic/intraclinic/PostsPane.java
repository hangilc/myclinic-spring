package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicCommentDTO;
import jp.chang.myclinic.dto.IntraclinicPostDTO;
import jp.chang.myclinic.dto.IntraclinicPostFullDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDate;
import java.util.List;

class PostsPane extends JPanel {

    interface Callback {
        void onRendered();
    }

    private int width;
    private boolean isAdmin;
    private String today = LocalDate.now().toString();
    private Callback callback;

    PostsPane(List<IntraclinicPostFullDTO> fullPosts, boolean isAdmin, Callback callback){
        this.isAdmin = isAdmin;
        this.callback = callback;
        setLayout(new MigLayout("insets 0, fill", "", ""));
        renderPosts(fullPosts);
    }

    void updatePosts(List<IntraclinicPostFullDTO> fullPosts){
        removeAll();
        renderPosts(fullPosts);
        repaint();
        revalidate();
    }

    private void renderPosts(List<IntraclinicPostFullDTO> fullPosts){
        for(IntraclinicPostFullDTO fullPost: fullPosts){
            add(makeUnit(fullPost), "growx, wrap");
        }
        callback.onRendered();
    }

    private JComponent makeUnit(IntraclinicPostFullDTO fullPost){
        JPanel panel = new JPanel(new MigLayout("insets 0, fill", "", ""));
        panel.setBorder(null);
        panel.add(makeUnitContent(fullPost, panel), "growx");
        return panel;
    }

    private JComponent makeUnitContent(IntraclinicPostFullDTO fullPost, Container wrapper) {
        JPanel panel = new JPanel(new MigLayout("insets 0, fill", "", ""));
        JLabel title = new JLabel(DateTimeUtil.sqlDateToKanjiWithYoubi(fullPost.post.createdAt));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setBackground(new Color(0xdd, 0xdd, 0xdd));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        panel.add(title, "growx, wrap");
        JEditorPane wt = new JEditorPane();
        wt.setBackground(getBackground());
        wt.setText(fullPost.post.content);
        {
            JPopupMenu popup = new JPopupMenu();
            JMenuItem item = new JMenuItem("コピー");
            item.addActionListener(event -> {
                String sel = wt.getSelectedText();
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(sel), null);
            });
            popup.add(item);
            wt.setComponentPopupMenu(popup);
        }
        panel.add(wt, "growx, wrap");
        boolean isToday = today.equals(fullPost.post.createdAt);
        if( isAdmin && isToday ){
            JButton editButton = new JButton("編集");
            editButton.addActionListener(event -> doEdit(fullPost.post, wrapper));
            panel.add(editButton, "wrap");
        }
        panel.add(makeCommentsBox(fullPost.comments, isToday), "growx");
        return panel;
    }

    private JComponent makeCommentsBox(List<IntraclinicCommentDTO> comments, boolean isToday){
        JPanel panel = new JPanel(new MigLayout("fill, gapy 0", "", ""));
        panel.setBorder(BorderFactory.createTitledBorder("コメント"));
        int ncom = comments.size();
        for(int i=0;i<ncom;i++){
            IntraclinicCommentDTO c = comments.get(i);
            String text = c.name + "：" + c.content;
            JEditorPane wt = new JEditorPane();
            wt.setText(text);
            wt.setBackground(getBackground());
            wt.setBorder(null);
            {
                JPopupMenu popup = new JPopupMenu();
                JMenuItem item = new JMenuItem("コピー");
                item.addActionListener(event -> {
                    String sel = wt.getSelectedText();
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(sel), null);
                });
                popup.add(item);
                wt.setComponentPopupMenu(popup);
            }
            panel.add(wt, i == (ncom - 1) ? "" : "wrap");
        }
        // TODO: set default text for comment input
        if( isToday ){
            JTextField textField = new JTextField();
            panel.add(textField, "newline, split 2, growx");
            JButton submitButton = new JButton("投稿");
        }
        return panel;
    }

    private void doEdit(IntraclinicPostDTO post, Container wrapper){
        EditPostDialog dialog = new EditPostDialog(SwingUtilities.getWindowAncestor(this), post, new EditPostDialog.Callback(){
            @Override
            public void onUpdate(){
                Service.api.getPost(post.id)
                        .thenAccept(postFull -> {
                            EventQueue.invokeLater(() -> {
                                wrapper.removeAll();
                                wrapper.add(makeUnitContent(postFull, wrapper), "growx");
                                wrapper.repaint();
                                wrapper.revalidate();
                            });
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            alert(t.toString());
                            return null;
                        });
            }
        });
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(null, message);
    }
}
