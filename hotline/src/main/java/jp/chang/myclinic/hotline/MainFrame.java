package jp.chang.myclinic.hotline;

import jp.chang.myclinic.dto.HotlineDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class MainFrame extends JFrame {

    private User sender;
    private User recipient;
    private JTextArea textArea;
    private JButton sendButton = new JButton("送信");
    private JButton ryoukaiButton = new JButton("了解");
    private JButton beepButton = new JButton("ビープ");
    private JButton closeButton = new JButton("閉じる");
    private DispPane dispPane;
    private Reloader reloader;
    private JScrollPane scrollPane;
    private static String BeepMessage = "[BEEP]";
    private JLabel statusBar = new JLabel(" ");

    MainFrame(User sender, User recipient){
        this.sender = sender;
        this.recipient = recipient;
        setTitle(recipient.getDispName());
        setupStatusBar();
        setLayout(new MigLayout("", "[180!]", ""));
        add(makeDisp(), "growx, h 260, wrap");
        add(makeInput(), "growx, h 60, wrap");
        add(sendButton, "sizegroup btn, wrap");
        add(ryoukaiButton, "split 2, sizegroup btn");
        add(beepButton, "sizegroup btn, wrap");
        add(makeSouth(), "right, wrap");
        add(statusBar, "gap top 4, growx");
        bind();
        pack();
    }

    void setReloader(Reloader reloader){
        this.reloader = reloader;
    }

    void showMessage(String message){
        statusBar.setText(message);
    }

    private void setupStatusBar(){
        statusBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    private JComponent makeDisp(){
        dispPane = new DispPane(this::scrollToBottom);
        dispPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 22));
        scrollPane = new JScrollPane(dispPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void scrollToBottom(){
        EventQueue.invokeLater(() -> {
            JScrollBar sb = scrollPane.getVerticalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    private JComponent makeInput(){
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        return new JScrollPane(textArea);
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(closeButton, "");
        return panel;
    }

    private void bind(){
        sendButton.addActionListener(event -> doSend());
        ryoukaiButton.addActionListener(event -> sendMessage("了解", false));
        beepButton.addActionListener(event -> sendMessage(BeepMessage, false));
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
        hotline.sender = sender.getName();
        hotline.recipient = recipient.getName();
        hotline.postedAt = LocalDate.now().toString();
        Service.api.enterHotline(hotline)
                .thenAccept(hotlineId -> EventQueue.invokeLater(() -> {
                    if( clearTextArea ) {
                        textArea.setText("");
                    }
                    if( reloader != null ){
                        reloader.trigger();
                    }
                }))
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
                if( maxMyHotlineId > 0 ) {
                    needBeep = true;
                }
                if( !BeepMessage.equals(h.message) ){
                    mine.add(h);
                }
            } else if( isSentFromMe(h) ){
                if( !BeepMessage.equals(h.message) ){
                    mine.add(h);
                }
            }
        }
        if( mine.size() > 0 ){
            dispPane.addHotlines(mine, this::scrollToBottom);
        }
        if( needBeep ){
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isSentFromMe(HotlineDTO hotline){
        return hotline.sender.equals(sender.getName()) && hotline.recipient.equals(recipient.getName());
    }

    private boolean isSentToMe(HotlineDTO hotline){
        return hotline.sender.equals(recipient.getName()) && hotline.recipient.equals(sender.getName());
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
