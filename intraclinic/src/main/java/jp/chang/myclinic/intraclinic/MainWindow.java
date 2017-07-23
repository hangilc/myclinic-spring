package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.IntraclinicPostFullPageDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class MainWindow extends JFrame {

    MainWindow(boolean isAdmin, String name, IntraclinicPostFullPageDTO initialPage){
        super("院内ミーティング");
        setLayout(new MigLayout("", "", ""));
        PostsPane postsPane = new PostsPane(initialPage.posts, isAdmin);
        postsPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 24));
        JScrollPane scrollPane = new JScrollPane(postsPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        add(scrollPane, "w 360!, h 500");
        pack();
    }

}
