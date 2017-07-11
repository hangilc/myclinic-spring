package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.printer.manage.PrinterManageDialog;
import jp.chang.myclinic.drawer.printer.manage.SettingChooserDialog;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class MainFrame extends JFrame {

    private PharmaQueueList pharmaQueueList;
    private Workarea workarea;
    private AuxControl auxControl;
    private JButton closeButton = new JButton("閉じる");
    private JCheckBox includePrescribedCheckBox = new JCheckBox("処方済の患者も含める");
    private JButton updatePatientListButton = new JButton("更新");
    private JButton startPrescButton = new JButton("調剤開始");
    private JTextField prevTechouSearchField = new JTextField(6);
    private JButton searchPrevTechouButton = new JButton("検索");
    private JPanel searchPrevTechouResult;
    private JScrollPane searchPrevTechouResultScroll;
    private JMenuItem prescPrinterSettingItem = new JMenuItem("処方内容印刷設定");
    private JMenuItem drugbagPrinterSettingItem = new JMenuItem("薬袋印刷設定");
    private JMenuItem techouPrinterSettingItem = new JMenuItem("お薬手帳印刷設定");
    private JMenuItem printManageItem = new JMenuItem("印刷管理");
    private JScrollPane rightScroll;
    private static Icon waitCashierIcon;
    private static Icon waitDrugIcon;

    // TODO: print blank drug bag
    // TODO: move close button to menu
    public MainFrame(){
        super("薬局");
        try {
            setupIcons();
        } catch(IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("failed to load icons");
        }
        setupMenu();
        setLayout(new MigLayout("fill", "[260px!]5px![360px!]", "[460px]"));
        add(makeLeft(), "growx, top");
        add(makeRight(), "grow, top");
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
        JMenu drugInfoMenu = new JMenu("薬剤情報");
        {
            JMenuItem item = new JMenuItem("新規作成");
            drugInfoMenu.add(item);
            item.addActionListener(event -> {
                NewDrugInfoDialog dialog = new NewDrugInfoDialog();
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            });
        }
        {
            JMenuItem item = new JMenuItem("表示・編集");
            drugInfoMenu.add(item);
            item.addActionListener(event -> {
                EditDrugInfoDialog dialog = new EditDrugInfoDialog();
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            });
        }
        menuBar.add(drugInfoMenu);
        JMenu settingMenu = new JMenu("設定");
        settingMenu.add(prescPrinterSettingItem);
        settingMenu.add(drugbagPrinterSettingItem);
        settingMenu.add(techouPrinterSettingItem);
        settingMenu.add(printManageItem);
        menuBar.add(settingMenu);
        setJMenuBar(menuBar);
    }

    private JComponent makeLeft(){
        pharmaQueueList = new PharmaQueueList(waitCashierIcon, waitDrugIcon);
        JPanel panel = new JPanel(new MigLayout("fill", "", ""));
        panel.add(new JLabel("患者リスト"), "left, wrap");
        panel.add(new JScrollPane(pharmaQueueList), "grow, wrap");
        panel.add(makePatientListSub(), "growx, wrap");
        panel.add(makePrevTechou(), "growx");
        return panel;
    }

    private JComponent makePatientListSub(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "", ""));
        panel.add(makePatientListSubRow1(), "wrap");
        panel.add(makePatientListSubRow2(), "wrap");
        panel.add(makePatientListSubRow3(), "");
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
        return panel;
    }

    private JComponent makePatientListSubRow3(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(startPrescButton);
        return panel;
    }

    private JComponent makePrevTechou(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.setBorder(BorderFactory.createTitledBorder("過去のお薬手帳"));
        panel.add(prevTechouSearchField);
        panel.add(searchPrevTechouButton, "wrap");
        searchPrevTechouResult = new JPanel(new MigLayout("insets 0", "[220!]", ""));
        JScrollPane searchPrevTechouResultScroll = new JScrollPane(panel);
        panel.add(searchPrevTechouResult, "span 2, h n:n:300");
        return panel;
    }

    private JComponent makeRight(){
        int width = 330;
        JPanel auxSubControl = new JPanel(new MigLayout("", "", ""));
        AuxDispRecords dispRecords = new AuxDispRecords(width);
        auxControl = new AuxControl(auxSubControl, dispRecords, width - 2);
        JPanel panel = new JPanel(new MigLayout("", "[" + width + "!]", ""));
        panel.add(new JLabel("投薬"), "growx, wrap");
        panel.add(makeWorkarea(), "growx, wrap");
        {
            JPanel control = new JPanel(new MigLayout("insets 0", "", "[]2[]"));
            auxControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            auxSubControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            control.add(auxControl, "growx, wrap");
            control.add(auxSubControl, "growx");
            panel.add(control, "growx, wrap");
        }
        panel.add(dispRecords, "grow");
        rightScroll = new JScrollPane(panel);
        rightScroll.setBorder(null);
        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightScroll.getVerticalScrollBar().setUnitIncrement(12);
        return rightScroll;
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
        prescPrinterSettingItem.addActionListener(event -> doPrescPrinterSetting());
        drugbagPrinterSettingItem.addActionListener(event -> doDrugbagPrinterSetting());
        techouPrinterSettingItem.addActionListener(event -> doTechouPrinterSetting());
        printManageItem.addActionListener(event -> doManagePrint());
        searchPrevTechouButton.addActionListener(event -> doSearchPrevTechou());
    }

    private void doPrescPrinterSetting() {
        SettingChooserDialog dialog = new SettingChooserDialog(this, "処方内容の印刷設定");
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        if( !dialog.isCanceled() ){
            PharmaConfig.INSTANCE.setPrescPrinterSetting(dialog.getSelectedSetting());
            try {
                PharmaConfig.INSTANCE.writeToConfigFile();
            } catch(IOException ex){
                ex.printStackTrace();
                alert("設定ファイルの保存に失敗しました。\n" + ex);
            }
        }
    }

    private void doDrugbagPrinterSetting() {
        SettingChooserDialog dialog = new SettingChooserDialog(this, "薬袋の印刷設定");
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        if( !dialog.isCanceled() ){
            PharmaConfig.INSTANCE.setDrugbagPrinterSetting(dialog.getSelectedSetting());
            try {
                PharmaConfig.INSTANCE.writeToConfigFile();
            } catch(IOException ex){
                ex.printStackTrace();
                alert("設定ファイルの保存に失敗しました。\n" + ex);
            }
        }
    }

    private void doTechouPrinterSetting() {
        SettingChooserDialog dialog = new SettingChooserDialog(this, "お薬手帳の印刷設定");
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        if( !dialog.isCanceled() ){
            PharmaConfig.INSTANCE.setTechouPrinterSetting(dialog.getSelectedSetting());
            try {
                PharmaConfig.INSTANCE.writeToConfigFile();
            } catch(IOException ex){
                ex.printStackTrace();
                alert("設定ファイルの保存に失敗しました。\n" + ex);
            }
        }
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
                        auxControl.update(pharmaQueueFull.patient);
                        rightScroll.getVerticalScrollBar().setValue(0);
                    });
                })
                .exceptionally(t -> null);
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
        PrinterManageDialog printerManageDialog = new PrinterManageDialog(this);
        printerManageDialog.setLocationByPlatform(true);
        printerManageDialog.setVisible(true);
    }

    private class AlertException extends RuntimeException {
        private String message;

        AlertException(String message){
            this.message = message;
        }

        @Override
        public String toString(){
            return message;
        }
    }

    private void doSearchPrevTechou(){
        try {
            int patientId = Integer.parseInt(prevTechouSearchField.getText());
            PrevTechouStorage storage = new PrevTechouStorage();
            Cursor origCursor = getCursor();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            Service.api.findPatient(patientId)
                    .thenCompose(patient -> {
                        if( patient != null ){
                            storage.patient = patient;
                            return Service.api.listVisitIdVisitedAtForPatient(patientId);
                        } else {
                            throw new AlertException("該当患者が見つかりません。");
                        }
                    })
                    .thenAccept(visits -> {
                        EventQueue.invokeLater(() -> {
                            setCursor(origCursor);
                            prevTechouSearchField.setText("");
                            PrevTechouDialog dialog = new PrevTechouDialog(storage.patient, visits.subList(0, Math.min(20, visits.size())));
                            dialog.setLocationByPlatform(true);
                            dialog.setVisible(true);
                        });
                    })
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            setCursor(origCursor);
                            String message = t.toString();
                            if( t instanceof CompletionException ){
                                message = t.getCause().toString();
                            }
                            alert(message);
                        });
                        return null;
                    });
        } catch(NumberFormatException ex){
            alert("患者番号の入力が不適切です。");
            return;
        }
     }

    private static class PrevTechouStorage {
        PatientDTO patient;
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }
}
