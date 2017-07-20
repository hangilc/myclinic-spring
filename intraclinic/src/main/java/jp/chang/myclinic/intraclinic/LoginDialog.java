package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.UserInfoDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class LoginDialog extends JFrame {

    private JTextField userNameInput = new JTextField(8);
    private JTextField passwordInput = new JTextField(8);
    private JButton enterButton = new JButton("入力");
    private JButton cancelButton = new JButton("キャンセル");
    private JLabel errorMessage = new JLabel("");
    private UserInfoDTO userInfo;

    LoginDialog(){
        super("ログイン");
        setupErrorMessage();
        setLayout(new MigLayout("", "", ""));
        add(new JLabel("院内ミーティングへログイン"), "span, wrap");
        add(errorMessage, "span, hidemode 3, wrap");
        add(new JLabel("ユーザー名："));
        add(userNameInput, "grow, wrap");
        add(new JLabel("パスワード："));
        add(passwordInput, "grow, wrap");
        add(enterButton, "span, split 2, sizegroup btn");
        add(cancelButton, "sizegroup btn");
        enterButton.addActionListener(event -> {
            Service.api.login(userNameInput.getText(), passwordInput.getText())
                    .thenAccept(result -> {
                        System.out.println(result);
                        userInfo = result;
                        dispose();
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            errorMessage.setText("ログインできません。");
                            errorMessage.setVisible(true);
                            pack();
                        });
                        return null;
                    });
        });
        cancelButton.addActionListener(event -> {
            dispose();
            System.exit(2);
        });
        pack();
    }

    UserInfoDTO getUserInfo(){
        return userInfo;
    }

    private void setupErrorMessage(){
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
    }
}

