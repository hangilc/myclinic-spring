package jp.chang.myclinic.intraclinic;

import jp.chang.myclinic.dto.UserInfoDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class LoginDialog extends JFrame {
    interface Callback {
        void ok(UserInfoDTO userInfo);
    }

    private JTextField userNameInput = new JTextField(8);
    private JPasswordField passwordInput = new JPasswordField(8);
    private JButton enterButton = new JButton("入力");
    private JButton cancelButton = new JButton("キャンセル");
    private JLabel errorMessage = new JLabel("");
    private Callback callback;

    LoginDialog(Callback callback){
        super("ログイン");
        this.callback = callback;
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
            char[] chars = passwordInput.getPassword();
            Service.api.login(userNameInput.getText(), new String(chars))
                    .thenAccept(result -> {
                        dispose();
                        callback.ok(result);
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

    private void setupErrorMessage(){
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
    }
}

