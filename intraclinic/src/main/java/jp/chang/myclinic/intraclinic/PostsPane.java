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
    private String name;
    private boolean isAdmin;
    private String today = LocalDate.now().toString();
    private Callback callback;

    PostsPane(List<IntraclinicPostFullDTO> fullPosts, String name, boolean isAdmin, Callback callback){
        this.name = name;
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
        panel.add(makeCommentsBox(fullPost.comments, isToday, fullPost, wrapper), "growx");
        return panel;
    }

    private JComponent makeCommentsBox(List<IntraclinicCommentDTO> comments, boolean isToday,
                                       IntraclinicPostFullDTO postFull, Container wrapper){
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
        if( isToday ){
            JTextField textField = new JTextField();
            textField.setText(defaultCommentInput(comments));
            panel.add(textField, "newline, split 2, growx");
            JButton submitButton = new JButton("投稿");
            submitButton.addActionListener(event -> doEnterComment(postFull.post.id, textField.getText(), wrapper));
            panel.add(submitButton);
        }
        return panel;
    }

    private String defaultCommentInput(List<IntraclinicCommentDTO> comments){
        if( isAdmin ){
            return "";
        } else {
            String hasRead = "閲覧しました";
            for(IntraclinicCommentDTO comment: comments){
                if( name.equals(comment.name) && hasRead.equals(comment.content) ){
                    return "";
                }
            }
            return hasRead;
        }
    }

    private void doEnterComment(int postId, String content, Container wrapper){
        if( content.isEmpty() ){
            return;
        }
        IntraclinicCommentDTO comment = new IntraclinicCommentDTO();
        comment.name = name;
        comment.postId = postId;
        comment.content = content;
        comment.createdAt = LocalDate.now().toString();
        Service.api.enterComment(comment)
                .thenCompose(commentId -> Service.api.getPost(postId))
                .thenAccept(newPostFull -> {
                    EventQueue.invokeLater(() -> {
                        wrapper.removeAll();
                        wrapper.add(makeUnitContent(newPostFull, wrapper), "growx");
                        wrapper.repaint();
                        wrapper.revalidate();
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
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
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
