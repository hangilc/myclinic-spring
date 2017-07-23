package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicCommentDTO;
import jp.chang.myclinic.dto.IntraclinicPostFullDTO;
import jp.chang.myclinic.intraclinic.wrappedtext.Strut;
import jp.chang.myclinic.intraclinic.wrappedtext.WrappedText;
import jp.chang.myclinic.util.DateTimeUtil;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

class PostsPane extends JPanel {

    private int width;
    private List<IntraclinicPostFullDTO> fullPosts;
    private boolean isAdmin;
    private String today = LocalDate.now().toString();

    PostsPane(List<IntraclinicPostFullDTO> fullPosts, boolean isAdmin){
        this.fullPosts = fullPosts;
        this.isAdmin = isAdmin;
        System.out.println("today: " + today);
        setLayout(new MigLayout("insets 0, fill", "", ""));
        add(new Strut(w -> {
            width = w;
            EventQueue.invokeLater(() -> {
                renderPosts();
                repaint();
                revalidate();
            });
        }), "growx");
    }

    private void renderPosts(){
        for(IntraclinicPostFullDTO fullPost: fullPosts){
            add(makeUnit(fullPost), "growx, wrap");
        }
    }

    private JComponent makeUnit(IntraclinicPostFullDTO fullPost) {
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JLabel title = new JLabel(DateTimeUtil.sqlDateToKanjiWithYoubi(fullPost.post.createdAt));
        title.setFont(title.getFont().deriveFont(Font.BOLD));
        title.setBackground(new Color(0xdd, 0xdd, 0xdd));
        title.setOpaque(true);
        title.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        panel.add(title, "growx, wrap");
        WrappedText wt = new WrappedText(width, fullPost.post.content);
        panel.add(wt, "wrap");
        panel.add(makeCommentsBox(fullPost.comments), "growx");
        return panel;
    }

    private JComponent makeCommentsBox(List<IntraclinicCommentDTO> comments){
        JPanel panel = new JPanel(new MigLayout("fill", "", ""));
        panel.setBorder(BorderFactory.createTitledBorder("コメント"));
        panel.add(new Strut(w -> {
            EventQueue.invokeLater(() -> {
                int ncom = comments.size();
                for(int i=0;i<ncom;i++){
                    IntraclinicCommentDTO c = comments.get(i);
                    String text = c.name + "：" + c.content;
                    WrappedText wt = new WrappedText(w, text);
                    panel.add(wt, i == (ncom-1) ? "" : "wrap");
                }
                // TODO: restrict entering comment only to today's post
                JTextField textField = new JTextField();
                panel.add(textField, "newline, split 2, growx");
                JButton submitButton = new JButton("投稿");
                panel.add(submitButton);
                panel.repaint();
                panel.revalidate();
            });
        }), "growx");
        return panel;
    }
}
