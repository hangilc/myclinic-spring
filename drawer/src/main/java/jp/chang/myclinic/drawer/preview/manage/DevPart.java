package jp.chang.myclinic.drawer.preview.manage;

import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class DevPart extends JPanel {

    interface Callback {
        default void onModify(byte[] devnames, byte[] devmode){}
    }

    private JLabel label = new JLabel();
    private Callback callback = new Callback(){};
    private byte[] devnames;
    private byte[] devmode;
    private JLabel deviceLabel = new JLabel();
    private JLabel orientationLabel = new JLabel();
    private JLabel paperSizeLabel = new JLabel();
    private JLabel defaultSourceLabel = new JLabel();
    private JLabel printQualityLabel = new JLabel();
    private JLabel copiesLabel = new JLabel();

    DevPart(){
        setLayout(new MigLayout("insets 0, gapy 3", "", ""));
        JButton modifyButton = new JButton("変更");
        modifyButton.addActionListener(evt -> callback.onModify(devnames, devmode));
        add(new JLabel("デバイス名"), "right");
        add(deviceLabel, "wrap");
        add(new JLabel("向き"), "right");
        add(orientationLabel, "wrap");
        add(new JLabel("用紙サイズ"), "right");
        add(paperSizeLabel, "wrap");
        add(new JLabel("トレイ"), "right");
        add(defaultSourceLabel, "wrap");
        add(new JLabel("印刷品質"), "right");
        add(printQualityLabel, "wrap");
        add(new JLabel("コピー数"), "right");
        add(copiesLabel, "wrap");
        add(modifyButton);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    void clear(){
        label.setText("");
    }

    void setData(byte[] devnames, byte[] devmode){
        this.devnames = devnames;
        this.devmode = devmode;
        DevnamesInfo devnamesInfo = new DevnamesInfo(devnames);
        DevmodeInfo devmodeInfo = new DevmodeInfo(devmode);
        deviceLabel.setText(devnamesInfo.getDevice());
        orientationLabel.setText(devmodeInfo.getOrientationLabel());
        paperSizeLabel.setText(devmodeInfo.getPaperSizeLabel());
        defaultSourceLabel.setText(devmodeInfo.getDefaultSourceLabel());
        printQualityLabel.setText(devmodeInfo.getPrintQualityLabel());
        copiesLabel.setText("" + devmodeInfo.getCopies());
    }

}
