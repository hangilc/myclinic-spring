package jp.chang.myclinic.pharma;

import jp.chang.myclinic.drawer.printer.manage.PrinterManageDialog;
import jp.chang.myclinic.drawer.printer.manage.SettingChooserDialog;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.leftpane.LeftPane;
import jp.chang.myclinic.pharma.rightpane.RightPane;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CompletionException;

class MainFrame extends JFrame {

    //private PharmaQueueList pharmaQueueList;
//    private Workarea workarea;
//    private AuxControl auxControl;
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
    //private JScrollPane rightScroll;
    private static Icon waitCashierIcon;
    private static Icon waitDrugIcon;

    private LeftPane leftPane;

    // TODO: print blank drug bag
    MainFrame(){
        super("薬局");
        try {
            setupIcons();
        } catch(IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("failed to load icons");
        }
        setupMenu();
        setLayout(new MigLayout("fill", "[260px!]5px![360px!]", "[460px]"));
        JScrollPane rightScroll = new JScrollPane();
        rightScroll.setBorder(null);
        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightScroll.getVerticalScrollBar().setUnitIncrement(12);
        leftPane = new LeftPane(waitCashierIcon, waitDrugIcon){
            @Override
            public void onStartPresc(PharmaQueueFullDTO pharmaQueueFull, java.util.List<DrugFullDTO> drugs){
                RightPane rightPane = new RightPane(pharmaQueueFull, drugs);
                rightScroll.getViewport().setView(rightPane);
                rightScroll.getVerticalScrollBar().setValue(0);
                //rightScroll.repaint();
                //rightScroll.revalidate();
            }
        };
        add(leftPane, "growx, top");
        add(rightScroll, "grow, top");
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
        {
            JMenuItem item = new JMenuItem("一覧");
            drugInfoMenu.add(item);
            item.addActionListener(event -> {
                Service.api.listAllPharmaDrugNames()
                        .thenAccept(list -> {
                            ListAllPharmaDrugDialog dialog = new ListAllPharmaDrugDialog(list);
                            dialog.setLocationByPlatform(true);
                            dialog.setVisible(true);
                        })
                        .exceptionally(t -> {
                            t.printStackTrace();
                            EventQueue.invokeLater(() -> {
                                alert(t.toString());
                            });
                            return null;
                        });
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

//    private JComponent makeRight(){
//        int width = 330;
//        JPanel auxSubControl = new JPanel(new MigLayout("", "", ""));
//        AuxDispRecords dispRecords = new AuxDispRecords(width);
//        auxControl = new AuxControl(auxSubControl, dispRecords, width - 2);
//        JPanel panel = new JPanel(new MigLayout("", "[" + width + "!]", ""));
//        panel.add(new JLabel("投薬"), "growx, wrap");
//        panel.add(makeWorkarea(), "growx, wrap");
//        {
//            JPanel control = new JPanel(new MigLayout("insets 0", "", "[]2[]"));
//            auxControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//            auxSubControl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//            control.add(auxControl, "growx, wrap");
//            control.add(auxSubControl, "growx");
//            panel.add(control, "growx, wrap");
//        }
//        panel.add(dispRecords, "grow");
//        rightScroll = new JScrollPane(panel);
//        rightScroll.setBorder(null);
//        rightScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        rightScroll.getVerticalScrollBar().setUnitIncrement(12);
//        return rightScroll;
//    }

//    private JComponent makeWorkarea(){
//        Workarea wa = new Workarea(){
//            @Override
//            public void onPrescDone(){
//                // TODO: update patient list
//                clearRight();
//            }
//
//            @Override
//            public void onCancel(){
//                clearRight();
//            }
//
//            private void clearRight(){
//                clear();
//                // TODO: clear records
//            }
//        };
//        wa.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        workarea = wa;
//        return wa;
//    }

    private void bind(){
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
//        PharmaQueueFullDTO pharmaQueueFull = pharmaQueueList.getSelectedValue();
//        if( pharmaQueueFull == null ){
//            return;
//        }
//        Service.api.listDrugFull(pharmaQueueFull.visitId)
//                .thenAccept(drugs -> {
//                    EventQueue.invokeLater(() -> {
//                        workarea.update(pharmaQueueFull.patient, drugs);
//                        auxControl.update(pharmaQueueFull.patient);
//                        rightScroll.getVerticalScrollBar().setValue(0);
//                    });
//                })
//                .exceptionally(t -> null);
    }

    private void doUpdatePatientList() {
//        CompletableFuture<List<PharmaQueueFullDTO>> pharmaList;
//        if( includePrescribedCheckBox.isSelected() ){
//            pharmaList = Service.api.listPharmaQueueForToday();
//        } else {
//            pharmaList = Service.api.listPharmaQueueForPrescription();
//        }
//        pharmaList.thenAccept(result -> {
//            EventQueue.invokeLater(() -> {
//                pharmaQueueList.setListData(result.toArray(new PharmaQueueFullDTO[]{}));
//            });
//        })
//        .exceptionally(t -> {
//            t.printStackTrace();
//            return null;
//        });
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
