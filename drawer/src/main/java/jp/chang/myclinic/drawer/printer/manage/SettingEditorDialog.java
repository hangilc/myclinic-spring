package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SettingEditorDialog extends JDialog {

    private String settingName;
    private byte[] devnamesData;
    private byte[] devmodeData;
    private AuxSetting auxSetting;
    private PrinterInfoPane printerInfoPane = new PrinterInfoPane();
    private AuxSettingPane auxSettingPane = new AuxSettingPane();
    private JButton printerDialogButton = new JButton("変更");
    private JButton modifyAuxButton = new JButton("変更");
    private JButton okButton = new JButton("OK");
    private JButton cancelButton = new JButton("キャンセル");
    private boolean canceled = true;

    public SettingEditorDialog(Window owner, String name, byte[] devnamesData, byte[] devmodeData, AuxSetting auxSetting){
        super(owner, "印刷設定の編集", ModalityType.DOCUMENT_MODAL);
        this.settingName = name;
        this.devnamesData = devnamesData;
        this.devmodeData = devmodeData;
        this.auxSetting = auxSetting;
        setupUI();
        bind();
    }

    public boolean isCanceled(){
        return canceled;
    }

    public byte[] getDevnamesData() {
        return devnamesData;
    }

    public byte[] getDevmodeData() {
        return devmodeData;
    }

    public AuxSetting getAuxSetting() {
        return auxSetting;
    }

    private void setupUI(){
        setLayout(new MigLayout("fill", "[grow]", ""));
        add(makeTitle(), "wrap");
        add(makePrinterInfo(), "grow, wrap");
        add(makeAuxInfo(), "grow, wrap");
        add(makeSouth(), "right");
        pack();
    }

    private JComponent makeTitle(){
        return new JLabel("設定名：" + settingName);
    }

    private JComponent makePrinterInfo(){
        JPanel panel = new JPanel(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        panel.setBorder(BorderFactory.createTitledBorder("プリンター設定"));
        DevnamesInfo devnamesInfo = new DevnamesInfo(devnamesData);
        DevmodeInfo devmodeInfo = new DevmodeInfo(devmodeData);
        printerInfoPane.setup(devnamesInfo, devmodeInfo);
        panel.add(printerInfoPane, "gaptop 5, wrap");
        panel.add(printerDialogButton, "right");
        return panel;
    }

    private JComponent makeAuxInfo(){
        JPanel panel = new JPanel(new MigLayout("inset 0, gapy 0, fill", "[grow]", ""));
        panel.setBorder(BorderFactory.createTitledBorder("その他"));
        auxSettingPane.setup(auxSetting);
        panel.add(auxSettingPane, "gaptop 5, wrap");
        panel.add(modifyAuxButton, "right");
        return panel;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(okButton, "sizegroup btn");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void updatePrinterInfo(DevnamesInfo devnames, DevmodeInfo devmode){
        printerInfoPane.update(new DevnamesInfo(devnamesData), new DevmodeInfo(devmodeData));
        pack();
    }

    private void updateAuxSetting(AuxSetting auxSetting){
        auxSettingPane.update(auxSetting);
        pack();
    }

    private void bind(){
        printerDialogButton.addActionListener(event -> {
            DrawerPrinter printer = new DrawerPrinter();
            DrawerPrinter.DialogResult result = printer.printDialog(SettingEditorDialog.this, devmodeData, devnamesData);
            if( result.ok ){
                devnamesData = result.devnamesData;
                devmodeData = result.devmodeData;
                updatePrinterInfo(new DevnamesInfo(devnamesData), new DevmodeInfo(devmodeData));
            }
        });
        modifyAuxButton.addActionListener(event -> {
            AuxSettingEditor editor = new AuxSettingEditor(this, auxSetting);
            editor.setLocationByPlatform(true);
            editor.setVisible(true);
            if( !editor.isCanceled() ){
                updateAuxSetting(auxSetting);
            }
        });
        okButton.addActionListener(event -> {
            canceled = false;
            dispose();
        });
        cancelButton.addActionListener(event -> {
            canceled = true;
            dispose();
        });
    }

    private static class PrinterInfoPane extends JPanel {

        PrinterInfoPane(){
            super(new MigLayout("insets 0, gapy 0", "[grow]", ""));
        }

        void setup(DevnamesInfo devnames, DevmodeInfo devmode){
            add(new JLabel(String.format("%s: %s", "プリンター", devnames.getDevice())), "wrap");
            add(new JLabel(String.format("%s: %s", "用紙", devmode.getPaperSizeLabel())), "wrap");
            add(new JLabel(String.format("%s: %s", "向き", devmode.getOrientationLabel())), "wrap");
            add(new JLabel(String.format("%s: %s", "印刷品質", devmode.getPrintQualityLabel())), "wrap");
            add(new JLabel(String.format("%s: %s", "給紙", devmode.getDefaultSourceLabel())), "wrap");
            add(new JLabel(String.format("%s: %s", "接続", devnames.getOutput())), "");
        }

        void update(DevnamesInfo devnames, DevmodeInfo devmode){
            removeAll();
            setup(devnames, devmode);
            repaint();
            revalidate();
        }
    }

    private static class AuxSettingPane extends JPanel {

        AuxSettingPane(){
            super(new MigLayout("inset 0, gapy 0, fill", "[grow]", ""));
        }

        void setup(AuxSetting auxSetting){
            add(new JLabel(String.format("%s: %s", "dx", auxSetting.getDx())), "wrap");
            add(new JLabel(String.format("%s: %s", "dx", auxSetting.getDy())), "wrap");
        }

        void update(AuxSetting auxSetting){
            removeAll();
            setup(auxSetting);
            repaint();
            revalidate();
        }
    }
}
