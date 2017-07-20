package jp.chang.myclinic.intraclinic;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class LoginDialog extends JFrame {

    private JTextField userNameInput = new JTextField(8);
    private JTextField passwordInput = new JTextField(8);
    private JButton enterButton = new JButton("入力");
    private JButton cancelButton = new JButton("キャンセル");

    LoginDialog(){
        super("ログイン");
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("院内ミーティングへログイン"), "span, wrap");
        add(new JLabel("ユーザー名："));
        add(userNameInput, "wrap");
        add(new JLabel("パスワード："));
        add(passwordInput, "wrap");
        add(enterButton, "span, split 2");
        add(cancelButton);
        cancelButton.addActionListener(event -> {
            dispose();
            System.exit(2);
        });
        pack();
    }
}

