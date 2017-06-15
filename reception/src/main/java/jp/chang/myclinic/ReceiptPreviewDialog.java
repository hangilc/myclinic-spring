package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.manage.PrinterManageDialog;
import jp.chang.myclinic.drawer.printer.manage.PrinterSetting;
import jp.chang.myclinic.drawer.printer.manage.SettingSelectorDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ReceiptPreviewDialog extends JDialog {

    private JButton printButton;
    private JButton cancelButton;
    private JMenuItem itemSelectPrinter = new JMenuItem("プリンター選択");
    private JMenuItem itemManagePrinter = new JMenuItem("プリンター管理");
    private JMenuItem itemClearPrintSetting = new JMenuItem("印刷設定をクリア");
    private java.util.List<Op> ops;

    public ReceiptPreviewDialog(Window owner, List<Op> ops){
        super(owner, "領収書プレビュー", Dialog.ModalityType.DOCUMENT_MODAL);
        this.ops = ops;
        setLayout(new MigLayout("fill", "[grow]", "[grow] []"));
        setupMenu();
        add(makeCenter(), "grow, wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPrintSetting = new JMenu("印刷設定");
        menuBar.add(menuPrintSetting);
        menuPrintSetting.add(itemSelectPrinter);
        menuPrintSetting.add(itemManagePrinter);
        JMenu otherMenu = new JMenu("その他");
        otherMenu.add(itemClearPrintSetting);
        menuPrintSetting.add(otherMenu);
        setJMenuBar(menuBar);
    }

    private JComponent makeCenter(){
        JComponent pane = new DrawerPreviewPane(ops, 148, 105);
        pane.setPreferredSize(new Dimension(300, 200));
        pane.setOpaque((true));
        pane.setBackground(Color.WHITE);
        return pane;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        printButton = new JButton("印刷");
        panel.add(printButton, "sizegroup btn");
        cancelButton = new JButton("キャンセル");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void bind(){
        printButton.addActionListener(event -> {
            DrawerPrinter printer = new DrawerPrinter();
            String currentSetting = ReceptionConfig.INSTANCE.getCurrentSetting();
            PrinterSetting printerSetting = new PrinterSetting(ReceptionConfig.INSTANCE.getSettingDir());
            if( currentSetting == null || !printerSetting.nameExists(currentSetting) ){
                alert("設定が有効でないので、プリンターを選択して印刷します。");
                currentSetting = null;
            }
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            if( currentSetting == null ){
                drawerPrinter.print(ops);
            } else {
                try {
                    byte[] devmodeData = printerSetting.readDevmode(currentSetting);
                    byte[] devnamesData = printerSetting.readDevnames(currentSetting);
                    AuxSetting auxSetting = printerSetting.readAuxSetting(currentSetting);
                    drawerPrinter.setDx(auxSetting.getDx());
                    drawerPrinter.setDy(auxSetting.getDy());
                    drawerPrinter.print(ops, devmodeData, devnamesData);
                } catch(IOException ex){
                    ex.printStackTrace();
                    alert(ex.toString());
                }
            }
        });
        cancelButton.addActionListener(event -> {
            dispose();
        });
        itemSelectPrinter.addActionListener(event -> {
            PrinterSetting printerSetting = new PrinterSetting(ReceptionConfig.INSTANCE.getSettingDir());
            try {
                List<String> names = printerSetting.listNames();
                if( names.size() == 0 ){
                    alert("選択できる印刷設定がありません。");
                    return;
                }
                SettingSelectorDialog selector = new SettingSelectorDialog(this, names, ReceptionConfig.INSTANCE.getCurrentSetting());
                selector.setLocationByPlatform(true);
                selector.setVisible(true);
                if( !selector.isCanceled() ){
                    String selectedName = selector.getSelectedItem();
                    ReceptionConfig.INSTANCE.setCurrentSetting(selectedName);
                    ReceptionConfig.INSTANCE.writeToConfigFile();
                }
            } catch(IOException ex){
                ex.printStackTrace();
                alert(ex.toString());
            }
        });
        itemManagePrinter.addActionListener(event -> {
            try {
                PrinterManageDialog dialog = new PrinterManageDialog(this);
                dialog.setLocationByPlatform(true);
                dialog.setVisible(true);
            } catch(IOException ex){
                ex.printStackTrace();
                alert(ex.toString());
            }
        });
        itemClearPrintSetting.addActionListener(event -> {
            if( !confirm("印刷設定をクリアしていいですか？") ){
                return;
            }
            ReceptionConfig.INSTANCE.setCurrentSetting("");
            try {
                ReceptionConfig.INSTANCE.writeToConfigFile();
            } catch(IOException ex){
                ex.printStackTrace();
                alert(ex.toString());
            }
        });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

    private boolean confirm(String message){
        int choice = JOptionPane.showConfirmDialog(this, message, "確認", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }
}
