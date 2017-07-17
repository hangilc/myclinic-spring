package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

class MainFrame extends JFrame {

    private String sender;
    private String recipient;
    private JTextArea textArea;
    private JButton sendButton = new JButton("送信");
    private JButton ryoukaiButton = new JButton("了解");
    private JButton beepButton = new JButton("ビープ");
    private JCheckBox soundCheckBox = new JCheckBox("サウンド", true);
    private JButton closeButton = new JButton("閉じる");
    private DispPane dispPane;

    MainFrame(String sender, String recipient){
        this.sender = sender;
        this.recipient = recipient;
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
        dispPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 22));
        JScrollPane sp = new JScrollPane(dispPane);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }

    private JComponent makeInput(){
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        JScrollPane sp = new JScrollPane(textArea);
        return sp;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(closeButton, "");
        return panel;
    }

    private void bind(){
        sendButton.addActionListener(event -> doSend());
        ryoukaiButton.addActionListener(event -> sendMessage("了解", false));
        closeButton.addActionListener(event -> {
            dispose();
            System.exit(0);
        });
    }

    private void doSend(){
        String text = textArea.getText();
        if( text.isEmpty() ){
            return;
        }
        sendMessage(text, true);
    }

    private void sendMessage(String message, boolean clearTextArea){
        HotlineDTO hotline = new HotlineDTO();
        hotline.message = message;
        hotline.sender = sender;
        hotline.recipient = recipient;
        hotline.postedAt = LocalDate.now().toString();
        Service.api.enterHotline(hotline)
                .thenAccept(hotlineId -> {
                    System.out.println("hotlineId: " + hotlineId);
                    if( clearTextArea ) {
                        textArea.setText("");
                    }
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> alert(t.toString()));
                    return null;
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
