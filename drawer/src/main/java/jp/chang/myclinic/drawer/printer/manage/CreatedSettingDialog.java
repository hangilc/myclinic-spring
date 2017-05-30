package jp.chang.myclinic.drawer.printer.manage;

import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CreatedSettingDialog extends JDialog {

    private String settingName;
    private byte[] devnamesData;
    private byte[] devmodeData;
    private AuxSetting auxSetting;
    private JButton printerDialogButton;
    private JButton modifyAuxButton;
    private JButton okButton;
    private JButton cancelButton;
    private boolean canceled;

    public CreatedSettingDialog(Window owner, String name, byte[] devnamesData, byte[] devmodeData, AuxSetting auxSetting){
        super(owner, "新規印刷設定", ModalityType.APPLICATION_MODAL);
        this.settingName = name;
        this.devnamesData = devnamesData;
        this.devmodeData = devmodeData;
        this.auxSetting = auxSetting;
        setupUI();
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
        bind();
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
        printerDialogButton = new JButton("変更");
        panel.add(new JLabel(String.format("%s: %s", "プリンター", devnamesInfo.getDevice())), "gaptop 5, wrap");
        panel.add(new JLabel(String.format("%s: %s", "用紙", devmodeInfo.getPaperSizeLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "向き", devmodeInfo.getOrientationLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "印刷品質", devmodeInfo.getPrintQualityLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "給紙", devmodeInfo.getDefaultSourceLabel())), "wrap");
        panel.add(new JLabel(String.format("%s: %s", "接続", devnamesInfo.getOutput())), "gapbottom 5, wrap");
        panel.add(printerDialogButton, "right");
        return panel;
    }

    private JComponent makeAuxInfo(){
        JPanel panel = new JPanel(new MigLayout("inset 0, gapy 0, fill", "[grow]", ""));
        modifyAuxButton = new JButton("変更");
        panel.setBorder(BorderFactory.createTitledBorder("その他"));
        panel.add(new JLabel(String.format("%s: %s", "dx", auxSetting.getDx())), "gaptop 5, wrap");
        panel.add(new JLabel(String.format("%s: %s", "dx", auxSetting.getDy())), "gapbottom 5, wrap");
        panel.add(modifyAuxButton, "right");
        return panel;
    }

    private JComponent makeSouth(){
        okButton = new JButton("OK");
        cancelButton = new JButton("キャンセル");
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        panel.add(okButton, "sizegroup btn");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void bind(){
        printerDialogButton.addActionListener(event -> {
            DrawerPrinter printer = new DrawerPrinter();
            DrawerPrinter.DialogResult result = printer.printDialog(null, null);
            if( result.ok ){
                devnamesData = result.devnamesData;
                devmodeData = result.devmodeData;
                getContentPane().removeAll();
                setupUI();
            }
        });
        modifyAuxButton.addActionListener(event -> {
            AuxSettingEditor editor = new AuxSettingEditor(this, auxSetting);
            editor.setLocationByPlatform(true);
            editor.setVisible(true);
            if( !editor.isCanceled() ){
                getContentPane().removeAll();
                setupUI();
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

}
