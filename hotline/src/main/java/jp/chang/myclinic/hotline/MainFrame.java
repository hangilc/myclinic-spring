package jp.chang.myclinic.hotline;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class MainFrame extends JFrame {

    private JButton sendButton = new JButton("送信");
    private JButton ryoukaiButton = new JButton("了解");
    private JButton beepButton = new JButton("ビープ");
    private JCheckBox soundCheckBox = new JCheckBox("サウンド", true);
    private JButton closeButton = new JButton("閉じる");
    private DispPane dispPane;

    MainFrame(){
        setLayout(new MigLayout("", "[180!]", ""));
        add(makeDisp(), "growx, h 260, wrap");
        add(makeInput(), "growx, h 60, wrap");
        add(sendButton, "sizegroup btn, wrap");
        add(ryoukaiButton, "split 2, sizegroup btn");
        add(beepButton, "sizegroup btn, wrap");
        add(soundCheckBox, "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
        reload();
    }

    private JComponent makeDisp(){
        dispPane = new DispPane();
        dispPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        JScrollPane sp = new JScrollPane(dispPane);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }

    private JComponent makeInput(){
        JTextArea ta = new JTextArea();
        ta.setLineWrap(true);
        JScrollPane sp = new JScrollPane(ta);
        return sp;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(closeButton, "");
        return panel;
    }

    private void bind(){
        closeButton.addActionListener(event -> {
            dispose();
            System.exit(0);
        });
    }

    private void reload(){
        Service.api.listTodaysHotline()
                .thenAccept(hotlines -> {
                    EventQueue.invokeLater(() -> dispPane.addHotlines(hotlines));
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> alert(t.toString()));
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
