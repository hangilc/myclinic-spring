package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.UserInfoDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class LoginDialog extends JFrame {
    interface Callback {
        void ok(UserInfoDTO userInfo);
    }

    LoginDialog(Callback callback){
        super("ログイン");
        JTextField userNameInput = new JTextField(14);
        JPasswordField passwordInput = new JPasswordField(14);
        JButton enterButton = new JButton("入力");
        JButton cancelButton = new JButton("キャンセル");
        JLabel errorMessage = new JLabel("");
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
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
            char[] chars = passwordInput.getPassword();
            Service.api.login(userNameInput.getText(), new String(chars))
                    .thenAccept(result -> EventQueue.invokeLater(() -> {
                        dispose();
                        callback.ok(result);
                    }))
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
        getRootPane().setDefaultButton(enterButton);
        pack();
    }

}

