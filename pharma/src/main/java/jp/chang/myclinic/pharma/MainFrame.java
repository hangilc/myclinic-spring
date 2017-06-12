package jp.chang.myclinic.pharma;

import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by hangil on 2017/06/05.
 */
public class MainFrame extends JFrame {

    private PharmaQueueList pharmaQueueList;
    private JButton closeButton = new JButton("閉じる");
    private JCheckBox includePrescribedCheckBox = new JCheckBox("処方済の患者も含める");
    private JButton updatePatientListButton = new JButton("更新");
    private JButton startPrescButton = new JButton("調剤開始");
    private JTextField prevTechouSearchField = new JTextField(6);
    private JButton searchPrevTechouButton = new JButton("検索");
    private static Icon waitCashierIcon;
    private static Icon waitDrugIcon;

    public MainFrame() throws IOException{
        super("薬局");
        setupIcons();
        setupMenu();
        setLayout(new MigLayout("fill", "[grow] [grow]", ""));
        add(makeLeft(), "top");
        add(makeRight(), "top");
        add(makeSouth(), "dock south, right");
        bind();
        pack();
    }

    private void setupIcons() throws IOException {
        Image waitCashierImage = ImageIO.read(getClass().getResource("/wait_cashier.bmp"));
        waitCashierIcon = new ImageIcon(waitCashierImage);
        Image waitPackImage = ImageIO.read(getClass().getResource("/wait_drug.bmp"));
        waitDrugIcon = new ImageIcon(waitPackImage);
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu settingMenu = new JMenu("設定");
        JMenuItem printSettingItem = new JMenuItem("印刷設定");
        settingMenu.add(printSettingItem);
        JMenuItem printManageItem = new JMenuItem("印刷管理");
        settingMenu.add(printManageItem);
        menuBar.add(settingMenu);
        setJMenuBar(menuBar);
    }

    private JComponent makeLeft(){
        pharmaQueueList = new PharmaQueueList(waitCashierIcon, waitDrugIcon);
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(new JLabel("患者リスト"), "left, wrap");
        panel.add(new JScrollPane(pharmaQueueList), "w 200, h 180, grow, wrap");
        panel.add(makePatientListSub(), "grow, wrap");
        panel.add(makePrevTechou(), "grow");
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
        JLabel waitCashierLabel = new JLabel("会計待ち");
        waitCashierLabel.setIcon(waitCashierIcon);
        JLabel waitPackLabel = new JLabel("薬渡待ち");
        waitPackLabel.setIcon(waitDrugIcon);
        panel.add(waitCashierLabel);
        panel.add(waitPackLabel);
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

    private JComponent makePrevTechou(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.setBorder(BorderFactory.createTitledBorder("過去のお薬手帳"));
        panel.add(prevTechouSearchField);
        panel.add(searchPrevTechouButton);
        return panel;
    }

    private JComponent makeRight(){
        JPanel panel = new JPanel(new MigLayout("", "[grow]", "[]"));
        panel.add(new JLabel("投薬"), "wrap");
        panel.add(makeWorkarea(), "w 300, h 180");
        return panel;
    }

    private JComponent makeWorkarea(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return panel;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("", "[grow]", ""));
        panel.add(closeButton, "right");
        return panel;
    }

    private void bind(){
        updatePatientListButton.addActionListener(event -> doUpdatePatientList());
        closeButton.addActionListener(event -> {
            dispose();
        });
    }

    private void doUpdatePatientList() {
        Service.api.listPharmaQueueForPrescription()
                .thenAccept(result -> {
                    pharmaQueueList.setListData(result.toArray(new PharmaQueueFullDTO[]{}));
                })
                .exceptionally(t -> {
                    t.printStackTrace();
                    //alert(t.toString());
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
