package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Reloader reloader;

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
    }

    void setReloader(Reloader reloader){
        this.reloader = reloader;
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
                    if( clearTextArea ) {
                        textArea.setText("");
                    }
                    if( reloader != null ){
                        reloader.trigger();
                    }
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> alert(t.toString()));
                    return null;
                });
    }

    void onNewHotline(List<HotlineDTO> hotlines){
        List<HotlineDTO> mine = new ArrayList<>();
        int maxMyHotlineId = dispPane.getLargestHotlineId();
        boolean needBeep = false;
        for(HotlineDTO h: hotlines){
            if( h.hotlineId <= maxMyHotlineId ){
                continue;
            }
            if( isSentToMe(h) ){
                needBeep = true;
                mine.add(h);
            } else if( isSentFromMe(h) ){
                mine.add(h);
            }
        }
        if( mine.size() > 0 ){
            dispPane.addHotlines(mine);
        }
        if( needBeep ){
            System.out.println("beep");
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isSentFromMe(HotlineDTO hotline){
        return hotline.sender.equals(sender) && hotline.recipient.equals(recipient);
    }

    private boolean isSentToMe(HotlineDTO hotline){
        return hotline.sender.equals(recipient) && hotline.recipient.equals(sender);
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
