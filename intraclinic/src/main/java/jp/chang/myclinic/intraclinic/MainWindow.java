package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostFullPageDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class MainWindow extends JFrame {

    private int currentPage;
    private int totalPages;
    private PostsPane postsPane;
    private JButton prevButton = new JButton("前へ");
    private JButton nextButton = new JButton("次へ");
    private JScrollPane scrollPane = new JScrollPane();

    MainWindow(boolean isAdmin, String name, IntraclinicPostFullPageDTO initialPage){
        super("院内ミーティング");
        this.currentPage = 0;
        this.totalPages = initialPage.totalPages;
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("ユーザー：" + name), "span, wrap");
        add(makeControl(isAdmin), "wrap");
        postsPane = new PostsPane(initialPage.posts, name, isAdmin, new PostsPane.Callback(){
            @Override
            public void onRendered(){
                EventQueue.invokeLater(() -> {
                    scrollPane.getVerticalScrollBar().setValue(0);
                });
            }
        });
        postsPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        scrollPane.setViewportView(postsPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, "w 360!, h 500");
        adaptButtons();
        pack();
    }

    private JComponent makeControl(boolean isAdmin){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        prevButton.addActionListener(event -> doPrev());
        nextButton.addActionListener(event -> doNext());
        panel.add(prevButton);
        panel.add(nextButton);
        if( isAdmin ){
            JButton newButton = new JButton("新規投稿");
            newButton.addActionListener(event -> doNew());
            panel.add(newButton);
        }
        return panel;
    }

    private void doPrev(){
        Service.api.listPost(currentPage - 1)
                .thenAccept(pages -> {
                    EventQueue.invokeLater(() -> {
                        postsPane.updatePosts(pages.posts);
                        currentPage -= 1;
                        adaptButtons();
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

    private void doNext(){
        Service.api.listPost(currentPage + 1)
                .thenAccept(pages -> {
                    EventQueue.invokeLater(() -> {
                        postsPane.updatePosts(pages.posts);
                        currentPage += 1;
                        adaptButtons();
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

    private void doNew(){
        NewPostDialog dialog = new NewPostDialog(getOwner(), new NewPostDialog.Callback(){
            @Override
            public void onPost(int postId){
                Service.api.listPost(currentPage)
                        .thenAccept(pages -> {
                            totalPages = pages.totalPages;
                            EventQueue.invokeLater(() -> {
                                postsPane.updatePosts(pages.posts);
                                adaptButtons();
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

    private void adaptButtons(){
        prevButton.setEnabled(currentPage > 0);
        nextButton.setEnabled(currentPage < totalPages - 1);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
