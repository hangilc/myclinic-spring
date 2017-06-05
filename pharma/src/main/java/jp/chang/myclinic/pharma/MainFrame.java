package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by hangil on 2017/06/05.
 */
public class MainFrame extends JFrame {

    private JButton closeButton = new JButton("閉じる");
    private JCheckBox includePrescribedCheckBox = new JCheckBox("処方済の患者も含める");
    private JButton updatePatientListButton = new JButton("更新");
    private JButton startPrescButton = new JButton("調剤開始");

    public MainFrame(){
        setLayout(new MigLayout("fill", "[grow] [grow]", ""));
        add(makeLeft(), "");
        add(makeRight(), "");
        add(makeSouth(), "dock south, right");
        bind();
        pack();
    }

    private JComponent makeLeft(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(new JLabel("患者リスト"), "left, wrap");
        panel.add(new JList(), "w 200, h 180, grow, wrap");
        panel.add(makePatientListSub(), "grow, wrap");
        return panel;
    }

    private JComponent makePatientListSub(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        panel.add(makePatientListSubRow1(), "wrap");
        panel.add(makePatientListSubRow2(), "");
        return panel;
    }

    private JComponent makePatientListSubRow1(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        try {
            Image waitCashierIcon = ImageIO.read(getClass().getResource("/wait_cashier.bmp"));
            JLabel waitCashierLabel = new JLabel("会計待ち");
            waitCashierLabel.setIcon(new ImageIcon(waitCashierIcon));
            Image waitPackIcon = ImageIO.read(getClass().getResource("/wait_drug.bmp"));
            JLabel waitPackLabel = new JLabel("薬渡待ち");
            waitPackLabel.setIcon(new ImageIcon(waitPackIcon));
            panel.add(waitCashierLabel);
            panel.add(waitPackLabel);
        } catch (IOException e) {
            e.printStackTrace();
            alert("画像を読み込めませんでした。");
        }
        return panel;
    }

    private JComponent makePatientListSubRow2(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        Insets insets = includePrescribedCheckBox.getInsets();
        insets.left = -1;
        includePrescribedCheckBox.setMargin(insets);
        panel.add(includePrescribedCheckBox, "");
        panel.add(updatePatientListButton);
        panel.add(startPrescButton);
        return panel;
    }

    private JComponent makeRight(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        return panel;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("", "[grow]", ""));
        panel.add(closeButton, "right");
        return panel;
    }

    private void bind(){
        closeButton.addActionListener(event -> {
            dispose();
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
