package jp.chang.myclinic.hotline;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MainFrame extends JFrame {

    private JButton sendButton = new JButton("送信");
    private JButton ryoukaiButton = new JButton("了解");
    private JButton beepButton = new JButton("ビープ");
    private JCheckBox soundCheckBox = new JCheckBox("サウンド", true);
    private JButton closeButton = new JButton("閉じる");

    public MainFrame(){
        setLayout(new MigLayout("", "[180!]", ""));
        add(makeDisp(), "growx, h 300, wrap");
        add(makeInput(), "growx, h 80, wrap");
        add(sendButton, "sizegroup btn, wrap");
        add(ryoukaiButton, "split 2, sizegroup btn");
        add(beepButton, "sizegroup btn, wrap");
        add(soundCheckBox, "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private JComponent makeDisp(){
        JPanel panel = new JPanel();
        JScrollPane sp = new JScrollPane(panel);
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
}
