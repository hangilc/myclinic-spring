package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.printer.manage.PrinterManageDialog;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by hangil on 2017/06/05.
 */
public class MainFrame extends JFrame {

    private PharmaQueueList pharmaQueueList;
    private Workarea workarea;
    private JButton closeButton = new JButton("閉じる");
    private JCheckBox includePrescribedCheckBox = new JCheckBox("処方済の患者も含める");
    private JButton updatePatientListButton = new JButton("更新");
    private JButton startPrescButton = new JButton("調剤開始");
    private JTextField prevTechouSearchField = new JTextField(6);
    private JButton searchPrevTechouButton = new JButton("検索");
    private JMenuItem printManageItem = new JMenuItem("印刷管理");
    private static Icon waitCashierIcon;
    private static Icon waitDrugIcon;

    public MainFrame(){
        super("薬局");
        try {
            setupIcons();
        } catch(IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("failed to load icons");
        }
        setupMenu();
        setLayout(new MigLayout("", "[] []", "[grow]"));
        add(makeLeft(), "top");
        {
            JScrollPane sp = new JScrollPane(makeRight());
            sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            sp.setBorder(BorderFactory.createEmptyBorder());
            add(sp, "top, grow");
        }
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
        JPanel panel = new JPanel(new MigLayout("insets n n n 22", "[]", "[]"));
        panel.add(new JLabel("投薬"), "wrap");
        panel.add(makeWorkarea(), "w 300, wrap");
        {
            AuxControll auxCtrl = new AuxControll();
            auxCtrl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            panel.add(auxCtrl, "grow, wrap");
        }
        {
            AuxArea auxArea = new AuxArea();
            panel.add(auxArea, "grow");
        }
        return panel;
    }

    private JComponent makeWorkarea(){
        Workarea wa = new Workarea();
        wa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        workarea = wa;
        return wa;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("", "[grow]", ""));
        panel.add(closeButton, "right");
        return panel;
    }

    private void bind(){
        updatePatientListButton.addActionListener(event -> doUpdatePatientList());
        startPrescButton.addActionListener(event -> doStartPresc());
        closeButton.addActionListener(event -> {
            dispose();
            System.exit(0);
        });
        printManageItem.addActionListener(event -> doManagePrint());
    }

    private void doStartPresc() {
        PharmaQueueFullDTO pharmaQueueFull = pharmaQueueList.getSelectedValue();
        if( pharmaQueueFull == null ){
            return;
        }
        Service.api.listDrugFull(pharmaQueueFull.visitId)
                .thenAccept(drugs -> {
                    EventQueue.invokeLater(() -> {
                        workarea.update(pharmaQueueFull.patient, drugs);
                    });
                })
                .exceptionally(t -> {
                    return null;
                });
    }

    private void doUpdatePatientList() {
        CompletableFuture<List<PharmaQueueFullDTO>> pharmaList;
        if( includePrescribedCheckBox.isSelected() ){
            pharmaList = Service.api.listPharmaQueueForToday();
        } else {
            pharmaList = Service.api.listPharmaQueueForPrescription();
        }
        pharmaList.thenAccept(result -> {
            EventQueue.invokeLater(() -> {
                pharmaQueueList.setListData(result.toArray(new PharmaQueueFullDTO[]{}));
            });
        })
        .exceptionally(t -> {
            t.printStackTrace();
            return null;
        });
    }

    private void doManagePrint(){
        try {
            PrinterManageDialog printerManageDialog = new PrinterManageDialog(this);
            printerManageDialog.setLocationByPlatform(true);
            printerManageDialog.setVisible(true);
        } catch(IOException ex){
            ex.printStackTrace();
            alert(ex.toString());
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
